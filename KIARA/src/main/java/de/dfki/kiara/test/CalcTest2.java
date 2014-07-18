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
package de.dfki.kiara.test;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import de.dfki.kiara.Connection;
import de.dfki.kiara.Context;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Message;
import de.dfki.kiara.MethodBinding;
import de.dfki.kiara.RemoteInterface;
import java.nio.ByteBuffer;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class CalcTest2 {

    public static interface Calc {

        public ListenableFuture<Integer> add(ListenableFuture<Integer> a, ListenableFuture<Integer> b);

        public ListenableFuture<Integer> add1(ListenableFuture<Integer> a, int b);

        public float addFloat(float a, float b);

        public int stringToInt32(String s);

        public String int32ToString(int i);

        public de.dfki.kiara.Message add_serializer(int a, int b);

        public int add_deserializer(de.dfki.kiara.Message msg);
    }

    public static void main(String[] args) throws Exception {
        try (Context context = Kiara.createContext();
                Connection connection = context.openConnection("http://localhost:8080/service")) {

            MethodBinding<Calc> binder
                    = new MethodBinding<>(Calc.class)
                    .bind("calc.add", "add")
                    .bind("calc.add", "add1")
                    .bind("calc.add", "add_serializer")
                    .bind("calc.add", "add_deserializer");

            Calc calc = connection.generateClientFunctions(binder);
            RemoteInterface ri = (RemoteInterface) calc;
            Connection conn = ri.getConnection();

            Message msg = calc.add_serializer(10, 12);
            System.out.println("Message data: " + new String(msg.getMessageData().array()));

            msg = calc.add_serializer(2, 30);
            System.out.println("Message data: " + new String(msg.getMessageData().array()));

            int a = 3;
            int b = 4;
            int c = 10;
            ListenableFuture<Integer> temp = calc.add(
                    Futures.<Integer>immediateFuture(a),
                    Futures.<Integer>immediateFuture(b));
            int result = calc.add(temp, Futures.<Integer>immediateFuture(c)).get();

            System.out.println("Performed remote call: " + a + "+" + b + " = " + result);

            temp = calc.add1(
                    Futures.<Integer>immediateFuture(a),
                    b);
            System.out.println("Performed remote call: " + a + "+" + b + " = " + temp.get());

            String res = "{\"jsonrpc\":\"2.0\", \"result\": 22}";
            Message responseMsg = msg.getProtocol().createResponseMessageFromData(ByteBuffer.wrap(res.getBytes("UTF-8")),
                    Calc.class.getMethod("add_deserializer", new Class<?>[]{de.dfki.kiara.Message.class}).getReturnType());
            result = calc.add_deserializer(responseMsg);
            System.out.println("Result of deserialization of: " + res + " -> " + result);

        } finally {
            Kiara.shutdownGracefully();
        }
    }
}
