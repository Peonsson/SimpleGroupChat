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
        ServerSocket serverSocket = null;
        ArrayList<Socket> clients = new ArrayList<Socket>();

        try {
            serverSocket = new ServerSocket(50015);
            System.out.println("Server: Waiting for connections..");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                ClientHandler cHandler = new ClientHandler(clientSocket, clients);
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
