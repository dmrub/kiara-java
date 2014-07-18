/*
 * Copyright (C) 2014 shahzad
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

/**
 *
 * @author shahzad
 */
public interface Binder {
    public void bindServiceMethod(String idlMethodName,Object serviceClass,
            String methodName) throws NoSuchMethodException, SecurityException ;
    public ServiceMethodBinder getServiceMethod(String idlMethodName);
    public void bindServiceMethod(String idlMethodName, Object serviceImpl,
            String serviceMethodName, Class<?>[] parameterTypes) throws
            NoSuchMethodException, SecurityException ;

    public void unbindServiceMethod(String idlMethodName) throws NoSuchMethodException;
}
