/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dfki.sb.apachethriftproject;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Shahzad
 */
public class ThriftJavaServerTest {
    
    public ThriftJavaServerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        Runnable startServer = new Runnable() {
            @Override
            public void run() {
                String[] args = null;
                ThriftJavaServer.main(args);
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
     * Test Client.
     */
    @Test
    public void testClient(){
        ThriftJavaClient.main(null);
    }
}
