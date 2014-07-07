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

import de.dfki.kiara.AlreadyBoundException;
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
     * @param idlMethod
     * @param serviceImpl
     * @param serviceMethod
     * @throws AlreadyBoundException
     */
    @Override
    public void registerServiceFunction(String idlMethod, Object serviceImpl, String serviceMethod) throws AlreadyBoundException{
        if(binder.getServiceMethod(idlMethod) != null){
            throw new AlreadyBoundException("Service method already bound");
        }
        binder.bindServiceMethod(idlMethod, serviceImpl, serviceMethod);
    }

    @Override
    public void removeBinding(String idlMethod) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
