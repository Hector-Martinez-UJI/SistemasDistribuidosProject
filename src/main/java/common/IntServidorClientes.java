package common;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntServidorClientes extends Remote {
    void guardaDatos() throws RemoteException;
    JSONArray listaPaquetesCliente(String codCliente) throws RemoteException;
    JSONObject enviaPaquete(String codCliente, String cpOrigen, String cpDestino, double peso) throws RemoteException;
    JSONObject modificaPaquete(String codCliente, long codPaquete, String cpOrigen, String cpDestino, double peso) throws RemoteException;
    JSONObject retiraPaquete(String codCliente, long codPaquete) throws RemoteException;

    /**
     * Métodos para callbacks
     */
    void registrarCallback(IntCallbackCliente cli) throws RemoteException;
    void borrarCallback(IntCallbackCliente cli) throws RemoteException;
}
