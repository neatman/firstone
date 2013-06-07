package com.sohu.mtc.push.ssl;

import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FetchAppleSSLCertificate {

	protected static final Log logger = LogFactory.getLog(FetchAppleSSLCertificate.class);
	private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

	public static KeyStore fetch(String paramString, int paramInt) throws Exception {
		KeyStore localKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		localKeyStore.load(null);
		SSLContext localSSLContext = SSLContext.getInstance("TLS");
		TrustManagerFactory localTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
				.getDefaultAlgorithm());
		localTrustManagerFactory.init(localKeyStore);
		X509TrustManager localX509TrustManager = (X509TrustManager) localTrustManagerFactory.getTrustManagers()[0];
		SavingTrustManager localSavingTrustManager = new SavingTrustManager(localX509TrustManager);
		localSSLContext.init(null, new TrustManager[] { localSavingTrustManager }, null);
		SSLSocketFactory localSSLSocketFactory = localSSLContext.getSocketFactory();
		logger.debug(new StringBuilder().append("Opening connection to ").append(paramString).append(":")
				.append(paramInt).append("...").toString());
		SSLSocket localSSLSocket = (SSLSocket) localSSLSocketFactory.createSocket(paramString, paramInt);
		localSSLSocket.setSoTimeout(10000);
		try {
			logger.debug("Starting SSL handshake...");
			localSSLSocket.startHandshake();
			localSSLSocket.close();
			logger.debug("No errors, certificate is already trusted");
		} catch (SSLException localSSLException) {
			logger.debug("Known error occurs here");
		}
		X509Certificate[] arrayOfX509Certificate = localSavingTrustManager.chain;
		if (arrayOfX509Certificate == null)
			throw new Exception("Could not obtain server certificate chain!");
		logger.debug(new StringBuilder().append("Server sent ").append(arrayOfX509Certificate.length)
				.append(" certificate(s):").toString());
		MessageDigest localMessageDigest1 = MessageDigest.getInstance("SHA1");
		MessageDigest localMessageDigest2 = MessageDigest.getInstance("MD5");
		for (int i = 0; i < arrayOfX509Certificate.length; ++i) {
			X509Certificate localX509Certificate = arrayOfX509Certificate[i];
			logger.debug(new StringBuilder().append(" ").append(i + 1).append(" Subject ")
					.append(localX509Certificate.getSubjectDN()).toString());
			logger.debug(new StringBuilder().append("   Issuer  ").append(localX509Certificate.getIssuerDN())
					.toString());
			localMessageDigest1.update(localX509Certificate.getEncoded());
			logger.debug(new StringBuilder().append("   sha1    ").append(toHexString(localMessageDigest1.digest()))
					.toString());
			localMessageDigest2.update(localX509Certificate.getEncoded());
			logger.debug(new StringBuilder().append("   md5     ").append(toHexString(localMessageDigest2.digest()))
					.toString());
		}
		int i = 0;
		X509Certificate localX509Certificate = arrayOfX509Certificate[i];
		String str = new StringBuilder().append(paramString).append("-").append(i + 1).toString();
		localKeyStore.setCertificateEntry(str, localX509Certificate);
		return localKeyStore;
	}

	private static String toHexString(byte[] paramArrayOfByte) {
		StringBuilder localStringBuilder = new StringBuilder(paramArrayOfByte.length * 3);
		for (int k : paramArrayOfByte) {
			k &= 255;
			localStringBuilder.append(HEXDIGITS[(k >> 4)]);
			localStringBuilder.append(HEXDIGITS[(k & 0xF)]);
			localStringBuilder.append(' ');
		}
		return localStringBuilder.toString();
	}

	private static class SavingTrustManager implements X509TrustManager {
		private final X509TrustManager tm;
		private X509Certificate[] chain;

		SavingTrustManager(X509TrustManager paramX509TrustManager) {
			this.tm = paramX509TrustManager;
		}

		public X509Certificate[] getAcceptedIssuers() {
			throw new UnsupportedOperationException();
		}

		public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
				throws CertificateException {
			throw new UnsupportedOperationException();
		}

		public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
				throws CertificateException {
			this.chain = paramArrayOfX509Certificate;
			this.tm.checkServerTrusted(paramArrayOfX509Certificate, paramString);
		}
	}
}
