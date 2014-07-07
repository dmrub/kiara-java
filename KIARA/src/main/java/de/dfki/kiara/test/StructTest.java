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

import de.dfki.kiara.Connection;
import de.dfki.kiara.Context;
import de.dfki.kiara.GenericRemoteException;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.MethodBinder;
import de.dfki.kiara.RemoteInterface;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class StructTest {

    public static class Data {

        public long ival;
        public String sval;

        Data() {
        }

        public Data(long ival, String sval) {
            this.ival = ival;
            this.sval = sval;
        }
    }

    public static class Vec3f {

        public float x;
        public float y;
        public float z;

        public Vec3f() {
        }

        public Vec3f(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

    }

    public static class Quatf {

        public float r;
        public Vec3f v;

        public Quatf() {
        }

        public Quatf(float r, Vec3f v) {
            this.r = r;
            this.v = v;
        }
    }

    public static class Location {

        public Vec3f position;
        public Quatf rotation;

        public Location() {
        }

        public Location(Vec3f position, Quatf rotation) {
            this.position = position;
            this.rotation = rotation;
        }
    }

    public static interface StructTestIface {

        public Data pack(long ival, String sval);

        public long getInteger(Data data);

        public String getString(Data data);

        public Location getLocation();

        public void setLocation(Location location);

        void throwException(int code, String message) throws GenericRemoteException;
    }

    public static void main(String[] args) throws Exception {
        String uri;
        if (args.length > 0) {
            uri = args[0];
        } else {
            uri = "http://localhost:8080/service2";
        }

        System.out.format("Opening connection to %s...\n", uri);

        try (Context context = Kiara.createContext();
                Connection connection = context.openConnection(uri)) {

            MethodBinder<StructTestIface> binder
                    = new MethodBinder<>(StructTestIface.class)
                    .bind("StructTest.pack", "pack")
                    .bind("StructTest.getInteger", "getInteger")
                    .bind("StructTest.getString", "getString")
                    .bind("StructTest.setLocation", "setLocation")
                    .bind("StructTest.getLocation", "getLocation")
                    .bind("StructTest.throwException", "throwException");

            StructTestIface structTest = connection.generateClientFunctions(binder);
            RemoteInterface ri = (RemoteInterface) structTest;
            Connection c = ri.getConnection();

            {
                Data data = structTest.pack(21, "test21");
                System.out.format("StructTest.pack: result = { ival : %d, sval : \"%s\" }\n", data.ival, data.sval);
            }

            {
                Data data = new Data(25, "test25");
                long result = structTest.getInteger(data);
                System.out.format("StructTest.getInteger: result = %d\n", result);
            }

            {
                Data data = new Data(72, "test72");
                String result = structTest.getString(data);
                System.out.format("StructTest.getString: result = %s\n", result);
            }

            {
                Location location = new Location(new Vec3f(0.5f, 10.5f, 8.0f), new Quatf(0.707107f, new Vec3f(0.0f, 0.0f, 0.70710701f)));
                structTest.setLocation(location);
                System.out.println("StructTest.setLocation: DONE");
            }

            {
                Location location = structTest.getLocation();
                System.out.format("StructTest.getLocation: result = position %f %f %f rotation %f %f %f %f\n",
                        location.position.x,
                        location.position.y,
                        location.position.z,
                        location.rotation.r,
                        location.rotation.v.x,
                        location.rotation.v.y,
                        location.rotation.v.z);
            }

            try {
                structTest.throwException(1234, "error");
            } catch (GenericRemoteException ex) {
                System.err.format("Server exception raised: %d %s\n", ex.getErrorCode(), ex.getMessage());
            }

        } finally {
            Kiara.shutdownGracefully();
        }
    }
}
