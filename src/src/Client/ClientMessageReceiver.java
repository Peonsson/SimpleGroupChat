package src.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Assignment 1B.
 */
public class ClientMessageReceiver extends Thread {
    BufferedReader in = null;

    public ClientMessageReceiver(Socket server) {
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        }
        catch (IOException ioe) {
            System.out.println("Input stream doesn't exist.");
        }
    }

    public void run() {
        String receiveText = null;

        try {
            while (true) {
                receiveText = in.readLine();
                System.out.println(receiveText);
            }
        }
        catch (IOException ioe) {
            System.out.println("IOException.");
        }
    }
}
