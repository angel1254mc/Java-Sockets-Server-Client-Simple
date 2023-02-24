import java.io.*;
import java.net.*;

/**
 * @Class Client contains all the variables and methods necessary for the functionality
 * of the Socket client
 * It connects to the socket server opened on the port specified, then allows the client to send messages to it
 */

public class Client {

    Socket clientSocket;
    DataOutputStream output;
    DataInputStream input;
    BufferedReader terminalInput;

    
    public Client(String IP, int port) throws IOException {
        clientSocket = new Socket(IP, port);

        output =  new DataOutputStream(clientSocket.getOutputStream());
        input = new DataInputStream(clientSocket.getInputStream());
        terminalInput = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        // Get the input from the server and run our program based on that, since
        // the program itself ends when the server replies to a "Bye" with "disconnected"

        fromUser = terminalInput.readLine();
        while (true) {
            output.writeUTF(fromUser);
            fromServer = input.readUTF();
            if (fromServer.equals("disconnected"))
                break;
            System.out.println("Server Response looks like: " + fromServer);
            fromUser = terminalInput.readLine();
        }
        System.out.println("Closing client");
        output.writeUTF("Client left");
        input.close();
        output.close();
        clientSocket.close();
        System.exit(0);
    }    
    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 4200);
        } catch(IOException error) {
            System.out.println("There was an error???");
            System.out.println(error.getMessage());
        }
    }
}
