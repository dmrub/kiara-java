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

package de.dfki.kiara.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class ServerConfiguration {
    public String info = null;
    public String idlURL = null;
    public String idlContents = null;
    public List<ServerInfo> servers = new ArrayList<>();

    public final void clear() {
        info = null;
        idlURL = null;
        idlContents = null;
        if (servers != null)
            servers.clear();
    }

    public static ServerConfiguration fromJSON(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, ServerConfiguration.class);
    }

    public String toJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

}
