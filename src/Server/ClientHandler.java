package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Peonsson and roppe546 on 2015-09-26.
 */
public class ClientHandler extends Thread {
    private ConnectedClient client = null;
    private ArrayList<ConnectedClient> connectedClients = null;

    public ClientHandler(ConnectedClient client, ArrayList<ConnectedClient> connectedClients) {
        this.client = client;
        this.connectedClients = connectedClients;
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            String message = null;
            in = new BufferedReader(new InputStreamReader(client.getClientSocket().getInputStream()));

            //TODO check messages for commands.. strings starting with "/"
            //TODO commands: /quit, /who, /nick <nickname>, /help, / prints "unknown command"
            while (true) {
                System.out.println("bufferedReader ID: " + in.toString());
                System.out.println("Thread " + Thread.currentThread().getId() + " blocking at readLine...");
                message = in.readLine();

                if (message.equals("") || message.equals("\n")) {
                    System.out.println("message equals \"\" or \"\\n\"");

                    int clientToRemove = connectedClients.indexOf(client);
                    connectedClients.remove(clientToRemove);

                    System.out.println("breaking threadID: " + Thread.currentThread().getId());
                    break;
                }

                if (!checkForCommands(message)) {
                    System.out.println("ClientHandler: No command entered.");
                    for (int i = 0; i < connectedClients.size(); i++) {

                        //do not send my own message to myself
                        if (connectedClients.get(i).equals(client))
                            continue;

                        out = new PrintWriter(connectedClients.get(i).getClientSocket().getOutputStream(), true);
                        System.out.println("Sending: " + message + " from client: " + client.getClientSocket().getInetAddress() + ":" + client.getClientSocket().getPort() + " to client " + connectedClients.get(i).getClientSocket().getInetAddress() + ":" + connectedClients.get(i).getClientSocket().getPort());
                        out.println(client.getNickname() + ": " + message);
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println("ClientHandler: IOException." + Thread.currentThread().getId());
            System.out.println(ioe.getMessage());
        } catch (NullPointerException npe) {
            //TODO: CLEAR INCORRECTLY DISCONNECTED CLIENTS FROM LIST
        } finally {
            System.out.println("Thread " + Thread.currentThread().getId() + " is in finally block. Closing in, out, and socket.");
            try {
                client.getClientSocket().close();
            } catch (IOException ioe) {
                System.out.println("Couldn't close client socket properly.");
            }
        }
    }

    private boolean checkForCommands(String message) {
        try {
            PrintWriter out = new PrintWriter(client.getClientSocket().getOutputStream(), true);

            switch (message) {
                case "/quit":
                    synchronized (connectedClients) {
                        int clientToRemove = connectedClients.indexOf(client);
                        connectedClients.remove(clientToRemove);
                    }
                    broadcast(client.getNickname() + " has left chat.");
                    return true;
                case "/who":
                    String whoMsg = "";
                    for (int i = 0; i < connectedClients.size(); i++) {
                        whoMsg += connectedClients.get(i).getNickname() + "\n";
                    }
                    out.println(whoMsg);
                    return true;
                case "/help":
                    out.println("You can use the following commands:\n/who - list all clients online\n/nick <NICKNAME> - change nickname\n/quit - quit the chat");
                    return true;
                default:
                    if (message.startsWith("/nick")) {
                        String[] myStrings = message.split(" ");
                        for (int i = 0; i < connectedClients.size(); i++) {
                            if (myStrings[1].equals(connectedClients.get(i).getNickname())) {
                                out.println("Nickname already in use please choose another one!");
                                return true;
                            }
                        }
                        client.setNickname(myStrings[1]);
                        return true;
                    } else if (message.startsWith("/")) {
                        out.println("Unknown command");
                        return true;
                    }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    private void broadcast(String message) {
        PrintWriter out = null;
        for (int i = 0; i < connectedClients.size(); i++) {
            try {
                out = new PrintWriter(connectedClients.get(i).getClientSocket().getOutputStream(), true);
                out.println(message);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
