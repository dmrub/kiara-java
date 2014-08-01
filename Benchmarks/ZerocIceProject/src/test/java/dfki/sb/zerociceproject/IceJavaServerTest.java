/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.zerociceproject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shahzad
 */
public class IceJavaServerTest {

    public IceJavaServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        String[] args = null;
        Runnable startServer = new Runnable() {
            @Override
            public void run() {
                IceJavaServer.main(null);
            }
        };
        new Thread(startServer).start();
        Thread.sleep(100);
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
     * Test of main method, of class IceJavaServer.
     */
    @Test
    public void testMain() {
        IceJavaClient.main(null);
    }
}
