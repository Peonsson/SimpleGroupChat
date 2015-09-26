package Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Created by Peonsson and roppe546 on 2015-09-26.
 */
public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            InetAddress server = InetAddress.getLocalHost();
            socket = new Socket(server, 50015);

            ClientMessageReceiver receiver = new ClientMessageReceiver(socket);
            receiver.start();

            out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scan = new Scanner(System.in);

            String message = "";
            while (true) {
                try {
                    message = scan.nextLine();
                } catch(NoSuchElementException e) {
                    return;
                }
                out.println(message);
                if (message.equals("/quit")) {
                    break;
                }
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
            } catch (NullPointerException e) {
                System.out.println("Server probably not alive.");
            }
            catch (IOException ioe) {
                System.out.println("Couldn't close socket.");
            }
        }
    }
}
