package com.sohu.mtc.push.strategy.impl;

import org.json.JSONObject;

import com.sohu.mtc.push.model.Message;
import com.sohu.mtc.push.model.PushConfig;
import com.sohu.mtc.push.model.SymbianMessage;
import com.sohu.mtc.push.strategy.PushStrategy;
import com.sohu.mtc.push.utils.Constants;
import com.sohu.mtc.push.utils.SymbianHttpUtil;
import com.sohu.t.api.model.others.PushUser;

public class SymbianPushStrategy extends PushStrategy{

	public Message createMessage(PushUser pushUser, String queueItem, PushConfig config)
	{
		SymbianMessage message = null;
		try
		{
			JSONObject jsonObject = new JSONObject(queueItem);
			// Symbian平台只接收私信的PUSH信息
			if(Integer.parseInt(jsonObject.get("dm").toString()) == 0)
			{
				return null;
			}
			message = new SymbianMessage();
			message.setServiceId(Constants.SERVICE_ID);
			message.setServiceSecret(Constants.SERVICE_SECRET);
			fillMessage(message, pushUser, null);
		}
		catch(Exception e)
		{
			log.debug("create message for nokia device error: " + e);
			e.printStackTrace();
		}
		return message;
	}
	
	public void sendMessage(Message message)
	{
		SymbianMessage symbianMessage = (SymbianMessage) message;
		SymbianHttpUtil.getInstance().postMessage(symbianMessage);
	}

}
