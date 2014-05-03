/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dfki.sb.apachethriftproject;
import benchmark.Benchmark;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;
/**
 *
 * @author Shahzad
 */
public class ThriftJavaServer {
    public static BenchmarkHandler handler;
    public static Benchmark.Processor  processor;
    static TProtocolFactory protocolFactory;
    static TTransportFactory transportFactory;
    public static void main(String[] args) {
        try {
            handler = new BenchmarkHandler();
            processor = new Benchmark.Processor(handler);
            Runnable startServer = new Runnable() {
                public void run() {
                    startServer(processor);
                }
            };
            new Thread(startServer).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
    
    public static void startServer(Benchmark.Processor processor) {
        try {
            System.out.println("Starting ApacheThrift server...");
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
            System.out.println("ApacheThrift server started...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
  }
}
