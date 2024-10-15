package cliente;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class MensajeroSockets {


    // Sustituye esta clase por tu versión de la clase MensajeroLocal de la práctica 1

    // Modifícala para que instancie un objeto de la clase AuxiliarClientePaquetes

    // Modifica todas las llamadas al objeto de la clase GestorPaquetes
    // por llamadas al objeto de la clase AuxiliarClientePaquetes.
    // Los métodos a llamar tendrán la misma signatura.

    public static int menu(Scanner teclado) {
        int opcion;
        System.out.println("\n\n");
        System.out.println("=====================================================");
        System.out.println("============            MENU        =================");
        System.out.println("=====================================================");
        System.out.println("0. Salir");
        System.out.println("1. Listar los paquetes enviados a un CP");
        System.out.println("2. Recoger un paquete");
        do {
            System.out.print("\nElige una opcion (0..2): ");
            opcion = teclado.nextInt();
        } while ( (opcion<0) || (opcion>2) );
        teclado.nextLine(); // Elimina retorno de carro del buffer de entrada
        return opcion;
    }

    /**
     * Programa principal. Muestra el menú repetidamente y atiende las peticiones del mensajero.
     *
     * @param args	no se usan argumentos de entrada al programa principal
     */
    public static void main(String[] args)  {

        Scanner teclado = new Scanner(System.in);

        // Crea un gestor de valoraciones
        AuxiliarClientePaquetes gestor = null;
        try {
            gestor = new AuxiliarClientePaquetes("localhost", "12345");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.print("Introduce tu código de mensajero: ");
        String codMensajero = teclado.nextLine();

        int opcion;
        do {
            opcion = menu(teclado);
            switch (opcion) {
                case 0 -> { // Cerrar sesion
                    gestor.cierraSesion();
                }
                case 1 -> { // Listar los paquetes enviados a un CP
                    // Se pide CP
                    System.out.print("\nIntroduzca el CP: ");
                    String cp = teclado.next();
                    JSONArray array = gestor.listaPaquetesCP(cp);

                    if (array.isEmpty()) {
                        System.out.println("No se ha enviado ningún paquete al CP " + cp);
                    } else {
                        // Si el array no está vacío se muestran los paquetes
                        for (Object o : array){
                            JSONObject obj = (JSONObject) o;
                            System.out.println(obj.toJSONString());
                        }
                    }
                }
                case 2 -> { // Recoger un paquete con un código dado
                    // Se piden los datos
                    System.out.print("\nIntroduzca el código del paquete: ");
                    long codPaquete = teclado.nextLong();

                    // Se recoge el paquete
                    JSONObject paquete = gestor.recogePaquete(codPaquete, codMensajero);

                    if (paquete.isEmpty()) {
                        System.out.println("Paquete no existente.");
                    } else {
                        // Si se ha recogido con éxito se muestra el paquete
                        System.out.println("Paquete " + codPaquete + " recogido!");
                        System.out.println(paquete.toJSONString());
                    }
                }

            } // fin switch

        } while (opcion != 0);

    } // fin de main

} // fin class
