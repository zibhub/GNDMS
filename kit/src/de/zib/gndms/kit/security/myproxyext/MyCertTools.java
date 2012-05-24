package de.awi.grid.c3.myproxy;

import net.sourceforge.jcetaglib.tools.Hex;
import net.sourceforge.jcetaglib.tools.KeyTools;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERInputStream;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.util.encoders.Base64;

import java.io.*;
import java.security.*;
import java.security.cert.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.StringTokenizer;

/**
 * Tools to handle common certificate operations.
 *
 * @version $Id: CertTools.java,v 1.4 2004/04/15 07:28:25 hamgert Exp $
 */
public class MyCertTools {

    //private static Category cat = Category.getInstance(CertTools.class.getName());

    /** Creates new CertTools */
    public MyCertTools() {
    }


    /**
     * Gets a specified part of a DN.
     *
     * @param dn String containing DN, The DN string has the format "C=SE, O=xx, OU=yy, CN=zz".
     * @param dnpart String specifying which part of the DN to get, should be "CN" or "OU" etc.
     * @return String containing dnpart or null if dnpart is not present
     */
    public static String getPartFromDN(String dn, String dnpart) {
        String trimmeddn = dn.trim();
        String part = null, o = null;
        StringTokenizer st = new StringTokenizer(trimmeddn, ",=");
        while (st.hasMoreTokens()) {
            o = st.nextToken();
            if (o.trim().equalsIgnoreCase(dnpart)) {
                part = st.nextToken();
            }
        }
        return part;
    } //getCNFromDN

    public static PrivateKey getPrivatefromPEM(String keyFile, String keypwd)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, Exception {
        InputStream inStrm = new FileInputStream(keyFile);
        PrivateKey privKey = getPrivatefromPEM(inStrm, keypwd);
        return privKey;
    }

    public static PrivateKey getPrivatefromPEM(InputStream keystream, String keypwd)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, Exception {

        String beginKey;
        String endKey;

        if (keypwd == null || keypwd == "") {
            beginKey = "-----BEGIN RSA PRIVATE KEY-----";
            endKey = "-----END RSA PRIVATE KEY-----";
        } else {
            beginKey = "-----BEGIN ENCRYPTED PRIVATE KEY-----";
            endKey = "-----END ENCRYPTED PRIVATE KEY-----";
        }

        BufferedReader bufRdr = new BufferedReader(new InputStreamReader(keystream));
        ByteArrayOutputStream ostr = new ByteArrayOutputStream();
        PrintStream opstr = new PrintStream(ostr);
        String temp;
        while ((temp = bufRdr.readLine()) != null &&
                !temp.equals(beginKey))
            continue;
        if (temp == null)
            throw new IOException("Error in " + keystream.toString() + ", missing " + beginKey + " boundary");
        while ((temp = bufRdr.readLine()) != null &&
                !temp.equals(endKey))
            opstr.print(temp);
        if (temp == null)
            throw new IOException("Error in " + keystream.toString() + ", missing " + endKey + " boundary");

        opstr.close();

        byte[] keybuf = Base64.decode(ostr.toByteArray());

        PrivateKey privKey = null;

        // get the private key - private keys are encoded in PKCS#8 format:
        PKCS8EncodedKeySpec prvSpec = new PKCS8EncodedKeySpec(keybuf);

        if (keypwd == null || keypwd == "") {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privKey = kf.generatePrivate(prvSpec);
        } else {
            privKey = KeyTools.decryptPrivateKey(keybuf, keypwd);
        }

        return privKey;
    }


    /**
     * Reads a certificate in PEM-format from a file. The file may contain other things,
     * the first certificate in the file is read.
     *
     * @param certFile the file containing the certificate in PEM-format
     * @return X509Certificate
     * @exception IOException if the filen cannot be read.
     * @exception CertificateException if the filen does not contain a correct certificate.
     */
    public static X509Certificate getCertfromPEM(String certFile) throws IOException, CertificateException {
        InputStream inStrm = new FileInputStream(certFile);
        X509Certificate cert = getCertfromPEM(inStrm);
        return cert;
    }

    /**
     * Reads a certificate in PEM-format from an InputStream. The stream may contain other things,
     * the first certificate in the stream is read.
     *
     * @param certstream the input stream containing the certificate in PEM-format
     * @return X509Certificate
     * @exception IOException if the stream cannot be read.
     * @exception CertificateException if the stream does not contain a correct certificate.
     */
    public static X509Certificate getCertfromPEM(InputStream certstream)
            throws IOException, CertificateException {

        String beginKey = "-----BEGIN CERTIFICATE-----";
        String endKey = "-----END CERTIFICATE-----";
        BufferedReader bufRdr = new BufferedReader(new InputStreamReader(certstream));
        ByteArrayOutputStream ostr = new ByteArrayOutputStream();
        PrintStream opstr = new PrintStream(ostr);
        String temp;
        while ((temp = bufRdr.readLine()) != null &&
                !temp.equals(beginKey))
            continue;
        if (temp == null)
            throw new IOException("Error in " + certstream.toString() + ", missing " + beginKey + " boundary");
        while ((temp = bufRdr.readLine()) != null &&
                !temp.equals(endKey))
            opstr.print(temp);
        if (temp == null)
            throw new IOException("Error in " + certstream.toString() + ", missing " + endKey + " boundary");
        opstr.close();

        byte[] certbuf = Base64.decode(ostr.toByteArray());

        // Phweeew, were done, now decode the cert from file back to X509Certificate object
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate x509cert = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certbuf));

        return x509cert;
    } // getCertfromPEM

    /**
     * Creates X509CRL from byte[].
     *
     * @param crl byte array containing CRL in DER-format
     * @return X509CRL
     * @exception CertificateException if the byte arrayen does not contani a correct CRL.
     * @exception CRLException if the byte arrayen does not contani a correct CRL.
     */
    public static X509CRL getCRLfromByteArray(byte[] crl)
            throws CertificateException, CRLException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509CRL x509crl = (X509CRL) cf.generateCRL(new ByteArrayInputStream(crl));
        return x509crl;
    } // getCRLfromByteArray

    /**
     * Checks if a certificate is self signed by verifying if subject and issuer are the same.
     *
     * @param cert the certificate that skall be checked.
     * @return boolean true if the certificate has the same issuer and subject, false otherwise.
     */
    public static boolean isSelfSigned(X509Certificate cert) {
        boolean ret = cert.getSubjectDN().equals(cert.getIssuerDN());
        return ret;
    } // isSelfSigned

    //
    // create the authority key identifier.
    //
    public static SubjectKeyIdentifier createSubjectKeyId(PublicKey pubKey) {
        try {
            ByteArrayInputStream bIn = new ByteArrayInputStream(pubKey.getEncoded());
            SubjectPublicKeyInfo info = new SubjectPublicKeyInfo((ASN1Sequence) new DERInputStream(bIn).readObject());

            return new SubjectKeyIdentifier(info);
        } catch (Exception e) {
            throw new RuntimeException("error creating key");
        }
    }

    public static AuthorityKeyIdentifier createAuthorityKeyId(PublicKey pubKey) {
        try {
            ByteArrayInputStream bIn = new ByteArrayInputStream(pubKey.getEncoded());
            SubjectPublicKeyInfo info = new SubjectPublicKeyInfo((ASN1Sequence) new DERInputStream(bIn).readObject());

            return new AuthorityKeyIdentifier(info);
        } catch (Exception e) {
            throw new RuntimeException("error creating AuthorityKeyId");
        }
    }


    /**
     * Generate SHA1 fingerprint of certificate in string representation.
     *
     * @param cert X509Certificate.
     * @return String containing hex format of SHA1 fingerprint.
     **/
    public static String getFingerprintAsString(X509Certificate cert) {

        try {
            byte[] res = generateSHA1Fingerprint(cert.getEncoded());
            return Hex.encode(res);
        } catch (CertificateEncodingException cee) {
            System.out.println("Error encoding X509 certificate." + cee);
        }
        return null;
    }

    /**
     * Generate a SHA1 fingerprint from a byte array containing a X.509 certificate
     *
     * @param ba Byte array containing DER encoded X509Certificate.
     * @return Byte array containing SHA1 hash of DER encoded certificate.
     */
    public static byte[] generateSHA1Fingerprint(byte[] ba) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return md.digest(ba);
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println("SHA1 algorithm not supported" + nsae);
        }
        return null;
    } // generateSHA1Fingerprint

} // CertTools
