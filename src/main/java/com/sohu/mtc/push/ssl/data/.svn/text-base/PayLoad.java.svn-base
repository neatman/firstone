package com.sohu.mtc.push.ssl.data;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.sohu.mtc.push.ssl.DeviceFactory;

public class PayLoad {

	protected static final Log logger = LogFactory.getLog(DeviceFactory.class);
	private JSONObject payload;
	private JSONObject apsDictionary;

	public PayLoad() {
		this.payload = new JSONObject();
		this.apsDictionary = new JSONObject();
		try {
			this.payload.put("aps", this.apsDictionary);
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
	}

	public PayLoad(String paramString1, int paramInt, String paramString2) throws JSONException {
		addAlert(paramString1);
		addBadge(paramInt);
		if (paramString2 == null)
			return;
		addSound(paramString2);
	}

	public void addBadge(int paramInt) throws JSONException {
		logger.debug("Adding badge [" + paramInt + "]");
		this.apsDictionary.putOpt("badge", Integer.valueOf(paramInt));
	}

	public void addSound(String paramString) throws JSONException {
		logger.debug("Adding sound [" + paramString + "]");
		this.apsDictionary.putOpt("sound", paramString);
	}

	public void addAlert(String paramString) throws JSONException {
		logger.debug("Adding alert [" + paramString + "]");
		this.apsDictionary.put("alert", paramString);
	}

	public void addCustomAlert(PayLoadCustomAlert paramPayLoadCustomAlert) throws JSONException {
		logger.debug("Adding custom Alert");
		this.apsDictionary.put("alert", paramPayLoadCustomAlert);
	}

	public void addCustomDictionary(String paramString1, String paramString2) throws JSONException {
		logger.debug("Adding custom Dictionary [" + paramString1 + "] = [" + paramString2 + "]");
		this.payload.put(paramString1, paramString2);
	}

	public void addCustomDictionary(String paramString, int paramInt) throws JSONException {
		logger.debug("Adding custom Dictionary [" + paramString + "] = [" + paramInt + "]");
		this.payload.put(paramString, paramInt);
	}

	public void addCustomDictionary(String paramString, List paramList) throws JSONException {
		logger.debug("Adding custom Dictionary [" + paramString + "] = (list)");
		this.payload.put(paramString, paramList);
	}

	public String toString() {
		return this.payload.toString();
	}

	public byte[] getPayloadAsBytes() throws Exception {
		byte[] arrayOfByte = null;
		try {
			arrayOfByte = toString().getBytes("UTF-8");
		} catch (Exception localException) {
			arrayOfByte = toString().getBytes();
		}
		if (arrayOfByte.length > 256)
			throw new Exception("Payload too large...[256 Bytes is the limit]");
		return arrayOfByte;
	}
}
