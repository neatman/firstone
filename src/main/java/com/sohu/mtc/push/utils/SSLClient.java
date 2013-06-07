package com.sohu.mtc.push.utils;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;

import com.sohu.mtc.logser.Logger;
import com.sohu.mtc.logser.LoggerFactory;
import com.sohu.mtc.push.model.IosMessage;
import com.sohu.mtc.push.ssl.PushNotificationManager;
import com.sohu.mtc.push.ssl.SSLConnectionHelper;
import com.sohu.mtc.push.ssl.data.Device;
import com.sohu.mtc.push.ssl.data.PayLoad;

public class SSLClient{

	private static final Log				log			= LogFactory.getLog(SSLClient.class);
	private static final Logger				PUSHLOG		= LoggerFactory.getLogger("push_log");
	private static final String				TARGETURL	= "gateway.push.apple.com";
	private static final int				PORT		= 2195;
	private static final String				CER_PATH	= "src/main/config/2012_07_10.p12";
	private static final String				PASSWORD	= "123456";
	private static PushNotificationManager	pushManager;
	private static SSLClient				instance	= new SSLClient();

	private SSLClient()
	{
		try
		{
			pushManager = PushNotificationManager.getInstance();
			pushManager.initializeConnection(TARGETURL, PORT, CER_PATH, PASSWORD, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
		}
		catch(Exception e)
		{
			log.error("create  SSL connect   error");
			e.printStackTrace();
		}
	}

	public static SSLClient getInstance()
	{
		return instance;
	}

	public void sendMessage(IosMessage message)
	{
		try
		{
			PayLoad payLoad = getPayLoad(message);
			if(payLoad == null)
			{
				return;
			}
			// Send Push
			Device device = new Device("", message.getnId(), new Timestamp(Calendar.getInstance().getTime().getTime()));
			pushManager.sendNotification(device, payLoad);
			String m = "client=iphone>>>result=success>>>userid=" + message.getUserId() + ">>>nid=" + message.getnId()+">>>badge="+message.getMessageCount();
			PUSHLOG.Debug(m);
			log.debug("<<<Push-Result>>>" + m);
			// pushManager.removeDevice(message.getRandom());
		}
		catch(Exception e)
		{
			String m = "client=iphone>>>result=failure>>>userid=" + message.getUserId() + ">>>nid=" + message.getnId();
			PUSHLOG.Debug(m);
			log.error("<<<Push-Result>>>" + m + ">>>message=(" + e.getMessage() + ")");
			e.printStackTrace();
		}
	}

	private PayLoad getPayLoad(IosMessage message) throws JSONException
	{
		PayLoad payLoad = null;
		if(message.getAlertMessage() != null && message.getNewspaperUrl() != null)
		{
			payLoad = new PayLoad();
			payLoad.addAlert(message.getAlertMessage());// push的内容
			payLoad.addCustomDictionary("url", message.getNewspaperUrl());
		}
		else if(message.getMessageCount() != 0)
		{
			payLoad = new PayLoad();
			payLoad.addBadge(message.getMessageCount());// 图标小红圈的数值
		}
//		payLoad.addAlert("test alert.");// push的内容
//		payLoad.addBadge(message.getMessageCount());// 图标小红圈的数值
//		payLoad.addSound("default");// 铃音
//		payLoad.addCustomDictionary("fs", message.getNewFansCount());
//		payLoad.addCustomDictionary("tm", message.getNewAtCount());
//		payLoad.addCustomDictionary("pm", message.getNewPrivateMsgCount());
//		payLoad.addCustomDictionary("rm", message.getNewCommentCount());
		return payLoad;

	}
}
