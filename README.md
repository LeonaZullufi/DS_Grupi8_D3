# DS_Grupi8_D3

# 🔐 Simulimi i SSL/TLS Handshake me Verifikim të Certifikatave në Java
Ky projekt demonstron një simulim të plotë të procesit të SSL/TLS handshake midis një klienti dhe serveri me verifikim të certifikatave.

---

## 🧰 Kërkesat për ekzekutim

- Java JDK 8 ose më e lartë
- Keytool (për gjenerimin e certifikatave)
- Certifikatat e nevojshme në folderin src/main/resources/certificates/

---

## 🚀 Si të ekzekutohet programi

### Përmes IDE-së:
1. Hap projektin në IDE-në tuaj (Eclipse, IntelliJ, etj.)
2. Së pari ekzekuto ServerApp.java për të nisur serverin.
3. Pastaj ekzekuto ClientApp.java për të nisur klientin
 ```
 Përmes terminalit:
1. Hap terminalin në IntelliJ (alt+f12) sigurohu qe je ne rrenjen e projektit:
 p.sh cd C:\Users\HP\IdeaProjects\DS_Grupi8_D3
 
2. Kompilo të gjitha .java nga të gjitha nënfolderët
javac -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })
Kjo do i kompilojë të gjitha klasat dhe do i ruajë në folderin out.

3. Ekzekuto Server-in
java -cp out server.ServerApp

4. Ekzekuto Client-in (në një dritare të re të terminalit)
java -cp out client.ClientApp

 ```
## 📋 Udhëzime për përdorim
Fillimisht nisni serverin
Pastaj nisni klientin
Ndiqni udhëzimet në ekran për të:
Iniciuar handshake-in
Verifikuar certifikatat
Shkëmbyer mesazhe të sigurta
Shkruani "exit" për të mbyllur lidhjen


🔄 Logjika e rrjedhës
Kur ekzekutoni ServerApp:

image
-🔐 Initializing SSL/TLS Server...
✅ SSL Server i nisur në portin: 4433
⌛ Duke pritur për një klient...

Kur ekzekutoni ClientApp pas ServerApp:
🔐 Initializing SSL/TLS Client...
🌐 U lidh me serverin SSL në  localhost:4433
🔒 Secure channel established. Type 'exit' to disconnect.
📤 Dërgimi i mesazhit: ClientHello
📥 Përgjigje nga serveri: ServerHello
🔄 ServerHello u pranua me sukses!
Shkruaj mesazhin per serverin:



# Përshkrimi i SSL TLS HANDSHAKE - HISTORIA
## shkurt

### 🔐 Procesi i SSL/TLS Handshake:
ClientHello - Klienti nis lidhjen
ServerHello - Serveri përgjigjet me certifikatën e tij
Verifikimi i Certifikatës - Klienti verifikon certifikatën e serverit
Shkëmbimi i Çelësave - Bëhet shkëmbimi i çelësave Diffie-Hellman
Change Cipher Spec - Përcaktohen parametrat e enkriptimit
Finished - Përfundon handshake-i dhe krijohet kanali i sigurt

# Përshkrimi i File-ve:

## CertificateManager.java
Kjo klasë menaxhon ngarkimin dhe verifikimin e certifikatave X.509 për nevojat e simulimit të sigurisë SSL/TLS. Ajo ofron metoda për të lexuar certifikata nga file system (loadCertificate), për të ngarkuar një listë të certifikatave të besuara të CA-ve (loadTrustedCAs), si dhe për të verifikuar vlefshmërinë dhe nënshkrimin e një certifikate (verifyCertificate) kundrejt CA-ve të besuara.

## KeyExchangeManager.java
Kjo klase simulon shkëmbimin e çelësave duke përdorur algoritmin Diffie-Hellman.
Krijon çiftin e çelësave publik/privat dhe gjeneron një çelës të përbashkët AES në bazë të çelësit publik të palës tjetër (generateSharedSecret). Ky çelës përdoret më pas për komunikim të enkriptuar. Klasës gjithashtu ofron metoda për të aksesuar çelësat dhe për të printuar çelësin e përbashkët për qëllime debug.

## HandshakeSimulator.java
Kjo klasë simulon rrjedhën e plotë të një SSL/TLS handshake mes një klienti dhe serveri, duke përfshirë dërgimin e mesazheve ClientHello, ServerHello, ServerKeyExchange, ChangeCipherSpec dhe Finished. Gjatë simulimit, ajo përdor certifikatat e serverit dhe CA-ve, i verifikon ato, dhe simuluon shkëmbimin e çelësave për të krijuar një sesion të sigurt.


## ChangeCipherSpec.java
Kjo klasë përfaqëson fazën e ChangeCipherSpec në protokollin SSL/TLS. Ajo dërgon një mesazh përmes një socket-i SSL për të sinjalizuar se komunikimi i ardhshëm do të bëhet duke përdorur çelësin e negociuar më parë gjatë handshake-it. Kjo është një hap kyç për të aktivizuar enkriptimin e trafikut ndërmjet klientit dhe serverit, përdor një SSLSocket dhe një PrintWriter për të dërguar mesazhin në mënyrë të sigurt.

## Finished.java
Kjo klasë përfaqëson dërgimin e mesazhit Finished, i cili sinjalizon përfundimin e handshake-it SSL/TLS nga ana e serverit. Ajo përdor një SSLSocket për të dërguar një mesazh të thjeshtë tekstual "Finished" përmes rrjetit. Ky veprim konfirmon që serveri ka përfunduar procesin e sigurt të inicializimit të komunikimit me klientin.



## Handshake.java
Kjo klasë është përgjegjëse për simulimin e procesit të handshake-it SSL nga ana e serverit. Pasi pranon mesazhin fillestar ClientHello, ajo i përgjigjet me ServerHello, simulon shkëmbimin e çelësave, dërgon mesazhet ChangeCipherSpec dhe Finished, dhe më pas fillon komunikimin e sigurt me klientin. Ajo përdor një SSLSocket dhe një thread për çdo klient, duke mundësuar trajtim paralel të lidhjeve të shumëfishta.

## ServerHello.java
Kjo klasë përfaqëson mesazhin ServerHello në rrjedhën e handshake-it SSL/TLS. Pas pranimit të ClientHello, serveri përdor këtë klasë për të dërguar një mesazh te klienti për të sinjalizuar se është gati për të vazhduar me negociatat e sigurisë. Komunikimi realizohet përmes SSLSocket dhe PrintWriter.

## ServerKeyExchange.java
Kjo klasë simulon dërgimin e mesazhit "ServerKeyExchange" gjatë procesit të handshake-it SSL/TLS. Në një sistem real, ky mesazh do të përmbante parametrat kriptografikë që klienti do t’i përdorë për të krijuar një çelës të përbashkët. Në këtë implementim të thjeshtësuar, ajo dërgon vetëm një string “ServerKeyExchange” për të treguar që kjo fazë e shkëmbimit të çelësave po ndodh. Ajo përdor një SSLSocket për të realizuar komunikimin dhe një PrintWriter për të dërguar mesazhin te klienti

## ServerApp.java
Kjo është klasa kryesore që simulon një server SSL. Ajo inicializon një SSLServerSocket duke përdorur një keystore dhe një protokoll TLSv1.2. Për çdo klient që lidhet, ajo krijon një thread të veçantë që trajton komunikimin me të. Gjatë lidhjes, ajo kryen simulimin e handshake-it dhe më pas lejon komunikim të sigurt përmes socket-it SSL.

## ClientApp.java
Kjo është klasa kryesore që simulon një klient SSL. Ajo krijon një lidhje të sigurt me serverin përmes SSLSocket, duke përdorur një TrustStore që përmban certifikatën e serverit. Pas kryerjes së handshake-it me mesazhet ClientHello dhe pritjes së ServerHello, përdoruesi mund të shkëmbejë mesazhe me serverin në mënyrë të sigurt. Lidhja mbyllet nëse dërgohet komanda “exit”



