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

    // Stuff to do with individual client
    public void run() {
        BufferedReader in;

        try {
            String message;
            in = new BufferedReader(new InputStreamReader(client.getClientSocket().getInputStream()));

            while (true) {
                message = in.readLine();

                if (message.equals("") || message.equals("\n")) {
                    int clientToRemove = connectedClients.indexOf(client);
                    connectedClients.remove(clientToRemove);

                    break;
                }

                if (!checkForCommands(message)) {
                    broadcast(client.getNickname() + ": " + message);
                }
            }
        }
        catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        finally {
            try {
                client.getClientSocket().close();
            }
            catch (IOException ioe) {
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
                    }
                    else if (message.startsWith("/")) {
                        out.println("Unknown command");
                        return true;
                    }
            }
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    private void broadcast(String message) {
        PrintWriter out;

        for (int i = 0; i < connectedClients.size(); i++) {
            // Do not send clients own message to itself
            if (connectedClients.get(i).equals(client))
                continue;

            try {
                out = new PrintWriter(connectedClients.get(i).getClientSocket().getOutputStream(), true);
                out.println(message);
                System.out.println("Sending: " + message + " from client: " + client.getClientSocket().getInetAddress() + ":" + client.getClientSocket().getPort() + " to client " + connectedClients.get(i).getClientSocket().getInetAddress() + ":" + connectedClients.get(i).getClientSocket().getPort());

            }
            catch (IOException e) {
                System.out.println("ClientHandler: Couldn't send to all clients registered.");

                for (int j = 0; j < connectedClients.size(); j++) {
                    if (connectedClients.get(j).getClientSocket().isClosed()) {
                        synchronized (connectedClients) {
                            connectedClients.remove(j);
                        }
                        i--;
                    }
                }

                System.out.println(connectedClients.toString());
            }
        }
    }
}
