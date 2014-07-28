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

import de.dfki.kiara.Protocol;
import de.dfki.kiara.ProtocolRegistry;
import de.dfki.kiara.Service;
import de.dfki.kiara.Transport;
import de.dfki.kiara.config.ProtocolInfo;
import java.io.Closeable;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServiceHandler implements Closeable {
    private final Service service;
    private final ProtocolInfo protocolInfo;
    private final Protocol protocol;

    public ServiceHandler(Service service, Transport transport, String protocolName) throws InstantiationException, IllegalAccessException {
        this.service = service;
        this.protocolInfo = new ProtocolInfo();
        this.protocolInfo.name = protocolName;
        this.protocol = ProtocolRegistry.newProtocolByName(protocolName);
    }

    public Service getService() {
        return service;
    }

    @Override
    public void close() {
    }
}
