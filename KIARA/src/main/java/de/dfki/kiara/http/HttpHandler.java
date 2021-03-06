/* KIARA - Middleware for efficient and QoS/Security-aware invocation of services and exchange of messages
 *
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara.http;

import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportConnectionListener;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.TransportMessageListener;
import de.dfki.kiara.Util;
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
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import io.netty.handler.codec.http.HttpVersion;
import static io.netty.handler.codec.http.HttpVersion.*;
import io.netty.handler.codec.http.LastHttpContent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
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
public class HttpHandler extends SimpleChannelInboundHandler<Object> implements TransportConnection {

    private static final Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    private HttpHeaders headers = null;
    private final NoCopyByteArrayOutputStream bout;

    private final HttpTransport transport;
    private final URI uri;
    private final HttpMethod method;

    private volatile Channel channel = null;

    private final TransportConnectionListener connectionListener;

    private final List<TransportMessageListener> listeners = new ArrayList<>();

    @Override
    public TransportAddress getLocalTransportAddress() {
        try {
            if (uri != null)
                return new HttpAddress(transport, uri);
            else {
                InetSocketAddress sa = ((InetSocketAddress)getLocalAddress());
                return new HttpAddress(transport, sa.getHostName(), sa.getPort(), "");
            }
        } catch (InvalidAddressException ex) {
            throw new IllegalStateException(ex);
        } catch (UnknownHostException ex) {
            throw new IllegalStateException(ex);
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Transport getTransport() {
        return transport;
    }

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

    public HttpHandler(HttpTransport transport, URI uri, HttpMethod method) {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        if (method == null) {
            throw new NullPointerException("method");
        }
        this.transport = transport;
        this.uri = uri;
        this.method = method;
        this.connectionListener = null;
        this.state = State.UNINITIALIZED;
        this.mode = Mode.CLIENT;
        this.bout = new NoCopyByteArrayOutputStream(1024);
    }

    public HttpHandler(HttpTransport transport, TransportConnectionListener connectionListener) {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (connectionListener == null) {
            throw new NullPointerException("connectionListener");
        }
        this.transport = transport;
        this.uri = null;
        this.method = null;
        this.connectionListener = connectionListener;
        this.state = State.UNINITIALIZED;
        this.mode = Mode.SERVER;
        this.bout = null;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        switch (state) {
            case UNINITIALIZED:
            case WAIT_CONNECT:
                state = State.CONNECTED;
                if (connectionListener != null) {
                    connectionListener.onConnectionOpened(this);
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
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Http channel closed {}", ctx);
        state = State.CLOSED;
        channel = null;
        if (connectionListener != null) {
            connectionListener.onConnectionClosed(this);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Handler: {} / Channel: {}", this, ctx.channel());
        if (mode == Mode.SERVER) {
            if (msg instanceof FullHttpRequest) {
                final FullHttpRequest request = (FullHttpRequest) msg;

                HttpRequestMessage transportMessage = new HttpRequestMessage(this, request);
                transportMessage.setPayload(request.content().nioBuffer());

                if (logger.isDebugEnabled()) {
                    logger.debug("RECEIVED REQUEST WITH CONTENT {}", Util.bufferToString(transportMessage.getPayload()));
                }

                synchronized (listeners) {
                    if (!listeners.isEmpty()) {
                        for (TransportMessageListener listener : listeners) {
                            listener.onMessage(transportMessage);
                        }
                    }
                }

                boolean keepAlive = HttpHeaders.isKeepAlive(request);
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

        logger.error("Http error", cause);
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

    private void onResponse(HttpResponseMessage response) {
        if (logger.isDebugEnabled()) {
            logger.debug("RECEIVED RESPONSE WITH CONTENT {}", new String(response.getPayload().array(), response.getPayload().arrayOffset(), response.getPayload().remaining()));
        }

        synchronized (listeners) {
            if (!listeners.isEmpty()) {
                for (TransportMessageListener listener : listeners) {
                    listener.onMessage(response);
                }
            }
        }
    }

    @Override
    public TransportMessage createRequest() {
        if (mode == Mode.SERVER) {
            throw new IllegalStateException("Requests from server are not supported");
        }
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
    public TransportMessage createTransportMessage(TransportMessage transportMessage) {
        if (transportMessage instanceof HttpRequestMessage)
            return createResponse(transportMessage);
        else
            return createRequest();
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
        if (state != State.CONNECTED || channel == null) {
            throw new IllegalStateException("state=" + state.toString() + " channel=" + channel);
        }

        HttpMessage httpMsg;

        boolean keepAlive = true;

        if (message instanceof HttpRequestMessage) {
            HttpRequestMessage msg = (HttpRequestMessage) message;

            httpMsg = msg.finalizeRequest();

            if (logger.isDebugEnabled()) {
                logger.debug("SEND CONTENT: {}", msg.getContent().content().toString(StandardCharsets.UTF_8));
            }
        } else if (message instanceof HttpResponseMessage) {
            HttpResponseMessage msg = (HttpResponseMessage) message;

            httpMsg = msg.finalizeResponse();

            keepAlive = HttpHeaders.isKeepAlive(httpMsg);

            if (logger.isDebugEnabled()) {
                logger.debug("SEND CONTENT: {}", msg.getContent().content().toString(StandardCharsets.UTF_8));
            }
        } else {
            throw new IllegalArgumentException("msg is neither of type HttpRequestMessage nor HttpResponseMessage");
        }

        ChannelFuture result = channel.writeAndFlush(httpMsg);

        if (!keepAlive) {
            // If keep-alive is off, close the connection once the content is fully written.
            channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }

        return new ListenableConstantFutureAdapter<>(result, null);
    }

    @Override
    public void addMessageListener(TransportMessageListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public boolean removeMessageListener(TransportMessageListener listener) {
        if (listener == null) {
            return false;
        }
        synchronized (listeners) {
            listeners.remove(listener);
        }
        return false;
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
