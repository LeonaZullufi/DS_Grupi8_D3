package Client;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class ClientApp {
    private static final String TRUSTSTORE_LOCATION = "src/main/resources/certificates/client.truststore";
    private static final String TRUSTSTORE_PASSWORD = "changeit";
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8443;

    public static void main(String[] args) {
        try {
            System.out.println("🔐 Initializing SSL/TLS Client...");

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(TRUSTSTORE_LOCATION), TRUSTSTORE_PASSWORD.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(SERVER_HOST, SERVER_PORT);

            System.out.println("🌐 U lidh me serverin SSL në " + SERVER_HOST + ":" + SERVER_PORT);

            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true)
            ) {
                String message = "Përshëndetje nga klienti!";
                System.out.println("📤 Dërgimi i mesazhit: " + message);
                out.println(message);

                String response = in.readLine();
                System.out.println("📥 Përgjigje nga serveri: " + response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
