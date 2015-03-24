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

import de.dfki.kiara.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Dmitri Rubinstein on 29/9/14.
 */
@RunWith(Parameterized.class)
public class ListTest {

    public static class KeyValuePair implements Serializable {

        private String key;
        private String value;

        public KeyValuePair() {
        }

        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof KeyValuePair))
                return false;
            KeyValuePair other = (KeyValuePair)obj;
            return other.key.equals(this.key) && other.value.equals(this.value);
        }

        @Override
        public String toString() {
            return "KeyValuePair("+key+", "+value+")";
        }
    }

    public static interface GetSetKV {

        public void setKeyValuePairs(List<KeyValuePair> keyValuePairs);

        public List<KeyValuePair> getKeyValuePairs();
    }

    public static class GetSetKVImpl {

        private List<KeyValuePair> keyValuePairs = null;

        public void setKeyValuePairs(List<KeyValuePair> keyValuePairs) {
            int num = keyValuePairs.size();
            for (int i = 0; i < num; ++i) {
                System.out.printf("KeyValue %s %s%n",
                        keyValuePairs.get(i).getKey(),
                        keyValuePairs.get(i).getValue());
            }

            this.keyValuePairs = keyValuePairs;
        }

        public List<KeyValuePair> getKeyValuePairs() {
            return keyValuePairs;
        }
    }

    public static class ListTestSetup extends TestSetup<GetSetKV> {

        public ListTestSetup(int port, String transport, String protocol, String configPath) {
            super(port, transport, protocol, configPath);
        }

        @Override
        protected Server createServer(Context context, int port, String transport, String protocol, String configPath) throws Exception {
            Service service = context.createService();
            service.loadServiceIDLFromString("KIARA",
                    "namespace * listtest "
                            + "struct KeyValue {"
                            + " string key, "
                            + " string value "
                            + "} "
                            + "service listtest { "
                            + "  void setKeyValuePairs(array<KeyValue> keyValuePairs); "
                            + "  array<KeyValue> getKeyValuePairs(); "
                            + "} "
            );

            System.out.printf("Register server functions ....%n");
            GetSetKVImpl impl = new GetSetKVImpl();
            service.registerServiceFunction("listtest.setKeyValuePairs", impl, "setKeyValuePairs");
            service.registerServiceFunction("listtest.getKeyValuePairs", impl, "getKeyValuePairs");
            System.out.printf("Starting server...%n");

            Server server = context.createServer("0.0.0.0", port, "/service");
            if ("http".equals(transport))
                server.addService("/rpc/listtest", protocol, service);
            else if ("tcp".equals(transport))
                server.addService("tcp://0.0.0.0:53212", protocol, service);
            else if ("ws".equals(transport))
                server.addService("ws://0.0.0.0:9090/websocket", protocol, service);
            else
                throw new IllegalArgumentException("Unknown transport "+transport);
            return server;
        }

        @Override
        protected GetSetKV createClient(Connection connection) throws Exception {
            MethodBinding<GetSetKV> binder
                    = new MethodBinding<GetSetKV>(GetSetKV.class)
                    .bind("listtest.setKeyValuePairs", "setKeyValuePairs")
                    .bind("listtest.getKeyValuePairs", "getKeyValuePairs");

            return connection.getServiceInterface(binder);
        }
    }

    private final ListTestSetup listTestSetup;
    private GetSetKV getSetKV = null;

    @Before
    public void setUp() throws Exception {
        getSetKV = listTestSetup.start(100);
    }

    @After
    public void tearDown() throws Exception {
        listTestSetup.shutdown();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Kiara.shutdownGracefully();
    }

    @Parameterized.Parameters
    public static Collection configs() {
        Object[][] data = new Object[][] {
                { "http", "jsonrpc" },
                { "http", "javaobjectstream" },
                { "tcp", "jsonrpc" },
                { "tcp", "javaobjectstream" },
                { "ws", "jsonrpc" },
                { "ws", "javaobjectstream" }
        };
        return Arrays.asList(data);
    }

    private final int PORT = 8080;

    public ListTest(String transport, String protocol) {
        listTestSetup =  new ListTestSetup(PORT, transport, protocol, "/service");
    }

    /**
     * Test of main method, of class AosTestServer.
     */
    @Test
    public void testList() throws Exception {

        List<KeyValuePair> kvPairs;
        {
            int num = 10;
            kvPairs = new ArrayList<>(num);
            for (int i = 0; i < num; ++i) {
                KeyValuePair kv = new KeyValuePair(
                        "key"+i,
                        "value"+i);
                kvPairs.add(kv);
            }
        }

        getSetKV.setKeyValuePairs(kvPairs);

        assertEquals(kvPairs, getSetKV.getKeyValuePairs());
    }

}
