package Server;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Class Server contains all the variables and methods necessary for the functionality
 * of the Socket Server
 */
public class Server {
    // In order to use this, just instantiate it with the constructor
    ServerSocket server;
    Socket clientListener;
    DataOutputStream output;
    DataInputStream input;
    String clientIP;

    public Server(int port) throws IOException {    
        // Note: Server throws IOException but I listen for a select few
        // errors that I care about (mainly the ones involving starting and ending the process)

        // Take the port and instantiate the server on that port
        try {
            server = new ServerSocket(4200);
            System.out.println("SERVER: Serversocket created on port " + port);
        } catch(IOException error) {
            System.out.println("Could not instantiate port " + port);
            System.out.println(error.getMessage());
            System.exit(-1);
        }
    }
    public String readJokeFile(String filename) throws IOException {
        Path path = Path.of(System.getProperty("user.dir") + "/" + filename );
        return Files.readString(path);

    }
    public void attemptConnection() throws IOException {
        try {
            // Listens for a connection on the socket specified by the constructor
            clientListener = server.accept();
            clientIP = clientListener.getRemoteSocketAddress().toString();
            System.out.println("SERVER: Successful connection from " + clientIP + ".");
        } catch(IOException error) {
            System.out.println("An error arose while listening for the client");
            System.out.println(error.getMessage());
            System.exit(-1);
        }
        try {
            // Creating input and output streams in order to send text and file byte arrays thru to the client
            input = new DataInputStream(new BufferedInputStream(clientListener.getInputStream()));
            output = new DataOutputStream(clientListener.getOutputStream());
            // The true in the second argument for "PrintWriter" autoflushes the buffer when new text is received


        } catch (IOException error) {
            System.out.println("There was an error instantiating the necessary tools for Input and Output reading/writing");
            System.out.println(error.getMessage());
            System.exit(-1);
        }

        // Allocating String to actually store the input and output text
        String inputLine;
        while (true) {
            // Wait for client message
            inputLine = input.readUTF();
            // Once received, print out to the console.
            System.out.println("CLIENT: " + inputLine);
            // Then, based on this text, decide what to send back by checking the string
            if (inputLine.equals("Joke 1")) {
                // Send back joke 1
                String fileContents = readJokeFile("Server/joke1.txt");
                // Print out the contents of the file
                System.out.println(fileContents);

                System.out.println("File Contents outputted successfully!");

                output.writeUTF(fileContents);
            } else if (inputLine.equals("Joke 2")) {
                // Send back joke 2
                String fileContents = readJokeFile("Server/joke2.txt");
                // Print out the contents of the file
                System.out.println(fileContents);

                System.out.println("File Contents outputted successfully!");

                output.writeUTF(fileContents);
            } else if (inputLine.equals("Joke 3")) {
                // Send back joke 3
                String fileContents = readJokeFile("Server/joke3.txt");
                // Print out the contents of the file
                System.out.println(fileContents);

                System.out.println("File Contents outputted successfully!");

                output.writeUTF(fileContents);
            } else if (inputLine.equals("bye")) {
                // Break out of the infinite loop, notify the client that it can disconnect,
                break;
            } else {
                // Send back that there was an error
                output.writeUTF("ERROR: Unrecognized input! Please refer to documentation \n");
            }
        }
        // Notify the client that they've been disconnected
        try {
            output.writeUTF("disconnected");
            input.readUTF();
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
        System.out.println("SERVER: " + clientIP + " has disconnected from the server.");
        input.close();
        output.close();
        clientListener.close();

    }


    public static void main(String args[]) {
        try {
            Server server = new Server(4200);
            while (true) {
                server.attemptConnection();
            }
        } catch(IOException error) {
            System.out.println("There was an error with the server");
            System.out.println(error.getMessage());
        } finally {
        }
    }
}
