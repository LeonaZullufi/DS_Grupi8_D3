package managers;

import java.io.FileInputStream;
import java.security.cert.*;
import java.util.*;

public class CertificateManager {

    // Ngarkon një certifikatë X.509 nga file path
    public X509Certificate loadCertificate(String filePath) throws Exception {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return (X509Certificate) factory.generateCertificate(fis);
        }
    }

    // Ngarkon të gjitha certifikatat CA nga një listë file-esh
    public List<X509Certificate> loadTrustedCAs(List<String> caPaths) throws Exception {
        List<X509Certificate> trustedCAs = new ArrayList<>();
        for (String path : caPaths) {
            trustedCAs.add(loadCertificate(path));
        }
        return trustedCAs;
    }

    // Verifikon certifikatën e serverit me një nga certifikatat CA të besuara
    public boolean verifyCertificate(X509Certificate cert, List<X509Certificate> trustedCAs) {
        try {
            for (X509Certificate caCert : trustedCAs) {
                cert.verify(caCert.getPublicKey()); // validon nënshkrimin
                cert.checkValidity();              // validon afatin kohor
                return true;                       // nqs kalon, është valide
            }
        } catch (Exception e) {
            System.out.println("Verifikimi i certifikatës dështoi: " + e.getMessage());
        }
        return false;
    }
}

