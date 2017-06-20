import java.net.*;
import java.io.*;

public class IMServerThread extends Thread {
    private Socket socket = null;
    private int name;

    public int getOtherName() {
        if (getCurrentName() == 1) {
            return 0;     
        } else {
            return 1;
        }
    }

    public IMServerThread(Socket socket, int newName) {
        super(String.valueOf(newName));
        this.name = newName;
        this.socket = socket;
    }

    public int getCurrentName() {
        return this.name;
    }
    
    public void run() {
        while (IMServer.getSocket(getOtherName()) == null) {}

        Socket otherSocket = IMServer.getSocket(getOtherName());

        try (
            PrintWriter otherOut = new PrintWriter(otherSocket.getOutputStream(), true);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                System.out.println("Client " + (getCurrentName()+1) + ": " + inputLine);
                otherOut.println(inputLine);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}