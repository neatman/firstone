package com.sohu.mtc.push.task.schedule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;

import com.sohu.mtc.push.model.SymbianMessage;
import com.sohu.mtc.push.utils.SymbianHttpUtil;

public class RepeatPushSymbian{
	private static final Log			log						= LogFactory.getLog(RepeatPushSymbian.class);
	public static List<SymbianMessage>	unreachableMessageList	= new ArrayList<SymbianMessage>();
	private TaskExecutor				taskExecutor;
	private ThreadPoolExecutor  		executor;

	private ThreadPoolExecutor initThreadPool()
	{
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(100, 100, 10, TimeUnit.SECONDS, queue);
		return executor;
	}
	
	private List<SymbianMessage> getSynList()
	{
		synchronized(unreachableMessageList)
		{
			List<SymbianMessage> tempList = unreachableMessageList;
			unreachableMessageList = new ArrayList<SymbianMessage>();
			return tempList;
		}
	}

	public void pushMessage()
	{
		try
		{
			log.debug("Repeat-Push-Symbian>>>Size=" + unreachableMessageList.size());
			this.initThreadPool();
			Iterator<SymbianMessage> iterator = this.getSynList().iterator();
			while(iterator.hasNext())
			{
				SymbianMessage message = iterator.next();
				// 由于需要重试的消息数量众多，故也使用线程池处理
//				taskExecutor.execute(new PushTask(message));
				executor.execute(new PushTask(message));
			}
			executor.shutdown();
		}
		catch(Exception e)
		{
			log.debug("Repeat-Push-ONA-Unreached-Message error...");
			e.printStackTrace();
		}
	}

	/**
	 * 推送处理流程
	 * 
	 * @author jiakangliang
	 */
	private class PushTask implements Runnable{

		private SymbianMessage	message;

		public PushTask(SymbianMessage message)
		{
			this.message = message;
		}

		public void run()
		{
			SymbianHttpUtil.getInstance().postMessage(message);
		}
	}

	public void setTaskExecutor(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}

}
