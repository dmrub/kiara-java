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
import de.dfki.kiara.AsyncHandler;
import de.dfki.kiara.TransportConnection;
import de.dfki.kiara.TransportMessage;
import de.dfki.kiara.netty.AsyncCallbackAdapter;
import de.dfki.kiara.netty.ListenableConstantFutureAdapter;
import de.dfki.kiara.netty.AbstractTransportConnection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class HttpTransportConnection extends AbstractTransportConnection {

    private static final Logger logger = LoggerFactory.getLogger(HttpTransportConnection.class);

    static enum State {

        UNINITIALIZED,
        WAIT_CONNECT,
        CONNECTED,
        WAIT_CLOSE,
        CLOSED
    }

    private final URI uri;
    private final HttpMethod method;
    private Channel channel;
    private State state;
    private Throwable error;
    private final List<AsyncHandler<TransportMessage>> handlers = new ArrayList<>();

    HttpTransportConnection(URI uri, HttpMethod method) {
        this.uri = uri;
        this.method = method;
        this.channel = null;
        this.state = State.UNINITIALIZED;
        this.error = null;
    }

    public void init(Channel channel) {
        if (channel == null) {
            throw new NullPointerException();
        }
        this.channel = channel;
        switch (state) {
            case UNINITIALIZED:
            case WAIT_CONNECT:
                state = State.CONNECTED;
                break;
            case WAIT_CLOSE:
                closeChannel();
                break;
            default:
                throw new IllegalStateException();
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
    public Future<TransportMessage> receive(AsyncHandler<TransportMessage> callback) {
        return null;
    }

    @Override
    public ListenableFuture<Void> send(TransportMessage msg) {
        if (msg == null) {
            throw new NullPointerException("msg");
        }
        if (!(msg instanceof HttpRequestMessage)) {
            throw new IllegalArgumentException("msg is not of type HttpRequestMessage");
        }
        HttpRequestMessage httpMsg = (HttpRequestMessage) msg;

        if (state != State.CONNECTED || channel == null) {
            throw new IllegalStateException("state=" + state.toString() + " channel=" + channel);
        }

        FullHttpRequest request = httpMsg.finalizeRequest();

        if (logger.isDebugEnabled()) {
            logger.debug("SEND CONTENT: {}", request.content().toString(StandardCharsets.UTF_8));
        }

        ChannelFuture result = channel.writeAndFlush(request);
        return new ListenableConstantFutureAdapter<>(result, null);
    }

    public void doGET() {
        if (state != State.CONNECTED || channel == null) {
            throw new IllegalStateException("state=" + state.toString() + " channel=" + channel);
        }
        // Prepare the HTTP request.
        String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        HttpRequest request = new DefaultFullHttpRequest(
                HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
        request.headers().set(HttpHeaders.Names.HOST, host);
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);

        // Send the HTTP request.
        channel.writeAndFlush(request);
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

    public void onResponse(HttpResponseMessage response) {
        if (logger.isDebugEnabled()) {
            logger.debug("RECEIVED CONTENT {}", new String(response.getPayload().array(), response.getPayload().arrayOffset(), response.getPayload().remaining()));
        }
        for (AsyncHandler<TransportMessage> handler : handlers) {
            handler.onSuccess(response);
        }
    }

    public void onErrorResponse(Throwable error) {
        for (AsyncHandler<TransportMessage> handler : handlers) {
            handler.onFailure(error);
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

        System.err.println("state: " + state);
        System.err.println("channel: " + channel);

        state = State.WAIT_CLOSE;
        closeChannel();
    }

    @Override
    public void addResponseHandler(AsyncHandler<TransportMessage> handler) {
        handlers.add(handler);
    }

    @Override
    public void removeResponseHandler(AsyncHandler<TransportMessage> handler) {
        handlers.remove(handler);
    }

}
