package com.sohu.mtc.push.strategy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;

import com.sohu.mtc.push.model.Message;
import com.sohu.mtc.push.model.PushConfig;
import com.sohu.mtc.push.service.TimelineService;
import com.sohu.mtc.push.utils.BeanFactory;
import com.sohu.t.api.model.others.PushUser;

public abstract class PushStrategy{
	
	protected static final Log log = LogFactory.getLog(PushStrategy.class);
	
	/**
	 * 将相关信息，如平台类型，设备nid，用户ID等设置到Message对象中去。
	 * 
	 * @param jsonObject
	 * @param message
	 * @param user
	 * @param config
	 * @return
	 * @throws JSONException
	 */
	public Message fillMessage(Message message, PushUser user, PushConfig config)
	{
		message.setClientId(user.getClientId());
		message.setnId(user.getnId());
		message.setUserId(user.getUserId());
		try
		{
			TimelineService timelineService = (TimelineService) BeanFactory.getBean("timelineService");
			message = timelineService.getUserNotification(message, config);
		}
		catch(Exception e)
		{
			log.debug("get user notification timeline error," + e);
			e.printStackTrace();
		}
		return message;
	}
	
	/**
	 * 创建消息集合
	 * @param pushUser
	 * @param queueItem
	 * @param config
	 * @return
	 */
	public abstract Message createMessage(PushUser pushUser, String queueItem, PushConfig config);

	/**
	 * 发送PUSH消息
	 * 
	 * @return
	 */
	public abstract void sendMessage(Message message);
}
