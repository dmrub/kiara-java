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
package de.dfki.kiara.example;

import de.dfki.kiara.Connection;
import de.dfki.kiara.Context;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.MethodBinding;
import de.dfki.kiara.RemoteInterface;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class CalcTest {

    public static interface Calc {

        public int add(int a, int b);

        public float addFloat(float a, float b);

        public int stringToInt32(String s);

        public String int32ToString(int i);

        public Integer stringToInt32_CharSequence(CharSequence s);

        public CharSequence int32ToString_Integer(Integer i);
    }

    public static void main(String[] args) throws Exception {
        String uri;
        if (args.length > 0) {
            uri = args[0];
        } else {
            uri = "http://localhost:8080/service";
        }

        System.out.format("Opening connection to %s...\n", uri);

        try (Context context = Kiara.createContext();
                Connection connection = context.openConnection(uri)) {

            MethodBinding<Calc> binder
                    = new MethodBinding<>(Calc.class)
                    .bind("calc.add", "add")
                    .bind("calc.addf", "addFloat")
                    .bind("calc.stringToInt32", "stringToInt32")
                    .bind("calc.int32ToString", "int32ToString")
                    .bind("calc.stringToInt32", "stringToInt32_CharSequence")
                    .bind("calc.int32ToString", "int32ToString_Integer");

            Calc calc = connection.getServiceInterface(binder);
            RemoteInterface ri = (RemoteInterface) calc;
            Connection c = ri.getConnection();

            {
                int result = calc.add(21, 32);
                System.out.format("calc.add: result = %d\n", result);
            }

            {
                float result = calc.addFloat(21, 32);
                System.out.format("calc.addf: result = %f\n", result);
            }

            {
                int result = calc.stringToInt32("-125");
                System.out.format("calc.stringToInt32: result = %d\n", result);
            }

            {
                String result = calc.int32ToString(-42);
                System.out.format("calc.int32ToString: result = %s\n", result);
            }

            {
                Integer result = calc.stringToInt32_CharSequence("521");
                System.out.format("calc.stringToInt32: result = %d\n", result);
            }

            {
                CharSequence result = calc.int32ToString_Integer(142);
                System.out.format("calc.int32ToString: result = %s\n", result);
            }

        } finally {
            Kiara.shutdownGracefully();
        }
    }
}
