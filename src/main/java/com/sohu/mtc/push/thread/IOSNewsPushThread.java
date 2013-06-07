package com.sohu.mtc.push.thread;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sohu.mtc.logser.Logger;
import com.sohu.mtc.logser.LoggerFactory;
import com.sohu.mtc.push.model.IosMessage;
import com.sohu.mtc.push.service.PushUserService;
import com.sohu.mtc.push.utils.SSLClient;
import com.sohu.t.api.memcache.JedisClient;
import com.sohu.t.api.model.others.AppleConfig;
import com.sohu.t.api.model.others.PushUser;

public class IOSNewsPushThread extends Thread{
	private static final Log	log	= LogFactory.getLog(IOSNewsPushThread.class);
	private ThreadPoolExecutor	executor;
	private PushUserService		pushUserService;
	private Map<String,String>  pushMap;
	private static final Logger visitorTransformLog = LoggerFactory.getLogger("visitor_transform_log");

	public IOSNewsPushThread(PushUserService service, Map<String,String> map)
	{
		pushUserService = service;
		pushMap = map;
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		executor = new ThreadPoolExecutor(100, 100, 10, TimeUnit.SECONDS, queue);
	}
	
	
	
	@Override
	public void run()
	{
		if(pushMap.size() > 0)
		{
			List<String> tokens = pushUserService.getAllNpAppleConfig();
			if(tokens != null)
			{
				for(String token : tokens)
				{
					IosMessage iosMessage = new IosMessage();
					iosMessage.setAlertMessage(pushMap.get("title") + ":" + pushMap.get("content"));
					iosMessage.setNewspaperUrl(pushMap.get("url"));
					iosMessage.setnId(token);
					//用户转化第一步：向用户发出推送情况统计（包含成功与失败）
//					PushUser pushUser = pushUserService.getPushUserByNid(appleConfig.getNid());
//					if(pushUser != null)
//					{
//						boolean isVisitor = JedisClient.getInstance().existsInSet("visitor_uids", pushUser.getUserId());
//						if(isVisitor)
//						{
//							iosMessage.setAlertMessage("香港，你把大陆人当什么了？说出你的看法参与投票，赢ipad mini大奖");
//							iosMessage.setNewspaperUrl(pushMap.get("url"));
//							iosMessage.setnId(appleConfig.getNid());
//							visitorTransformLog.Debug("<<<Ios-Push-All>>>userid="+pushUser.getUserId()+",token="+appleConfig.getNid());
//						}
//					}
					executor.execute(new PushTask(iosMessage));
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

		private IosMessage	iosMessage;

		public PushTask(IosMessage iosMessage)
		{
			this.iosMessage = iosMessage;
		}

		public void run()
		{
//			IosPushClient.getInstance().sendMessage(iosMessage);
			SSLClient.getInstance().sendMessage(iosMessage);
			log.debug("Push news to ios success,nId=" + iosMessage.getnId() + ",url=" + iosMessage.getNewspaperUrl());
		}
	}
}
