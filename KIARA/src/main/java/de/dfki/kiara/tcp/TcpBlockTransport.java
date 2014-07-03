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

package de.dfki.kiara.tcp;

import de.dfki.kiara.AsyncHandler;
import de.dfki.kiara.Transport;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportConnection;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.Future;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class TcpBlockTransport implements Transport {

    @Override
    public String getName() {
        return "tcp";
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public boolean isHttpTransport() {
        return false;
    }

    @Override
    public boolean isSecureTransport() {
        return false;
    }

    @Override
    public TransportAddress createAddress(String uri) {
        return null;
    }

    @Override
    public Future<TransportConnection> openConnection(String uri, Map<String, Object> settings, AsyncHandler<TransportConnection> handler) throws URISyntaxException {
        return null;
    }


}
