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

package de.dfki.kiara;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import java.io.Closeable;
import java.net.SocketAddress;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public interface TransportConnection extends Closeable {
    public SocketAddress getLocalAddress();
    public SocketAddress getRemoteAddress();

    public TransportMessage createRequest();

    /** Handler is executed in the unspecified thread.
     *
     * @param handler
     */
    public void addResponseHandler(Handler<TransportMessage> handler);

    /** Handler is executed in the unspecified thread.
     *
     * @param handler
     * @return
     */
    public boolean removeResponseHandler(Handler<TransportMessage> handler);

    public ListenableFuture<Void> send(TransportMessage message);

}
