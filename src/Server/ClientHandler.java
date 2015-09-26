package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * 2015-09-26
 * by Peonsson and roppe546
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
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //TODO check message for commands.. strings starting with "/"
            //TODO commands: /quit, /who, /nick <nickname>, /help, / prints "unknown command"
            while (message == null || !message.equals("")) {
                // Get message from client and echo it back
                message = in.readLine();

                // Send message to all clients
                for (int i = 0; i < clientSockets.size(); i++) {
                    if(clientSockets.get(i).isClosed())
                        clientSockets.remove(i);

                    if(clientSockets.get(i).equals(clientSocket))
                        continue;
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
