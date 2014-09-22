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

import java.nio.ByteBuffer;

/**
 *
 * @author shahzad
 */
public interface Service {

    public void loadIDL(String fileName);

    public void loadServiceIDLFromString(String idlLanguage, String idlContents) throws IDLParseException;

    public String getIDLContents();

    public void registerServiceFunction(String idlMethodName, Object serviceImpl,
            String serviceMethodName) throws
            MethodAlreadyBoundException, NoSuchMethodException, SecurityException;

    public void registerServiceFunction(String idlMethodName, Object serviceImpl,
            String serviceMethodName, Class<?>... parameterTypes) throws
            MethodAlreadyBoundException, NoSuchMethodException, SecurityException;

    public void unregisterServiceFunction(String idlMethodName) throws NoSuchMethodException;

    public Object dbgSimulateCall(ByteBuffer payload) throws Exception;
}
