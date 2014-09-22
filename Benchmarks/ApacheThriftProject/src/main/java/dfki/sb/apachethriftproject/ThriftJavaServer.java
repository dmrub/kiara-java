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
package dfki.sb.apachethriftproject;

import benchmark.Benchmark;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TTransportFactory;

/**
 *
 * @author Shahzad
 */
public class ThriftJavaServer {

    public static BenchmarkHandler handler;
    public static Benchmark.Processor processor;
    static TProtocolFactory protocolFactory;
    static TTransportFactory transportFactory;

    public static void main(String[] args) {
        try {
            handler = new BenchmarkHandler();
            processor = new Benchmark.Processor(handler);
            startServer(processor);
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
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
}
