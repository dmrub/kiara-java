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
import de.dfki.kiara.Handler;
import de.dfki.kiara.RequestHandler;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.netty.ListenableConstantFutureAdapter;
import de.dfki.kiara.util.NoCopyByteArrayOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import io.netty.handler.codec.http.HttpVersion;
import static io.netty.handler.codec.http.HttpVersion.*;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> implements TransportConnection {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientHandler.class);

    private HttpHeaders headers = null;
    private final NoCopyByteArrayOutputStream bout = new NoCopyByteArrayOutputStream(1024);

    private final URI uri;
    private final HttpMethod method;

    private volatile Channel channel = null;

    private final Handler<TransportConnection> connectionHandler;

    private final List<RequestHandler<TransportMessage, TransportMessage>> requestHandlers = new ArrayList<>();
    private final List<Handler<TransportMessage>> handlers = new ArrayList<>();

    static enum Mode {

        CLIENT,
        SERVER
    }
    private final Mode mode;

    static enum State {

        UNINITIALIZED,
        WAIT_CONNECT,
        CONNECTED,
        WAIT_CLOSE,
        CLOSED
    }
    private State state;

    public HttpServerHandler(URI uri, HttpMethod method) {
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        if (method == null) {
            throw new NullPointerException("method");
        }
        this.uri = uri;
        this.method = method;
        this.connectionHandler = null;
        this.state = State.UNINITIALIZED;
        this.mode = Mode.CLIENT;
    }

    public HttpServerHandler(Handler<TransportConnection> connectionHandler) {
        if (connectionHandler == null) {
            throw new NullPointerException("connectionHandler");
        }
        this.uri = null;
        this.method = null;
        this.connectionHandler = connectionHandler;
        this.state = State.UNINITIALIZED;
        this.mode = Mode.SERVER;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        switch (state) {
            case UNINITIALIZED:
            case WAIT_CONNECT:
                state = State.CONNECTED;
                if (connectionHandler != null) {
                    connectionHandler.onSuccess(this);
                }
                break;
            case WAIT_CLOSE:
                closeChannel();
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Handler: {} / Channel: {}", this, ctx.channel());
        if (mode == Mode.SERVER) {
            if (msg instanceof FullHttpRequest) {
                FullHttpRequest request = (FullHttpRequest) msg;

                HttpRequestMessage transportMessage = new HttpRequestMessage(this, request);
                transportMessage.setPayload(request.content().nioBuffer());

                HttpResponseMessage responseTransportMessage = null;
                for (RequestHandler<TransportMessage, TransportMessage> requestHandler : requestHandlers) {
                    TransportMessage tm = requestHandler.onRequest(transportMessage);
                    if (tm != null) {
                        if (!(tm instanceof HttpResponseMessage)) {
                            // FIXME handle error
                            continue;
                        }
                        responseTransportMessage = (HttpResponseMessage) tm;
                        break;
                    }
                }

                boolean keepAlive = HttpHeaders.isKeepAlive(request);

                if (responseTransportMessage != null) {
                    // FIXME move this to HttpResponseMessage
                    ByteBuf bbuf = Unpooled.wrappedBuffer(responseTransportMessage.getPayload());

                    HttpResponse httpResponse = responseTransportMessage.getResponse();
                    responseTransportMessage.getContent().content().clear();
                    responseTransportMessage.getContent().content().writeBytes(bbuf);
                    logger.debug("RESPONSE CONTENT: {}", responseTransportMessage.getContent().content().toString(StandardCharsets.UTF_8));

                    httpResponse.headers().set(CONTENT_LENGTH, responseTransportMessage.getContent().content().readableBytes());
                    ctx.write(httpResponse);
                } else {
                    FullHttpResponse response = new DefaultFullHttpResponse(
                            HTTP_1_1, BAD_REQUEST, Unpooled.copiedBuffer("Could not handle request", CharsetUtil.UTF_8));
                    ctx.write(response);
                }

                if (!keepAlive) {
                    // If keep-alive is off, close the connection once the content is fully written.
                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
                }
            }
        } else {
            // CLIENT
            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;
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
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        if (mode == Mode.CLIENT) {
            onErrorResponse(cause);
        }
    }

    @Override
    public SocketAddress getLocalAddress() {
        if (channel == null) {
            throw new IllegalStateException();
        }
        return channel.localAddress();
    }

    @Override
    public SocketAddress getRemoteAddress() {
        if (channel == null) {
            throw new IllegalStateException();
        }
        return channel.remoteAddress();
    }

    public void onResponse(HttpResponseMessage response) {
        if (logger.isDebugEnabled()) {
            logger.debug("RECEIVED CONTENT {}", new String(response.getPayload().array(), response.getPayload().arrayOffset(), response.getPayload().remaining()));
        }

        synchronized (handlers) {
            if (!handlers.isEmpty()) {
                for (Handler<TransportMessage> handler : handlers) {
                    if (handler.onSuccess(response)) {
                        return;
                    }
                }
            }
        }
    }

    public void onErrorResponse(Throwable error) {
        if (logger.isDebugEnabled()) {
            logger.debug("RECEIVED ERROR {}", error);
        }

        synchronized (handlers) {
            if (!handlers.isEmpty()) {
                for (Handler<TransportMessage> handler : handlers) {
                    if (handler.onFailure(error)) {
                        return;
                    }
                }
            }
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
    public TransportMessage createResponse(TransportMessage transportMessage) {
        if (!(transportMessage instanceof HttpRequestMessage)) {
            throw new IllegalArgumentException("request is not of type HttpRequestMessage");
        }
        HttpRequestMessage request = (HttpRequestMessage) transportMessage;

        // Decide whether to close the connection or not.
        boolean keepAlive = HttpHeaders.isKeepAlive(request.getRequest());
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, request.getRequest().getDecoderResult().isSuccess() ? OK : BAD_REQUEST);

        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        return new HttpResponseMessage(this, response);
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

        HttpRequest request = httpMsg.finalizeRequest();

        if (logger.isDebugEnabled()) {
            logger.debug("SEND CONTENT: {}", httpMsg.getContent().content().toString(StandardCharsets.UTF_8));
        }

        ChannelFuture result = channel.writeAndFlush(request);
        return new ListenableConstantFutureAdapter<>(result, null);
    }

    @Override
    public void addResponseHandler(Handler<TransportMessage> handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        synchronized (handlers) {
            handlers.add(handler);
        }
    }

    @Override
    public boolean removeResponseHandler(Handler<TransportMessage> handler) {
        if (handler == null) {
            return false;
        }
        synchronized (handlers) {
            handlers.remove(handler);
        }
        return false;
    }

    @Override
    public void addRequestHandler(RequestHandler<TransportMessage, TransportMessage> handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        synchronized (requestHandlers) {
            requestHandlers.add(handler);
        }
    }

    @Override
    public void removeRequestHandler(RequestHandler<TransportMessage, TransportMessage> handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        synchronized (requestHandlers) {
            requestHandlers.remove(handler);
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
    public void close() throws IOException {
        if (state == State.WAIT_CLOSE || state == State.CLOSED) {
            return;
        }

        logger.debug("Closing transport connection state={} channel={}", state, channel);

        state = State.WAIT_CLOSE;
        closeChannel();
    }

}
