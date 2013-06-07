package com.sohu.mtc.push.strategy.impl;

import org.json.JSONObject;

import com.sohu.mtc.push.model.Message;
import com.sohu.mtc.push.model.PushConfig;
import com.sohu.mtc.push.model.WindowsPhoneMessage;
import com.sohu.mtc.push.strategy.PushStrategy;
import com.sohu.mtc.push.utils.WindowsPhoneHttpUtil;
import com.sohu.t.api.model.others.PushUser;

public class Wp7PushStrategy extends PushStrategy{

	@Override
	public Message createMessage(PushUser pushUser, String queueItem, PushConfig config)
	{
		Message message = null;
		try
		{
			JSONObject jsonObject = new JSONObject(queueItem);
			if(!this.isSendTrue(jsonObject, config))
			{
				return null;
			}
			message = new WindowsPhoneMessage();
			fillMessage(message, pushUser, config);
		}
		catch(Exception e)
		{
			log.debug("create message for windowsphone device error: " + e);
			e.printStackTrace();
		}
		return message;
	}

	@Override
	public void sendMessage(Message message)
	{
		WindowsPhoneMessage windowsPhoneMessage = (WindowsPhoneMessage) message;
		WindowsPhoneHttpUtil.sendWindowsPhone7Message(windowsPhoneMessage);
	}
	
	/**
	 * 从队列中取出的json串中，包含四个变量：提到我的（mt）、我的评论（cm）、粉丝（fd）、私信（dm）
	 * 这四个变量只有一个值大于0
	 * 所以当某变量大于0时，同时当WindowsPhone配置信息中指定不推送此变量时，返回false，最终结果将不推送此Message
	 * 配置信息中0表示开 1表示关
	 * @param jsonObject
	 * @param config
	 * @return
	 * @throws Exception
	 */
	public boolean isSendTrue(JSONObject jsonObject, PushConfig config) throws Exception
	{
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
