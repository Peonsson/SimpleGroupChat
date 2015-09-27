package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Peonsson and roppe546 on 2015-09-26.
 */
public class ClientMessageReceiver extends Thread {

    private BufferedReader in = null;
    private Socket server = null;
    public ClientMessageReceiver(Socket server) {
        this.server = server;
        try {
            in = new BufferedReader(new InputStreamReader(server.getInputStream()));
        }
        catch (IOException ioe) {
            System.out.println("Input stream doesn't exist.");
        }
    }

    public void run() {

        String receiveText;
        try {
            while (true) {
                receiveText = in.readLine();
                System.out.println(receiveText);
            }
        }
        catch (IOException ioe) {
            System.out.println("Disconnected.");
            try {
                server.close();
            }catch(IOException e ) {
                System.out.println(e.getMessage());
            }
        }
    }
}
