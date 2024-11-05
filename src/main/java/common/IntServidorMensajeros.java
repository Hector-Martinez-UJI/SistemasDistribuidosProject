package common;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntServidorMensajeros extends Remote {
    JSONArray listaPaquetesCP(String CPDestino) throws RemoteException;
    JSONObject recogePaquete(long codPaquete, String codMensajero) throws RemoteException;
    void guardaDatos() throws RemoteException;
}
