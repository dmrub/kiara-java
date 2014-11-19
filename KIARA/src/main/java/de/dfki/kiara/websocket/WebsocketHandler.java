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
package de.dfki.kiara.websocket;

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
import de.dfki.kiara.util.Glob;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class WebsocketHandler extends SimpleChannelInboundHandler<Object> implements TransportConnection {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketHandler.class);

    private HttpHeaders headers = null;

    private final WebsocketTransport transport;
    private final URI uri;
    private final WebSocketClientHandshaker clientHandshaker;
    private WebSocketServerHandshaker serverHandshaker;
    private ChannelPromise handshakeFuture;
    private final HttpMethod method;

    private volatile Channel channel = null;

    private final TransportConnectionListener connectionListener;

    private final List<TransportMessageListener> listeners = new ArrayList<>();

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

    /**
     * Initialize client
     */
    public WebsocketHandler(WebsocketTransport transport, URI uri, WebSocketClientHandshaker handshaker, HttpMethod method) {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        if (handshaker == null) {
            throw new NullPointerException("handshaker");
        }
        if (method == null) {
            throw new NullPointerException("method");
        }
        this.transport = transport;
        this.uri = uri;
        this.clientHandshaker = handshaker;
        this.serverHandshaker = null;
        this.method = method;
        this.connectionListener = null;
        this.state = State.UNINITIALIZED;
        this.mode = Mode.CLIENT;
    }

    /**
     * Initialize server
     */
    public WebsocketHandler(WebsocketTransport transport, String path, TransportConnectionListener connectionListener) {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (connectionListener == null) {
            throw new NullPointerException("connectionListener");
        }
        this.transport = transport;
        URI tmp = null;
        try {
            tmp = path != null ? new URI(path.isEmpty() ? "/" : path).normalize() : null;
        } catch (URISyntaxException ex) {
        }
        this.uri = tmp;
        this.clientHandshaker = null;
        this.serverHandshaker = null;
        this.method = null;
        this.connectionListener = connectionListener;
        this.state = State.UNINITIALIZED;
        this.mode = Mode.SERVER;
    }

    public ChannelPromise getHandshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public TransportAddress getLocalTransportAddress() {
        try {
            if (uri != null && uri.isAbsolute()) {
                return new WebsocketAddress(transport, uri);
            } else {
                InetSocketAddress sa = ((InetSocketAddress) getLocalAddress());
                return new WebsocketAddress(transport, sa.getHostName(), sa.getPort(), "");
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

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        if (clientHandshaker != null) {
            clientHandshaker.handshake(ctx.channel());
        }
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
        logger.debug("Websocket channel closed {}", ctx);
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

                // Handle a bad request.
                if (!request.getDecoderResult().isSuccess()) {
                    sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
                    return;
                }

                // Allow only GET methods.
                if (request.getMethod() != GET) {
                    sendHttpResponse(ctx, request, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
                    return;
                }

                if (uri.getPath().equals(request.getUri()) || Pattern.matches(Glob.convertGlobToRegex(uri.getPath()), request.getUri())) {
                    // Handshake
                    WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                            request.getUri()/*headers().get(HOST)*/, null, false);
                    serverHandshaker = wsFactory.newHandshaker(request);
                    if (serverHandshaker == null) {
                        WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
                    } else {
                        serverHandshaker.handshake(ctx.channel(), request);
                    }
                    return;
                }

                // Send the demo page and favicon.ico
                if ("/".equals(request.getUri())) {
                    ByteBuf content = Unpooled.copiedBuffer("WEBSOCKET ROOT", CharsetUtil.UTF_8);
                    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, content);

                    response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
                    HttpHeaders.setContentLength(response, content.readableBytes());

                    sendHttpResponse(ctx, request, response);
                    return;
                }
                if ("/favicon.ico".equals(request.getUri())) {
                    FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
                    sendHttpResponse(ctx, request, res);
                    return;
                }

                FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
                sendHttpResponse(ctx, request, res);
                return;

                //boolean keepAlive = HttpHeaders.isKeepAlive(request);
            }
            if (msg instanceof WebSocketFrame) {
                WebSocketFrame frame = (WebSocketFrame) msg;
                // Check for closing frame
                if (frame instanceof CloseWebSocketFrame) {
                    serverHandshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
                    return;
                }
                if (frame instanceof PingWebSocketFrame) {
                    ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
                    return;
                }
                WebsocketMessage transportMessage;

                if (frame instanceof TextWebSocketFrame) {
                    transportMessage = new WebsocketMessage(this, Util.stringToBuffer(((TextWebSocketFrame) frame).text(), "UTF-8"));
                } else if (frame instanceof BinaryWebSocketFrame) {
                    transportMessage = new WebsocketMessage(this, de.dfki.kiara.netty.Util.toByteBuffer(((BinaryWebSocketFrame)frame).content()));
                } else {
                    throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass()
                            .getName()));
                }

                // Send the uppercase string back.
                //String request = ((TextWebSocketFrame) frame).text();
                //System.err.printf("%s received %s%n", ctx.channel(), request);
                //ctx.channel().write(new TextWebSocketFrame(request.toUpperCase()));

                if (logger.isDebugEnabled()) {
                    logger.debug("RECEIVED REQUEST WITH CONTENT {}", Util.bufferToString(transportMessage.getPayload()));
                }

                notifyListeners(transportMessage);
            }
        } else {
            // CLIENT
            Channel ch = ctx.channel();
            if (!clientHandshaker.isHandshakeComplete()) {
                final FullHttpResponse response = (FullHttpResponse) msg;
                headers = response.headers();
                clientHandshaker.finishHandshake(ch, response);
                logger.debug("WebSocket client connected!");
                handshakeFuture.setSuccess();
                return;
            }

            if (msg instanceof FullHttpResponse) {
                FullHttpResponse response = (FullHttpResponse) msg;
                throw new IllegalStateException(
                        "Unexpected FullHttpResponse (getStatus=" + response.getStatus()
                        + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
            }

            WebSocketFrame frame = (WebSocketFrame) msg;
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
                if (logger.isDebugEnabled()) {
                    logger.debug("WebSocket Client received message: {}", textFrame.text());
                }
                WebsocketResponseMessage response = new WebsocketResponseMessage(this, headers);
                response.setPayload(Util.stringToBuffer(textFrame.text(), "UTF-8"));
                onResponse(response);
            } else if (frame instanceof BinaryWebSocketFrame) {
                BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) frame;
                if (logger.isDebugEnabled()) {
                    logger.debug("WebSocket Client received message: {}", binaryFrame.content().toString(CharsetUtil.UTF_8));
                }
                WebsocketMessage response = new WebsocketMessage(this, de.dfki.kiara.netty.Util.toByteBuffer(binaryFrame.content()));
                onResponse(response);
            } else if (frame instanceof PongWebSocketFrame) {
                logger.debug("WebSocket Client received pong");
            } else if (frame instanceof CloseWebSocketFrame) {
                logger.debug("WebSocket Client received closing");
                ch.close();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Websocket error", cause);
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
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

    private void notifyListeners(final TransportMessage message) {
        TransportMessageListener currentListeners[] = null;
        synchronized (listeners) {
            if (!listeners.isEmpty()) {
                currentListeners = listeners.toArray(new TransportMessageListener[listeners.size()]);
            }
        }
        if (currentListeners != null) {
            for (TransportMessageListener listener : currentListeners) {
                listener.onMessage(message);
            }
        }
    }


    private void onResponse(TransportMessage response) {
        if (logger.isDebugEnabled()) {
            logger.debug("RECEIVED RESPONSE WITH CONTENT {}", new String(response.getPayload().array(), response.getPayload().arrayOffset(), response.getPayload().remaining()));
        }

        notifyListeners(response);
    }

    @Override
    public TransportMessage createRequest() {
        WebsocketMessage msg = new WebsocketMessage(this, null);
        if (uri != null) {
            msg.set("websocket-uri", uri);
        }
        return msg;
    }

    @Override
    public TransportMessage createTransportMessage(TransportMessage transportMessage) {
        if (transportMessage instanceof WebsocketRequestMessage) {
            return createResponse(transportMessage);
        } else {
            return createRequest();
        }
    }

    @Override
    public TransportMessage createResponse(TransportMessage transportMessage) {
        return new WebsocketMessage(this, null);
    }

    private static void sendHttpResponse(
            ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        // Generate an error page if response getStatus code is not OK (200).
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public ListenableFuture<Void> send(TransportMessage message) {
        if (message == null) {
            throw new NullPointerException("message");
        }
        if (state != State.CONNECTED || channel == null) {
            throw new IllegalStateException("state=" + state.toString() + " channel=" + channel);
        }

        HttpMessage httpMsg = null;

        boolean keepAlive = true;

        if (message instanceof WebsocketRequestMessage) {
            WebsocketRequestMessage msg = (WebsocketRequestMessage) message;

            httpMsg = msg.finalizeRequest();

            if (logger.isDebugEnabled()) {
                logger.debug("SEND CONTENT: {}", msg.getContent().content().toString(StandardCharsets.UTF_8));
            }
        } else if (message instanceof WebsocketResponseMessage) {
            WebsocketResponseMessage msg = (WebsocketResponseMessage) message;

            httpMsg = msg.finalizeResponse();

            keepAlive = HttpHeaders.isKeepAlive(httpMsg);

            if (logger.isDebugEnabled()) {
                logger.debug("SEND CONTENT: {}", msg.getContent().content().toString(StandardCharsets.UTF_8));
            }
        } else if (message instanceof WebsocketMessage) {
                //ctx.channel().write(new TextWebSocketFrame(request.toUpperCase()));
                // new TextWebSocketFrame(Util.bufferToString(message.getPayload(), "UTF-8"));
                WebSocketFrame frame = new BinaryWebSocketFrame(Unpooled.wrappedBuffer(message.getPayload()));
                ChannelFuture result = channel.writeAndFlush(frame);
                return new ListenableConstantFutureAdapter<>(result, null);
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
