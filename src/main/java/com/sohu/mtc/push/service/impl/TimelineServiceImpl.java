package com.sohu.mtc.push.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinaren.twitter.dto.Msg;
import com.sohu.mtc.push.model.Message;
import com.sohu.mtc.push.model.PushConfig;
import com.sohu.mtc.push.model.Tile;
import com.sohu.mtc.push.service.TimelineService;
import com.sohu.twitter.msg.service.MsgService;
import com.sohu.twitter.timeline.service.MyTimelineService;
import com.sohu.twitter.user.service.UserNumService;
import com.sohu.twitter.util.tool.bin.BinUtil;

public class TimelineServiceImpl implements TimelineService{

	private static final Log	log	= LogFactory.getLog(TimelineServiceImpl.class);
	private UserNumService 		userNumService;
	private MyTimelineService 	myTimelineService;
	private MsgService 			msgService;

	@Override
	public Message getUserNotification(Message message, PushConfig config)
	{
		try
		{
			long userId = Long.parseLong(message.getUserId());
			String commnum = this.getNewCommentCount(userId);
			String atnum = this.getNewAtCount(userId);
			String mailnum = this.getNewPrivateMsgCount(userId);
			String fans = this.getNewFansCount(userId);
			if(config != null)
			{
				message.setNewAtCount(config.getMt().equals("1") ? atnum : "0");
				message.setNewCommentCount(config.getCm().equals("1") ? commnum : "0");
				message.setNewPrivateMsgCount(config.getDm().equals("1") ? mailnum : "0");
				message.setNewFansCount(config.getFd().equals("1") ? fans : "0");
			}
			else
			{
				message.setNewAtCount(atnum);
				message.setNewCommentCount(commnum);
				message.setNewPrivateMsgCount(mailnum);
				message.setNewFansCount(fans);
			}
		}
		catch(Exception e)
		{
			log.debug("getUserNotification error: " + e);
			e.printStackTrace();
			message = null;
		}
		return message;
	}
	
	public Tile getNewMicroblog(long uid)
	{
		Tile newMicroblog = null;
		try
		{
			byte[] bytes = myTimelineService.get(uid, 0, 0, -1, 10);
			List<Long> idList = BinUtil.bytesToLongList(bytes);
			List<Msg> msgList = msgService.loadMulti(idList);
			Msg msg = null;
			if(msgList != null && msgList.size() > 0)
			{
				msg = msgList.get(0);				
			}
			if(msg != null)
			{
				//由于是每隔5分钟调执行一次pin用户的推送，所以与当前时间间隔小于5分钟的微博一定是最新微博
				long timeInterval = System.currentTimeMillis() - msg.getCreatedOn().getTime();
				if(timeInterval < 1000 * 60 * 5)
				{
					newMicroblog = new Tile();
					// 只截取微博文字内容的前50个字符
					String content = msg.getBodyRaw().length() > 50 ? msg.getBodyRaw().substring(0, 50) : msg.getBodyRaw();
					newMicroblog.setUserName(msg.getUsrUsername());
					newMicroblog.setContent(content);
					newMicroblog.setCreatedTime(msg.getCreatedOn().getTime());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.debug("getNewMicroblog error:"+e);
		}
		return newMicroblog;
	}

	public String getNewAtCount(long uid)
	{
		try
		{
			return String.valueOf(userNumService.getNewAtMeNum(uid));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.debug("getNewAtCount error:"+e);
			return "0";
		}
	}

	public String getNewCommentCount(long uid)
	{
		try
		{
			return String.valueOf(userNumService.getNewReplyMeNum(uid));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.debug("getNewCommentCount error:"+e);
			return "0";
		}
	}

	public String getNewFansCount(long uid)
	{
		try
		{
			return String.valueOf(userNumService.getNewFollowMeNum(uid));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.debug("getNewFansCount error:"+e);
			return "0";
		}
	}

	public String getNewPrivateMsgCount(long uid)
	{
		try
		{
			return String.valueOf(userNumService.getNewPriMailMeNum(uid));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.debug("getNewPrivateMsgCount error:"+e);
			return "0";
		}
	}

	public void setUserNumService(UserNumService userNumService)
	{
		this.userNumService = userNumService;
	}

	public void setMyTimelineService(MyTimelineService myTimelineService)
	{
		this.myTimelineService = myTimelineService;
	}

	public void setMsgService(MsgService msgService)
	{
		this.msgService = msgService;
	}

}
