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

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            String message = null;
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //TODO check messages for commands.. strings starting with "/"
            //TODO commands: /quit, /who, /nick <nickname>, /help, / prints "unknown command"

            while (true) {
                message = in.readLine();

                if (message.equals("") || message.equals("\n")) {
                    System.out.println("message equals \"\" or \"\\n\"");

                    int socketToRemove = clientSockets.indexOf(clientSocket);
                    clientSockets.remove(socketToRemove);

                    System.out.println("breaking threadID: " + Thread.currentThread().getId());
                    break;
                }

                //IN PRODUCTION
                System.out.println(checkForCommands(message));

                System.out.println("ClientHandler: TEXT WAS ENTERED");
                for (int i = 0; i < clientSockets.size(); i++) {

                    //do not send my own message to myself
                    if (clientSockets.get(i).equals(clientSocket))
                        continue;

                    out = new PrintWriter(clientSockets.get(i).getOutputStream(), true);
                    System.out.println("Sending: " + message + " from client: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " to client " + clientSockets.get(i).getInetAddress() + ":" + clientSockets.get(i).getPort());
                    out.println(message);
                    out.flush();
                }
            }
        } catch (IOException ioe) {
            System.out.println("ClientHandler: IOException." + Thread.currentThread().getId());
            System.out.println(ioe.getMessage());
        } finally {
            try {
                //in.close();
                //out.close();
                clientSocket.close();
            } catch (IOException ioe) {
                System.out.println("Couldn't close client socket properly.");
            }
        }
    }

    private boolean checkForCommands(String message) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            switch (message) {
                case "/":
                    out.println("Unknown command");
                    out.flush();
                    return true;
                case "/quit":
                    out.println("Qutting..");
                    out.flush();
                    return true;
                case "who":
                    out.println("Who am I?");
                    out.flush();
                    return true;
                case "/nick":
                    out.println("Unknown command");
                    out.flush();
                    return true;
                case "/help":
                    out.println("Unknown command");
                    out.flush();
                    return true;
            }
        }catch (IOException e) {
            System.err.println(e.getMessage());
        }finally {
            return false;
        }

    }
}
