package com.sohu.mtc.push.strategy;

import com.sohu.mtc.push.model.Message;
import com.sohu.mtc.push.model.PushConfig;
import com.sohu.t.api.model.others.PushUser;

public class PushStrategyOperator{

	private PushStrategy pushStrategy;

	public void send(PushUser pushUser, String queueItem, PushConfig config)
	{
		Message message = this.pushStrategy.createMessage(pushUser, queueItem, config);
		if(message != null)
		{
			this.pushStrategy.sendMessage(message);
		}
	}

	public PushStrategy getPushStrategy()
	{
		return pushStrategy;
	}

	public void setPushStrategy(PushStrategy pushStrategy)
	{
		this.pushStrategy = pushStrategy;
	}

}
