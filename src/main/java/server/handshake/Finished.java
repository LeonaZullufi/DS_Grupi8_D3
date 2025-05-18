package server.handshake;

import java.io.*;
import javax.net.ssl.SSLSocket;

public class Finished {

    private final SSLSocket socket;

    public Finished(SSLSocket socket) {
        this.socket = socket;
    }

    public void sendFinished() {
        try {
            System.out.println("🔚 Sending Finished...");

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Finished");
            System.out.println("📤 Sent: Finished");
        } catch (IOException e) {
            System.out.println("❌ Failed to send Finished.");
            e.printStackTrace();
        }
    }
}
