package com.sohu.mtc.push.task.schedule;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sohu.mtc.logser.Logger;
import com.sohu.mtc.logser.LoggerFactory;
import com.sohu.mtc.push.ssl.FeedbackServiceManager;
import com.sohu.mtc.push.ssl.SSLConnectionHelper;
import com.sohu.mtc.push.ssl.data.Device;
import com.sohu.mtc.push.utils.Constants;
import com.sohu.mtc.push.utils.MemcacheUtil;
import com.sohu.sns.dal.dao.exception.DaoException;
import com.sohu.t.api.memcache.JedisClient;
import com.sohu.t.api.model.others.PushUser;
import com.sohu.t.api.service.ApiIntegratedService;

public class IphonePushClear{

	private static final Log		log				= LogFactory.getLog(IphonePushClear.class);
	private static final String		TARGETURL		= "feedback.push.apple.com";
	private static final int		PORT			= 2196;
	private static final String		CER_PATH		= "src/main/config/2012_07_10.p12";
	private static final String		PASSWORD		= "123456";
	private ApiIntegratedService	userService;
	private static final Logger visitorTransformLog = LoggerFactory.getLogger("visitor_transform_log");

	/**
	 * 删除不能push的苹果设备
	 */
	public void clearAppleDevices()
	{
		try
		{
			log.debug("<<<APNs-Feedback-Service-Invocation>>>");
			FeedbackServiceManager feedbackManager = FeedbackServiceManager.getInstance();
			LinkedList<Device> devices = feedbackManager.getDevices(TARGETURL, PORT, CER_PATH, PASSWORD, SSLConnectionHelper.KEYSTORE_TYPE_PKCS12);
			ListIterator<Device> itr = devices.listIterator();
			while(itr.hasNext())
			{
				Device device = itr.next();
				this.removePushUser(device.getToken());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("<<<Error get feedback nid list>>>" + e.getMessage());
		}
	}

	/**
	 * 删除缓存和数据库中的PushUser
	 * 
	 * @param nId
	 */
	@SuppressWarnings("unchecked")
	private void removePushUser(String nId)
	{
		try
		{
			PushUser pushUser = userService.getPushUserByNID(nId);
			if(pushUser == null)
			{
				log.debug("<<<Nid has already been removed>>>");
				return;
			}
			//游客用户转化第二步：推送失败情况统计
//			boolean isVisitor = JedisClient.getInstance().existsInSet("visitor_uids", pushUser.getUserId());
//			if(isVisitor)
//			{
//				visitorTransformLog.Debug("<<<Ios-Push-Failure>>>userid="+pushUser.getUserId()+",token="+nId);
//			}
			Object obj = MemcacheUtil.getInstance().getObjValue(Constants.KEY_PUSH + pushUser.getUserId());
			if(obj instanceof List)
			{
				List<PushUser> pus = (List<PushUser>) obj;
				Iterator<PushUser> iterator = pus.iterator();
				while(iterator.hasNext())
				{
					PushUser pu = iterator.next();
					if(nId.equals(pu.getnId()))
					{
						iterator.remove();
						break;
					}
				}
				MemcacheUtil.getInstance().putObjValue(Constants.KEY_PUSH + pushUser.getUserId(), pus, MemcacheUtil.DAY_TTL);
			}
			userService.deletePushUser(nId);
			log.debug("<<<PushUser is removed successfully both from mysql and memcached>>>");
		}
		catch(DaoException e)
		{
			e.printStackTrace();
			log.error("<<<Removed pushUser error from mysql>>>Nid=" + nId);
		}
	}

	public void setUserService(ApiIntegratedService userService)
	{
		this.userService = userService;
	}

}
