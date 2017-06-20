import java.io.*;
import java.net.*;
 
public class IMClient {

    public static void exit() {
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
         
        if (args.length != 3) {
            System.err.println("Usage: java IMClient <host name> <port number> <ID>");
            System.exit(1);
        }
 
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        String ID = args[2];
 
        try (
            Socket socket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            new IMClientListenThread(socket).start();

            out.println(ID);

            String userInput;
            while ((userInput = stdIn.readLine()) != null) 
                out.println(userInput);
            
            socket.close();
            System.out.println("Socket Closed by client");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        } 
    }
}