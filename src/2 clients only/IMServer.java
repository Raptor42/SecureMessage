import java.net.*;
import java.io.*;
 
public class IMServer {

    private static Socket[] sockets = new Socket[2];

    public static Socket getSocket(int i) {
        return sockets[i];
    }

    public static void main(String[] args) throws IOException {
         
        if (args.length != 1) {
            System.err.println("Usage: java IMServer <port number>");
            System.exit(1);
        }
         
        int portNumber = Integer.parseInt(args[0]);
         
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            int i = 0;

            while (true) {
                sockets[i] = serverSocket.accept();
                new IMServerThread(getSocket(i), i).start();
                i++;

                System.out.println("Clients: " + i);
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}