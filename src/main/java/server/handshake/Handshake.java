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

            // 1️⃣ Lexo ClientHello nga klienti
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String clientMessage = in.readLine();

            if ("ClientHello".equals(clientMessage)) {
                System.out.println("📥 Received: ClientHello");

                // 2️⃣ Dërgo ServerHello
                out.println("ServerHello");
                System.out.println("📤 Sent: ServerHello");

                // 3️⃣ Dërgojmë ServerKeyExchange
                out.println("ServerKeyExchange");
                System.out.println("🔑 Sent: ServerKeyExchange");

                // 4️⃣ Dërgojmë ChangeCipherSpec
                out.println("ChangeCipherSpec");
                System.out.println("🔐 Sent: ChangeCipherSpec");

                // 5️⃣ Dërgojmë Finished
                out.println("Finished");
                System.out.println("🔚 Sent: Finished");

                // 6️⃣ Fillojmë komunikimin e sigurt
                System.out.println("🔒 Secure channel established. Ready for communication...");

                // Loop për të pranuar mesazhe
                while (true) {
                    String messageFromClient = in.readLine();
                    if (messageFromClient == null || messageFromClient.equalsIgnoreCase("exit")) {
                        System.out.println("❌ Client disconnected.");
                        break;
                    }
                    System.out.println("📥 Klienti: " + messageFromClient);

                    // Dërgojmë një përgjigje të thjeshtë
                    out.println("Server received: " + messageFromClient);
                    System.out.println("📤 Server: U dërgua përgjigja.");
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