package com.sohu.mtc.push.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.sohu.mtc.logser.Logger;
import com.sohu.mtc.logser.LoggerFactory;
import com.sohu.mtc.push.model.SymbianMessage;
import com.sohu.mtc.push.task.schedule.RepeatPushSymbian;

public class SymbianHttpUtil{

	private static final Log			log				= LogFactory.getLog(SymbianHttpUtil.class);
	private static SymbianHttpUtil		httpClientUtils	= new SymbianHttpUtil();
	private static final Logger			PUSHLOG			= LoggerFactory.getLogger("push_log");
	private static final Logger			NEWSLOG			= LoggerFactory.getLogger("mtc_news_log");
	private String						host[]			= { "nnapi.ovi.com", "nnapi.ovi.com.cn" };

	public static SymbianHttpUtil getInstance()
	{
		return httpClientUtils;
	}

	/**
	 * 推送搜狐微博
	 * 
	 * @param message
	 * @param url
	 */
	private boolean pushMicroBlog(HttpClient client, SymbianMessage message, String host)
	{
		boolean isPushSuccess = false;
		HttpPost httpPost = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream is = null;
		int code = 0;
		try
		{
			List<NameValuePair> headerParameters = new ArrayList<NameValuePair>();
			headerParameters.add(new BasicNameValuePair("payload", message.getContent()));//payload在此处仅仅是占位符，并未传任何实际内容

			String encodedNid = URLEncoder.encode(message.getnId(), "UTF-8");

			httpPost = new HttpPost("https://" + host + "/nnapi/1.0/nid/" + encodedNid);
			httpPost.setEntity(new UrlEncodedFormEntity(headerParameters, "UTF-8"));
			response = client.execute(httpPost);
			if(response != null)
			{
				code = response.getStatusLine().getStatusCode();
				entity = response.getEntity();
			}
			if(entity != null)
			{
				is = entity.getContent();
			}
			if(code == 202)
			{
				isPushSuccess = true;
			}
		}
		catch(Exception e)
		{
			httpPost.abort();
			e.printStackTrace();
		}
		finally
		{
			String userId = message.getUserId();
			String nId = message.getnId();
			int pushTime = message.getPushTimes() + 1;
			String m = "client=Symbian>>>code=" + code + ">>>host=" + host + ">>>userId=" + userId + ">>>nid=" + nId + ">>>pushTime=" + pushTime;
			log.debug("<<<Push-Result>>>" + m + ">>>appId=com.sohumblog");
			PUSHLOG.Debug(m);
			if(is != null)
			{
				try
				{
					is.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return isPushSuccess;
	}

	/**
	 * 推送搜狐新闻
	 * 
	 * @param message
	 * @param url
	 */
	private boolean pushSohuNews(HttpClient client, SymbianMessage message, String host)
	{
		boolean isPushSuccess = false;
		HttpPost httpPost = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		InputStream is = null;
		int code = 0;
		try
		{
			List<NameValuePair> headerParameters = new ArrayList<NameValuePair>();
			headerParameters.add(new BasicNameValuePair("payload", message.getContent()));
			String title = message.getContent().split("\t")[1];
			headerParameters.add(new BasicNameValuePair("title", title));

			String encodedNid = URLEncoder.encode(message.getnId(), "UTF-8");

			httpPost = new HttpPost("https://" + host + "/nnapi/1.0/nid/" + encodedNid);
			httpPost.setEntity(new UrlEncodedFormEntity(headerParameters, "UTF-8"));
			response = client.execute(httpPost);
			if(response != null)
			{
				code = response.getStatusLine().getStatusCode();
				entity = response.getEntity();
			}
			if(entity != null)
			{
				is = entity.getContent();
			}
			if(code == 202)
			{
				isPushSuccess = true;
			}
		}
		catch(Exception e)
		{
			httpPost.abort();
			e.printStackTrace();
		}
		finally
		{
			String nId = message.getnId();
			int pushTime = message.getPushTimes() + 1;
			String m = "client=Symbian>>>code=" + code + ">>>host=" + host + ">>>nid=" + nId + ">>>pushTime=" + pushTime;
			log.debug("<<<Push-Result>>>" + m + ">>>appId=com.sohunews");
			NEWSLOG.Debug(m);
			if(is != null)
			{
				try
				{
					is.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return isPushSuccess;
	}

	public void postMessage(SymbianMessage message)
	{
		try
		{
			HttpClient client = null;
			boolean isPushSuccess = false;
			JSONArray array = new JSONArray(message.getServiceSecret());
			
			// 由于塞班的微博推送只推私信，所以根据Message是否包含私信来判断是向微博推送还是向新闻推送
			if(message.getNewPrivateMsgCount() != null)
			{
				for(int i = array.length() - 1; i >= 0; i--)
				{
					client = HttpClientUtil.getHttpClient(host[i], message.getServiceId(), array.getJSONObject(i).getString("serviceSecret"));
					isPushSuccess = pushMicroBlog(client, message, host[i]);
					if(isPushSuccess)
					{
						break;
					}
				}
			}
			else
			{
				for(int i = array.length() - 1; i >= 0; i--)
				{
					client = HttpClientUtil.getHttpClient(host[i], message.getServiceId(), array.getJSONObject(i).getString("serviceSecret"));
					isPushSuccess = pushSohuNews(client, message, host[i]);
					if(isPushSuccess)
					{
						break;
					}
				}
			}
			// 重试机制
			if(!isPushSuccess)
			{
				//对于第一次推送失败的消息，还能推送一次
				if(message.getPushTimes() < 1)
				{
					message.setPushTimes(message.getPushTimes() + 1);
					RepeatPushSymbian.unreachableMessageList.add(message);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
}
