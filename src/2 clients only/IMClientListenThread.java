import java.io.*;
import java.net.*;

public class IMClientListenThread extends Thread {
    private Socket socket = null;

    public IMClientListenThread(Socket socket) {
        super("ListenThread");
        this.socket = socket;
    }
    
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String response;
            while ((response = in.readLine()) != null) 
                System.out.println("Response: " + response);
            
            socket.close();
            System.out.println("Socket Closed by client listen thread");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host ");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to ");
            System.exit(1);
        } 
    }
}