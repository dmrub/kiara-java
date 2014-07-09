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
public interface Service {
    public void loadIDL(String fileName);
    public void loadServiceIDLFromString(String idlLanguage,String idlContents) throws IDLParseException;
    public String getIDLContents();
    public void registerServiceFunction(String idlMethodName, Object serviceImpl,
            String serviceMethodName) throws MethodAlreadyBoundException;
    public void registerServiceFunction(String idlMethodName, Object serviceImpl,
            String serviceMethodName,Class<?>... parameterTypes) throws MethodAlreadyBoundException;
    public void unregisterServiceFunction(String idlMethodName);
}
