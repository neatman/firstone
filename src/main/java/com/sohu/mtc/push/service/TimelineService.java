package com.sohu.mtc.push.service;

import com.sohu.mtc.push.model.Message;
import com.sohu.mtc.push.model.PushConfig;
import com.sohu.mtc.push.model.Tile;

public interface TimelineService{
	
	/**
	 * 根据用户ID获取Message的相关信息：提到我的、我的评论、粉丝、私信的数量
	 * 
	 * @param message
	 * @param config
	 * @return
	 */
	public Message getUserNotification(Message message, PushConfig config);
	
	public String getNewAtCount(long uid);

	public String getNewCommentCount(long uid);

	public String getNewFansCount(long uid);

	public String getNewPrivateMsgCount(long uid);
	
	/**
	 * 获取某用户5分钟之内最新发表的一个微博
	 * @param uid
	 * @return
	 * @throws RmiException
	 */
	public Tile getNewMicroblog(long uid);
	
}
