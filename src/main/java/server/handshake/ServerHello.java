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
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("ServerHello");
            System.out.println("📤 Sent: ServerHello");
        } catch (IOException e) {
            System.out.println("❌ Failed to send ServerHello.");
            e.printStackTrace();
        }
    }

    public void sendChangeCipherSpec() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("ChangeCipherSpec");
            System.out.println("📤 Sent: ChangeCipherSpec");
        } catch (IOException e) {
            System.out.println("❌ Failed to send ChangeCipherSpec.");
            e.printStackTrace();
        }
    }
}