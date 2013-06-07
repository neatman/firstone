package com.sohu.mtc.push.task;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;

import com.sohu.mtc.push.model.Tile;
import com.sohu.mtc.push.service.PushUserService;
import com.sohu.mtc.push.service.TimelineService;
import com.sohu.mtc.push.utils.WindowsPhoneHttpUtil;
import com.sohu.t.api.model.WP7PinUser;

public class WP7PinUserTaskExecutor{

	private static final Log		log	= LogFactory.getLog(WP7PinUserTaskExecutor.class);
	private PushUserService			pushUserService;
	private TimelineService	        timelineService;
	private TaskExecutor			taskExecutor;

	public WP7PinUserTaskExecutor(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}

	public void setPushUserService(PushUserService pushUserService)
	{
		this.pushUserService = pushUserService;
	}

	public void setTimelineService(TimelineService timelineService)
	{
		this.timelineService = timelineService;
	}

	public void executePushTask(Long userId)
	{
		try
		{
			taskExecutor.execute(new PushTask(userId));
		}
		catch(TaskRejectedException e)
		{
			log.error("worker is busy, try to wait");
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e1)
			{
				e1.printStackTrace();
			}
			executePushTask(userId);
		}
	}

	/**
	 * 推送处理流程
	 * 
	 * @author jiakangliang
	 */
	private class PushTask implements Runnable{

		private Long	userId;

		public PushTask(Long userId)
		{
			this.userId = userId;
		}

		public void run()
		{
			List<WP7PinUser> pinUsers = pushUserService.getPinUsers(String.valueOf(userId));
			if(pinUsers != null)
			{
				Tile microblog = null;
				try
				{
					microblog = timelineService.getNewMicroblog(userId);
					if(microblog != null)
					{
						for(WP7PinUser pinUser:pinUsers)
						{
							WindowsPhoneHttpUtil.sendWP7PinUserMessage(pinUser, microblog);
						}						
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

}
