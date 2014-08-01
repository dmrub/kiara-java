/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cg.cs.kiararmiclient;

import cg.cs.kiararmiinterface.KIARARMIInterface;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shahzad
 */
public class RMIClient{
    public static void main(String[] args) {
        RMIClient client = new RMIClient();
        client.connectServer();
    }    

    private void connectServer() {
        try {
            Registry reg = LocateRegistry.getRegistry("127.0.0.1", 9989);
            KIARARMIInterface rmi = (KIARARMIInterface) reg.lookup("server");
            System.out.println("Connecting to server.");
            System.out.println(rmi.getGreetings("Shahzad"));
        } catch (RemoteException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(RMIClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
