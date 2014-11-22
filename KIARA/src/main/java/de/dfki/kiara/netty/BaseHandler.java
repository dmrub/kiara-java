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
package de.dfki.kiara.netty;

import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportFactory;
import de.dfki.kiara.TransportListener;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.TransportMessageListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 * @param <I>
 * @param <T>
 */
public abstract class BaseHandler<I, T extends TransportFactory> extends SimpleChannelInboundHandler<I> implements Transport {

    protected final T transportFactory;
    protected volatile Channel channel = null;
    protected final TransportListener connectionListener;
    protected final List<TransportMessageListener> listeners = new ArrayList<>();

    public static enum Mode {

        CLIENT,
        SERVER
    }
    protected final Mode mode;

    public static enum State {

        UNINITIALIZED,
        WAIT_CONNECT,
        CONNECTED,
        WAIT_CLOSE,
        CLOSED
    }
    protected State state;

    protected BaseHandler(Mode mode, State state, T transportFactory, TransportListener connectionListener) {
        this.mode = mode;
        this.state = state;
        this.connectionListener = connectionListener;
        this.transportFactory = transportFactory;
    }

    @Override
    public T getTransportFactory() {
        return transportFactory;
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

    protected final void notifyListeners(final TransportMessage message) {
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

    protected final void closeChannel() {
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

        state = State.WAIT_CLOSE;
        closeChannel();
    }

}
