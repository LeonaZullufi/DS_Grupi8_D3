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
            System.out.println("🤝 Inicializing Handshake...");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String clientMessage = in.readLine();

            if ("ClientHello".equals(clientMessage)) {
                System.out.println("📥 Received: ClientHello");

                out.println("ServerHello");
                System.out.println("📤 Sent: ServerHello");

                out.println("ServerKeyExchange");
                System.out.println("🔑 Sent: ServerKeyExchange");

                out.println("ChangeCipherSpec");
                System.out.println("🔐 Sent: ChangeCipherSpec");

                out.println("Finished");
                System.out.println("🔚 Sent: Finished");

                System.out.println("🔒 Secure channel established. Ready for communication...");

                while (true) {
                    String messageFromClient = in.readLine();
                    if (messageFromClient == null || messageFromClient.equalsIgnoreCase("exit")) {
                        System.out.println("❌ Client disconnected.");
                        break;
                    }
                    System.out.println("📥 Client: " + messageFromClient);

                    out.println("Server received: " + messageFromClient);
                    System.out.println("📤Server: Response sent.");
                }
            } else {
                throw new IOException("❌ Unexpected response: " + clientMessage);
            }
        } catch (IOException e) {
            System.out.println("❌ Handshake failed!");
            e.printStackTrace();
        }
    }
}