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

package de.dfki.kiara.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 * @param <V>
 */
public class FutureResult<V> implements java.util.concurrent.Future<V> {

    private final V value;
    private final boolean cancelled;
    private final boolean done;

    public FutureResult(boolean cancelled, boolean done) {
        this.value = null;
        this.cancelled = cancelled;
        this.done = done;
    }

    public FutureResult(V value, boolean cancelled, boolean done) {
        this.value = value;
        this.cancelled = cancelled;
        this.done = done;
    }

    @Override
    public boolean cancel(boolean bln) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return value;
    }

    @Override
    public V get(long l, TimeUnit tu) throws InterruptedException, ExecutionException, TimeoutException {
        return value;
    }

}
