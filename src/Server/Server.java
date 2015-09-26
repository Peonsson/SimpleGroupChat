package Server;

import java.io.IOException;
import java.io.PrintWriter;
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
            PrintWriter out = null;
            System.out.println("Server: Waiting for connections..");
            while (true) {
                Socket clientSocket = serverSocket.accept();

                out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Welcome message!");
                out.flush();

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
