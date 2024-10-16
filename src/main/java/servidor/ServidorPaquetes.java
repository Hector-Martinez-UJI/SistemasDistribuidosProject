package servidor;

import comun.MyStreamSocket;
import gestor.GestorPaquetes;

import java.io.IOException;
import java.net.ServerSocket;


/**
 * Este módulo contiene la lógica de aplicación del servidor de paquetería
 * Utiliza sockets en modo stream para llevar a cabo la comunicación entre procesos.
 * Puede servir a varios clientes de modo concurrente lanzando una hebra para atender a cada uno de ellos.
 * Se le puede indicar el puerto del servidor en línea de órdenes.
 */

/**
 * En esta version del servidor, todos los hilos que atienden concurrentemente las sesiones de
 * distintos clientes comparten un mismo gestor de paquetes.
 * Este gestor se pasa como argumento a cada hilo, junto con el socket.
 */


public class ServidorPaquetes {

    public static void main(String[] args) {
		int serverPort = 12345;    // puerto por defecto
		ServerSocket myConnectionSocket = null;
        GestorPaquetes gestor = new GestorPaquetes(); // Crea el gestor que, a su vez, crea/sobreescribe el fichero de paquetes

        if (args.length == 1 )
			serverPort = Integer.parseInt(args[0]);       
		try { 
			// Instancia un socket stream para aceptar conexiones
			myConnectionSocket = new ServerSocket(serverPort); 
			/**/     System.out.println("Servidor de la valoraciones listo.");  
			while (true) {  // Bucle infinito aceptando y sirviendo conexiones
				// Espera una conexion de un cliente
				/**/        System.out.println("Esperando conexion de algun cliente.");
				MyStreamSocket myDataSocket = new MyStreamSocket(myConnectionSocket.accept( ));
				/**/        System.out.println("Conexion aceptada");
				// Arranca una nueva hebra para atender la sesion de un nuevo cliente
				Thread theThread = new Thread(new HiloServidorPaquetes(myDataSocket, gestor));
				theThread.start();
				// y pasa a esperar otros clientes.
			} // fin del bucle infinito
		} // fin try
		catch (Exception ex) {
			ex.printStackTrace( );
		} // fin catch
		finally {
			try {
                if (myConnectionSocket != null) {
                    myConnectionSocket.close();
                }
            } catch (IOException e) {
				e.printStackTrace();
			}
			gestor.guardaDatos();
		}
	} //fin main
} // fin class
