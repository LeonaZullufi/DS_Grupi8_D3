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

            // ✅ Vendosja e protokollit TLSv1.2
            Security.setProperty("jdk.tls.server.protocols", "TLSv1.2");

            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(KEYSTORE_LOCATION), KEYSTORE_PASSWORD.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT);

            System.out.println("✅ SSL Server i nisur në portin: " + PORT);

            while (true) {
                System.out.println("⌛ Duke pritur për një klient...");
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                System.out.println("🌐 Klienti u lidh: " + sslSocket.getInetAddress());

                // ✅ Krijimi i Thread për çdo klient
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
                    System.out.println("❌ Klienti u shkëput.");
                    break;
                }

                // ✅ Kontrollojmë nëse është "ClientHello"
                if (clientMessage.equals("ClientHello")) {
                    System.out.println("📥 Klienti kërkoi: ClientHello");
                    out.println("ServerHello");
                    System.out.println("📤 Serveri u përgjigj me: ServerHello");
                    continue;
                }

                // ✅ Shfaqja e mesazhit
                System.out.println("📥 Klienti: " + clientMessage);

                // ✅ Përgjigje
                String response = "Server received: " + clientMessage;
                out.println(response);
                System.out.println("📤 Server: U dërgua përgjigja.");
            }
        } catch (IOException e) {
            System.out.println("❌ Error during communication: " + e.getMessage());
        }
    }
}
