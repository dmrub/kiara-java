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

package de.dfki.kiara.netty;

import de.dfki.kiara.Handler;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.GenericFutureListener;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class AsyncCallbackAdapter implements GenericFutureListener<ChannelFuture> {
    private final Handler<Void> callback;

    public AsyncCallbackAdapter(Handler<Void> callback) {
        if (callback == null)
            throw new NullPointerException("callback");
        this.callback = callback;
    }

    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess())
            callback.onSuccess(null);
        else if (future.cause() != null)
            callback.onFailure(future.cause());
        else {
            callback.onFailure(null);
        }
    }

}
