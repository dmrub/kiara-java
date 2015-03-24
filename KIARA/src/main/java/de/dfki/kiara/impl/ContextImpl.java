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

import de.dfki.kiara.Connection;
import de.dfki.kiara.Context;
import de.dfki.kiara.Server;
import de.dfki.kiara.Service;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ContextImpl implements Context {

    @Override
    public Connection connect(String url) throws IOException {
        return new ConnectionImpl(url);
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public Server createServer(String host, int port, String configPath) throws IOException, URISyntaxException {
        return new ServerImpl(host, port, configPath);
    }

    @Override
    public Service createService() {
        return new ServiceImpl(this);
    }

}
