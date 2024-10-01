package servidor;

import comun.MyStreamSocket;
import gestor.GestorPaquetes;

import java.net.ServerSocket;

public class ServidorPaquetes {
    public static GestorPaquetes gestor = new GestorPaquetes();

    public static void main(String[] args) {
        int port = 1234;
        try {
            ServerSocket server = new ServerSocket(port);
            while (true) {
                MyStreamSocket socket = new MyStreamSocket(server.accept());
                HiloServidorPaquetes hp = new HiloServidorPaquetes(socket);
                hp.start();
                System.out.println("Cliente conectado");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        gestor.guardaDatos();
    }
}
