package client;

import common.IntCallbackCliente;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ImpCallbackCliente extends UnicastRemoteObject implements IntCallbackCliente {
    private final String codCli;
    private final List<String> mensajes;
    private boolean isSuscribed;

    public ImpCallbackCliente(String codCli) throws RemoteException {
        super();
        this.mensajes = new ArrayList<>();
        this.codCli = codCli;
        this.isSuscribed = false;
    }

    @Override
    public synchronized void notifica(String mensaje)
            throws RemoteException {
        this.mensajes.add(mensaje);
    }

    @Override
    public String getCodCli()
            throws RemoteException {
        return this.codCli;
    }

    public synchronized boolean hasPendingNotifications() {
        return !this.mensajes.isEmpty();
    }

    public boolean isSuscribed() {
        return this.isSuscribed;
    }

    public synchronized void seeNotifications() {
        for (String mensaje : this.mensajes) {
            System.out.println(mensaje);
        }
        this.mensajes.clear();
    }

    public void setSuscribed(boolean state) {
        this.isSuscribed = state;
    }
}
