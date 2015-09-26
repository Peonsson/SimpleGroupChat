package Server;

import src.Server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Assignment 1B.
 */
public class ClientHandler extends Thread {
    Socket clientSocket = null;
    ArrayList<Socket> clientSockets = null;

    public ClientHandler(Socket clientSocket, ArrayList<Socket> clientSockets) {
        this.clientSocket = clientSocket;
        this.clientSockets = clientSockets;
    }

    // Stuff to do with individual client
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            String message = null;

            while (message == null || !message.equals("")) {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Get message from client and echo it back
                message = in.readLine();

                // Send message to all clients
                for (int i = 0; i < clientSockets.size(); i++) {
                    out = new PrintWriter(clientSockets.get(i).getOutputStream(), true);

                    System.out.println("Sending: " + message + " to client; " + clientSockets.get(i).getInetAddress() + ": " + clientSockets.get(i).getPort());
                    out.println(message);
                    out.flush();
                }
            }
        }
        catch (IOException ioe) {
            System.out.println("IOException.");
        }
        finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            }
            catch (IOException ioe) {
                System.out.println("Couldn't close client socket properly.");
            }
        }
    }
}
