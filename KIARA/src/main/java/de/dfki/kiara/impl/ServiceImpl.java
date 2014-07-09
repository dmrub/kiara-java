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
package de.dfki.kiara.impl;

import de.dfki.kiara.MethodAlreadyBoundException;
import de.dfki.kiara.Binder;
import de.dfki.kiara.IDLParseException;
import de.dfki.kiara.Service;

/**
 *
 * @author shahzad
 */
public class ServiceImpl implements Service {

    private Binder binder;
    ServiceImpl(Binder binder) {
        this.binder = binder;
    }

    @Override
    public void loadIDL(String fileName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadServiceIDLFromString(String idlLanguage, String idlContents) throws IDLParseException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getIDLContents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param idlMethodName
     * @param serviceMethodName
     * @param serviceImpl
     * @throws MethodAlreadyBoundException
     */
    @Override
    public void registerServiceFunction(String idlMethodName, Object serviceImpl, String serviceMethodName) throws MethodAlreadyBoundException{
        if(binder.getServiceMethod(idlMethodName) != null){
            throw new MethodAlreadyBoundException("Service method already bound");
        }
        binder.bindServiceMethod(idlMethodName, serviceImpl, serviceMethodName);
    }

    /**
    *
    * @param idlMethodName
    * @param serviceMethodName
    * @param serviceImpl
     * @param parameterTypes
    * @throws MethodAlreadyBoundException
    */

    @Override
    public void registerServiceFunction(String idlMethodName, Object serviceImpl,
            String serviceMethodName,Class<?>... parameterTypes) throws MethodAlreadyBoundException {
        if(binder.getServiceMethod(idlMethodName) != null){
            throw new MethodAlreadyBoundException("Service method already bound");
        }
        binder.bindServiceMethod(idlMethodName, serviceImpl, serviceMethodName,parameterTypes);
    }

    @Override
    public void unregisterServiceFunction(String idlMethodName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
