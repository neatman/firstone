package com.sohu.mtc.push.model;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long	serialVersionUID	= -1554130485749071892L;

	// 客户度类型,如iphone,Symbian等
	private String				clientId;
	private String				userId;
	private String				nId;
	private String				imei;
	// 新@数
	private String				newAtCount;
	// 新评论数
	private String				newCommentCount;
	// 新粉丝数
	private String				newFansCount;
	// 新私信数
	private String				newPrivateMsgCount;
	private String				content;

	/**
	 * 获得新消息数，包括新at我的，新粉我的，新私信，我的新评论
	 */
	public int getMessageCount()
	{
		if(this.getNewAtCount() == null) this.setNewAtCount("0");
		if(this.getNewCommentCount() == null) this.setNewCommentCount("0"); 
		if(this.getNewFansCount() == null) this.setNewFansCount("0"); 
		if(this.getNewPrivateMsgCount() == null) this.setNewPrivateMsgCount("0"); 
		
		return Integer.parseInt(this.getNewAtCount()) + Integer.parseInt(this.getNewCommentCount()) 
				+ Integer.parseInt(this.getNewFansCount()) + Integer.parseInt(this.getNewPrivateMsgCount());
	}

	public String getClientId()
	{
		return clientId;
	}

	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getnId()
	{
		return nId;
	}

	public void setnId(String nId)
	{
		this.nId = nId;
	}

	public String getImei()
	{
		return imei;
	}

	public void setImei(String imei)
	{
		this.imei = imei;
	}

	public String getNewAtCount()
	{
		return newAtCount;
	}

	public void setNewAtCount(String newAtCount)
	{
		this.newAtCount = newAtCount;
	}

	public String getNewCommentCount()
	{
		return newCommentCount;
	}

	public void setNewCommentCount(String newCommentCount)
	{
		this.newCommentCount = newCommentCount;
	}

	public String getNewFansCount()
	{
		return newFansCount;
	}

	public void setNewFansCount(String newFansCount)
	{
		this.newFansCount = newFansCount;
	}

	public String getNewPrivateMsgCount()
	{
		return newPrivateMsgCount;
	}

	public void setNewPrivateMsgCount(String newPrivateMsgCount)
	{
		this.newPrivateMsgCount = newPrivateMsgCount;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

}
