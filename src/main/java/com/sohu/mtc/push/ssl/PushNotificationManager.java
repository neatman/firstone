package com.sohu.mtc.push.ssl;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLSocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sohu.mtc.push.ssl.data.Device;
import com.sohu.mtc.push.ssl.data.PayLoad;
import com.sohu.mtc.push.ssl.exception.DuplicateDeviceException;
import com.sohu.mtc.push.ssl.exception.NullDeviceTokenException;
import com.sohu.mtc.push.ssl.exception.NullIdException;
import com.sohu.mtc.push.ssl.exception.UnknownDeviceException;

public class PushNotificationManager {
	protected static final Log logger = LogFactory.getLog(PushNotificationManager.class);
	public static final int DEFAULT_RETRIES = 3;
	private static PushNotificationManager instance;
	private static final Object synclock = new Object();
	private static SSLConnectionHelper connectionHelper;
	private static SSLSocket socket;
	private int retryAttempts = 3;

	public static PushNotificationManager getInstance() {
		synchronized (synclock) {
			if (instance == null)
				instance = new PushNotificationManager();
		}
		return instance;
	}

	public void initializeConnection(String paramString1, int paramInt, String paramString2, String paramString3,
			String paramString4) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, NoSuchProviderException {
		logger.debug("Initializing Connection to Host: [" + paramString1 + "] Port: [" + paramInt
				+ "] with KeyStorePath [" + paramString2 + "]/[" + paramString4 + "]");
		this.connectionHelper = new SSLConnectionHelper(paramString1, paramInt, paramString2, paramString3,
				paramString4);
		this.socket = this.connectionHelper.getSSLSocket();
		socket.setEnableSessionCreation(true);
//		socket.setSendBufferSize(512);
//		logger.debug("<<<<<<timeout>>>>>>" + this.socket.getSoTimeout());
		// socket.setSoTimeout(1000);
	}

	public void initializeConnection(String paramString1, int paramInt, InputStream paramInputStream,
			String paramString2, String paramString3) throws UnrecoverableKeyException, KeyManagementException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException,
			NoSuchProviderException {
		logger.debug("Initializing Connection to Host: [" + paramString1 + "] Port: [" + paramInt
				+ "] with KeyStoreStream/[" + paramString3 + "]");
		this.connectionHelper = new SSLConnectionHelper(paramString1, paramInt, paramInputStream, paramString2,
				paramString3);
		this.socket = this.connectionHelper.getSSLSocket();
	}

	public void stopConnection() throws IOException {
		logger.debug("Closing connection");
		this.socket.close();
	}

	public void sendNotification(Device paramDevice, PayLoad paramPayLoad) throws UnrecoverableKeyException,
			KeyManagementException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException, Exception {
		byte[] arrayOfByte = getMessage(paramDevice.getToken(), paramPayLoad);
		int i = 0;
		while (i == 0)
			try {
				logger.debug("Attempting to send Notification [" + paramPayLoad.toString() + "]");
				OutputStream output = this.socket.getOutputStream();
				output.write(arrayOfByte);
//				output.flush();
				// this.socket.set
				i = 1;
				logger.debug("Notification sent");
			} catch (IOException localIOException) {
				logger.info("Attempt failed... trying again" , localIOException);
				try {
					this.socket.close();
				} catch (Exception localException) {
					logger.info("socket closed" , localException);
				}
				this.socket = this.connectionHelper.getSSLSocket();
				continue;
			}
	}

	public void addDevice(String paramString1, String paramString2) throws DuplicateDeviceException, NullIdException,
			NullDeviceTokenException {
		logger.debug("Adding Token [" + paramString2 + "] to Device [" + paramString1 + "]");
		DeviceFactory.getInstance().addDevice(paramString1, paramString2);
	}

	public Device getDevice(String paramString) throws UnknownDeviceException, NullIdException {
		logger.debug("Getting Token from Device [" + paramString + "]");
		return DeviceFactory.getInstance().getDevice(paramString);
	}

	public void removeDevice(String paramString) throws UnknownDeviceException, NullIdException {
		logger.debug("Removing Token from Device [" + paramString + "]");
		DeviceFactory.getInstance().removeDevice(paramString);
	}

	public void setProxy(String paramString1, String paramString2) {
		System.setProperty("http.proxyHost", paramString1);
		System.setProperty("http.proxyPort", paramString2);
		System.setProperty("https.proxyHost", paramString1);
		System.setProperty("https.proxyPort", paramString2);
	}

	public static byte[] getMessage(String paramString, PayLoad paramPayLoad) throws IOException, Exception {
		logger.debug("Building Raw message from deviceToken and payload");
		byte[] arrayOfByte = new byte[paramString.length() / 2];
		paramString = paramString.toUpperCase();
		int i = 0;
		Object localObject;
		for (int j = 0; j < paramString.length(); j += 2) {
			localObject = paramString.substring(j, j + 2);
			int k = Integer.parseInt((String) localObject, 16);
			arrayOfByte[(i++)] = (byte) k;
		}
		int j = 3 + arrayOfByte.length + 2 + paramPayLoad.getPayloadAsBytes().length;
		localObject = new ByteArrayOutputStream(j);
		int k = 0;
		((ByteArrayOutputStream) localObject).write(k);
		int l = arrayOfByte.length;
		((ByteArrayOutputStream) localObject).write((byte) (l & 0xFF00) >> 8);
		((ByteArrayOutputStream) localObject).write((byte) (l & 0xFF));
		((ByteArrayOutputStream) localObject).write(arrayOfByte);
		int i1 = paramPayLoad.getPayloadAsBytes().length;
		((ByteArrayOutputStream) localObject).write((byte) (i1 & 0xFF00) >> 8);
		((ByteArrayOutputStream) localObject).write((byte) (i1 & 0xFF));
		((ByteArrayOutputStream) localObject).write(paramPayLoad.getPayloadAsBytes());
		return (((ByteArrayOutputStream) localObject).toByteArray());
	}

	public int getRetryAttempts() {
		return this.retryAttempts;
	}

	public void setRetryAttempts(int paramInt) {
		this.retryAttempts = paramInt;
	}
}