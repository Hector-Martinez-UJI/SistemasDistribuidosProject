package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntCallbackCliente extends Remote {
    void notifica(String mensaje) throws RemoteException;
    String getCodCli() throws RemoteException;
}
