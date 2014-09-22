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

package de.dfki.kiara;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public interface MessageIO {
    public void writeStructBegin(String name) throws IOException;
    public void writeStructEnd() throws IOException;
    public void writeFieldBegin(String name) throws IOException;
    public void writeFieldEnd() throws IOException;

    public void readStructBegin() throws IOException;
    public void readStructEnd() throws IOException;
    public void readFieldBegin(String name) throws IOException;
    public void readFieldEnd() throws IOException;

    public void writeArrayBegin(long size) throws IOException;
    public void writeArrayEnd() throws IOException;

    public void readArrayBegin(long size) throws IOException;
    public void readArrayEnd() throws IOException;

    /* Write boolean type to message */
    public void writeBoolean(boolean value) throws IOException;
    /* Read boolean type from message */
    public boolean readBoolean() throws IOException;

    /* Write i8 type to message */
    public void writeI8(byte value) throws IOException;
    /* Read i8 type from message */
    public byte readI8() throws IOException;

    /* Write u8 type to message */
    public void writeU8(byte value) throws IOException;
    /* Read u8 type from message */
    public byte readU8() throws IOException;

    /* Write i16 type to message */
    public void writeI16(short value) throws IOException;
    /* Read i16 type from message */
    public short readI16() throws IOException;

    /* Write u16 type to message */
    public void writeU16(short value) throws IOException;
    /* Read u16 type from message */
    public short readU16() throws IOException;

    /* Write i32 type to message */
    public void writeI32(int value) throws IOException;
    /* Read i32 type from message */
    public int readI32() throws IOException;

    /* Write u32 type to message */
    public void writeU32(int value) throws IOException;
    /* Read u32 type from message */
    public int readU32() throws IOException;

    /* Write i64 type to message */
    public void writeI64(long value) throws IOException;
    /* Read i64 type from message */
    public long readI64() throws IOException;

    /* Write i64 type to message */
    public void writeU64(long value) throws IOException;
    /* Read i64 type from message */
    public long readU64() throws IOException;

    /* Write float type to message */
    public void writeFloat(float value) throws IOException;
    /* Read float type from message */
    public float readFloat() throws IOException;

    /* Write float type to message */
    public void writeDouble(double value) throws IOException;
    /* Read float type from message */
    public double readDouble() throws IOException;

    /* Write zero terminated string to message */
    public void writeString(String value) throws IOException;

    /* Read zero terminated string from message */
    public String readString() throws IOException;

    public void writeBinary(ByteBuffer buffer) throws IOException;
    public ByteBuffer readBinary() throws IOException;
}
