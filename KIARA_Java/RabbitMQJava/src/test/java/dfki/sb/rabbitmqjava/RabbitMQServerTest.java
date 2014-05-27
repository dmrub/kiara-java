/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.rabbitmqjava;

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
 * @author Administrator
 */
public class RabbitMQServerTest {

    public RabbitMQServerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws InterruptedException {
        Runnable startServer = new Runnable() {
            @Override
            public void run() {
                String[] argv = null;
                RabbitMQServer.main(argv);
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
        RabbitMQClient.main(null);
    }

}
