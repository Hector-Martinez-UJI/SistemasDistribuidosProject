package cliente;

import comun.MyStreamSocket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Esta clase es un módulo que proporciona la lógica de aplicación
 * para el Cliente del servicio de paquetería usando sockets de tipo stream
 */

public class AuxiliarClientePaquetes {

	private final MyStreamSocket mySocket; // Socket de datos para comunicarse con el servidor
	JSONParser parser;

	/**
	 * Construye un objeto auxiliar asociado a un cliente del servicio.
	 * Crea un socket para conectar con el servidor.
	 * @param	hostName	nombre de la máquina que ejecuta el servidor
	 * @param	portNum		número de puerto asociado al servicio en el servidor
	 */
	AuxiliarClientePaquetes(String hostName, String portNum)
			throws IOException {
		// Instantiates a stream-mode socket and wait for a connection.
		this.mySocket = new MyStreamSocket(InetAddress.getByName(hostName), Integer.parseInt(portNum));
		/**/  System.out.println("Hecha peticion de conexion");
		parser = new JSONParser();
	} // end constructor



	public JSONArray listaPaquetesCliente(String codCliente) {
		JSONObject obj = new JSONObject();
		obj.put("operacion", 1);
		obj.put("codCli", codCliente);

		sendJSON(obj);
		return parseJSONArray();
	} // end listaPaquetesCliente


	public JSONObject enviaPaquete(String codCliente, String CPOrigen, String CPDestino, double peso) {
		JSONObject obj = new JSONObject();
		obj.put("operacion", 2);
		obj.put("codCli", codCliente);
		obj.put("CPOrigen", CPOrigen);
		obj.put("CPDestino", CPDestino);
		obj.put("peso", peso);

		sendJSON(obj);
		return parseJSONObject();
	} // end enviaPaquete


	public JSONObject modificaPaquete(String codCliente, long codPaquete, String CPOrigen, String CPDestino, double peso) {
		JSONObject obj = new JSONObject();
		obj.put("operacion", 3);
		obj.put("codCli", codCliente);
		obj.put("codPaquete", codPaquete);
		obj.put("CPOrigen", CPOrigen);
		obj.put("CPDestino", CPDestino);
		obj.put("peso", peso);

		sendJSON(obj);
		return parseJSONObject();
	} // end modificaPaquete


	public JSONObject retiraPaquete(String codCliente, long codPaquete) {
		JSONObject obj = new JSONObject();
		obj.put("operacion", 4);
		obj.put("codCli", codCliente);
		obj.put("codPaquete", codPaquete);

		sendJSON(obj);
		return parseJSONObject();
	} // end retiraPaquete


	public JSONArray listaPaquetesCP(String CPDestino) {
		JSONObject obj = new JSONObject();
		obj.put("operacion", 5);
		obj.put("CPDestino", CPDestino);

		sendJSON(obj);
		return parseJSONArray();
	} // end listaPaquetesCP

	public JSONObject recogePaquete(long codPaquete, String codMensajero) {
		JSONObject obj = new JSONObject();
		obj.put("operacion", 6);
		obj.put("codPaquete", codPaquete);
		obj.put("codMensajero", codMensajero);

		sendJSON(obj);
		return parseJSONObject();
	} // end enviaPaquete

	/**
	 * Finaliza la conexión con el servidor
	 */
	public void cierraSesion( ) {
		JSONObject obj = new JSONObject();
		obj.put("operacion", 0);
		sendJSON(obj);

		try {
			mySocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // end done

	private void sendJSON(JSONObject obj){
		try {
			mySocket.sendMessage(obj.toJSONString());
		}
		catch (Exception e){
			System.out.println("Error al enviar paquete");
		}
	}
	private JSONArray parseJSONArray(){
		try{
			return (JSONArray) parser.parse(mySocket.receiveMessage());
		}
		catch (ParseException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	private JSONObject parseJSONObject(){
		try{
			return (JSONObject) parser.parse(mySocket.receiveMessage());
		}
		catch (ParseException | IOException e) {
			throw new RuntimeException(e);
		}
	}

} //end class
