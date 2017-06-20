import java.net.*;
import java.io.*;
import java.util.*;
 
public class IMServer {

    private static HashMap<String, Socket> sockets = new HashMap<String, Socket>();

    public static String listID = null;

    public static void addToIDSring(String s) {
        listID += s + System.lineSeparator();
    }

    public static String getSocketList() {
        listID = null;
        listID = "ID" + System.lineSeparator() + System.lineSeparator();
        sockets.forEach((k,v) -> {
                addToIDSring(k);
            });
        return listID;
    }

    public static Socket getSocket(String key) {
        return sockets.get(key);
    }

    public static void removeClient(String ID) {
        sockets.remove(ID);
    }

    public static void main(String[] args) throws IOException {
         
        if (args.length != 1) {
            System.err.println("Usage: java IMServer <port number>");
            System.exit(1);
        }
         
        int portNumber = Integer.parseInt(args[0]);
         
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                Socket newSocket = serverSocket.accept();
                System.out.println("New Socket: " + newSocket);
                String ID = null;

                System.out.println("Getting ID");
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(newSocket.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        ID = inputLine;
                        break;
                    }
                    System.out.println("ID: " + ID);
                } catch (IOException e) {
                    e.printStackTrace();
                } 

                System.out.println("Adding to map");
                sockets.put(ID, newSocket);
                
                System.out.println("Creating thread");
                new IMServerThread(sockets.get(ID), ID).start();
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}