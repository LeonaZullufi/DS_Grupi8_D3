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

            // ✅ Konfigurimi i protokollit TLS
            System.setProperty("jdk.tls.client.protocols", "TLSv1.2");

            // Ngarkimi i TrustStore për të pranuar certifikatën e serverit
            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(TRUSTSTORE_LOCATION), TRUSTSTORE_PASSWORD.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            // Krijimi i SSLSocket për lidhje të sigurt
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(SERVER_HOST, SERVER_PORT);

            // ✅ Startimi i Handshake
            sslSocket.startHandshake();

            System.out.println("🌐 U lidh me serverin SSL në  " + SERVER_HOST + ":" + SERVER_PORT);

            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
                    Scanner scanner = new Scanner(System.in)
            ) {
                System.out.println("🔒 Secure channel established. Type 'exit' to disconnect.");

                // ✅ 1️⃣ Dërgo ClientHello
                System.out.println("📤 Dërgimi i mesazhit: ClientHello");
                out.println("ClientHello");

                // ✅ 2️⃣ Leximi i ServerHello
                String serverResponse = in.readLine();
                System.out.println("📥 Përgjigje nga serveri: " + serverResponse);

                if ("ServerHello".equals(serverResponse)) {
                    System.out.println("🔄 ServerHello u pranua me sukses!");

                    // ✅ Fillimi i komunikimit të sigurt
                    while (true) {
                        System.out.print("📝 Shkruaj mesazhin për serverin: ");
                        String message = scanner.nextLine().trim();

                        // ✅ Kontroll nëse është bosh
                        if (message.isEmpty()) {
                            System.out.println("⚠️ Mesazhi nuk mund të jetë bosh. Provo përsëri.");
                            continue;
                        }

                        // ✅ Dërgo mesazhin te serveri
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
                        System.out.println("📥 Serveri u përgjigj: " + serverReply);
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

