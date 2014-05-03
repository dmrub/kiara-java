/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dfki.sb.apachethriftproject;

import benchmark.Benchmark;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
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
        String[] args = null;
        ThriftJavaServer.main(args);
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
    public void testClient() throws Exception{
        int numMessages = 1000;
        String host = "localhost";
        TTransport transport = new TSocket(host, 9090);
        TProtocol protocol = new TBinaryProtocol(transport);
        Benchmark.Client client = new Benchmark.Client(protocol);
        transport.open();
        long startTime = System.nanoTime();
        for (int i = 0; i < numMessages; i++) {
            // Send 10 MarketDatas for each QuoteRequest
            if (i % 10 == 5) {
                client.sendQuoteRequest(Util.createQuoteRequestData());
            } else {
                client.sendMarketData(Util.createMarketData());
            }
        }
        long finishTime = System.nanoTime();
        long difference = finishTime - startTime;
        transport.close();
        double latency = (double) difference / (numMessages * 2.0);
        System.out.println("\n\nAverage latency is " + String.format("%.3f",latency) + " nano seconds\n\n\n");    
        System.out.println("Finished");
    }
}
