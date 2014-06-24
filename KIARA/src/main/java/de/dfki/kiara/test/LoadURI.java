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

package de.dfki.kiara.test;

import de.dfki.kiara.util.URILoader;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author Dmitri Rubinstein <dmitri.rubinstein@dfki.de>
 */
public class LoadURI {
    public static void main(String[] args) {
        try {
            byte[] data = URILoader.load("https://localhost:8080/service");
            System.out.println(new String(data, "UTF-8"));
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException: ");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println("IOException: ");
            ex.printStackTrace();
        }
    }
}
