package de.zib.gndms.gndmstest;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class SSLConnectionFactory extends SimpleClientHttpRequestFactory {
	
	private SSLContext sslContext;
	
	public SSLConnectionFactory() throws Exception {
		
		Properties p = PropertyFileLoader.loadPropertyFile("web/META-INF/config.conf");
		String keystoreLocation = p.getProperty("keystoreLocation");
	    String keystorePassword = p.getProperty("keystorePassword");
	    String keystorePrivateKeyPassword = p.getProperty("privateKeyPassword");
	    String truststoreLocation = p.getProperty("truststoreLocation");
	    String truststorePassword = p.getProperty("truststorePassword");

	    KeyStore keystore;
	    KeyStore truststore;
	    KeyManagerFactory keymanager;
	    TrustManagerFactory trustmanager;
	    
		keystore = KeyStore.getInstance("JKS");
		keystore.load(new FileInputStream(keystoreLocation), 
				keystorePassword.toCharArray());
		if (truststoreLocation == null) {
			truststoreLocation = keystoreLocation;
			truststorePassword = keystorePassword;
		} else if (truststorePassword == null)
			throw new IllegalArgumentException("Missing truststore password.");
		truststore = KeyStore.getInstance("JKS");
		truststore.load(new FileInputStream(truststoreLocation), 
				truststorePassword.toCharArray());
		sslContext = SSLContext.getInstance("TLS");
		keymanager = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		keymanager.init(keystore, keystorePrivateKeyPassword.toCharArray());
		trustmanager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustmanager.init(truststore);
		sslContext.init(keymanager.getKeyManagers(), 
				trustmanager.getTrustManagers(), new SecureRandom());
	}
	
	@Override
	protected void prepareConnection(HttpURLConnection connection,
			String httpMethod) throws IOException {
		super.prepareConnection(connection, httpMethod);
		 if (connection instanceof HttpsURLConnection) {
			 HttpsURLConnection httpscon = (HttpsURLConnection) connection;
			 httpscon.setSSLSocketFactory(this.sslContext.getSocketFactory());
		 }
	}

}
