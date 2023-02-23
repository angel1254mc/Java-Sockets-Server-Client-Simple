import java.io.*;
import java.net.*;

/**
 * @Class Server contains all the variables and methods necessary for the functionality
 * of the Socket Server
 */
public class Server {
    // In order to use this, just instantiate it with the constructor
    ServerSocket server;
    Socket clientListener;
    PrintWriter output;
    BufferedReader input;
    public Server(int port) throws IOException {    
        System.out.println("Welcome to the Websocket Server Constructor!");
        System.out.println("Starting up Sockets and Tools");
        // Note: Server throws IOException but I listen for a select few
        // errors that I care about (mainly the ones involving starting and ending the process)

        // Take the port and instantiate the server on that port
        try {
            server = new ServerSocket(4200);
        } catch(IOException error) {
            System.out.println("Could not instantiate port " + port);
            System.out.println(error.getMessage());
            System.exit(-1);
        }

        System.out.println("Waiting for a client ...");
        try {
            // Listens for a connection on the socket specified by the constructor
            clientListener = server.accept();
        } catch(IOException error) {
            System.out.println("An error arose while listening for the client");
            System.out.println(error.getMessage());
            System.exit(-1);
        }
        try {
            // Hooks off the Input Stream reader and allows us to listen for client input as it occurs
            // This helps us build a while loop that runs until the user says "bye"
            input = new BufferedReader(new InputStreamReader(clientListener.getInputStream()));
            output = new PrintWriter(clientListener.getOutputStream(), true);
            // The true in the second argument for "PrintWriter" autoflushes the buffer when new text is received


        } catch (IOException error) {
            System.out.println("There was an error instantiating the necessary tools for Input and Output reading/writing");
            System.out.println(error.getMessage());
            System.exit(-1);
        }

        // Allocating String to actually store the input and output text
        String inputLine, outputLine;
        while ((inputLine = input.readLine()) != "bye") {
            System.out.println("Received: " + inputLine);
            outputLine = "Server received: " + inputLine;
            output.println(outputLine);
        }
        System.out.println("See ya!");

        input.close();
        output.close();
        clientListener.close();
        server.close();
    }


    public static void main(String args[]) {
        try {
            Server server = new Server(4200);
        } catch(IOException error) {
            System.out.println(error.getMessage());
        }
    }
}
