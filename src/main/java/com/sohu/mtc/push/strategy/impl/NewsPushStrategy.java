package com.sohu.mtc.push.strategy.impl;

import org.json.JSONObject;

import com.sohu.mtc.push.model.Message;
import com.sohu.mtc.push.model.PushConfig;
import com.sohu.mtc.push.model.SymbianMessage;
import com.sohu.mtc.push.strategy.PushStrategy;
import com.sohu.mtc.push.utils.SymbianHttpUtil;
import com.sohu.t.api.model.others.PushUser;

public class NewsPushStrategy extends PushStrategy{

	@Override
	public Message createMessage(PushUser pushUser, String queueItem, PushConfig config)
	{
		Message message = null;
		try
		{
			JSONObject jsonObject = new JSONObject(queueItem);
			message = createPushMessage(jsonObject);
		}
		catch(Exception e)
		{
			log.debug("handle>>>"+queueItem+">>>error: " + e);
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 当新闻部门使用我们的PUSH功能的时候，直接调用此方法生成PushMessage，此功能只是针对Symbian平台。
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public Message createPushMessage(JSONObject jsonObject) throws Exception
	{
		SymbianMessage message = new SymbianMessage();
		message.setServiceId(jsonObject.getString("service_id"));
		message.setServiceSecret(jsonObject.getString("service_secret"));
		message.setContent(jsonObject.getString("data"));
		message.setnId(jsonObject.getString("nid"));
		message.setClientId(jsonObject.getString("platformType"));
		return message;

	}
	
	@Override
	public void sendMessage(Message message)
	{
		SymbianMessage symbianMessage = (SymbianMessage) message;
		SymbianHttpUtil.getInstance().postMessage(symbianMessage);
	}

}
