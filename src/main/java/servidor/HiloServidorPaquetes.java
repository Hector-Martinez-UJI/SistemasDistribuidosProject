package servidor;

import comun.MyStreamSocket;
import gestor.GestorPaquetes;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.SocketException;

/**
 * Clase ejecutada por cada hebra encargada de servir a un cliente del servicio de paquetería.
 * El método run contiene la lógica para gestionar una sesión con un cliente.
 */

class HiloServidorPaquetes implements Runnable {


	final private MyStreamSocket myDataSocket;
	final private GestorPaquetes gestor;

	/**
	 * Construye el objeto a ejecutar por la hebra para servir a un cliente
	 * @param	myDataSocket	socket stream para comunicarse con el cliente
	 * @param	unGestor		gestor de viajes
	 */
	HiloServidorPaquetes(MyStreamSocket myDataSocket, GestorPaquetes unGestor) {
		this.myDataSocket = myDataSocket;
		this.gestor = unGestor;
	}

	/**
	 * Gestiona una sesión con un cliente
	 */
	public void run( ) {

		// Declaracion de variables
		boolean done = false;
		String operacion, codCliente, CPOrigen, CPDestino, codMensajero;
		long codPaquete;
		double peso;
		JSONParser parser = new JSONParser();

		try {
			while (!done) {
				// Recibe una petición del cliente
				String mensaje = myDataSocket.receiveMessage();

				// Extrae la operación y sus parámetros
				JSONObject obj = (JSONObject) parser.parse(mensaje);
				operacion = obj.get("operacion").toString();


				switch (operacion) {
					case "0": {
						done = true;
						myDataSocket.close(); // Cerramos la conexión
						break;
					}
					case "1": { // Devuelve una lista de paquetes enviados por un cliente dado
						codCliente = obj.get("codCli").toString();
						JSONArray array = gestor.listaPaquetesCliente(codCliente);

						myDataSocket.sendMessage(array.toJSONString());
						break;
					}
					case "2": { // Un cliente hace un envío
						codCliente = obj.get("codCli").toString();
						CPOrigen = obj.get("CPOrigen").toString();
						CPDestino = obj.get("CPDestino").toString();
						peso = Double.parseDouble(obj.get("peso").toString());
						JSONObject paquete = gestor.enviaPaquete(codCliente, CPOrigen, CPDestino, peso);

						myDataSocket.sendMessage(paquete.toJSONString());
						break;
					}
					case "3": { // Un cliente modifica uno de sus envíos
						codCliente = obj.get("codCli").toString();
						codPaquete = Long.parseLong(obj.get("codPaquete").toString());
						CPOrigen = obj.get("CPOrigen").toString();
						CPDestino = obj.get("CPDestino").toString();
						peso = Double.parseDouble(obj.get("peso").toString());
						JSONObject modificado = gestor.modificaPaquete(codCliente, codPaquete, CPOrigen, CPDestino, peso);

						myDataSocket.sendMessage(modificado.toJSONString());
						break;
					}
					case "4": { // Un cliente retira uno de sus paquetes
						codPaquete = Long.parseLong(obj.get("codPaquete").toString());
						codCliente = obj.get("codCli").toString();
						JSONObject retirado = gestor.retiraPaquete(codCliente, codPaquete);

						myDataSocket.sendMessage(retirado.toJSONString());
						break;
					}
					case "5": {  // Devuelve una lista con los paquetes enviados a un CP dado
						CPDestino = obj.get("CPDestino").toString();
						JSONArray arr = gestor.listaPaquetesCP(CPDestino);

						myDataSocket.sendMessage(arr.toJSONString());
						break;
					}
					case "6": {  // Un mensajero recoge un paquete
						codPaquete = Long.parseLong(obj.get("codPaquete").toString());
						codMensajero = obj.get("codMensajero").toString();
						JSONObject recogido = gestor.recogePaquete(codPaquete, codMensajero);

						myDataSocket.sendMessage(recogido.toJSONString());
						break;
					}
				} // fin switch
			} // fin while
		} // fin try
		catch (SocketException ex) {
			System.out.println("Capturada SocketException");
		}
		catch (IOException ex) {
			System.out.println("Capturada IOException");
		}
		catch (Exception ex) {
			System.out.println("Exception caught in thread: " + ex);
		} // fin catch
	} //fin run

} //fin class 
