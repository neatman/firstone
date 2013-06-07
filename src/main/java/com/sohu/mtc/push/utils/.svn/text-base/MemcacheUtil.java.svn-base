package com.sohu.mtc.push.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Future;

import net.spy.memcached.MemcachedClient;

import com.sohu.mtc.logser.Logger;
import com.sohu.mtc.logser.LoggerFactory;

public class MemcacheUtil{

	private static MemcacheUtil		_instance	= new MemcacheUtil();
	private static MemcachedClient	memCachedClient;
	public static final int			TTL			= 5 * 60;
	public static final int			DAY_TTL		= 24 * 60 * 60;
	public static final int			LOGIN_TTL	= 60 * 60;
	public static final int			CREATE_TTL	= 10;
	public static final int			M_TTL		= 60;

//	private static final Logger		memlog		= LoggerFactory.getLogger("inner_no_memcached");

	public static MemcacheUtil getInstance()
	{
		return _instance;
	}

	static
	{
		try
		{
			memCachedClient = new MemcachedClient(new InetSocketAddress(Constants.MEMHOST, Constants.MEMPORT));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public String getValue(String key)
	{
		Object obj = memCachedClient.get(key);
		if(obj instanceof String)
		{
			return (String) obj;
		}
		else
		{
//			memlog.Debug("no memcache->key=" + key);
			return null;
		}
	}

	public boolean putValue(String key, String val, int TTL)
	{
		Future<Boolean> ret = memCachedClient.set(key, TTL, val);
		try
		{
			return ret.get().booleanValue();
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public boolean putValue(String key, String val)
	{
		Future<Boolean> ret = memCachedClient.set(key, TTL, val);
		try
		{
			return ret.get().booleanValue();
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public Object getObjValue(String key)
	{
		Object obj = memCachedClient.get(key);
		if(null == obj)
		{
//			memlog.Debug("no memcache->key=" + key);
		}
		return obj;
	}

	public boolean putObjValue(String key, Object val, int TTL)
	{
		if(val == null)
		{
			return false;
		}
		Future<Boolean> ret = memCachedClient.set(key, TTL, val);
		try
		{
			return ret.get().booleanValue();
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public boolean add(String key, String val, int TTL)
	{
		Future<Boolean> ret = memCachedClient.add(key, TTL, val);
		try
		{
			return ret.get().booleanValue();
		}
		catch(Exception e)
		{
			return false;
		}
	}

	public void removeValue(String key)
	{
		memCachedClient.delete(key);
	}
}
