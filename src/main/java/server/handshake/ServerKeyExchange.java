package server.handshake;

import java.io.*;
import javax.net.ssl.SSLSocket;

public class ServerKeyExchange {

    private final SSLSocket socket;

    public ServerKeyExchange(SSLSocket socket) {
        this.socket = socket;
    }

    public void sendServerKeyExchange() {
        try {
            System.out.println("🔑 Sending ServerKeyExchange...");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("ServerKeyExchange");
            System.out.println("📤 Sent: ServerKeyExchange");
        } catch (IOException e) {
            System.out.println("❌ Failed to send ServerKeyExchange.");
            e.printStackTrace();
        }
    }
}