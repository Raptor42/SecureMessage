import java.net.*;
import java.io.*;

public class IMServerThread extends Thread {
    private Socket socket = null;
    private Socket otherSocket = null;
    private String name;

    public IMServerThread(Socket socket, String ID) {
        super(ID);
        this.socket = socket;
        this.name = ID;
        System.out.println("Thread Created - Socket: " + getCurrentSocket() + " ID: " + getCurrentName());

    }

    public String getCurrentName() {
        return this.name;
    }

    public Socket getCurrentSocket() {
        return this.socket;
    }

    public void chooseSocket() {
        String otherID = null;

        while (otherID == null) {
            System.out.println("Prompting for Target ID");
            writeToMe("Please choose ID from list, Please write exactly, or write \"!refresh\" to get new list. ");

            System.out.println("Getting ID list");
            String list = IMServer.getSocketList();

            System.out.println(list);

            System.out.println("Printing ID list to " + getCurrentName());
            writeToMe(list);

            System.out.println("Getting choice");
            String response = getInput();

            if (!response.equals("!refresh"))
                otherID = response;
        }

        otherSocket = IMServer.getSocket(otherID);

        writeToMe("Connected to " + otherID);
    }

    public void listen() {
        String inputLine;
        while ((!(inputLine = getInput()).equals("!exit"))&&(!(inputLine.equals("!menu")))) {
            writeToYou(name + ": " + inputLine);
        }

        if (inputLine.equals("!menu")) {
            chooseSocket();
            listen();
        }
    }

    public void exit() {
        IMServer.removeClient(getCurrentName());
        writeToMe("!ListenThreadClose");

        try {
            socket.close();
        } catch (IOException ioE) {
            ioE.printStackTrace();
        }
    }
    
    public void run() {
        chooseSocket();

        listen();

        exit();
    }

    private String getInput() {
        String inputLine = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Client " + (getCurrentName()+1) + ": " + inputLine);
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputLine;
    }

    private void writeToMe(String s) {
        System.out.println("trying to write \"" + s + "\" to socket " + getCurrentSocket());
        try {
            PrintWriter out = new PrintWriter(getCurrentSocket().getOutputStream(), true);
            System.out.println("Writng \"" + s + "\" to socket " + getCurrentSocket());
            out.println(s);
            System.out.println("Written.");
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    private void writeToYou(String s) {
        try {
            PrintWriter out = new PrintWriter(otherSocket.getOutputStream(), true);
            out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
}