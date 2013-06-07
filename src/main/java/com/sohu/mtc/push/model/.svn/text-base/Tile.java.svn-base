package com.sohu.mtc.push.model;

import java.io.Serializable;



public class Tile implements Comparable<Tile>,Serializable{

	private static final long	serialVersionUID	= 5794021790169013354L;
	private int		type;		// 1:at 2:comment 3:privateMail 4:microBlog
	private String	icon;
	private String	userName;
	private String	content;
	private long	createdTime;
	
	
	public int compareTo(Tile o)
	{
		if(o.getCreatedTime() > this.getCreatedTime())
		{
			return 1;
		}
		else if(o.getCreatedTime() < this.getCreatedTime())
		{
			return -1;
		}
		return 0;
	}
	
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public String getIcon()
	{
		return icon;
	}
	public void setIcon(String icon)
	{
		this.icon = icon;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public long getCreatedTime()
	{
		return createdTime;
	}
	public void setCreatedTime(long createdTime)
	{
		this.createdTime = createdTime;
	}
	
}
