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
package de.dfki.kiara.test;

import de.dfki.kiara.Connection;
import de.dfki.kiara.Context;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.MethodBinding;
import de.dfki.kiara.RemoteInterface;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class VectorTest {


    public static class Vector {

        public float x;
        public float y;
        public float z;

        public Vector() {
        }

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static interface Example {
        public Vector addVectors(Vector a, Vector b);
    }

    public static void main(String[] args) throws Exception {
        String uri;
        if (args.length > 0) {
            uri = args[0];
        } else {
            uri = "http://134.96.220.21/kiara_test/service";
        }

        System.out.format("Opening connection to %s...\n", uri);

        try (Context context = Kiara.createContext();
                Connection connection = context.openConnection(uri)) {

            MethodBinding<Example> binder
                    = new MethodBinding<>(Example.class)
                    .bind("example.addVectors", "addVectors");

            Example example = connection.generateClientFunctions(binder);
            RemoteInterface ri = (RemoteInterface) example;
            Connection c = ri.getConnection();

            {
                Vector result = example.addVectors(new Vector(1,2,3), new Vector(3,2,1));
                System.out.format("example.addVectors: result = %f %f %f\n", result.x, result.y, result.z);
            }

        } finally {
            Kiara.shutdownGracefully();
        }
    }
}
