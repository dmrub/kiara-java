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

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public final class ProtocolRegistry {
    private ProtocolRegistry() { }

    private static final Map<String, Class<? extends Protocol>> protocols = new HashMap<>();

    public static synchronized Class<? extends Protocol> getProtocolClassByName(String protocolName) {
        return protocols.get(protocolName);
    }

    public static synchronized Protocol newProtocolByName(String protocolName) throws InstantiationException, IllegalAccessException {
        Class<? extends Protocol> cls = getProtocolClassByName(protocolName);
        if (cls == null)
            return null;
        return cls.newInstance();
    }

    public static synchronized void registerProtocol(String protocolName, Class<? extends Protocol> protocolClass) {
        protocols.put(protocolName, protocolClass);
    }

}
