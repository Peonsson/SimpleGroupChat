package src.Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Assignment 1B.
 */
public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            // Create socket to address server at port 50020
            InetAddress server = InetAddress.getLocalHost();
            socket = new Socket(server, 50015);

            ClientMessageReceiver receiver = new ClientMessageReceiver(socket);
            receiver.start();

            // Create socket output objects
            out = new PrintWriter(socket.getOutputStream(), true);

            Scanner scan = new Scanner(System.in);

            String message = null;
            while (message == null || !message.equals("")) {
                System.out.print("Message: ");
                message = scan.nextLine();
                out.println(message);
            }
            scan.close();
        }
        catch (UnknownHostException uhe) {
            System.out.println("UnknownHostException.");
        }
        catch (IOException ioe) {
            System.out.println("Socket error.");
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException ioe) {
                System.out.println("Couldn't close socket.");
            }
        }
    }
}
