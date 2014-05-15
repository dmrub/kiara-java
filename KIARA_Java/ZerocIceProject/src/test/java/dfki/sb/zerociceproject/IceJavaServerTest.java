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
    public static void setUpClass() {
        String[] args = null;
        IceJavaServer.main(args);
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
        int numMessages = 1000;
        String host = "localhost";
        long startTime = 0;
        Ice.Communicator ic = null;
        try {
            ic = Ice.Util.initialize(new String[]{});
            Ice.ObjectPrx base = ic.stringToProxy("Benchmark:tcp -p 10000 -h "+host);
            dfki.sb.zerociceproject.Main.BenchmarkPrx benchmark = dfki.sb.zerociceproject.Main.BenchmarkPrxHelper.checkedCast(base);
            assertFalse(benchmark == null);
            startTime = System.currentTimeMillis();
            for (int i = 0; i < numMessages; i++) {
                // Send 10 MarketDatas for each QuoteRequest
                if (i % 10 == 5) {
                    benchmark.sendQuoteRequest(Util.createQuoteRequestData());
                } else {
                    benchmark.sendMarketData(Util.createMarketData());
                }
            }
        } catch (Ice.LocalException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        long finishTime = System.currentTimeMillis();
        long difference = finishTime - startTime;
        difference = difference * 1000;
        if (ic != null) {
            // Clean up
            //
            try {
                ic.destroy();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        double latency = (double) difference / (numMessages * 2.0);
        System.out.println("\n\nAverage latency is " + String.format("%.3f", latency) + " microseconds\n\n");
        System.out.println("\t\tFinished");
    }
}
