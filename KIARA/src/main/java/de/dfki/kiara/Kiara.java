/*
 * Copyright (C) 2014 German Research Center for Artificial Intelligence (DFKI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.dfki.kiara;

import de.dfki.kiara.http.HttpTransport;
import de.dfki.kiara.impl.ContextImpl;
import de.dfki.kiara.impl.TransportServerImpl;
import de.dfki.kiara.impl.KiaraServiceImpl;
import de.dfki.kiara.impl.ServiceImpl;
import de.dfki.kiara.jos.JosProtocol;
import de.dfki.kiara.jsonrpc.JsonRpcProtocol;
import de.dfki.kiara.tcp.TcpBlockTransport;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Kiara {

    private static final List<RunningService> runningServices = new ArrayList<>();

    static {
        // initialize protocols
        ProtocolRegistry.registerProtocol("jsonrpc", JsonRpcProtocol.class);
        ProtocolRegistry.registerProtocol("javaobjectstream", JosProtocol.class);

        // initialize transports
        TcpBlockTransport tcpTransport = new TcpBlockTransport();
        TransportRegistry.registerTransport("tcp", tcpTransport);
        TransportRegistry.registerTransport("tcps", tcpTransport);

        HttpTransport httpTransport = new HttpTransport();
        TransportRegistry.registerTransport("http", httpTransport);
        TransportRegistry.registerTransport("https", httpTransport);
    }

    public static void init() {
    }

    public static TransportServer createTransportServer() {
        try {
            return new TransportServerImpl();
        } catch (CertificateException ex) {
            throw new RuntimeException(ex);
        } catch (SSLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Context createContext() {
        return new ContextImpl();
    }

    public static void addRunningService(RunningService service) {
        synchronized (runningServices) {
            runningServices.add(service);
        }
    }

    public static void removeRunningService(RunningService service) {
        synchronized (runningServices) {
            runningServices.remove(service);
        }
    }

    public static void shutdownGracefully() {
        List<RunningService> tmp;
        synchronized (runningServices) {
            tmp = new ArrayList<>(runningServices);
        }
        for (RunningService s : tmp) {
            s.shutdownGracefully();
        }
    }

    public static KiaraService createService(Context context) {
        return new KiaraServiceImpl(context);
    }


}
