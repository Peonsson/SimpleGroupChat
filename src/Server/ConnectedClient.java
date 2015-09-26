package Server;

import java.net.Socket;

/**
 * Created by Peonsson on 2015-09-26.
 */
public class ConnectedClient {
    private String nickname;
    private Socket clientSocket;

    public ConnectedClient(String nickname, Socket clientSocket) {
        this.nickname = nickname;
        this.clientSocket = clientSocket;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
