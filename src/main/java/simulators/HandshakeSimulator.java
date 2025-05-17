package simulators;

import managers.CertificateManager;
import java.net.Socket;
import java.security.cert.X509Certificate;
import java.util.List;

public class HandshakeSimulator {

    public void simulateHandshake(Socket socket, CertificateManager certManager,
                                  String serverCertPath, List<String> caCertPaths) {
        try {
            System.out.println("🔐 Starting SSL/TLS Handshake Simulation...");

            clientHello();
            serverHello();

            System.out.println("📤 Loading server certificate...");
            X509Certificate serverCert = certManager.loadCertificate(serverCertPath);

            System.out.println("📥 Loading trusted CA certificates...");
            List<X509Certificate> trustedCAs = certManager.loadTrustedCAs(caCertPaths);

            System.out.println("✅ Verifying server certificate...");
            boolean valid = certManager.verifyCertificate(serverCert, trustedCAs);

            if (!valid) {
                System.out.println("❌ Handshake failed: Invalid certificate.");
                return;
            }

            System.out.println("🔑 Performing key exchange...");
            keyExchange();

            System.out.println("🔄 Sending ChangeCipherSpec...");
            changeCipherSpec();

            System.out.println("✅ Sending Finished message...");
            finished();

            System.out.println("🎉 Handshake completed successfully!");

        } catch (Exception e) {
            System.out.println("❗ Error during handshake: " + e.getMessage());
        }
    }

    private void clientHello() {
        System.out.println("🧑‍💻 Client → Hello");
    }

    private void serverHello() {
        System.out.println("🖥️ Server → Hello");
    }

    private void keyExchange() {
        System.out.println("🔐 Key exchange simulated.");
    }

    private void changeCipherSpec() {
        System.out.println("🔄 ChangeCipherSpec sent.");
    }

    private void finished() {
        System.out.println("🏁 Finished message sent.");
    }
}

