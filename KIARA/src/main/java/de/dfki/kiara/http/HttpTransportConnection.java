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
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpTransportConnection implements TransportConnection {

    static enum State {
        UNINITIALIZED,
        WAIT_CONNECT,
        CONNECTED,
        WAIT_CLOSE,
        CLOSED
    }

    private final URI uri;
    private Channel channel;
    private State state;

    HttpTransportConnection(URI uri) {
        this.uri = uri;
        channel = null;
        state = State.UNINITIALIZED;
    }

    public void init(ChannelFuture connectFuture) {
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

                    // HACK

                    // Prepare the HTTP request.
                    String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
                    HttpRequest request = new DefaultFullHttpRequest(
                            HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
                    request.headers().set(HttpHeaders.Names.HOST, host);
                    request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
                    request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);

                    // Send the HTTP request.
                    channel.writeAndFlush(request);

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

    public void onContent(byte[] content, int offset, int size) {
        System.err.println("CONTENT: "+new String(content, offset, size));
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
