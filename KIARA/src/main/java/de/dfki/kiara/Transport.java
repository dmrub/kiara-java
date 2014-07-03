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

import com.google.common.util.concurrent.ListenableFuture;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public interface Transport {

    public String getName();

    public int getPriority();

    public boolean isHttpTransport();

    public boolean isSecureTransport();

    public TransportAddress createAddress(String uri);

    public ListenableFuture<TransportConnection> openConnection(String uri, Map<String, Object> settings) throws URISyntaxException, IOException;

}
