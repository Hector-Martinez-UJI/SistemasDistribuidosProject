package server;

import common.IntCallbackCliente;
import common.IntServidorClientes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class ImplServidorClientes extends UnicastRemoteObject implements IntServidorClientes {

    private final GestorPaquetes gestor;
    private final Map<String, IntCallbackCliente> clientesRegistrados;

    public ImplServidorClientes(GestorPaquetes gestor, Map<String, IntCallbackCliente> clientesRegistrados) throws RemoteException {
        super();
        this.gestor = gestor;
        this.clientesRegistrados = clientesRegistrados;
    }
    public void guardaDatos() throws RemoteException {
        gestor.guardaDatos();
    }
    public JSONArray listaPaquetesCliente(String codCliente) throws RemoteException {
        return gestor.listaPaquetesCliente(codCliente);
    }
    public JSONObject enviaPaquete(String codCliente, String cpOrigen, String cpDestino, double peso)
        throws  RemoteException {
        return gestor.enviaPaquete(codCliente, cpOrigen, cpDestino, peso);
    }
    public JSONObject modificaPaquete(String codCliente, long codPaquete, String cpOrigen, String cpDestino, double peso)
            throws RemoteException {
        return gestor.modificaPaquete(codCliente, codPaquete, cpOrigen, cpDestino, peso);
    }
    public JSONObject retiraPaquete(String codCliente, long codPaquete) throws RemoteException {
        return gestor.retiraPaquete(codCliente, codPaquete);
    }

    /**
     * Métodos para callbacks
     */

    @Override
    public void registrarCallback(IntCallbackCliente cli) throws RemoteException {
        synchronized (clientesRegistrados) {
            this.clientesRegistrados.put(cli.getCodCli(), cli);
        }
    }

    @Override
    public void borrarCallback(IntCallbackCliente cli) throws RemoteException {
        synchronized (clientesRegistrados) {
            this.clientesRegistrados.remove(cli.getCodCli());
        }
    }
}
