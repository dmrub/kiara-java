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
package de.dfki.kiara.impl;

import de.dfki.kiara.Server;
import de.dfki.kiara.Service;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author shahzad
 */
public class ServerImpl implements Server {
    private final String configHost;
    private final int configPort;
    private final String configPath;
    private final URI configUri;

    private static Logger logger =  LoggerFactory.getLogger(ServerImpl.class);

    public ServerImpl(String host, int port, String configPath) throws URISyntaxException {
        this.configHost = host;
        this.configPort = port;
        this.configPath = configPath;
        this.configUri = new URI("http://" + configHost + ":" + Integer.toString(configPort) + "/" + configPath);

        // listen for negotiation connection
        addPortListener(configHost, configPort, "http");
    }

    public URI getConfigUri() {
        return configUri;
    }

    private boolean addPortListener(String host, int port, String transportName) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void addService(String path, String protocol, Service service) throws IOException {
        URI uri = configUri.resolve(path);

        logger.debug("Server::addService: {}", uri);

        Transport transport = TransportRegistry.getTransportByName(uri.getScheme());

        if (transport == null)
        {
            throw new IOException("Unsupported transport: "+uri.getScheme());
        }
/*
        ServiceHandler *handler = new ServiceHandler(service, transport, protocol);
        if (handler->isError())
        {
            setError(handler->getError());
            return getErrorCode();
        }

        // FIXME this will fail when transport changes for a given host/port combination
        if (!addPortListener(uri.getHost(), uri.getPort(), uri.getScheme()))
            throw new IOException("Could not add port listener");

        Transport::TransportAddress::Ptr address = transport->createAddress(urlStr);

        logger.debug("Register: {} {}", uri.getScheme(), address.toString());

        ServiceHandlerMap::iterator it = serviceHandlers_.begin();
        ServiceHandlerMap::iterator end = serviceHandlers_.end();
        for (; it != end; ++it)
        {
            if (it->first->equals(address))
                break;
        }

        if (it != end)
        {
            delete it->second;
            it->second = handler;
        }
        else
        {
            serviceHandlers_.push_back(std::make_pair(address, handler));
        }
        services_.insert(service);
        return KIARA_SUCCESS;
        */
    }

    @Override
    public boolean removeService(Service service) {
        return false;
    }

    @Override
    public void run() {

    }
}
