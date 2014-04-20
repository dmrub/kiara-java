/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cg.cs.kiararmiserver;

import cg.cs.kiararmiinterface.KIARARMIInterface;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shahzad
 */
public class RMIServer extends UnicastRemoteObject implements KIARARMIInterface{
    
    public RMIServer() throws RemoteException{
        super();
    }
    public static void main(String[] args) {
        try {
            Registry reg = LocateRegistry.createRegistry(9989);
            reg.rebind("server", new RMIServer());
            System.out.println("Server Started on port 9989.");
        } catch (RemoteException ex) {
            Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getGreetings(String name) throws RemoteException{
        return "Welcome " +name;
    }
}
