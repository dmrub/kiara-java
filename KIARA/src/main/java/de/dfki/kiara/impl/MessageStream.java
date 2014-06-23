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

import de.dfki.kiara.MessageIO;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class MessageStream implements MessageIO {

    @Override
    public void writeStructBegin(String name) throws IOException {
    }

    @Override
    public void writeStructEnd() throws IOException {
    }

    @Override
    public void writeFieldBegin(String name) throws IOException {
    }

    @Override
    public void writeFieldEnd() throws IOException {
    }

    @Override
    public void readStructBegin() throws IOException {
    }

    @Override
    public void readStructEnd() throws IOException {
    }

    @Override
    public void readFieldBegin(String name) throws IOException {
    }

    @Override
    public void readFieldEnd() throws IOException {
    }

    @Override
    public void writeArrayBegin(long size) throws IOException {
    }

    @Override
    public void writeArrayEnd() throws IOException {
    }

    @Override
    public void readArrayBegin(long size) throws IOException {
    }

    @Override
    public void readArrayEnd() throws IOException {
    }

    @Override
    public void writeBoolean(boolean value) throws IOException {
    }

    @Override
    public boolean readBoolean() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeI8(byte value) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte readI8() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeU8(byte value) throws IOException {
    }

    @Override
    public byte readU8() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeI16(short value) throws IOException {
    }

    @Override
    public short readI16() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeU16(short value) throws IOException {
    }

    @Override
    public short readU16() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeI32(int value) throws IOException {
    }

    @Override
    public int readI32() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeU32(int value) throws IOException {
    }

    @Override
    public int readU32() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeI64(long value) throws IOException {
    }

    @Override
    public long readI64() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeU64(long value) throws IOException {
    }

    @Override
    public long readU64() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeFloat(float value) throws IOException {
    }

    @Override
    public float readFloat() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeDouble(double value) throws IOException {
    }

    @Override
    public double readDouble() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeString(String value) throws IOException {
    }

    @Override
    public String readString() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBinary(ByteBuffer buffer) throws IOException {
    }

    @Override
    public ByteBuffer readBinary() throws IOException {
        throw new UnsupportedOperationException();
    }

}
