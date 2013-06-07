package com.sohu.mtc.push.ssl.exception;

public class NullDeviceTokenException extends Exception {

	private String message;

	public NullDeviceTokenException() {
		this.message = "Client already exists";
	}

	public NullDeviceTokenException(String paramString) {
		this.message = paramString;
	}

	public String toString() {
		return this.message;
	}
}
