/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.tcpsocketproject;

import dfki.sb.tcpsocketproject.model.Util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
public class TCPServerTest {

    public TCPServerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Runnable startServer = new Runnable() {
            @Override
            public void run() {
                try {
                    TCPServer.main(null);
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(TCPServerTest.class.getName()).log(Level.SEVERE, null, ex);
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
     * Test of main method, of class TCPServer.
     */
    @Test
    public void testClient() {
        try {
            Socket sender = new Socket("localhost", 8081);
            if (sender.isConnected()) {
                int numMessages = 1000;                
                ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(sender.getOutputStream()));
                oos.flush();                
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(sender.getInputStream()));
                long startTime = System.currentTimeMillis();
                for (int i = 0; i < numMessages; i++) {
                    // Send 10 MarketDatas for each QuoteRequest
                    if (i % 10 == 5) {
                        oos.writeInt(1);
                        oos.writeObject(Util.createQuoteRequestData());
                        oos.flush();                        
                        ois.readObject();   
                    } else {                        
                        oos.writeInt(2);
                        oos.writeObject(Util.createMarketData());
                        oos.flush();                        
                        ois.readObject();                        
                    }
                }                
                long finishTime = System.currentTimeMillis();
                oos.writeInt(-1);
                oos.flush();   
                oos.close();
                ois.close();
                long difference = finishTime - startTime;
                difference = difference * 1000;
                double latency = (double) difference / (numMessages * 2.0);
                System.out.println("\n\nAverage latency is " + String.format("%.3f", latency) + " microseconds\n\n\n");
                System.out.println("Finished");
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
