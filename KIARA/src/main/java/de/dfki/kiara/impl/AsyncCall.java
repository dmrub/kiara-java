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
package de.dfki.kiara.impl;

import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import de.dfki.kiara.Message;
import de.dfki.kiara.MessageConnection;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class AsyncCall {

    public static ListenableFuture<Message> performRemoteAsyncCall(final MessageConnection messageConnection, final Message request, ListeningExecutorService executor) {
        final ListenableFuture<Message> result = messageConnection.receive(request.getMessageId());
        final ListenableFuture<Void> reqSent = messageConnection.send(request);

        AsyncFunction<Void, Message> f = new AsyncFunction<Void, Message>() {

            @Override
            public ListenableFuture<Message> apply(Void input) throws Exception {
                return result;
            }
        };
        return Futures.transform(reqSent, f);
    }

}
