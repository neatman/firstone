package com.sohu.mtc.push.thread;

import net.rubyeye.xmemcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sohu.mtc.push.task.MicroblogTaskExecutor;
import com.sohu.mtc.push.utils.BeanFactory;
import com.sohu.mtc.push.utils.Constants;
import com.sohu.mtc.push.utils.KestrelQueueUtil;

public class MicroblogClientThread extends Thread{

	private static final Log		log				= LogFactory.getLog(MicroblogClientThread.class);
	private MemcachedClient			kestrelClient	= KestrelQueueUtil.getInstance(Constants.KESHOST_MicroBlog);
	private MicroblogTaskExecutor	microblogTaskExecutor;

	public MicroblogClientThread()
	{
		microblogTaskExecutor = (MicroblogTaskExecutor) BeanFactory.getBean("microblogTaskExecutor");
	}

	@Override
	public void run()
	{
		log.debug("<<<Micro-Blog-Push>>>Start reading queue......");
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
					log.debug("<<<MicroBlog-Queue-Message>>>>>>" + message);
					microblogTaskExecutor.executePushTask(message.toString());
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
