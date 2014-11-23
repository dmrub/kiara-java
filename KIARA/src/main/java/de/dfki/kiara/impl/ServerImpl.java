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
package de.dfki.kiara.impl;

import com.google.common.base.Objects;
import de.dfki.kiara.InvalidAddressException;
import de.dfki.kiara.Kiara;
import de.dfki.kiara.Server;
import de.dfki.kiara.ServerConnectionListener;
import de.dfki.kiara.Service;
import de.dfki.kiara.TransportFactory;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportConnectionListener;
import de.dfki.kiara.TransportFactoryRegistry;
import de.dfki.kiara.TransportServer;
import de.dfki.kiara.config.ServerConfiguration;
import de.dfki.kiara.config.ServerInfo;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shahzad, Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServerImpl implements Server, TransportConnectionListener {

    private final String configHost;
    private final int configPort;
    private final String configPath;
    private final URI configUri;
    private final Set<ServiceImpl> services;
    private final List<TransportAddressAndServiceHandler> serviceHandlers;
    private final Map<HostAndPort, TransportEntry> transportEntries;
    private final TransportServer transportServer;
    private final IdentityHashMap<Transport, ServerConnectionHandler> connectionHandlers;
    private final List<ServerConnectionListener> eventListeners;

    private static class HostAndPort implements Comparable<HostAndPort> {

        public final String host;
        public final int port;

        public HostAndPort(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public int compareTo(HostAndPort o) {
            if (o == null) {
                return -1;
            }

            int cmp = host == null ? (o.host == null ? 0 : -1) : (o.host == null ? +1
                    : (host).compareTo(o.host));

            return cmp == 0 ? Integer.compare(port, o.port) : cmp;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other instanceof HostAndPort) {
                HostAndPort that = (HostAndPort) other;
                return Objects.equal(this.host, that.host) && this.port == that.port;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (host == null ? 0 : host.hashCode()) ^ port;
        }
    }

    private static class TransportEntry {

        public final TransportFactory transport;
        public int numServices;

        public TransportEntry(TransportFactory transport, int numServices) {
            this.transport = transport;
            this.numServices = numServices;
        }

        public TransportEntry(TransportFactory transport) {
            this(transport, 0);
        }

    }

    private static final Logger logger = LoggerFactory.getLogger(ServerImpl.class);

    public ServerImpl(String host, int port, String configPath) throws IOException, URISyntaxException {
        this.configHost = host;
        this.configPort = port;
        this.configPath = configPath;
        this.configUri = new URI("http://" + configHost + ":" + Integer.toString(configPort) + "/" + configPath).normalize();
        this.services = new HashSet<>();
        this.serviceHandlers = new ArrayList<>();
        this.transportEntries = new HashMap<>();
        this.transportServer = Kiara.createTransportServer();
        this.connectionHandlers = new IdentityHashMap<>();
        this.eventListeners = new ArrayList<>();
        // listen for negotiation connection
        addPortListener(configHost, configPort, this.configUri.getPath(), "http");
    }

    public URI getConfigUri() {
        return configUri;
    }

    private void addPortListener(String host, int port, String path, String transportName) throws IOException {
        logger.debug("addPortListener: {} : {} {}", host, port, transportName);

        HostAndPort hostAndPort = new HostAndPort(host, port);
        TransportEntry entry = transportEntries.get(hostAndPort);
        if (entry != null) {
            if (!transportName.equals(entry.transport.getName())) {
                throw new IOException("Port " + port + " already bound to transport " + entry.transport.getName());
            }
            return;
        }

        final TransportFactory transport = TransportFactoryRegistry.getTransportFactoryByName(transportName);
        transportEntries.put(hostAndPort, new TransportEntry(transport));

        transportServer.listen(host, Integer.toString(port), path, transport, this);
    }

    @Override
    public void addService(String path, String protocol, Service service) throws IOException {
        URI uri = configUri.resolve(path);

        final ServiceImpl serviceImpl = (ServiceImpl) service;

        logger.debug("Server::addService: {}", uri);

        TransportFactory transport = TransportFactoryRegistry.getTransportFactoryByName(uri.getScheme());

        if (transport == null) {
            throw new IOException("Unsupported transport: " + uri.getScheme());
        }

        ServiceHandler handler;
        try {
            handler = new ServiceHandler(serviceImpl, protocol);
        } catch (InstantiationException ex) {
            throw new IOException(ex);
        } catch (IllegalAccessException ex) {
            throw new IOException(ex);
        }

        // FIXME this will fail when transport changes for a given host/port combination
        addPortListener(uri.getHost(), uri.getPort(), uri.getPath(), uri.getScheme());

        TransportAddress address;
        try {
            address = transport.createAddress(uri.toString());
        } catch (InvalidAddressException ex) {
            throw new IOException(ex);
        } catch (UnknownHostException ex) {
            throw new IOException(ex);
        }

        logger.debug("Register: {} {}", uri.getScheme(), address.toString());

        boolean added = false;

        synchronized (serviceHandlers) {
            for (TransportAddressAndServiceHandler element : serviceHandlers) {
                if (element.transportAddress.equals(address)) {
                    if (element.serviceHandler != null) {
                        element.serviceHandler.close();
                        element.serviceHandler = handler;
                        added = true;
                    }
                }
            }

            if (!added) {
                serviceHandlers.add(new TransportAddressAndServiceHandler(address, handler));
            }
        }

        synchronized (services) {
            services.add(serviceImpl);
        }
    }

    @Override
    public boolean removeService(Service service) {
        boolean removed = false;
        synchronized (serviceHandlers) {
            for (Iterator<TransportAddressAndServiceHandler> iter = serviceHandlers.iterator();
                    iter.hasNext();) {
                TransportAddressAndServiceHandler element = iter.next();
                if (element.serviceHandler != null) {
                    if (element.serviceHandler.getService().equals(service)) {
                        element.serviceHandler.close();
                        iter.remove();
                        removed = true;
                    }
                }
            }
        }
        synchronized (services) {
            services.remove(service);
        }
        return removed;
    }

    @Override
    public void run() throws IOException {
        transportServer.run();
    }

    @Override
    public void close() throws IOException {
        transportServer.close();
        synchronized (serviceHandlers) {
            for (TransportAddressAndServiceHandler element : serviceHandlers) {
                if (element.serviceHandler != null) {
                    element.serviceHandler.close();
                    element.serviceHandler = null;
                }
            }
            serviceHandlers.clear();
        }
    }

    public ServiceHandler findAcceptingServiceHandler(TransportAddress transportAddress) {
        synchronized (serviceHandlers) {
            for (TransportAddressAndServiceHandler element : serviceHandlers) {
                if (element.transportAddress.acceptsConnection(transportAddress)) {
                    return element.serviceHandler;
                }
            }
        }
        return null;
    }

    private List<TransportAddressAndServiceHandler> findAllServiceHandlers(TransportAddress transportAddress) {
        List<TransportAddressAndServiceHandler> handlers = new ArrayList<>();
        synchronized (serviceHandlers) {
            for (TransportAddressAndServiceHandler element : serviceHandlers) {
                if (element.transportAddress.acceptsTransportConnection(transportAddress)) {
                    handlers.add(element);
                }
            }
        }
        return handlers;
    }

    public ServerConfiguration generateServerConfiguration(String localHostName, String remoteHostName) {
        ServerConfiguration serverConfiguration = new ServerConfiguration();
        synchronized (serviceHandlers) {
            for (TransportAddressAndServiceHandler element : serviceHandlers) {
                ServerInfo serverInfo = new ServerInfo();
                serverInfo.protocol = element.serviceHandler.getProtocolInfo();
                serverInfo.services.add("*");
                serverInfo.transport.name = element.transportAddress.getTransportFactory().getName();
                try {
                    URI uri = new URI(element.transportAddress.toString());
                    if ("0.0.0.0".equals(uri.getHost())) {
                        uri = new URI(uri.getScheme(),
                                uri.getUserInfo(), localHostName, uri.getPort(),
                                uri.getPath(), uri.getQuery(),
                                uri.getFragment());
                    }
                    serverInfo.transport.url = uri.toString();
                    serverConfiguration.servers.add(serverInfo);
                } catch (URISyntaxException ex) {
                }
            }
        }
        synchronized (services) {
            StringBuilder builder = new StringBuilder();
            for (Service service : services) {
                builder.append(service.getIDLContents());
            }
            serverConfiguration.idlContents = builder.toString();
        }
        return serverConfiguration;
    }

    @Override
    public void onConnectionOpened(Transport connection) {
        logger.debug("Opened connection {}, local address {}, remote address {}",
                connection, connection.getLocalAddress(), connection.getRemoteAddress());
        List<TransportAddressAndServiceHandler> serviceHandlers = findAllServiceHandlers(connection.getLocalTransportAddress());
        ServerConnectionHandler handler = new ServerConnectionHandler(this, connection, serviceHandlers);
        synchronized (connectionHandlers) {
            connectionHandlers.put(connection, handler);
        }
        handler.fireClientConnectionOpened(eventListeners);
    }

    @Override
    public void onConnectionClosed(Transport connection) {
        logger.debug("Closed connection {}", connection);
        ServerConnectionHandler handler;
        synchronized (connectionHandlers) {
            handler = connectionHandlers.remove(connection);
        }
        if (handler != null) {
            handler.fireClientConnectionClosed(eventListeners);
        }
    }

    @Override
    public void addEventListener(ServerConnectionListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        synchronized (eventListeners) {
            eventListeners.add(listener);
        }
    }

    @Override
    public void removeEventListener(ServerConnectionListener listener) {
        if (listener == null) {
            throw new NullPointerException("listener");
        }
        synchronized (eventListeners) {
            eventListeners.remove(listener);
        }
    }

}
