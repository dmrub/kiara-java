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

import de.dfki.kiara.RemoteInterface;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public final class SpecialMethods {

    public static final Method riGetConnection = getMethodOfClass(RemoteInterface.class, "getConnection");

    private static Method getMethodOfClass(Class<?> clazz, String methodName) {
        try {
            return clazz.getMethod(methodName);
        } catch (final NoSuchMethodException ex) {
            Logger.getLogger(SpecialMethods.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error(ex);
        } catch (final SecurityException ex) {
            Logger.getLogger(SpecialMethods.class.getName()).log(Level.SEVERE, null, ex);
            throw new Error(ex);
        }
    }
}
