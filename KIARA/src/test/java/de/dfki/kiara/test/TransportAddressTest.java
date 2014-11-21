/*
 * Copyright (C) 2014 rubinste
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

import de.dfki.kiara.Kiara;
import de.dfki.kiara.TransportAddress;
import de.dfki.kiara.TransportFactoryRegistry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rubinste
 */
public class TransportAddressTest {

    public TransportAddressTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Kiara.init();
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAddress() throws Exception {
        String patternUri = "http://localhost:8080/*/bar";
        String addrUri1 = "http://localhost:8080/foo/bar";
        String addrUri2 = "http://localhost:8080/moo/tar";

        TransportAddress patternAddr = TransportFactoryRegistry.createTransportAddressFromURI(patternUri);
        TransportAddress addr = TransportFactoryRegistry.createTransportAddressFromURI(addrUri1);

        assertTrue(patternAddr.acceptsConnection(addr));

        addr = TransportFactoryRegistry.createTransportAddressFromURI(addrUri2);

        assertFalse(patternAddr.acceptsConnection(addr));
    }
}
