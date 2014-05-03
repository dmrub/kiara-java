/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.zerociceproject;

/**
 *
 * @author Shahzad
 */
public class IceJavaServer {

    public static void startServer() {
        int status = 0;
        Ice.Communicator ic = null;
        try {
            ic = Ice.Util.initialize();
            Ice.ObjectAdapter adapter = ic.createObjectAdapterWithEndpoints(
                    "BenchmarkAdapter", "tcp -p 10000");
            adapter.add(new BenchmarkI(), ic.stringToIdentity("Benchmark"));
            System.out.println("Starting Ice server...");
            adapter.activate();
            System.out.println("Started Ice server...");
            ic.waitForShutdown();
        } catch (Ice.LocalException e) {
            e.printStackTrace();
            status = 1;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            status = 1;
        }
        if (ic != null) {
            // Clean up
            //
            try {
                ic.destroy();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                status = 1;
            }
        }
        System.exit(status);

    }

    public static void main(String[] args) {
        try {
            Runnable startServer = new Runnable() {
                public void run() {
                    startServer();
                }
            };
            new Thread(startServer).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
