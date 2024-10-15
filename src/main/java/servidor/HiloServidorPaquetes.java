package servidor;

import comun.MyStreamSocket;
import gestor.Paquete;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.Socket;

public class HiloServidorPaquetes extends Thread{
    private MyStreamSocket socket;
    public HiloServidorPaquetes(MyStreamSocket s){
        socket = s;
    }

    @Override
    public void run(){
        boolean finSesion = false;

        try {
            while (!finSesion) {
                String mensaje = socket.receiveMessage();
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(mensaje);
                int operacion = Integer.parseInt(obj.get("operacion").toString());

                /**
                 * Variables para los posibles campos
                 */

                String codCliente, CPOrigen, CPDestino, codMensajero;
                long codPaquete;
                double peso;

                switch (operacion){
                    case 0: // Cerrar sesion
                        finSesion = true;
                        break;
                    case 1: // Listar paquetes enviados
                        codCliente = obj.get("codCli").toString();
                        JSONArray array = ServidorPaquetes.gestor.listaPaquetesCliente(codCliente);

                        socket.sendMessage(array.toJSONString());
                        break;

                    case 2: // Enviar paquete
                        codCliente = obj.get("codCli").toString();
                        CPOrigen = obj.get("CPOrigen").toString();
                        CPDestino = obj.get("CPDestino").toString();
                        peso = Float.parseFloat(obj.get("peso").toString());
                        JSONObject paquete = ServidorPaquetes.gestor.enviaPaquete(codCliente, CPOrigen, CPDestino, peso);

                        socket.sendMessage(paquete.toJSONString());

                        break;

                    case 3: // Modificar paquete

                        codCliente = obj.get("codCli").toString();
                        codPaquete = Long.parseLong(obj.get("codPaquete").toString());
                        CPOrigen = obj.get("CPOrigen").toString();
                        CPDestino = obj.get("CPDestino").toString();
                        peso = Float.parseFloat(obj.get("peso").toString());

                        JSONObject modificado = ServidorPaquetes.gestor.modificaPaquete(codCliente, codPaquete, CPOrigen, CPDestino, peso);

                        socket.sendMessage(modificado.toJSONString());

                        break;

                    case 4: // Retirar paquete
                        codPaquete = Long.parseLong(obj.get("codPaquete").toString());
                        codCliente = obj.get("codCli").toString();
                        JSONObject retirado = ServidorPaquetes.gestor.retiraPaquete(codCliente, codPaquete);

                        socket.sendMessage(retirado.toJSONString());

                        break;


                    // Parte del switch correspondiente al mensajero

                    case 10: // Listar paqutes enviados a un código postal
                        CPDestino = obj.get("CPDestino").toString();
                        JSONArray arr = ServidorPaquetes.gestor.listaPaquetesCP(CPDestino);

                        socket.sendMessage(arr.toJSONString());
                        break;

                    case 20: // Recoger paquete
                        codPaquete = Long.parseLong(obj.get("codPaquete").toString());
                        codMensajero = obj.get("codMensajero").toString();
                        JSONObject recogido = ServidorPaquetes.gestor.recogePaquete(codPaquete, codMensajero);

                        socket.sendMessage(recogido.toJSONString());
                        break;
                } // switch
            } // while

            socket.close();

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
