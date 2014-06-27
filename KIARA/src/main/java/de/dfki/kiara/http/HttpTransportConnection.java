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

import de.dfki.kiara.TransportConnection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import java.io.IOException;
import java.net.SocketAddress;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpTransportConnection implements TransportConnection {

    static enum State {
        WAIT_CONNECT,
        CONNECTED,
        WAIT_CLOSE,
        CLOSED
    }

    private Channel channel;
    private State state;

    HttpTransportConnection(ChannelFuture connectFuture) {
        if (connectFuture == null) {
            throw new NullPointerException();
        }
        state = State.WAIT_CONNECT;
        connectFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                future.removeListener(this);
                if (state == State.WAIT_CONNECT) {
                    state = State.CONNECTED;
                    channel = future.channel();
                } else if (state == State.WAIT_CLOSE) {
                    closeChannel();
                }
            }

        });
    }

    public boolean isOpen() {
        return state == State.CONNECTED && channel != null;
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
        if (state == State.WAIT_CLOSE || state == State.CLOSED)
            return;

        System.err.println("state: " + state);
        System.err.println("channel: " + channel);

        state = State.WAIT_CLOSE;
        closeChannel();
    }

}
