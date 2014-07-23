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

import de.dfki.kiara.Server;
import de.dfki.kiara.Service;

/**
 *
 * @author shahzad
 */
public class ServerImpl implements Server{    
    private final String host;
    private final int port;
    private final String configPath;
    public ServerImpl(String host, int port, String configPath){
        this.host = host;
        this.port = port;
        this.configPath = configPath;
    }        

    @Override
    public void addService(String path, String protocol, Service service) {

    }

    @Override
    public void run() {

    }
}
