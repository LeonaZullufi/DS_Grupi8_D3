package server.handshake;

import java.io.*;
import javax.net.ssl.SSLSocket;

public class ServerHello {

    private final SSLSocket socket;

    public ServerHello(SSLSocket socket) {
        this.socket = socket;
    }

    public void sendServerHello() {
        try {
            System.out.println("🤝 Sending ServerHello...");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("ServerHello");
            System.out.println("📤 Sent: ServerHello");
        } catch (IOException e) {
            System.out.println("❌ Failed to send ServerHello.");
            e.printStackTrace();
        }
    }
}