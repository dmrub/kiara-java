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
package de.dfki.kiara.tcp;

import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.Handler;
import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.RequestHandler;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.Util;
import de.dfki.kiara.netty.ListenableConstantFutureAdapter;
import de.dfki.kiara.util.NoCopyByteArrayOutputStream;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;

import io.netty.handler.codec.http.HttpMethod;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TcpHandler extends SimpleChannelInboundHandler<Object> implements TransportConnection {

    private static final Logger logger = LoggerFactory.getLogger(TcpHandler.class);

    private HttpHeaders headers = null;
    private final NoCopyByteArrayOutputStream bout;

    private final TcpBlockTransport transport;
    private final URI uri;
    private final HttpMethod method;

    private volatile Channel channel = null;
    private volatile String sessionId = null;

    private final Handler<TransportConnection> connectionHandler;

    private final List<RequestHandler<TransportMessage, ListenableFuture<TransportMessage>>> requestHandlers = new ArrayList<>();
    private final List<Handler<TransportMessage>> responseHandlers = new ArrayList<>();

    @Override
    public TransportAddress getLocalTransportAddress() {
        try {
            if (uri != null)
                return new TcpBlockAddress(transport, uri);
            else {
                InetSocketAddress sa = ((InetSocketAddress) getLocalAddress());
                return new TcpBlockAddress(transport, sa.getHostName(), sa.getPort());
            }
        } catch (InvalidAddressException ex) {
            throw new IllegalStateException(ex);
        } catch (UnknownHostException ex) {
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

    public TcpHandler(TcpBlockTransport transport, URI uri, HttpMethod method) {
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
        this.connectionHandler = null;
        this.state = State.UNINITIALIZED;
        this.mode = Mode.CLIENT;
        this.bout = new NoCopyByteArrayOutputStream(1024);
    }

    public TcpHandler(TcpBlockTransport transport, Handler<TransportConnection> connectionHandler) {
        if (transport == null) {
            throw new NullPointerException("transport");
        }
        if (connectionHandler == null) {
            throw new NullPointerException("connectionHandler");
        }
        this.transport = transport;
        this.uri = null;
        this.method = null;
        this.connectionHandler = connectionHandler;
        this.state = State.UNINITIALIZED;
        this.mode = Mode.SERVER;
        this.bout = null;
    }

    private static final byte[] EMPTY_ARRAY = new byte[0];
    private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(EMPTY_ARRAY);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channel = ctx.channel();
        switch (state) {
            case UNINITIALIZED:
            case WAIT_CONNECT:
                state = State.CONNECTED;
                if (connectionHandler != null) {
                    connectionHandler.onSuccess(this);
                }
                if (mode == Mode.CLIENT) {
                    // FIXME send sessionID
                    ctx.writeAndFlush(EMPTY_BUFFER);
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
        logger.debug("Handler: {} / Mode: {} / Channel: {} / Message class {}", this, mode, ctx.channel(), msg.getClass());

        if (msg instanceof ByteBuffer) {
            ByteBuffer message = (ByteBuffer) msg;
            TcpBlockMessage transportMessage = new TcpBlockMessage(this, (ByteBuffer) msg);

            if (mode == Mode.SERVER) {
                if (sessionId == null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Set session ID to '{}'", Util.bufferToString(transportMessage.getPayload()));
                    }
                    sessionId = Util.bufferToString(transportMessage.getPayload(), "UTF-8");
                } else {
                    ListenableFuture<TransportMessage> tm = null;
                    for (RequestHandler<TransportMessage, ListenableFuture<TransportMessage>> requestHandler : requestHandlers) {
                        tm = requestHandler.onRequest(transportMessage);
                        if (tm != null) {
                            break;
                        }
                    }

                    TcpBlockMessage responseTransportMessage = tm != null ? (TcpBlockMessage)tm.get() : null;

                    if (responseTransportMessage != null && responseTransportMessage.getPayload() != null) {
                        logger.debug("RESPONSE CONTENT: {}", Util.bufferToString(responseTransportMessage.getPayload()));
                        ctx.write(responseTransportMessage.getPayload());
                    } else {
                        logger.info("NO RESPONSE CONTENT");
                    }
                }
            } else {
                // CLIENT
                onResponse(transportMessage);
            }

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();

        logger.error("Tcp Error", cause);

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

    public void onResponse(TcpBlockMessage response) {
        if (logger.isDebugEnabled()) {
            logger.debug("RECEIVED CONTENT {}", new String(response.getPayload().array(), response.getPayload().arrayOffset(), response.getPayload().remaining()));
        }

        synchronized (responseHandlers) {
            if (!responseHandlers.isEmpty()) {
                for (Handler<TransportMessage> handler : responseHandlers) {
                    if (handler.onSuccess(response)) {
                        return;
                    }
                }
            }
        }
    }

    public void onErrorResponse(Throwable error) {
        synchronized (responseHandlers) {
            if (!responseHandlers.isEmpty()) {
                for (Handler<TransportMessage> handler : responseHandlers) {
                    if (handler.onFailure(error)) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    public TransportMessage createRequest() {
        return new TcpBlockMessage(this, null);
    }

    @Override
    public TransportMessage createResponse(TransportMessage transportMessage) {
        if (!(transportMessage instanceof TcpBlockMessage)) {
            throw new IllegalArgumentException("request is not of type TcpBlockMessage");
        }
        TcpBlockMessage request = (TcpBlockMessage) transportMessage;

        return new TcpBlockMessage(this, null);
    }

    @Override
    public ListenableFuture<Void> send(TransportMessage message) {
        if (message == null) {
            throw new NullPointerException("msg");
        }
        if (state != State.CONNECTED || channel == null) {
            throw new IllegalStateException("state=" + state.toString() + " channel=" + channel);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("SEND CONTENT: {}", Util.bufferToString(message.getPayload()));
        }

        ChannelFuture result = channel.writeAndFlush(message.getPayload());
        return new ListenableConstantFutureAdapter<>(result, null);
    }

    @Override
    public void addResponseHandler(Handler<TransportMessage> handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        synchronized (responseHandlers) {
            responseHandlers.add(handler);
        }
    }

    @Override
    public boolean removeResponseHandler(Handler<TransportMessage> handler) {
        if (handler == null) {
            return false;
        }
        synchronized (responseHandlers) {
            responseHandlers.remove(handler);
        }
        return false;
    }

    @Override
    public void addRequestHandler(RequestHandler<TransportMessage, ListenableFuture<TransportMessage>> handler) {
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        synchronized (requestHandlers) {
            requestHandlers.add(handler);
        }
    }

    @Override
    public void removeRequestHandler(RequestHandler<TransportMessage, ListenableFuture<TransportMessage>> handler) {
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
