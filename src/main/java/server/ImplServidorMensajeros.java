package server;

import common.IntCallbackCliente;
import common.IntServidorClientes;
import common.IntServidorMensajeros;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.Map;

public class ImplServidorMensajeros extends UnicastRemoteObject implements IntServidorMensajeros {
    private final GestorPaquetes gestor;
    private final Map<String, IntCallbackCliente> clientesRegistrados;

    public ImplServidorMensajeros(GestorPaquetes gestor, Map<String, IntCallbackCliente> clientesRegistrados) throws RemoteException {
        super();
        this.gestor = gestor;
        this.clientesRegistrados = clientesRegistrados;
    }

    @Override
    public JSONArray listaPaquetesCP(String CPDestino)
            throws RemoteException {
        return gestor.listaPaquetesCP(CPDestino);
    }

    @Override
    public JSONObject recogePaquete(long codPaquete, String codMensajero)
            throws RemoteException {
        JSONObject paquete = gestor.recogePaquete(codPaquete, codMensajero);
        // Modificación para soportar callbacks: Sincronizado con el cerrojo del mapa de clientes registrados
        synchronized (clientesRegistrados) {
            IntCallbackCliente callback = this.clientesRegistrados.get(paquete.get("codCliente").toString());
            try {
                if (callback != null)
                    notificaRecogida(callback, codPaquete, codMensajero);
            } catch (RemoteException e) {
                System.out.println("Cliente no disponible, borrando entrada");
                this.clientesRegistrados.remove(paquete.get("codCliente").toString());
            }
        }
        return paquete;
    }

    /**
     * Método para callbacks
     */

    private void notificaRecogida(IntCallbackCliente callback, long codPaquete, String codMensajero) throws RemoteException {
        callback.notifica("Paquete " + codPaquete + " recogido por " + codMensajero);
    }

    @Override
    public void guardaDatos() throws RemoteException{
        gestor.guardaDatos();
    }
}
