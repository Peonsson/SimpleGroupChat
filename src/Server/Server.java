package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 2015-09-26
 * by Peonsson and roppe546
 * Assignment 1B.
 */
public class Server {
    public static void main(String[] args) {
        boolean listening = true;
        ServerSocket serverSocket = null;
        ArrayList<Socket> clients = new ArrayList<>();

        try {
            serverSocket = new ServerSocket(50015);
            System.out.println("Waiting for connections..");
            while (listening) {
//                Socket clientSocket = ;
                clients.add(serverSocket.accept());
                int indexOfLastAddedSocket = clients.size() - 1;

                ClientHandler cHandler = new ClientHandler(clients.get(indexOfLastAddedSocket), clients);

                // Start a new thread which handles an individual client
                cHandler.start();
            }
        }
        catch (IOException ioe) {
            System.out.println("Couldn't create server socket.");
        }
        finally {
            try {
                serverSocket.close();
            }
            catch (IOException ioe) {
                System.out.println("Couldn't close server socket.");
            }
        }
    }
}
