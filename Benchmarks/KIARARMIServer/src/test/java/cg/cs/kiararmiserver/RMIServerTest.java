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

package cg.cs.kiararmiserver;

import cg.cs.kiararmiinterface.KIARARMIInterface;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Shahzad
 */
public class RMIServerTest {
    
    public RMIServerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        Logger.getLogger("RMIServerTest").log(Level.INFO,"Setting up Server");        
        RMIServer.main(null);        
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

    /**
     * Test of getGreetings method, of class RMIServer.
     * @throws java.rmi.RemoteException
     * @throws java.rmi.AccessException
     * @throws java.rmi.NotBoundException
     */
    @Test
    public void testGetGreetings() throws RemoteException, AccessException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry("127.0.0.1", 9989);
        KIARARMIInterface rmi = (KIARARMIInterface) reg.lookup("server");
        assertFalse(rmi == null);
        Logger.getLogger(this.getClass().getName()).log(Level.INFO,"Connecting to server.");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Message received from server:");
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, rmi.getGreetings("Shahzad"));
        assertTrue(rmi.getGreetings("Shahzad").equalsIgnoreCase("Welcome Shahzad"));
    }
    
}
