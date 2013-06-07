package com.sohu.t.push;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientTest
{
	public static String namePwd = "microblog_uname:microblog_pwd";
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
//		get("http://localhost/statuses/getStatusVideo/7429131118.xml?key=gPYqgRaClXoXH5bUvlDf");
//		get("http://10.13.81.103/termPush.xml?termId=12345");
		List list = new LinkedList();
	}
	
	public static void get(String url)
	{
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse resp = null;
		HttpEntity entity = null;
		try
        {
    		byte[] token = namePwd.getBytes("utf-8");
    		get.addHeader("Authorization", "Basic "+new String(Base64.encodeBase64(token), "utf-8"));
	        resp = client.execute(get);
	        if(resp != null)
	        {
	        	entity = resp.getEntity();
	        	if(entity != null)
	        	{
	        		System.out.println(EntityUtils.toString(entity));	        		
	        	}
	        }
        }
        catch(Exception e)
        {
        	get.abort();
	        e.printStackTrace();
        }
	}

}
