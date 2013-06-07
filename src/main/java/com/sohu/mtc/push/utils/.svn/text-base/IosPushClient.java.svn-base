//package com.sohu.mtc.push.utils;
//
//import javapns.Push;
//import javapns.communication.exceptions.KeystoreException;
//import javapns.notification.PushNotificationPayload;
//import javapns.notification.transmission.NotificationThreads;
//import javapns.notification.transmission.PushQueue;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import org.json.JSONException;
//
//import com.sohu.mtc.logser.Logger;
//import com.sohu.mtc.logser.LoggerFactory;
//import com.sohu.mtc.push.model.IosMessage;
//
//public class IosPushClient{
//
//	private static final Log		log		= LogFactory.getLog(IosPushClient.class);
//	private static final Logger		PUSHLOG	= LoggerFactory.getLogger("push_log");
//	private static IosPushClient	client	= new IosPushClient();
//	private PushQueue				pushQueue;
//	private NotificationThreads     notificationThreads;
//
//	private IosPushClient()
//	{
//		try
//		{
//			notificationThreads = (NotificationThreads) Push.queue(Constants.CER_PATH, Constants.PASSWORD, true, 10);
//			notificationThreads.setMaxNotificationsPerConnection(10000);
//			notificationThreads.start();
//		}
//		catch(KeystoreException e)
//		{
//			log.debug("init push queue error," + e);
//			e.printStackTrace();
//		}
//	}
//
//	public static IosPushClient getInstance()
//	{
//		return client;
//	}
//
//	public void sendMessage(IosMessage message)
//	{
//		try
//		{
//			PushNotificationPayload payLoad = getPayLoad(message);
//			if(payLoad == null)
//			{
//				return;
//			}
//			notificationThreads.add(payLoad, message.getnId());
//			String m = "client=iphone>>>result=success>>>userid=" + message.getUserId() + ">>>nid=" + message.getnId() + ">>>badge="
//					+ message.getMessageCount();
//			log.debug("<<<Push-Result>>>" + m);
//			PUSHLOG.Debug(m);
//		}
//		catch(Exception e)
//		{
//			String m = "client=iphone>>>result=failure>>>userid=" + message.getUserId() + ">>>nid=" + message.getnId();
//			PUSHLOG.Debug(m);
//			log.error("<<<Push-Result>>>" + m + ">>>message=(" + e.getMessage() + ")");
//			e.printStackTrace();
//		}
//	}
//
//	private PushNotificationPayload getPayLoad(IosMessage message)
//	{
//		PushNotificationPayload payload = null;
//		try
//		{
//			//精选新闻推送
//			if(message.getAlertMessage() != null && message.getNewspaperUrl() != null)
//			{
//				payload = PushNotificationPayload.complex();
//				payload.addAlert(message.getAlertMessage());
//				payload.addCustomDictionary("url", message.getNewspaperUrl());
//			}
//			else if(message.getMessageCount() != 0)
//			{	//微博新消息数推送
//				payload = PushNotificationPayload.complex();
//				payload.addBadge(message.getMessageCount());				
//			}
//		}
//		catch(JSONException e)
//		{
//			log.debug("get payload error," + e);
//			e.printStackTrace();
//		}
//		return payload;
//	}
//}
