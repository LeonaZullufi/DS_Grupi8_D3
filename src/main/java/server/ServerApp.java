package server;

import server.handshake.Handshake;

import java.io.*;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import java.security.Security;

public class ServerApp {

    private static final int PORT = 4433;
    private static final String KEYSTORE_LOCATION = "src/main/resources/certificates/server.keystore";
    private static final String KEYSTORE_PASSWORD = "changeit";

    public static void main(String[] args) {
        try {
            System.out.println("🔐 Initializing SSL/TLS Server...");

            Security.setProperty("jdk.tls.server.protocols", "TLSv1.2");

            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(KEYSTORE_LOCATION), KEYSTORE_PASSWORD.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT);

            System.out.println("✅ SSL Server started on port: " + PORT);

            while (true) {
                System.out.println("⌛ Waiting for a client...");
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                System.out.println("🌐 The client connected: " + sslSocket.getInetAddress());


                new Thread(() -> handleClient(sslSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(SSLSocket sslSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
                PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true)
        ) {
            System.out.println("🔒 Secure channel established. Ready for communication...");

            while (true) {
                String clientMessage = in.readLine();
                if (clientMessage == null || clientMessage.equalsIgnoreCase("exit")) {
                    System.out.println("❌ The client was disconnected.");
                    break;
                }

                if (clientMessage.equals("ClientHello")) {
                    System.out.println("📥 The client requested: ClientHello");
                    out.println("ServerHello");
                    System.out.println("📤 The server responded with: ServerHello");
                    continue;
                }

                System.out.println("📥 Client: " + clientMessage);


                String response = "Server received: " + clientMessage;
                out.println(response);
                System.out.println("📤 Server: The response was sent.");
            }
        } catch (IOException e) {
            System.out.println("❌ Error during communication: " + e.getMessage());
        }
    }
}
