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
package de.dfki.kiara.example;

import com.google.common.primitives.UnsignedLong;
import com.google.common.primitives.UnsignedLongs;
import de.dfki.kiara.Connection;
import de.dfki.kiara.Context;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.MethodBinding;
import de.dfki.kiara.RemoteInterface;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class BasetypeTest {

    public static interface Basetype {

        public boolean send_boolean(boolean a);

        public byte send_i8(byte a);

        public int send_u8(int a);

        public short send_i16(short a);

        public int send_u16(int a);

        public int send_i32(int a);

        public long send_u32(long a);

        public long send_i64(long a);

        public long send_u64(long a);

        public float send_float(float a);

        public double send_double(double a);

        public String send_string(String a);
    }

    public static void main(String[] args) throws Exception {
        String uri;
        if (args.length > 0) {
            uri = args[0];
        } else {
            uri = "http://localhost:8080/service";
        }

        System.out.format("Opening connection to %s...%n", uri);

        try (Context context = Kiara.createContext();
                Connection connection = context.openConnection(uri)) {

            MethodBinding<Basetype> binder
                    = new MethodBinding<>(Basetype.class)
                    .bind("basetype.send_boolean", "send_boolean")
                    .bind("basetype.send_i8", "send_i8")
                    .bind("basetype.send_u8", "send_u8")
                    .bind("basetype.send_i16", "send_i16")
                    .bind("basetype.send_u16", "send_u16")
                    .bind("basetype.send_i32", "send_i32")
                    .bind("basetype.send_u32", "send_u32")
                    .bind("basetype.send_i64", "send_i64")
                    .bind("basetype.send_u64", "send_u64")
                    .bind("basetype.send_float", "send_float")
                    .bind("basetype.send_double", "send_double")
                    .bind("basetype.send_string", "send_string");

            Basetype basetype = connection.generateClientFunctions(binder);
            RemoteInterface ri = (RemoteInterface) basetype;
            Connection c = ri.getConnection();

            {
                boolean result = basetype.send_boolean(true);
                System.out.format("basetype.send_boolean: result = %d%n", result ? 1 : 0);
            }

            {
                byte result = basetype.send_i8((byte) -128);
                System.out.format("basetype.send_i8: result = %d%n", result);
            }

            {
                int result = basetype.send_u8(255);
                System.out.format("basetype.send_u8: result = %d%n", result);
            }

            {
                short result = basetype.send_i16((short) -32768);
                System.out.format("basetype.send_i16: result = %d%n", result);
            }

            {
                int result = basetype.send_u16(65535);
                System.out.format("basetype.send_u16: result = %d%n", result);
            }

            {
                int result = basetype.send_i32((int)-2147483648);
                System.out.format("basetype.send_i32: result = %d%n", result);
            }

            {
                long result = basetype.send_u32(4294967295l);
                System.out.format("basetype.send_u32: result = %d%n", result);
            }

            {
                long result = basetype.send_i64((long)-9223372036854775807L-1);
                System.out.format("basetype.send_i64: result = %d%n", result);
            }

            {
                UnsignedLong result = UnsignedLong.fromLongBits(basetype.send_u64(UnsignedLongs.parseUnsignedLong("18446744073709551615")));
                System.out.format("basetype.send_u64: result = %s%n", result);
            }

            {
                float result = basetype.send_float(64.132004f);
                System.out.format("basetype.send_float: result = %f%n", result);
            }

            {
                double result = basetype.send_double(21164.103021);
                System.out.format("basetype.send_double: result = %f%n", result);
            }

            {
                String result = basetype.send_string("test string");
                System.out.format("basetype.send_string: result = %s%n", result);
            }

        } finally {
            Kiara.shutdownGracefully();
        }
    }
}
