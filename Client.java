import java.io.*;
import java.net.*;

/**
 * @Class Client contains all the variables and methods necessary for the functionality
 * of the Socket client
 * It connects to the socket server opened on the port specified, then allows the client to send messages to it
 */

public class Client {

    Socket clientSocket;
    PrintWriter output;
    BufferedReader input;

    public Client(int port) throws IOException {
        clientSocket = new Socket("127.0.0.1", port);
        System.out.println("Client instantiated");
        output = new PrintWriter(clientSocket.getOutputStream());

        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String fromServer;
        String fromUser;
        // Get the input from the server and run our program based on that, since
        // the program itself ends when the server replies to a "Bye" with "disconnected"
        while ((fromServer = input.readLine()) != "disconnected") {
            if (fromServer != null)
                System.out.println(fromServer);
            fromUser = input.readLine();
            output.println(fromUser);
        }
        input.close();
        output.close();
        clientSocket.close();
        System.exit(0);
    }    
    public static void main(String[] args) {
        try {
            Client client = new Client(4200);
        } catch(IOException error) {
            System.out.println(error.getMessage());
        }
    }
}
