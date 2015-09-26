package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Peonsson and roppe546 on 2015-09-26.
 */
public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ArrayList<ConnectedClient> clients = new ArrayList<ConnectedClient>();
        int counter = 1;

        try {
            serverSocket = new ServerSocket(50015);
            PrintWriter out = null;
            System.out.println("Server: Waiting for connections..");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ConnectedClient client = new ConnectedClient("anonymous" + counter++, clientSocket);

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Welcome message!");
                out.flush();

                clients.add(client);
                ClientHandler cHandler = new ClientHandler(client, clients);
                cHandler.start();
            }
        }
        catch (IOException ioe) {
            System.out.println("Server: Couldn't create server socket.");
        }
        finally {
            try {
                serverSocket.close();
            }
            catch (IOException ioe) {
                System.out.println("Server: Couldn't close server socket.");
            }
        }
    }
}
