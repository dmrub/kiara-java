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
import de.dfki.kiara.jos.JosProtocol;
import de.dfki.kiara.jsonrpc.JsonRpcProtocol;
import de.dfki.kiara.tcp.TcpBlockTransport;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class Kiara {

    static {
        // initialize protocols
        ProtocolRegistry.registerProtocol("jsonrpc", JsonRpcProtocol.class);
        ProtocolRegistry.registerProtocol("javaobjectstream", JosProtocol.class);

        // initialize transports
        TransportRegistry.registerTransport(new TcpBlockTransport());
        TransportRegistry.registerTransport(new HttpTransport());
    }

    public static Context createContext() {
        return new ContextImpl();
    }
}
