package com.sohu.mtc.push.ssl.data;

import java.sql.Timestamp;

public class Device {

	private String id;
	private String token;
	private Timestamp lastRegister;

	public Device(String paramString1, String paramString2, Timestamp paramTimestamp) {
		this.id = paramString1;
		this.token = paramString2;
		this.lastRegister = paramTimestamp;
	}

	public String getId() {
		return this.id;
	}

	public String getToken() {
		return this.token;
	}

	public Timestamp getLastRegister() {
		return this.lastRegister;
	}
}
