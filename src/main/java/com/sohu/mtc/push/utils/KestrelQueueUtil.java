package com.sohu.mtc.push.utils;

import java.io.IOException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.KestrelCommandFactory;
import net.rubyeye.xmemcached.utils.AddrUtil;

import com.google.code.yanf4j.core.impl.StandardSocketOption;

/**
 * Kestrel queue Factory. Based on xmemcached.
 * 
 * @author wenfengsun
 * @since 2011-3-30 下午02:22:40
 */
public class KestrelQueueUtil{
	public static final int	QUEUE_TTL	= 5 * 60 * 1000;	// 5 mins

	/**
	 * AddrUtil.getAddresses("address1:port1 address2:port2"); XMemcachedClientBuilder new int[]{1,1}
	 * 
	 * @return
	 */
	public static MemcachedClient getInstance(String hostPort)
	{
		MemcachedClient _instance = null;
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(hostPort), new int[] { 1 });
		builder.setCommandFactory(new KestrelCommandFactory());
		builder.getConfiguration().setStatisticsServer(false);
//		builder.setConnectionPoolSize(2);
		builder.setSocketOption(StandardSocketOption.TCP_NODELAY, false);
		try
		{
			_instance = builder.build();
			_instance.setEnableHeartBeat(false);
			_instance.setOpTimeout(1000);
			_instance.setPrimitiveAsString(true);
			_instance.setConnectTimeout(1000);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return _instance;
	}
	
}
