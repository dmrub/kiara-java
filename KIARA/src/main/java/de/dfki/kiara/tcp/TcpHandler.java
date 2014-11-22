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
import com.google.common.util.concurrent.SettableFuture;
import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportListener;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.Util;
import de.dfki.kiara.netty.ListenableConstantFutureAdapter;
import de.dfki.kiara.util.NoCopyByteArrayOutputStream;
import de.dfki.kiara.netty.BaseHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TcpHandler extends BaseHandler<Object, TcpBlockTransportFactory> {

    private static final Logger logger = LoggerFactory.getLogger(TcpHandler.class);

    private final NoCopyByteArrayOutputStream bout;
    private final URI uri;
    private volatile String sessionId = null;
    private final boolean SEND_SESSION_ID = true;

    public TcpHandler(TcpBlockTransportFactory transportFactory, URI uri, SettableFuture<Transport> onConnectionActive) {
        super(Mode.CLIENT, State.UNINITIALIZED, transportFactory, null, onConnectionActive);
        if (transportFactory == null) {
            throw new NullPointerException("transportFactory");
        }
        if (uri == null) {
            throw new NullPointerException("uri");
        }
        this.uri = uri;
        this.bout = new NoCopyByteArrayOutputStream(1024);
    }

    public TcpHandler(TcpBlockTransportFactory transportFactory, String path, TransportListener connectionListener, SettableFuture<Transport> onConnectionActive) {
        super(Mode.SERVER, State.UNINITIALIZED, transportFactory, connectionListener, onConnectionActive);
        if (transportFactory == null) {
            throw new NullPointerException("transportFactory");
        }
        if (connectionListener == null) {
            throw new NullPointerException("connectionListener");
        }
        this.uri = null;
        this.bout = null;
    }

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

    private static final byte[] EMPTY_ARRAY = new byte[0];
    private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(EMPTY_ARRAY);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if ((state == State.UNINITIALIZED || state == State.WAIT_CONNECT) && mode == Mode.CLIENT && SEND_SESSION_ID) {
            // FIXME send sessionID
            ctx.writeAndFlush(EMPTY_BUFFER);
        }
        super.channelActive(ctx);
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

}
