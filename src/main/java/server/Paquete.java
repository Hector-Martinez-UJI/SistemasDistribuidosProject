package server;

import org.json.simple.JSONObject;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Clase Paquete que representa un paquete para envío.
 * Implementa la interfaz Serializable para permitir la serialización del objeto.
 */
public class Paquete implements Serializable {
	// Sustituye esta clase por tu versión de la misma de la práctica 1

    @Serial
    private static final long serialVersionUID = 1L;

    // Atributos de la clase Paquete
    private final long codPaquete;  // código único del paquete generado al enviarlo
    private String codCliente;      // código único del cliente que envía el paquete
    private String codMensajero;    // código único del mensajero que recoge el paquete. Vacío si no recogido
    private String CPOrigen;        // código postal del origen del paquete
    private String CPDestino;       // código postal del destino del paquete. Vacío si no lo ha recogido un mensajero
    private String fechaEnvio;      // fecha de envío en formato "dd-mm-yyyy"
    private String fechaRecogida;   // fecha de recogida en formato "dd-mm-yyyy"
    private double peso;            // Peso en kilos

    // Formateador de fechas para el formato "dd-mm-yyyy"
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Devuelve la fecha actual en formato "dd-MM-yyyy".
     *
     * @return fecha actual en formato "dd-MM-yyyy"
     */
    public static String fechaHoy() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    /**
     * Genera un código único para el paquete como un entero aleatorio de 4 dígitos
     *
     * @return un código único para el paquete
     */
    private long generaCodPaquete() {
        return ThreadLocalRandom.current().nextLong(1000, 10000); // Genera un número aleatorio de 4 dígitos
    }

    /**
     * Constructor para crear una instancia de la clase Paquete.
     *
     * @param codCliente código único del cliente que envía el paquete
     * @param CPOrigen   código postal del origen del paquete
     * @param CPDestino  código postal del destino del paquete
     * @param peso       peso del paquete en kilos
     */
    public Paquete(String codCliente, String CPOrigen, String CPDestino, double peso) {
        this.codPaquete = generaCodPaquete();
        this.codCliente = codCliente;
        this.codMensajero = ""; // Al enviarse todavía no lo ha recogido ningún mensajero
        this.CPOrigen = CPOrigen;
        this.CPDestino = CPDestino;
        this.fechaEnvio = fechaHoy();
        this.fechaRecogida = ""; // Al enviarse, todavía no hay fecha de recogida
        this.peso = peso;
    }

    /**
     * Constructor de un Paquete a partir de su representación en formato JSON.
     *
     * @param jsonPaquete objeto JSON con los datos de un paquete
     */
    public Paquete(JSONObject jsonPaquete) {
        this.codPaquete = (long) jsonPaquete.get("codPaquete");
        this.codCliente = (String) jsonPaquete.get("codCliente");
        this.codMensajero = jsonPaquete.get("codMensajero").toString();
        this.CPOrigen = jsonPaquete.get("CPOrigen").toString();
        this.CPDestino = jsonPaquete.get("CPDestino").toString();
        this.fechaEnvio = jsonPaquete.get("fechaEnvio").toString();
        this.fechaRecogida = jsonPaquete.get("fechaRecogida").toString();
        this.peso = (double) jsonPaquete.get("peso");
    }

    /**
     * Devuelve los datos del paquete como cadena en formato JSON.
     *
     * @return cadena en formato JSON con los datos del paquete
     */
    @Override
    public String toString() {
        return this.toJSON().toJSONString();
    }

    /**
     * Devuelve un objeto JSON con los datos del paquete.
     *
     * @return objeto JSON con los datos del paquete
     */
    @SuppressWarnings("unchecked")
    public JSONObject toJSON() {

        JSONObject jsonPaquete = new JSONObject();
        jsonPaquete.put("codPaquete", codPaquete);
        jsonPaquete.put("codCliente", codCliente);
        jsonPaquete.put("codMensajero", codMensajero);
        jsonPaquete.put("CPOrigen", CPOrigen);
        jsonPaquete.put("CPDestino", CPDestino);
        jsonPaquete.put("fechaEnvio", fechaEnvio);
        jsonPaquete.put("fechaRecogida", fechaRecogida);
        jsonPaquete.put("peso", peso);

        return jsonPaquete;
    }

    // Getters and Setters
    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getCodMensajero() {
        return codMensajero;
    }

    public void setCodMensajero(String codMensajero) {
        this.codMensajero = codMensajero;
    }

    public long getCodPaquete() {
        return codPaquete;
    }

    public String getCPDestino() {
        return CPDestino;
    }

    public void setCPDestino(String CPDestino) {
        this.CPDestino = CPDestino;
    }

    public String getCPOrigen() {
        return CPOrigen;
    }

    public void setCPOrigen(String CPOrigen) {
        this.CPOrigen = CPOrigen;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getFechaRecogida() {
        return fechaRecogida;
    }

    public void setFechaRecogida(String fechaRecogida) {
        this.fechaRecogida = fechaRecogida;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
