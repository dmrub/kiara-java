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
package dfki.sb.rabbitmqjava;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class RabbitMQObjectStreamServerTest {

    public RabbitMQObjectStreamServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        Runnable startServer = new Runnable() {
            @Override
            public void run() {
                String[] argv = null;
                RabbitMQObjectStreamServer.main(argv);
            }
        };
        new Thread(startServer).start();
        Thread.sleep(1000);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test Client.
     */
    @Test
    public void testMain() {
        RabbitMQObjectStreamClient.main(null);  
    }

}
