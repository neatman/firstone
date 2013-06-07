//package com.sohu.mtc.push.task.schedule;
//
//import java.util.Iterator;
//import java.util.List;
//
//import javapns.Push;
//import javapns.devices.Device;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import com.sohu.mtc.push.utils.Constants;
//import com.sohu.mtc.push.utils.MemcacheUtil;
//import com.sohu.sns.dal.dao.exception.DaoException;
//import com.sohu.t.api.model.others.PushUser;
//import com.sohu.t.api.service.ApiIntegratedService;
//
//public class IosPushClearHelper{
//
//	private static final Log		log				= LogFactory.getLog(IosPushClearHelper.class);
//	private ApiIntegratedService	userService;
//
//	/**
//	 * 删除不能push的苹果设备
//	 */
//	public void clearAppleDevices()
//	{
//		try
//		{
//			log.debug("<<<APNs-Feedback-Service-Invocation>>>");
//			List<Device> inactiveDevices = Push.feedback(Constants.CER_PATH, Constants.PASSWORD, true);
//			Iterator<Device> iterator = inactiveDevices.iterator();
//			while(iterator.hasNext())
//			{
//				Device device = iterator.next();
//				this.removeDevice(device.getToken());
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//			log.error("<<<Error get feedback nid list>>>" + e.getMessage());
//		}
//	}
//	
//	/**
//	 * 删除过期用户处理流程,删除缓存和数据库中的PushUser
//	 * @param nId
//	 */
//	@SuppressWarnings("unchecked")
//	private void removeDevice(String nId)
//	{
//		try
//		{
//			PushUser pushUser = userService.getPushUserByNID(nId);
//			if(pushUser == null)
//			{
//				log.debug("<<<Nid has already been removed>>>");
//				return;
//			}
//			Object obj = MemcacheUtil.getInstance().getObjValue(Constants.KEY_PUSH + pushUser.getUserId());
//			if(obj instanceof List)
//			{
//				List<PushUser> pus = (List<PushUser>) obj;
//				Iterator<PushUser> iterator = pus.iterator();
//				while(iterator.hasNext())
//				{
//					PushUser pu = iterator.next();
//					if(nId.equals(pu.getnId()))
//					{
//						iterator.remove();
//						break;
//					}
//				}
//				MemcacheUtil.getInstance().putObjValue(Constants.KEY_PUSH + pushUser.getUserId(), pus, MemcacheUtil.DAY_TTL);
//			}
//			userService.deletePushUser(nId);
//			//删除相应设备配置信息
//			MemcacheUtil.getInstance().removeValue(Constants.KEY_CONFIG + nId);
//			userService.deleteAppaleConfig(nId);
//			log.debug("<<<PushUser is removed successfully both from mysql and memcached>>>Nid=" + nId);
//		}
//		catch(DaoException e)
//		{
//			e.printStackTrace();
//			log.error("<<<Removed pushUser error from mysql>>>Nid=" + nId);
//		}
//	}
//
//	public void setUserService(ApiIntegratedService userService)
//	{
//		this.userService = userService;
//	}
//
//}
