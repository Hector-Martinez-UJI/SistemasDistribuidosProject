package server;

import common.IntCallbackCliente;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class ServidorPaquetesRMI {
    public static void main(String[] args) {
        try {
            int RMIPortNum = 1099;
            startRegistry(RMIPortNum);

            // Crear diccionarios de registrados
            Map<String, IntCallbackCliente> clientesRegistrados = new HashMap<>();


            GestorPaquetes gestor = new GestorPaquetes();
            ImplServidorClientes exportedObjClientes = new ImplServidorClientes(gestor, clientesRegistrados);
            ImplServidorMensajeros exportedObjMensajeros = new ImplServidorMensajeros(gestor, clientesRegistrados);

            String clientURL = "rmi://localhost:" + RMIPortNum + "/cliente";
            String mensajeroURL = "rmi://localhost:" + RMIPortNum + "/mensajero";

            Naming.rebind(clientURL, exportedObjClientes);
            Naming.rebind(mensajeroURL, exportedObjMensajeros);

            System.out.println("Listando URLs");
            // list names currently in the registry
            listRegistry(clientURL);
            listRegistry(mensajeroURL);
            System.out.println("Servidor listo");
        }// end try
        catch (Exception re) {
            System.out.println("Exception in ServidorPaquetesRMI.main: " + re);
        }
    }

    private static void startRegistry(int RMIPortNum)
            throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();  // This call will throw an
            //exception if the registry does not already exist
        } catch (RemoteException e) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located at port " + RMIPortNum);
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
            System.out.println("RMI registry created at port " + RMIPortNum);
        }
    } // end startRegistry

    //This method lists the names registered with a Registry
    private static void listRegistry(String registryURL)
            throws RemoteException, MalformedURLException {
        System.out.println("Registry " + registryURL + " contains: ");
        String[] names = Naming.list(registryURL);
        for (String name : names) System.out.println(name);
    } //end listRegistry
}
