package managers;

import java.io.FileInputStream;
import java.security.cert.*;
import java.util.*;

public class CertificateManager {

    public X509Certificate loadCertificate(String filePath) throws Exception {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return (X509Certificate) factory.generateCertificate(fis);
        }
    }

    public List<X509Certificate> loadTrustedCAs(List<String> caPaths) throws Exception {
        List<X509Certificate> trustedCAs = new ArrayList<>();
        for (String path : caPaths) {
            trustedCAs.add(loadCertificate(path));
        }
        return trustedCAs;
    }

    public boolean verifyCertificate(X509Certificate cert, List<X509Certificate> trustedCAs) {
        try {
            for (X509Certificate caCert : trustedCAs) {
                cert.verify(caCert.getPublicKey());
                cert.checkValidity();
                return true;
            }
        } catch (Exception e) {
            System.out.println("Certificate verification failed: " + e.getMessage());
        }
        return false;
    }
}

