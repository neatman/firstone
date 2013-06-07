package com.sohu.mtc.push.task;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;

import com.sohu.mtc.push.model.PushConfig;
import com.sohu.mtc.push.service.PushUserService;
import com.sohu.mtc.push.strategy.PushStrategyOperator;
import com.sohu.mtc.push.strategy.impl.IosPushStrategy;
import com.sohu.mtc.push.strategy.impl.SymbianPushStrategy;
import com.sohu.mtc.push.strategy.impl.Wp7PushStrategy;
import com.sohu.mtc.push.task.schedule.NewsPush;
import com.sohu.mtc.push.utils.Constants;
import com.sohu.t.api.model.others.PushUser;

/**
 * @author jiakangliang
 */
public class MicroblogTaskExecutor{

	private static final Log	log	= LogFactory.getLog(MicroblogTaskExecutor.class);
	private PushUserService		pushUserService;
	private TaskExecutor		taskExecutor;
	private NewsPush			newsPush;

	public MicroblogTaskExecutor(TaskExecutor taskExecutor)
	{
		this.taskExecutor = taskExecutor;
	}
	
	public void setPushUserService(PushUserService pushUserService)
	{
		this.pushUserService = pushUserService;
	}
	
	public void setNewsPush(NewsPush newsPush)
	{
		this.newsPush = newsPush;
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
			if(queueItem.startsWith("user_prefix"))
			{
				// 手机客户端在客户登陆时，注册NID与UID的对应关系
				pushUserService.registerPushUser(queueItem);
			}
			else if(queueItem.indexOf("term_push") != -1)
			{
				newsPush.pushTerm();
			}
			else if(queueItem.indexOf("uid") != -1)
			{
				String uId = pushUserService.getFromJsonString(queueItem, "uid");
				// 获取此用户的所有推送设备，即此uid与所有nid的对应关系
				List<PushUser> pushUsers = pushUserService.getUsers(uId);
				// 检查用户是否是推送用户
				if(pushUsers != null && pushUsers.size() > 0)
				{
					//创建策略调用
					PushStrategyOperator pushOperator = new PushStrategyOperator();
					for(PushUser pushUser : pushUsers)
					{
						String clientId = pushUser.getClientId();
						String nId = pushUser.getnId();
						String imei = pushUser.getImei();
						PushConfig config = null;
						// 根据设备不同，创建不同的推送策略
						if(clientId != null && clientId.equals(Constants.APPALE))
						{
							pushOperator.setPushStrategy(new IosPushStrategy());
							config = pushUserService.getAppleConfig(nId);
						}
						else if(clientId != null && clientId.equals(Constants.SYMBIAN))
						{
							pushOperator.setPushStrategy(new SymbianPushStrategy());
						}
						else if(clientId != null && clientId.equals(Constants.WP7))
						{
							pushOperator.setPushStrategy(new Wp7PushStrategy());
							config = pushUserService.getWP7Config(imei);
						}
						if(pushOperator.getPushStrategy() != null)
						{
							log.debug("push to " + clientId + "<<<uid>>>>>>" + uId + "<<<nid>>>>>>" + nId + "<<<imei>>>" + imei);
							pushOperator.send(pushUser, queueItem, config);
						}
					}
				}
			}
		}

	}

}
