package server.handshake;

import java.io.*;
import javax.net.ssl.SSLSocket;

public class Handshake {

    private final SSLSocket socket;

    public Handshake(SSLSocket socket) {
        this.socket = socket;
    }

    public void performHandshake() {
        try {
            System.out.println("🤝 Initializing Handshake...");

            sendClientHello();
            receiveServerHello();
            sendClientKeyExchange();
            receiveChangeCipherSpec();
            confirmHandshake();

            System.out.println("✅ Handshake completed successfully!");

        } catch (IOException e) {
            System.out.println("❌ Handshake failed!");
            e.printStackTrace();
        }
    }

    private void sendClientHello() throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("ClientHello");
        System.out.println("📤 Sent: ClientHello");
    }

    private void receiveServerHello() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = in.readLine();
        System.out.println("📥 Received: " + response);
    }

    private void sendClientKeyExchange() throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("ClientKeyExchange");
        System.out.println("📤 Sent: ClientKeyExchange");
    }

    private void receiveChangeCipherSpec() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = in.readLine();
        System.out.println("📥 Received: " + response);
    }

    private void confirmHandshake() throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("Finished");
        System.out.println("📤 Sent: Finished");
    }
}