package com.sohu.mtc.push.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.CoreConnectionPNames;

public class HttpClientUtil{

	private static final Log					log			= LogFactory.getLog(HttpClientUtil.class);
	private static ThreadSafeClientConnManager	cm;

	// 初始化连接管理器，配置连接池，设置最大连接数与最大路由连接数
	static
	{
		SchemeRegistry sr = new SchemeRegistry();
		sr.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		sr.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		cm = new ThreadSafeClientConnManager(sr);
		cm.setMaxTotal(400);
		cm.setDefaultMaxPerRoute(20);
		HttpHost host1 = new HttpHost("nnapi.ovi.com", -1, "https");
		HttpHost host2 = new HttpHost("nnapi.ovi.com.cn", -1, "https");
		cm.setMaxForRoute(new HttpRoute(host1, null, true), 100);
		cm.setMaxForRoute(new HttpRoute(host2, null, true), 100);
		log.debug("<<<Init ClientConnManager>>>");
	}
	
	public static ThreadSafeClientConnManager getConnManager()
	{
		return cm;
	}

	public static HttpClient getHttpClient(String domain, String serviceId, String serviceSecret)
	{
		DefaultHttpClient client = new DefaultHttpClient(cm);
		Credentials creds = new UsernamePasswordCredentials(serviceId, serviceSecret);
		client.getCredentialsProvider().setCredentials(new AuthScope(domain, 443, AuthScope.ANY_REALM), creds);

		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
		client.getParams().setParameter(CoreConnectionPNames.TCP_NODELAY, true);
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);

		return client;
	}

	public static HttpClient getHttpClient()
	{
		DefaultHttpClient client = new DefaultHttpClient(cm);

		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
		client.getParams().setParameter(CoreConnectionPNames.TCP_NODELAY, true);
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		
		return client;
	}
}
