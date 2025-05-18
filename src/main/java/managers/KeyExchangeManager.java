package managers;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class KeyExchangeManager {

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private SecretKey sharedSecret;

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
            System.out.println(" 🔐 Diffie-Hellman Key Pair generated.");
        } catch (Exception e) {
            System.out.println(" ❌ Failed to generate DH key pair: " + e.getMessage());
        }
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public void generateSharedSecret(PublicKey receivedPublicKey) {
        try {
            KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(this.privateKey);
            keyAgreement.doPhase(receivedPublicKey, true);
            byte[] secretBytes = keyAgreement.generateSecret();
            this.sharedSecret = new SecretKeySpec(secretBytes, 0, 16, "AES");
            System.out.println(" ✅ Shared AES key generated.");
        } catch (Exception e) {
            System.out.println("❌ Failed to generate shared secret: " + e.getMessage());
        }
    }

    public SecretKey getSharedSecretKey() {
        return this.sharedSecret;
    }

    public void printSharedKey() {
        if (sharedSecret != null) {
            String base64Key = Base64.getEncoder().encodeToString(sharedSecret.getEncoded());
            System.out.println(" 🔐 Shared AES Key (Base64): " + base64Key);
        } else {
            System.out.println(" ⚠️ Shared key is null.");
        }
    }

}
