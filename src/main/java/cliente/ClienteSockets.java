package cliente;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class ClienteSockets {

    /**
     * Muestra el menu de opciones y lee repetidamente de teclado hasta obtener una opción válida
     *
     * @param teclado	stream para leer la opción elegida de teclado
     * @return			opción elegida
     */
    public static int menu(Scanner teclado) {
        int opcion;
        System.out.println("\n\n");
        System.out.println("=====================================================");
        System.out.println("============            MENU        =================");
        System.out.println("=====================================================");
        System.out.println("0. Cerrar conexión con el servidor");
        System.out.println("1. Listar los paquetes enviados");
        System.out.println("2. Enviar un paquete");
        System.out.println("3. Modificar un paquete");
        System.out.println("4. Retirar un paquete");
        do {
            System.out.print("\nElige una opcion (1..4): ");
            opcion = teclado.nextInt();
        } while ( (opcion<0) || (opcion>4) );
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

        // Crea un gestor de valoraciones
        AuxiliarClientePaquetes gestor = null;
        try {
            gestor = new AuxiliarClientePaquetes("localhost", "12345");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.print("Introduce tu código de cliente: ");
        String codCliente = teclado.nextLine();

        int opcion;
        do {
            opcion = menu(teclado);
            switch (opcion) {
                case 0 -> gestor.cierraSesion();
                case 1 -> { // Listar los paquetes enviados por el cliente
                    // Se solicitan los paquetes del cliente
                    JSONArray array = gestor.listaPaquetesCliente(codCliente);

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
                    JSONObject envio = gestor.enviaPaquete(codCliente, cpOrigen, cpDestino, peso);
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
                    JSONObject paquete = gestor.modificaPaquete(codCliente, codPaquete, CPOrigen, CPDestino, peso);

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
                    if (!gestor.retiraPaquete(codCliente, codPaquete).isEmpty()){
                        System.out.println("Paquete " + codPaquete + " retirado");
                    }
                    else System.out.println("Paquete " + codPaquete + " no existente o entregado");
                }

            } // fin switch

        } while (opcion != 0);

    } // fin de main


} // fin class
