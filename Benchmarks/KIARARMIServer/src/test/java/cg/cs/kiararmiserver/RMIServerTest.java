/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
