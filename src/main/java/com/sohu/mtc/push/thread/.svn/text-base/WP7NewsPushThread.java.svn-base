package com.sohu.mtc.push.thread;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sohu.mtc.push.service.PushUserService;
import com.sohu.mtc.push.utils.WindowsPhoneHttpUtil;
import com.sohu.t.api.model.others.WP7Config;

public class WP7NewsPushThread extends Thread{
	private static final Log	log	= LogFactory.getLog(WP7NewsPushThread.class);
	private ThreadPoolExecutor	executor;
	private PushUserService		pushUserService;
	private Map<String, String>	pushMap;

	public WP7NewsPushThread(PushUserService service, Map<String, String> map)
	{
		pushUserService = service;
		pushMap = map;
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(50, 50, 10, TimeUnit.SECONDS, queue);
	}
	
	@Override
	public void run()
	{
		if(pushMap.size() > 0)
		{
			List<WP7Config> wp7Configs = pushUserService.getAllNpWP7Config();
			if(wp7Configs != null)
			{
				for(WP7Config wp7Config : wp7Configs)
				{
					Map<String,String> map = new HashMap<String,String>();
					map.put("title", pushMap.get("title"));
					map.put("content", pushMap.get("content"));
					map.put("nid", wp7Config.getNid());
					executor.execute(new PushTask(map));
				}
				executor.shutdown();
			}
		}
	}



	/**
	 * 推送处理流程
	 * 
	 * @author jiakangliang
	 */
	private class PushTask implements Runnable{

		private Map<String,String>	map;

		public PushTask(Map<String,String>	map)
		{
			this.map = map;
		}

		public void run()
		{
			WindowsPhoneHttpUtil.sendWP7News(map);
			log.debug("Push news to wp7 success,nId=" + map.get("nid"));
		}
	}
}
