/*
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara.http;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.dfki.kiara.AsyncHandler;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.netty.ListenableConstantFutureAdapter;
import de.dfki.kiara.util.NoCopyByteArrayOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
class HttpClientHandler extends SimpleChannelInboundHandler<HttpObject> implements TransportConnection {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientHandler.class);

    private HttpResponseStatus status = null;
    private HttpHeaders headers = null;
    private final NoCopyByteArrayOutputStream bout = new NoCopyByteArrayOutputStream(1024);

    private final URI uri;
    private final HttpMethod method;
    private volatile Channel channel;

    private final List<AsyncHandler<TransportMessage>> handlers = new ArrayList<>();
    private final BlockingQueue<Object> queue = new LinkedBlockingDeque<>();
    private static final ListeningExecutorService sameThreadExecutor = MoreExecutors.sameThreadExecutor();

    static enum State {

        UNINITIALIZED,
        WAIT_CONNECT,
        CONNECTED,
        WAIT_CLOSE,
        CLOSED
    }
    private State state;

    public HttpClientHandler(URI uri, HttpMethod method) {
        this.uri = uri;
        this.method = method;
        this.state = State.UNINITIALIZED;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        channel = ctx.channel();
        switch (state) {
            case UNINITIALIZED:
            case WAIT_CONNECT:
                state = State.CONNECTED;
                break;
            case WAIT_CLOSE:
                closeChannel();
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            status = response.getStatus();
            headers = response.headers();
            //if (!response.headers().isEmpty()) {
            //    contentType = response.headers().get("Content-Type");
            //}
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ByteBuf buf = content.content();
            if (buf.isReadable()) {
                if (buf.hasArray()) {
                    bout.write(buf.array(), buf.readerIndex(), buf.readableBytes());
                } else {
                    byte[] bytes = new byte[buf.readableBytes()];
                    buf.getBytes(buf.readerIndex(), bytes);
                    bout.write(bytes);
                }
            }
            if (content instanceof LastHttpContent) {
                //ctx.close();
                bout.flush();
                HttpResponseMessage response = new HttpResponseMessage(this, headers);
                response.setPayload(ByteBuffer.wrap(bout.toByteArray(), 0, bout.size()));
                onResponse(response);
                bout.reset();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        onErrorResponse(cause);
    }

    @Override
    public SocketAddress getLocalAddress() {
        if (state == State.WAIT_CONNECT || channel == null) {
            throw new IllegalStateException();
        }
        return channel.localAddress();
    }

    @Override
    public SocketAddress getRemoteAddress() {
        if (state == State.WAIT_CONNECT || channel == null) {
            throw new IllegalStateException();
        }
        return channel.remoteAddress();
    }

    public void onResponse(HttpResponseMessage response) {
        if (logger.isDebugEnabled()) {
            logger.debug("RECEIVED CONTENT {}", new String(response.getPayload().array(), response.getPayload().arrayOffset(), response.getPayload().remaining()));
        }
        if (handlers.isEmpty()) {
            queue.add(response);
        } else {
            for (AsyncHandler<TransportMessage> handler : handlers) {
                handler.onSuccess(response);
            }
        }
    }

    public void onErrorResponse(Throwable error) {
        if (handlers.isEmpty()) {
            queue.add(error);
        } else {
            for (AsyncHandler<TransportMessage> handler : handlers) {
                handler.onFailure(error);
            }
        }
    }

    public void closeChannel() {
        if (channel != null) {
            channel.closeFuture().addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    future.removeListener(this);
                    state = State.CLOSED;
                    channel = null;
                }

            });
        }
    }

    @Override
    public TransportMessage createRequest() {
        // Prepare the HTTP request.
        String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        FullHttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, method, uri.getRawPath());

        request.headers().set(HttpHeaders.Names.HOST, host);
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);

        return new HttpRequestMessage(this, request);
    }

    @Override
    public void addResponseHandler(AsyncHandler<TransportMessage> handler) {
        handlers.add(handler);
    }

    @Override
    public void removeResponseHandler(AsyncHandler<TransportMessage> handler) {
        handlers.remove(handler);
    }

    @Override
    public ListenableFuture<Void> send(TransportMessage message) {
        if (message == null) {
            throw new NullPointerException("msg");
        }
        if (!(message instanceof HttpRequestMessage)) {
            throw new IllegalArgumentException("msg is not of type HttpRequestMessage");
        }
        HttpRequestMessage httpMsg = (HttpRequestMessage) message;

        if (state != State.CONNECTED || channel == null) {
            throw new IllegalStateException("state=" + state.toString() + " channel=" + channel);
        }

        FullHttpRequest request = httpMsg.finalizeRequest();

        if (logger.isDebugEnabled()) {
            logger.debug("SEND CONTENT: {}", request.content().toString(StandardCharsets.UTF_8));
        }

        ChannelFuture result = channel.writeAndFlush(request);
        return new ListenableConstantFutureAdapter<>(result, null);
    }

    @Override
    public ListenableFuture<TransportMessage> receive(ListeningExecutorService executor) {
        if (executor == null)
            executor = sameThreadExecutor;
        return executor.submit(new Callable<TransportMessage>() {

            @Override
            public TransportMessage call() throws Exception {
                boolean interrupted = false;
                try {
                    for (;;) {
                        try {
                            Object value = queue.take();
                            if (value instanceof Exception) {
                                throw (Exception) value;
                            }
                            return (TransportMessage) value;
                        } catch (InterruptedException ignore) {
                            interrupted = true;
                        }
                    }
                } finally {
                    if (interrupted) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
    }

    @Override
    public void close() throws IOException {
        if (state == State.WAIT_CLOSE || state == State.CLOSED) {
            return;
        }

        System.err.println("state: " + state);
        System.err.println("channel: " + channel);

        state = State.WAIT_CLOSE;
        closeChannel();
    }

}
