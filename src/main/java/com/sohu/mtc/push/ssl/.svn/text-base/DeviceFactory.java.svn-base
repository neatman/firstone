package com.sohu.mtc.push.ssl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sohu.mtc.push.ssl.data.Device;
import com.sohu.mtc.push.ssl.exception.DuplicateDeviceException;
import com.sohu.mtc.push.ssl.exception.NullDeviceTokenException;
import com.sohu.mtc.push.ssl.exception.NullIdException;
import com.sohu.mtc.push.ssl.exception.UnknownDeviceException;

public class DeviceFactory {
	protected static final Log logger = LogFactory.getLog(DeviceFactory.class);
	private Map<String, Device> devices = new HashMap();
	private static DeviceFactory instance;
	private static final Object synclock = new Object();

	public static DeviceFactory getInstance() {
		synchronized (synclock) {
			if (instance == null)
				instance = new DeviceFactory();
		}
		logger.debug("Get DeviceFactory Instance");
		return instance;
	}

	public void addDevice(String paramString1, String paramString2) throws DuplicateDeviceException, NullIdException,
			NullDeviceTokenException {
		logger.debug("Adding Token [" + paramString2 + "] to Device [" + paramString1 + "]");
		if ((paramString1 == null) || (paramString1.trim().equals("")))
			throw new NullIdException();
		if ((paramString2 == null) || (paramString2.trim().equals("")))
			throw new NullDeviceTokenException();
		if (!(this.devices.containsKey(paramString1))) {
			paramString2 = StringUtils.deleteWhitespace(paramString2);
			this.devices.put(paramString1, new Device(paramString1, paramString2, new Timestamp(Calendar.getInstance()
					.getTime().getTime())));
		} else {
			throw new DuplicateDeviceException();
		}
	}

	public Device getDevice(String paramString) throws UnknownDeviceException, NullIdException {
		logger.debug("Getting Token from Device [" + paramString + "]");
		if ((paramString == null) || (paramString.trim().equals("")))
			throw new NullIdException();
		if (this.devices.containsKey(paramString))
			return ((Device) this.devices.get(paramString));
		throw new UnknownDeviceException();
	}

	public void removeDevice(String paramString) throws UnknownDeviceException, NullIdException {
		logger.debug("Removing Token from Device [" + paramString + "]");
		if ((paramString == null) || (paramString.trim().equals("")))
			throw new NullIdException();
		if (this.devices.containsKey(paramString))
			this.devices.remove(paramString);
		else
			throw new UnknownDeviceException();
	}
}