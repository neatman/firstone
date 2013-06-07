package com.sohu.mtc.push.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;

import com.sohu.mtc.push.strategy.PushStrategyOperator;
import com.sohu.mtc.push.strategy.impl.NewsPushStrategy;

public class SohuNewsTaskExecutor{
	private static final Log	log	= LogFactory.getLog(SohuNewsTaskExecutor.class);
	private TaskExecutor		taskExecutor;

	public SohuNewsTaskExecutor(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}

	public void executePushTask(String queueItem)
	{
		try
		{
			taskExecutor.execute(new PushTask(queueItem));
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
			executePushTask(queueItem);
		}
	}

	/**
	 * 推送处理流程
	 * 
	 * @author jiakangliang
	 */
	private class PushTask implements Runnable{

		private String	queueItem;

		public PushTask(String queueItem)
		{
			this.queueItem = queueItem;
		}

		public void run()
		{
			if(queueItem.indexOf("service_secret") != -1)
			{
				PushStrategyOperator pushOperator = new PushStrategyOperator();
				pushOperator.setPushStrategy(new NewsPushStrategy());
				pushOperator.send(null, queueItem, null);
			}
		}
	}
}
