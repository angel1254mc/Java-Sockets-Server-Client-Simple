package Client;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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
        System.out.println("Successfully connected to IP: " + IP +", and Port: " + port );
        fromUser = terminalInput.readLine();
        String currentJoke = "-1";
        while (true) {
            
            output.writeUTF(fromUser);
            if (fromUser.contains("Joke")) {
                currentJoke = (fromUser.charAt(fromUser.length() - 1)) + "";
            }
            // Account for disconnected
            fromServer = input.readUTF();
            if (fromServer.equals("disconnected")) {
                System.out.println("Receive: disconnected");
                break;
            }
            // Print out received data
            if (fromServer.contains("ERROR")) {
                System.out.println("Receive: " + fromServer);
            } else if (!currentJoke.equals("-1")) {
                System.out.println("Receive: joke" + currentJoke + ".txt");
                writeFile(fromServer, "/joke" + currentJoke + ".txt");
            }
            
            fromUser = terminalInput.readLine();
        }

        output.writeUTF("Client left");
        System.out.println("Exit");
        input.close();
        output.close();
        clientSocket.close();
        System.exit(0);
    }    

    void writeFile(String fileContent, String fileName) throws IOException {
        // Writes a file to the local directory
        Path path = Path.of(System.getProperty("user.dir") + fileName );
        Files.writeString(path, fileContent, StandardCharsets.UTF_8);
    }
    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                Client client = new Client(args[0], 4200);
            } else {
                Client client = new Client("localhost", 4200);
            }
        } catch(IOException error) {
            System.out.println("There was an error???");
            System.out.println(error.getMessage());
        }
    }
}
