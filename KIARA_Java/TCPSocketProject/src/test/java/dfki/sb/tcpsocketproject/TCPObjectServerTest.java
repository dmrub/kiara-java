/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
