package cliente;

import comun.MyStreamSocket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class AuxiliarClientePaquetes {
    MyStreamSocket socket;
    JSONParser parser;

    public AuxiliarClientePaquetes(){
        try{
            socket = new MyStreamSocket("localhost", 1234);
            parser = new JSONParser();
        }
        catch (IOException e){
            System.out.println("Error al conectar al servidor");
        }
    }

    public void cerrarSesion() {
        JSONObject obj = new JSONObject();
        obj.put("operacion", 0);
        sendJSON(obj);

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray listaPaquetesCliente(String codCli){
        JSONObject obj = new JSONObject();
        obj.put("operacion", 1);
        obj.put("codCli", codCli);

        sendJSON(obj);
        return parseJSONArray();
    }

    public JSONObject enviaPaquete(String codCliente, String cpOrigen, String cpDestino, double peso){
        JSONObject obj = new JSONObject();
        obj.put("operacion", 2);
        obj.put("codCli", codCliente);
        obj.put("CPOrigen", cpOrigen);
        obj.put("CPDestino", cpDestino);
        obj.put("peso", peso);

        sendJSON(obj);
        return parseJSONObject();
    }

    public JSONObject modificaPaquete(String codCliente, long codPaquete, String cpOrigen, String cpDestino, double peso) {
        JSONObject obj = new JSONObject();
        obj.put("operacion", 3);
        obj.put("codCli", codCliente);
        obj.put("codPaquete", codPaquete);
        obj.put("CPOrigen", cpOrigen);
        obj.put("CPDestino", cpDestino);
        obj.put("peso", peso);

        sendJSON(obj);
        return parseJSONObject();
    }

    public JSONObject retiraPaquete(String codCliente, long codPaquete) {
        JSONObject obj = new JSONObject();
        obj.put("operacion", 4);
        obj.put("codCli", codCliente);
        obj.put("codPaquete", codPaquete);

        sendJSON(obj);
        return parseJSONObject();
    }
    
    public JSONArray listaPaquetesCP(String codPostal){
        JSONObject obj = new JSONObject();
        obj.put("operacion", 10);
        obj.put("CPDestino", codPostal);

        sendJSON(obj);
        return parseJSONArray();
    }

    public JSONObject recogePaquete(long codPaquete, String codMensajero) {
        JSONObject obj = new JSONObject();
        obj.put("operacion", 20);
        obj.put("codPaquete", codPaquete);
        obj.put("codMensajero", codMensajero);

        sendJSON(obj);
        return parseJSONObject();
    }

    private void sendJSON(JSONObject obj){
        try {
            socket.sendMessage(obj.toJSONString());
        }
        catch (Exception e){
            System.out.println("Error al enviar paquete");
        }
    }
    private JSONArray parseJSONArray(){
        try{
            return (JSONArray) parser.parse(socket.receiveMessage());
        }
        catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    private JSONObject parseJSONObject(){
        try{
            return (JSONObject) parser.parse(socket.receiveMessage());
        }
        catch (ParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }


}
