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

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 * @param <V>
 */
public interface Handler<V> {

    /**
     * Invoked with the result of the computation when it is
     * successful. Returns true when result is processed, and false otherwise.
     *
     * @param result
     * @return
     */
    public boolean onSuccess(V result);

    /**
     * Invoked when a computation fails or is canceled.
     * Returns true when result is processed, and false otherwise.
     *
     * @param t
     * @return
     */
    public boolean onFailure(Throwable t);
}