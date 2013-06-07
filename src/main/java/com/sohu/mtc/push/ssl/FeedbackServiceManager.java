package com.sohu.mtc.push.ssl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;

import javax.net.ssl.SSLSocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sohu.mtc.push.ssl.data.Device;

public class FeedbackServiceManager {

	protected static final Log logger = LogFactory.getLog(FeedbackServiceManager.class);
	private static FeedbackServiceManager instance;
	private static final Object synclock = new Object();
	private static final int FEEDBACK_TUPLE_SIZE = 38;

	public static FeedbackServiceManager getInstance() {
		synchronized (synclock) {
			if (instance == null)
				instance = new FeedbackServiceManager();
		}
		logger.debug("Get FeedbackServiceManager Instance");
		return instance;
	}

	public LinkedList<Device> getDevices(String paramString1, int paramInt, String paramString2, String paramString3,
			String paramString4) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException,
			NoSuchProviderException, Exception {
		logger.debug("Retrieving Devices from Host: [" + paramString1 + "] Port: [" + paramInt
				+ "] with KeyStorePath [" + paramString2 + "]/[" + paramString4 + "]");
		SSLConnectionHelper localSSLConnectionHelper = new SSLConnectionHelper(paramString1, paramInt, paramString2,
				paramString3, paramString4);
		SSLSocket localSSLSocket = localSSLConnectionHelper.getFeedbackSSLSocket();
		return getDevices(localSSLSocket);
	}

	public LinkedList<Device> getDevices(String paramString1, int paramInt, InputStream paramInputStream,
			String paramString2, String paramString3) throws UnrecoverableKeyException, KeyManagementException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException,
			NoSuchProviderException, Exception {
		logger.debug("Retrieving Devices from Host: [" + paramString1 + "] Port: [" + paramInt
				+ "] with KeyStoreStream/[" + paramString3 + "]");
		SSLConnectionHelper localSSLConnectionHelper = new SSLConnectionHelper(paramString1, paramInt,
				paramInputStream, paramString2, paramString3);
		SSLSocket localSSLSocket = localSSLConnectionHelper.getFeedbackSSLSocket();
		return getDevices(localSSLSocket);
	}

	private LinkedList<Device> getDevices(SSLSocket paramSSLSocket) throws IOException {
		InputStream localInputStream = paramSSLSocket.getInputStream();
		byte[] arrayOfByte1 = new byte[1024];
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		int i = 0;
		while ((i = localInputStream.read(arrayOfByte1, 0, 1024)) != -1)
			localByteArrayOutputStream.write(arrayOfByte1, 0, i);
		LinkedList localLinkedList = new LinkedList();
		byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
		int j = arrayOfByte2.length / 38;
		logger.debug("Found: [" + j + "]");
		for (int k = 0; k < j; ++k) {
			int l = k * 38;
			int i1 = 0;
			int i2 = 0;
			int i3 = 0;
			int i4 = 0;
			int i5 = 0;
			long l1 = 0L;
			i2 = 0xFF & arrayOfByte2[l];
			i3 = 0xFF & arrayOfByte2[(l + 1)];
			i4 = 0xFF & arrayOfByte2[(l + 2)];
			i5 = 0xFF & arrayOfByte2[(l + 3)];
			i1 += 4;
			l1 = (i2 << 24 | i3 << 16 | i4 << 8 | i5) & 0xFFFFFFFF;
			int i6 = arrayOfByte2[(l + 4)] << 8 | arrayOfByte2[(l + 5)];
			String str = "";
			int i7 = 0;
			for (int i8 = 0; i8 < 32; ++i8) {
				i7 = 0xFF & arrayOfByte2[(l + 6 + i8)];
				str = str.concat(String.format("%02x", new Object[] { Integer.valueOf(i7) }));
			}
			Device localDevice = new Device(null, str, new Timestamp(l1 * 1000L));
			localLinkedList.add(localDevice);
			logger.info("FeedbackManager retrieves one device :  " + new Date(l1 * 1000L) + ";" + i6 + ";" + str + ".");
		}
		paramSSLSocket.close();
		return localLinkedList;
	}
}
