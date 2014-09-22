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
package dfki.sb.tcpsocketproject;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Shahzad
 */
public class TCPObjectServerTest {

    public TCPObjectServerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Runnable startServer = new Runnable() {
            @Override
            public void run() {
                try {
                    TCPObjectServer.main(null);
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(TCPObjectServerTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        new Thread(startServer).start();      
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
     * Test of main method, of class TCPObjectServer.
     */
    @Test
    public void testClient() {
        TCPObjectClient.main(null);
    }
}
