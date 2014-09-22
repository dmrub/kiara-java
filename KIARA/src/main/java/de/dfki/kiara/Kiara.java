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
package de.dfki.kiara;

import de.dfki.kiara.http.HttpTransport;
import de.dfki.kiara.impl.ContextImpl;
import de.dfki.kiara.impl.ServiceImpl;
import de.dfki.kiara.impl.TransportServerImpl;
import de.dfki.kiara.jos.JosProtocol;
import de.dfki.kiara.jsonrpc.JsonRpcProtocol;
import de.dfki.kiara.tcp.TcpBlockTransport;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Kiara {

    private static final Logger logger = LoggerFactory.getLogger(Kiara.class);
    private static final List<RunningService> runningServices = new ArrayList<>();

    static {
        // initialize protocols
        ProtocolRegistry.registerProtocol("jsonrpc", JsonRpcProtocol.class);
        ProtocolRegistry.registerProtocol("javaobjectstream", JosProtocol.class);

        // initialize transports
        TransportRegistry.registerTransport(new TcpBlockTransport(/*secure = */false));
        TransportRegistry.registerTransport(new TcpBlockTransport(/*secure = */true));

        TransportRegistry.registerTransport(new HttpTransport(/*secure = */false));
        TransportRegistry.registerTransport(new HttpTransport(/*secure = */true));
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
        logger.info("shutdown {} services", tmp.size());
        for (RunningService s : tmp) {
            logger.info("shutdown {}", s);
            s.shutdownGracefully();
        }
    }

    public static Service createService(Context context) {
        return new ServiceImpl(context);
    }


}
