package com.sohu.mtc.push.utils;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.sohu.mtc.logser.Logger;
import com.sohu.mtc.logser.LoggerFactory;
import com.sohu.mtc.push.model.Tile;
import com.sohu.mtc.push.model.WindowsPhoneMessage;
import com.sohu.t.api.model.WP7PinUser;

public class WindowsPhoneHttpUtil{
	private static final Logger	PUSHLOG	= LoggerFactory.getLogger("push_log");
	private static final Log	log		= LogFactory.getLog(WindowsPhoneHttpUtil.class);
	
	public static void sendWindowsPhone7Message(WindowsPhoneMessage message)
	{

		HttpClient client = HttpClientUtil.getHttpClient();
		HttpPost post = new HttpPost(message.getnId());
		UUID uuid = UUID.randomUUID();
		post.addHeader("X-MessageID", uuid.toString());
		post.addHeader("X-NotificationClass", "1");
		post.addHeader("X-WindowsPhone-Target", "token");
		log.debug("message=====[" + message + "]");
		HttpResponse response = null;
		int code = 0;
		try
		{
			post.setEntity(createPostBody(message));
			response = client.execute(post);
		}
		catch(Exception e)
		{
			post.abort();
			e.printStackTrace();
		}
		finally
		{
			if(response != null)
			{
				code = response.getStatusLine().getStatusCode();
				if(response.getEntity() != null)
				{
					try
					{
						InputStream is = response.getEntity().getContent();
						if(is != null)
						{
							is.close();
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			String logMessage = "client=WindowsPhone7>>>code=" + code + ">>>userid=" + message.getUserId() + ">>>nid=" + message.getnId();
			PUSHLOG.Debug(logMessage);
			log.debug("<<<Push-Result>>>" + logMessage);
		}
	}

	/**
	 * 目前支持两种tile通知：
	 * 1.Main tile 推送新评论数和@数，
	 * 2.私信 tile 推送新私信数，
	 * 二者http header相同，差别在于消息内容； 
	 * <wp:Count>为整数，大于99将显示99，小于等于0将不显示，传值是可以直接传，也可以将大于99的数转换成99； 
	 * Main tile中的Count是评论和提及数的和； 
	 * BackContent中的数值，0 – 99 直接显示,大于99的显示99+； 
	 * BackContent 中的\n为换行符，需要utf8编码（也可以对整个xml utf8编码）； 
	 * 注意私信tile 中<wp:Tile> 有 ID属性。
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	private static StringEntity createPostBody(WindowsPhoneMessage message) throws Exception
	{
		StringEntity myEntity = null;
		String msgCountStr = String.valueOf(message.getMessageCount());
		msgCountStr = message.getMessageCount()>99?"99+":msgCountStr;
		String text = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" 
				+ "<wp:Notification xmlns:wp=\"WPNotification\">"
				+ "<wp:Tile>" 
				+ "<wp:Count>" + msgCountStr + "</wp:Count>" 
				+ "</wp:Tile>" 
				+ "</wp:Notification>";
		myEntity = new StringEntity(text, "utf-8");
		log.debug("" + EntityUtils.toString(myEntity));
		return myEntity;
	}
	
	/**
	 * 为WindowsPhone7 pin到桌面的用户推送一条最新的微博
	 * @param pinUser
	 * @param microblog
	 */
	public static void sendWP7PinUserMessage(WP7PinUser pinUser, Tile microblog)
	{
		HttpClient client = HttpClientUtil.getHttpClient();
		HttpPost post = new HttpPost(pinUser.getnId());
		UUID uuid = UUID.randomUUID();
		post.addHeader("X-MessageID", uuid.toString());
		post.addHeader("X-NotificationClass", "1");
		post.addHeader("X-WindowsPhone-Target", "token");
		HttpResponse response = null;
		int code = 0;
		try
		{
			post.setEntity(createPinUserPostBody(pinUser, microblog));
			response = client.execute(post);
		}
		catch(Exception e)
		{
			post.abort();
			e.printStackTrace();
		}
		finally
		{
			if(response != null)
			{
				code = response.getStatusLine().getStatusCode();
				if(response.getEntity() != null)
				{
					try
					{
						InputStream is = response.getEntity().getContent();
						if(is != null)
						{
							is.close();
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			String logMessage = "client=WP7_Pin_User>>>code=" + code + ">>>userid=" + pinUser.getUserId() + ">>>nid=" + pinUser.getnId();
			PUSHLOG.Debug(logMessage);
			log.debug("<<<Push-Result>>>" + logMessage);
		}
	}
	
	public static StringEntity createPinUserPostBody(WP7PinUser pinUser, Tile microblog) throws Exception
	{
		StringEntity myEntity = null;
		String text = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" 
			+ "<wp:Notification xmlns:wp=\"WPNotification\">"
			+ "<wp:Tile Id=\"/Archives/OtherArchivesPage.xaml?id="+pinUser.getUserId()+"\">" 
			+ "<wp:BackTitle>" + microblog.getUserName() + "</wp:BackTitle>"
			+ "<wp:BackContent>" + microblog.getContent() + "</wp:BackContent>" 
			+ "</wp:Tile>" 
			+ "</wp:Notification>";
		myEntity = new StringEntity(text, "utf-8");
		log.debug("" + EntityUtils.toString(myEntity));
		return myEntity;
	}
	
	public static void sendWP7News(Map<String,String> map)
	{
		sendWP7NewsToast(map);
		sendWP7NewsTile(map);
	}
	
	public static void sendWP7NewsToast(Map<String,String> map)
	{
		HttpClient client = HttpClientUtil.getHttpClient();
		HttpPost post = new HttpPost(map.get("nid"));
		UUID uuid = UUID.randomUUID();
		post.addHeader("X-MessageID", uuid.toString());
		post.addHeader("X-NotificationClass", "2");
		post.addHeader("X-WindowsPhone-Target", "toast");
		HttpResponse response = null;
		int code = 0;
		try
		{
			post.setEntity(createNewsToast(map));
			response = client.execute(post);
		}
		catch(Exception e)
		{
			post.abort();
			e.printStackTrace();
		}
		finally
		{
			if(response != null)
			{
				code = response.getStatusLine().getStatusCode();
				if(response.getEntity() != null)
				{
					try
					{
						InputStream is = response.getEntity().getContent();
						if(is != null)
						{
							is.close();
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			if(code == 412 && map.get("retry") == null)
			{
				map.put("retry", "1");
				sendWP7NewsToast(map);
				return;
			}
			String logMessage = "sendWP7NewsToast>>>code=" + code + ">>>nid=" + map.get("nid");
			log.debug("<<<Push-WP7-News-Result>>>" + logMessage);
		}
	}
	
	public static void sendWP7NewsTile(Map<String,String> map)
	{
		HttpClient client = HttpClientUtil.getHttpClient();
		HttpPost post = new HttpPost(map.get("nid"));
		UUID uuid = UUID.randomUUID();
		post.addHeader("X-MessageID", uuid.toString());
		post.setHeader("X-NotificationClass", "1");
		post.setHeader("X-WindowsPhone-Target", "token");
		HttpResponse response = null;
		int code = 0;
		try
		{
			post.setEntity(createNewsTile());
			response = client.execute(post);
		}
		catch(Exception e)
		{
			post.abort();
			e.printStackTrace();
		}
		finally
		{
			if(response != null)
			{
				code = response.getStatusLine().getStatusCode();
				if(response.getEntity() != null)
				{
					try
					{
						InputStream is = response.getEntity().getContent();
						if(is != null)
						{
							is.close();
						}
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			if(code == 412 && map.get("retry") == null)
			{
				map.put("retry", "1");
				sendWP7NewsTile(map);
				return;
			}
			String logMessage = "sendWP7NewsTile>>>code=" + code + ">>>nid=" + map.get("nid");
			log.debug("<<<Push-WP7-News-Result>>>" + logMessage);
		}
	}
	
	public static StringEntity createNewsTile() throws Exception
	{
		StringEntity myEntity = null;
		String text = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"<wp:Notification xmlns:wp=\"WPNotification\">" +
				"<wp:Tile id=\"/MorningAndEveningNewsPage.xaml\">" +
				"<wp:Count>1</wp:Count>" +
				"</wp:Tile>" +
				"</wp:Notification>";
		myEntity = new StringEntity(text, "utf-8");
		log.debug("createNewsTile>>>" + EntityUtils.toString(myEntity));
		return myEntity;
	}
	
	public static StringEntity createNewsToast(Map<String,String> map) throws Exception
	{
		StringEntity myEntity = null;
		String text = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
				"<wp:Notification xmlns:wp=\"WPNotification\">" +
				"<wp:Toast>" +
				"<wp:Text1>" + map.get("title") + "</wp:Text1>" +
				"<wp:Text2>" + map.get("content") + "</wp:Text2>" +
				"<wp:Param>/MorningAndEveningNewsPage.xaml?accessType=1</wp:Param>" +
				"</wp:Toast>" +
				"</wp:Notification>";
		myEntity = new StringEntity(text, "utf-8");
		log.debug("createNewsToast>>>" + EntityUtils.toString(myEntity));
		return myEntity;
	}
	
}