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
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;
import io.netty.handler.codec.http.ServerCookieEncoder;
import io.netty.util.CharsetUtil;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> implements TransportConnection {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientHandler.class);

    private FullHttpRequest request;
    /**
     * Buffer that stores the response content
     */
    private final StringBuilder buf = new StringBuilder();

    private volatile Channel channel = null;

    private final Handler<TransportConnection> connectionHandler;

    private final List<RequestHandler<TransportMessage, TransportMessage>> requestHandlers = new ArrayList<>();

    static enum State {

        UNINITIALIZED,
        WAIT_CONNECT,
        CONNECTED,
        WAIT_CLOSE,
        CLOSED
    }
    private State state;

    public HttpServerHandler(Handler<TransportConnection> connectionHandler) {
        if (connectionHandler == null) {
            throw new NullPointerException("connectionHandler");
        }
        this.connectionHandler = connectionHandler;
        this.state = State.UNINITIALIZED;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        switch (state) {
            case UNINITIALIZED:
            case WAIT_CONNECT:
                state = State.CONNECTED;
                connectionHandler.onSuccess(this);
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
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        logger.debug("Handler: {} / Channel: {}", this, ctx.channel());
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = this.request = (FullHttpRequest) msg;

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
    }

    private static void appendDecoderResult(StringBuilder buf, HttpObject o) {
        DecoderResult result = o.getDecoderResult();
        if (result.isSuccess()) {
            return;
        }

        buf.append(".. WITH DECODER FAILURE: ");
        buf.append(result.cause());
        buf.append("\r\n");
    }

    private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx) {
        // Decide whether to close the connection or not.
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, currentObj.getDecoderResult().isSuccess() ? OK : BAD_REQUEST,
                Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));

        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        // Encode the cookie.
        String cookieString = request.headers().get(COOKIE);
        if (cookieString != null) {
            Set<Cookie> cookies = CookieDecoder.decode(cookieString);
            if (!cookies.isEmpty()) {
                // Reset the cookies if necessary.
                for (Cookie cookie : cookies) {
                    response.headers().add(SET_COOKIE, ServerCookieEncoder.encode(cookie));
                }
            }
        } else {
            // Browser sent no cookie.  Add some.
            response.headers().add(SET_COOKIE, ServerCookieEncoder.encode("key1", "value1"));
            response.headers().add(SET_COOKIE, ServerCookieEncoder.encode("key2", "value2"));
        }

        // Write the response.
        ctx.write(response);

        return keepAlive;
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
        ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        cause.printStackTrace();
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

    public void onErrorResponse(Throwable error) {
        if (logger.isDebugEnabled()) {
            logger.debug("RECEIVED ERROR {}", error);
        }

        /*
         synchronized (handlers) {
         if (!handlers.isEmpty()) {
         for (Handler<TransportMessage> handler : handlers) {
         if (handler.onFailure(error)) {
         return;
         }
         }
         }
         }
         */
    }

    @Override
    public TransportMessage createRequest() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void addResponseHandler(Handler<TransportMessage> handler) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeResponseHandler(Handler<TransportMessage> handler) {
        throw new UnsupportedOperationException();
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
