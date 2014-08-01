/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dfki.sb.rmijavaproject;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shahzad
 */
public class RMIJavaServerTest {

    public RMIJavaServerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        Logger.getLogger("RMIJavaServerTest").log(Level.INFO, "Setting up Server");
        RMIJavaServer.main(null);
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
     * Test of main method, of class RMIJavaServer.
     */
    @Test
    public void testMain() throws RemoteException, NotBoundException {
        RMIJavaClient.main(null);
    }

}
