package client;

import common.IntServidorClientes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ClientePaquetesRMI {

    /**
     * Muestra el menu de opciones y lee repetidamente de teclado hasta obtener una opción válida
     *
     * @param teclado	stream para leer la opción elegida de teclado
     * @return			opción elegida
     */
    public static int menu(Scanner teclado, ImpCallbackCliente callback) {
        int opcion;
        boolean hasPending = callback.hasPendingNotifications();
        int limite_superior;
        System.out.println("\n\n");
        System.out.println("=====================================================");
        System.out.println("============            MENU        =================");
        System.out.println("=====================================================");
        System.out.println("0. Cerrar conexión con el servidor");
        System.out.println("1. Listar los paquetes enviados");
        System.out.println("2. Enviar un paquete");
        System.out.println("3. Modificar un paquete");
        System.out.println("4. Retirar un paquete");

        if (callback.isSuscribed()) {
            System.out.println("5. Quitar suscripción a notificaciones");
        } else {
            System.out.println("5. Suscribirse a notificaciones");
        }

        if (hasPending){
            System.out.println("6. Ver notificaciones pendientes");
            limite_superior = 6;
        } else {
            limite_superior = 5;
        }

        do {
            if (hasPending) {
                System.out.println("\nElige una opcion (1..6): ");
            } else {
                System.out.print("\nElige una opcion (1..5): ");
            }
            opcion = teclado.nextInt();
        } while ( (opcion<0) || (opcion>limite_superior) );
        teclado.nextLine(); // Elimina retorno de carro del buffer de entrada
        return opcion;
    }

    /**
     * Programa principal. Muestra el menú repetidamente y atiende las peticiones del cliente.
     *
     * @param args	no se usan argumentos de entrada al programa principal
     */
    public static void main(String[] args)  {

        Scanner teclado = new Scanner(System.in);

        int RMIPortnum = 1099;

        System.out.print("Introduce tu código de cliente: ");
        String codCliente = teclado.nextLine();

        IntServidorClientes gestor = null;
        ImpCallbackCliente callback = null;
        try {
            gestor = (IntServidorClientes) Naming.lookup("rmi://localhost:" + RMIPortnum + "/cliente");
        } catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }

        // Crear objeto remoto callback
        try {
            callback = new ImpCallbackCliente(codCliente);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        int opcion;
        do {
            opcion = menu(teclado, callback);
            switch (opcion) {
                case 0 ->   {
                    System.out.println("Cerrando sesion");
                    try{
                        gestor.guardaDatos();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    if (callback.isSuscribed()) {
                        try {
                            gestor.borrarCallback(callback);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                case 1 -> { // Listar los paquetes enviados por el cliente
                    // Se solicitan los paquetes del cliente
                    JSONArray array;

                    try {
                        array = gestor.listaPaquetesCliente(codCliente);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        break;
                    }

                    if (array.isEmpty()) {
                        System.out.println("No se han enviado paquetes");
                    } else {
                        // En caso de que el array no este vacío se listan
                        for (Object o : array) {
                            JSONObject obj = (JSONObject) o;
                            System.out.println(obj.toJSONString());
                        }
                    }

                }
                case 2 -> { // Hacer un envío
                    // Se piden datos
                    System.out.print("Introduce el código postal de origen: ");
                    String cpOrigen = teclado.next();

                    System.out.print("Introduce el código postal de destino: ");
                    String cpDestino = teclado.next();

                    System.out.print("Introduce el peso: ");
                    double peso = teclado.nextDouble();

                    // Se envía el paquete
                    JSONObject envio;

                    try {
                        envio = gestor.enviaPaquete(codCliente, cpOrigen, cpDestino, peso);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        break;
                    }

                    System.out.println("Paquete " + envio.get("codPaquete") + " enviado con éxito ");
                    System.out.println(envio.toJSONString());

                }
                case 3 -> { // Modificar un paquete enviado por ti y no recogido todavía
                    // Se piden los datos
                    System.out.print("Introduce el código del paquete: ");
                    long codPaquete = teclado.nextLong();

                    System.out.print("Introduce el código postal de origen: ");
                    String CPOrigen = teclado.next();

                    System.out.print("Introduce el código postal de destino: ");
                    String CPDestino = teclado.next();

                    System.out.print("Introduce el peso: ");
                    double peso = teclado.nextDouble();

                    // Se modifica el paquete
                    JSONObject paquete;

                    try {
                        paquete = gestor.modificaPaquete(codCliente, codPaquete, CPOrigen, CPDestino, peso);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        break;
                    }

                    if (paquete.isEmpty())
                        System.out.println("Paquete " + codPaquete + " no existente o entregado");
                    else {
                        // En caso de que la modificación sea exitosa, se imprime
                        System.out.println("Paquete " + codPaquete + " modificado correctamente");
                        System.out.println(paquete.toJSONString());
                    }
                }
                case 4 -> { // Retira un paquete envíado por ti y no recogido todavía
                    // Introduce datos
                    System.out.print("Introduce el código de paquete a retirar: ");
                    long codPaquete = teclado.nextLong();

                    // Se retira el paquete

                    JSONObject paquete;

                    try {
                        paquete = gestor.retiraPaquete(codCliente, codPaquete);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        break;
                    }

                    if (!paquete.isEmpty()){
                        System.out.println("Paquete " + codPaquete + " retirado");
                    }
                    else System.out.println("Paquete " + codPaquete + " no existente o entregado");
                }
                case 5 -> {
                    // Ver si esta suscrito, si no, suscribirse
                    if (callback.isSuscribed()) {
                        try {
                            gestor.borrarCallback(callback);
                            callback.setSuscribed(false);
                            System.out.println("Suscripción cancelada con éxito");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // Suscribir
                        try {
                            gestor.registrarCallback(callback);
                            callback.setSuscribed(true);
                            System.out.println("Cliente suscrito al registro con éxito");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                case 6 -> {
                    // Ver notificaciones
                    callback.seeNotifications();
                }

            } // fin switch

        } while (opcion != 0);

    } // fin de main


} // fin class
