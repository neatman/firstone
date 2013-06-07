package com.sohu.mtc.push.model;

public class WindowsPhoneMessage extends Message{

	private static final long	serialVersionUID	= -3833130980829776475L;

	/**
	 * 获得WindowsPhone7的新消息数，包括新at我的和我的新评论
	 */
	public int getWP7MessageCount()
	{
		if(super.getNewAtCount() == null)
		{
			super.setNewAtCount("0");
		}
		if(super.getNewCommentCount() == null)
		{
			super.setNewCommentCount("0");
		}
		return Integer.parseInt(super.getNewAtCount()) + Integer.parseInt(super.getNewCommentCount());

	}
}
