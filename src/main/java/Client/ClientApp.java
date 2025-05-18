package Client;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.util.Scanner;

public class ClientApp {
    private static final String TRUSTSTORE_LOCATION = "src/main/resources/certificates/client.truststore";
    private static final String TRUSTSTORE_PASSWORD = "changeit";
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4433;

    public static void main(String[] args) {
        try {
            System.out.println("🔐 Initializing SSL/TLS Client... ");

            System.setProperty("jdk.tls.client.protocols", "TLSv1.2");

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(TRUSTSTORE_LOCATION), TRUSTSTORE_PASSWORD.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(SERVER_HOST, SERVER_PORT);

            sslSocket.startHandshake();

            System.out.println("🌐 Connected to SSL server at  " + SERVER_HOST + ":" + SERVER_PORT);

            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
                    Scanner scanner = new Scanner(System.in)
            ) {
                System.out.println("🔒 Secure channel established. Type 'exit' to disconnect.");

                System.out.println("📤Sending the message: ClientHello");
                out.println("ClientHello");

                String serverResponse = in.readLine();
                System.out.println("📥 Server response: " + serverResponse);

                if ("ServerHello".equals(serverResponse)) {
                    System.out.println("🔄 ServerHello was successfully received!");

                    // ✅ Fillimi i komunikimit të sigurt
                    while (true) {
                        System.out.print("📝 Write the message for the server: ");
                        String message = scanner.nextLine().trim();

                        if (message.isEmpty()) {
                            System.out.println("⚠️The message cannot be empty. Please try again.");
                            continue;
                        }

                        out.println(message);

                        if ("exit".equalsIgnoreCase(message)) {
                            System.out.println("❌ Connection closed.");
                            sslSocket.close();
                            break;
                        }

                        // ✅ Lexo përgjigjen nga serveri
                        String serverReply = in.readLine();
                        if (serverReply == null) {
                            System.out.println("❌ Server disconnected.");
                            break;
                        }
                        System.out.println("📥 The server replied: " + serverReply);
                    }
                } else {
                    System.out.println("❌ Expected ServerHello but received: " + serverResponse);
                }
            }

        } catch (SSLHandshakeException e) {
            System.out.println("❌ SSL Handshake Failure: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ SSL Client Error: " + e.getMessage());
        }
    }
}

