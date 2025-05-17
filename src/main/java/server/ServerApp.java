package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import server.handshake.Handshake;

public class ServerApp {

    private static final int PORT = 8443;
    private static final String KEYSTORE_LOCATION = "src/main/resources/certificates/server.keystore";
    private static final String KEYSTORE_PASSWORD = "changeit";

    public static void main(String[] args) {
        try {
            System.out.println("🔐 Initializing SSL/TLS Server...");

            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(KEYSTORE_LOCATION), KEYSTORE_PASSWORD.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

            SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT);

            System.out.println("✅ SSL Server i nisur në portin: " + PORT);

            while (true) {
                System.out.println("⌛ Duke pritur për një klient...");
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                System.out.println("🌐 Klienti u lidh: " + sslSocket.getInetAddress());

                Handshake handshake = new Handshake(sslSocket);
                handshake.performHandshake();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}