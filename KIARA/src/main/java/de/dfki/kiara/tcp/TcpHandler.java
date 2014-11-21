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
package de.dfki.kiara.tcp;

import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportListener;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.TransportMessageListener;
import de.dfki.kiara.Util;
import de.dfki.kiara.netty.ListenableConstantFutureAdapter;
import de.dfki.kiara.util.NoCopyByteArrayOutputStream;
import de.dfki.kiara.netty.BaseHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMethod;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TcpHandler extends BaseHandler<Object, TcpBlockTransportFactory>  {

    private static final Logger logger = LoggerFactory.getLogger(TcpHandler.class);

    private final NoCopyByteArrayOutputStream bout;
    private final URI uri;
    private volatile String sessionId = null;
    private final boolean SEND_SESSION_ID = true;

    @Override
    public TransportAddress getLocalTransportAddress() {
        try {
            if (uri != null) {
                return new TcpBlockAddress(transportFactory, uri);
            } else {
                InetSocketAddress sa = ((InetSocketAddress) getLocalAddress());
                return new TcpBlockAddress(transportFactory, sa.getHostName(), sa.getPort());
            }
        } catch (InvalidAddressException ex) {
            throw new IllegalStateException(ex);
        } catch (UnknownHostException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public TcpHandler(TcpBlockTransportFactory transportFactory, URI uri) {
        super(Mode.CLIENT, State.UNINITIALIZED, transportFactory, null);
        if (transportFactory == null) {
            throw new NullPointerException("transportFactory");
        }
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        this.uri = uri;
        this.bout = new NoCopyByteArrayOutputStream(1024);
    }

    public TcpHandler(TcpBlockTransportFactory transportFactory, String path, TransportListener connectionListener) {
        super(Mode.SERVER, State.UNINITIALIZED, transportFactory, connectionListener);
        if (transportFactory == null) {
            throw new NullPointerException("transportFactory");
        }
        if (connectionListener == null) {
            throw new NullPointerException("connectionListener");
        }
        this.uri = null;
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
                if (mode == Mode.CLIENT && SEND_SESSION_ID) {
                    // FIXME send sessionID
                    ctx.writeAndFlush(EMPTY_BUFFER);
                }
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
        logger.debug("Tcp channel closed {}", ctx);
        state = State.CLOSED;
        channel = null;
        if (connectionListener != null) {
            connectionListener.onConnectionClosed(this);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // ctx.flush();
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("Handler: {} / Mode: {} / Channel: {} / Message class {}", this, mode, ctx.channel(), msg.getClass());

        final TcpBlockMessage transportMessage = new TcpBlockMessage(this, (ByteBuffer) msg);

        if (logger.isDebugEnabled()) {
            logger.debug("RECEIVED CONTENT {}", new String(transportMessage.getPayload().array(), transportMessage.getPayload().arrayOffset(), transportMessage.getPayload().remaining()));
        }

        if (mode == Mode.SERVER && sessionId == null && SEND_SESSION_ID) {
            if (logger.isDebugEnabled()) {
                logger.debug("Set session ID to '{}'", Util.bufferToString(transportMessage.getPayload()));
            }
            sessionId = Util.bufferToString(transportMessage.getPayload(), "UTF-8");
        } else {
            notifyListeners(transportMessage);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        logger.error("Tcp Error", cause);
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

    private void notifyListeners(final TcpBlockMessage message) {
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

    private TransportMessage createRequest() {
        return new TcpBlockMessage(this, null);
    }

    private TransportMessage createResponse(TransportMessage transportMessage) {
        if (!(transportMessage instanceof TcpBlockMessage)) {
            throw new IllegalArgumentException("request is not of type TcpBlockMessage");
        }

        return new TcpBlockMessage(this, null);
    }

    @Override
    public TransportMessage createTransportMessage(TransportMessage transportMessage) {
        return new TcpBlockMessage(this, null);
    }

    @Override
    public ListenableFuture<Void> send(TransportMessage message) {
        if (message == null) {
            throw new NullPointerException("message");
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
