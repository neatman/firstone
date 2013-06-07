package com.sohu.mtc.push.thread;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sohu.mtc.logser.Logger;
import com.sohu.mtc.logser.LoggerFactory;
import com.sohu.mtc.push.service.PushUserService;
import com.sohu.mtc.push.task.SohuNewsTaskExecutor;
import com.sohu.mtc.push.utils.BeanFactory;
import com.sohu.mtc.push.utils.Constants;
import com.sohu.mtc.push.utils.KestrelQueueUtil;

public class SohuNewsClientThread extends Thread{
	private static final Log		log				= LogFactory.getLog(SohuNewsClientThread.class);
	private static final Logger		NEWSLOG			= LoggerFactory.getLogger("mtc_news_log");
	private MemcachedClient			kestrelClient	= KestrelQueueUtil.getInstance(Constants.KESHOST_SohuNews);
	private SohuNewsTaskExecutor	sohuNewsTaskExecutor;
	private PushUserService			pushUserService;

	public SohuNewsClientThread()
	{
		sohuNewsTaskExecutor = (SohuNewsTaskExecutor) BeanFactory.getBean("sohuNewsTaskExecutor");
		pushUserService = (PushUserService) BeanFactory.getBean("pushUserService");
	}

	@Override
	public void run()
	{
		log.debug("<<<Sohu-News>>>Start reading queue......");
		while(true)
		{
			try
			{
				Object message = kestrelClient.get(Constants.QUEUE_NAME);
				if(message == null || message.toString().trim().length() == 0)
				{
					Thread.sleep(1000);
				}
				else
				{
					log.debug("<<<SohuNews-Queue-Message>>>>>>" + message);
					String nId = pushUserService.getFromJsonString(message.toString(), "nid");
					NEWSLOG.Debug("<<<Get-A-Queue-Message>>>nId=" + nId);
					sohuNewsTaskExecutor.executePushTask(message.toString());
				}
			}
			catch(Exception e)
			{
				try
				{
					log.error(e.getMessage());
					e.printStackTrace();
					Thread.sleep(1000);
					continue;
				}
				catch(InterruptedException e1)
				{
					log.error(e.getMessage());
					e.printStackTrace();
					continue;
				}
			}
		}
	}
}
