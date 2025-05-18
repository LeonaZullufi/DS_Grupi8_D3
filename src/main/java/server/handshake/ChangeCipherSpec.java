package server.handshake;

import java.io.*;
import javax.net.ssl.SSLSocket;

public class ChangeCipherSpec {

    private final SSLSocket socket;

    public ChangeCipherSpec(SSLSocket socket) {
        this.socket = socket;
    }

    public void sendChangeCipherSpec() {
        try {
            System.out.println("🔐 Sending ChangeCipherSpec...");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("ChangeCipherSpec");
            System.out.println("📤 Sent: ChangeCipherSpec");
        } catch (IOException e) {
            System.out.println("❌ Failed to send ChangeCipherSpec.");
            e.printStackTrace();
        }
    }
}
