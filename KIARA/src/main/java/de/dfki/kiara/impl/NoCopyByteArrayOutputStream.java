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

package de.dfki.kiara.impl;

import java.io.ByteArrayOutputStream;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class NoCopyByteArrayOutputStream extends ByteArrayOutputStream {

    public NoCopyByteArrayOutputStream() {
    }

    public NoCopyByteArrayOutputStream(int size) {
        super(size);
    }

    /**
     * Retrieves the underlying byte array used as this stream's buffer.
     * Unlike ByteArrayOutputStream.toByteArray(), this method does not create a copy of the buffer,
     * but return reference to the underlying byte array.
     *
     * @return     Underlying byte array by reference.
     */
    @Override
    public byte[] toByteArray() {
        return buf;
    }
}
