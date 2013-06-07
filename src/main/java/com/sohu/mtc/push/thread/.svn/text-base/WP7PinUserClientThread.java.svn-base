package com.sohu.mtc.push.thread;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sohu.mtc.push.service.PushUserService;
import com.sohu.mtc.push.task.WP7PinUserTaskExecutor;
import com.sohu.mtc.push.utils.BeanFactory;

public class WP7PinUserClientThread extends Thread{
	private static final Log			log	= LogFactory.getLog(WP7PinUserClientThread.class);
	private WP7PinUserTaskExecutor		wp7PinUserTaskExecutor;
	private PushUserService				pushUserService;

	public WP7PinUserClientThread()
	{
		wp7PinUserTaskExecutor = (WP7PinUserTaskExecutor) BeanFactory.getBean("wp7PinUserTaskExecutor");
		pushUserService = (PushUserService) BeanFactory.getBean("pushUserService");
	}

	@Override
	public void run()
	{
		log.debug("<<<WP7_Pin_User>>>Start pushing......");
		while(true)
		{
			try
			{
				List<Long> userIds = pushUserService.getAllDistinctPinUserIds();
				if(userIds != null)
				{
					for(Long userId : userIds)
					{
						wp7PinUserTaskExecutor.executePushTask(userId);
					}
				}
				Thread.sleep(1000 * 60 * 5);
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
