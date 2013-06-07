package com.sohu.mtc.push.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class SSLConnectionHelper {

	protected static final Log logger = LogFactory.getLog(SSLConnectionHelper.class);
	private String keyStorePass;
	private String appleHost;
	private int applePort;
	private KeyStore keyStore;
	private KeyStore feedbackKeyStore;
	private SSLSocketFactory pushSSLSocketFactory;
	private SSLSocketFactory feedbackSSLSocketFactory;
	private static final String ALGORITHM = "sunx509";
	private static final String PROTOCOL = "TLS";
	public static final String KEYSTORE_TYPE_PKCS12 = "PKCS12";
	public static final String KEYSTORE_TYPE_JKS = "JKS";

	public SSLConnectionHelper(String paramString1, int paramInt, String paramString2, String paramString3,
			String paramString4) throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException {
		logger.debug("Instantiate SSLConnectionHelper with Path to Keystore");
		this.appleHost = paramString1;
		this.applePort = paramInt;
		this.keyStorePass = paramString3;
		this.keyStore = KeyStore.getInstance(paramString4);
		this.keyStore.load(new FileInputStream(paramString2), this.keyStorePass.toCharArray());
	}

	public SSLConnectionHelper(String paramString1, int paramInt, InputStream paramInputStream, String paramString2,
			String paramString3) throws KeyStoreException, CertificateException, NoSuchAlgorithmException,
			NoSuchProviderException, IOException {
		logger.debug("Instantiate SSLConnectionHelper with Keystore as InputStream");
		this.appleHost = paramString1;
		this.applePort = paramInt;
		this.keyStorePass = paramString2;
		this.keyStore = KeyStore.getInstance(paramString3);
		this.keyStore.load(paramInputStream, this.keyStorePass.toCharArray());
	}

	private SSLSocketFactory getPushSSLSocketFactory() throws KeyStoreException, NoSuchProviderException,
			CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException,
			KeyManagementException {
		if (this.pushSSLSocketFactory == null)
			this.pushSSLSocketFactory = createSSLSocketFactoryWithTrustManagers(null);
		logger.debug("Returning Push SSLSocketFactory");
		return this.pushSSLSocketFactory;
	}

	private SSLSocketFactory getFeedbackSSLSocketFactory() throws KeyStoreException, NoSuchProviderException,
			CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException,
			KeyManagementException, Exception {
		if (this.feedbackSSLSocketFactory == null) {
			if (this.feedbackKeyStore == null)
				this.feedbackKeyStore = FetchAppleSSLCertificate.fetch(this.appleHost, this.applePort);
			TrustManagerFactory localTrustManagerFactory = TrustManagerFactory.getInstance("sunx509");
			localTrustManagerFactory.init(this.feedbackKeyStore);
			this.feedbackSSLSocketFactory = createSSLSocketFactoryWithTrustManagers(localTrustManagerFactory
					.getTrustManagers());
		}
		logger.debug("Returning Feedback SSLSocketFactory");
		return this.feedbackSSLSocketFactory;
	}

	private SSLSocketFactory createSSLSocketFactoryWithTrustManagers(TrustManager[] paramArrayOfTrustManager)
			throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException,
			UnrecoverableKeyException, KeyManagementException, KeyStoreException {
		logger.debug("Creating SSLSocketFactory");
		KeyManagerFactory localKeyManagerFactory = KeyManagerFactory.getInstance("sunx509");
		localKeyManagerFactory.init(this.keyStore, this.keyStorePass.toCharArray());
		SSLContext localSSLContext = SSLContext.getInstance("TLS");
		localSSLContext.init(localKeyManagerFactory.getKeyManagers(), paramArrayOfTrustManager, null);
		return localSSLContext.getSocketFactory();
	}

	public SSLSocket getSSLSocket() throws IOException, UnknownHostException, KeyStoreException,
			NoSuchProviderException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException,
			KeyManagementException {
		SSLSocketFactory localSSLSocketFactory = getPushSSLSocketFactory();
		logger.debug("Returning Push SSLSocket");
		return ((SSLSocket) localSSLSocketFactory.createSocket(this.appleHost, this.applePort));
	}

	public SSLSocket getFeedbackSSLSocket() throws Exception, IOException, UnknownHostException, KeyStoreException,
			NoSuchProviderException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException,
			KeyManagementException {
		SSLSocketFactory localSSLSocketFactory = getFeedbackSSLSocketFactory();
		logger.debug("Returning Feedback SSLSocket");
		return ((SSLSocket) localSSLSocketFactory.createSocket(this.appleHost, this.applePort));
	}

	public void fetchAppleCert(String paramString, int paramInt) throws Exception {
		this.feedbackKeyStore = FetchAppleSSLCertificate.fetch(paramString, paramInt);
	}

	public void fetchAppleCert(String paramString1, String paramString2) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		this.feedbackKeyStore = KeyStore.getInstance("JKS");
		this.feedbackKeyStore.load(new FileInputStream(new File(paramString1)), paramString2.toCharArray());
	}

	static {
		Security.addProvider(new BouncyCastleProvider());
	}
}
