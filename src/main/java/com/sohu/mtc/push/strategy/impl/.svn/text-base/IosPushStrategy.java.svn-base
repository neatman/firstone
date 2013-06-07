package com.sohu.mtc.push.strategy.impl;

import org.json.JSONObject;

import com.sohu.mtc.push.model.IosMessage;
import com.sohu.mtc.push.model.Message;
import com.sohu.mtc.push.model.PushConfig;
import com.sohu.mtc.push.strategy.PushStrategy;
import com.sohu.mtc.push.utils.SSLClient;
import com.sohu.t.api.model.others.PushUser;

public class IosPushStrategy extends PushStrategy{
	
	@Override
	public Message createMessage(PushUser pushUser, String queueItem, PushConfig config)
	{
		Message message = null;
		try
		{
			JSONObject jsonObject = new JSONObject(queueItem);
			//config为null时，为数据库无相关推送配置信息的情况，这种情况下四种类别的新消息数全部推送
			if(!this.isSendTrue(jsonObject, config))
			{
				return null;
			}
			message = new IosMessage();
			fillMessage(message, pushUser, config);
		}
		catch(Exception e)
		{
			log.debug("create message for apple device error: " + e);
			e.printStackTrace();
		}
		return message;
	}

	@Override
	public void sendMessage(Message message)
	{
		if(message.getnId() == null)
		{
			log.debug("Apple device nid is null!!!");
			return;
		}
		if(message.getnId() != null && message.getnId().trim().length() !=64)
		{
			log.debug("Apple device nid's length is not 64!!!");
			return;
		}
		IosMessage iosMessage = (IosMessage) message;
		if(iosMessage.getMessageCount() != 0)
		{
//			IosPushClient.getInstance().sendMessage(iosMessage);
			SSLClient.getInstance().sendMessage(iosMessage);
		}
	}
	
	/**
	 * 从队列中取出的json串中，包含四个变量：提到我的（mt）、我的评论（cm）、粉丝（fd）、私信（dm）
	 * 这四个变量只有一个值大于0
	 * 所以当某变量大于0时，同时当苹果配置信息中指定不推送此变量时，返回false，最终结果将不推送此Message
	 * @param jsonObject
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public boolean isSendTrue(JSONObject jsonObject, PushConfig config) throws Exception
	{
		//如果config为null，则说明是老版本iphone客户端用户配置信息，也就是说从未修改过推送配置信息，则也将不会在数据库添加一条配置信息记录
		//此时我们看作所有新消息推送配置全部打开
		if(config != null)
		{
			/**
			 * 消息里面应该只有一个是大于零的数
			 */
			if(Integer.parseInt(jsonObject.get("mt").toString()) > 0 && Integer.parseInt(config.getMt()) == 0)
			{
				return false;
			}
			if(Integer.parseInt(jsonObject.get("cm").toString()) > 0 && Integer.parseInt(config.getCm()) == 0)
			{
				return false;
			}
			if(Integer.parseInt(jsonObject.get("dm").toString()) > 0 && Integer.parseInt(config.getDm()) == 0)
			{
				return false;
			}
			if(Integer.parseInt(jsonObject.get("fd").toString()) > 0 && Integer.parseInt(config.getFd()) == 0)
			{
				return false;
			}
		}
		return true;
	}

}
