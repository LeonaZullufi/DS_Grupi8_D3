package server.handshake;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import javax.net.ssl.SSLSocket;

public class ServerKeyExchange {

    private final SSLSocket socket;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public ServerKeyExchange(SSLSocket socket) {
        this.socket = socket;
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(2048, random);
            KeyPair pair = keyGen.generateKeyPair();
            this.publicKey = pair.getPublic();
            this.privateKey = pair.getPrivate();
            System.out.println("🔐 Server Key Pair generated successfully.");
        } catch (Exception e) {
            System.out.println("❌ Failed to generate Server Key Pair.");
            e.printStackTrace();
        }
    }

    public void sendPublicKey() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(publicKey.toString());
            System.out.println("📤 Sent: Public Key");
        } catch (IOException e) {
            System.out.println("❌ Failed to send Public Key.");
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }
}