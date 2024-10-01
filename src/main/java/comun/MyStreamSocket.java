package comun;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class MyStreamSocket extends Socket {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public MyStreamSocket(String acceptorHost, int acceptorPort)  throws SocketException, IOException {
        socket = new Socket(acceptorHost, acceptorPort);
        setStreams();
    }
    public MyStreamSocket(Socket socket)  throws IOException {
        this.socket = socket;
        setStreams();
    }
    private void setStreams() throws IOException {
        input  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }
    public void sendMessage(String message) throws IOException {
        output.print(message + "\n");  output.flush();
    }
    public String receiveMessage() throws IOException {
        String message = input.readLine();  return message;
    }
    public void close() throws IOException { socket.close(); }
}