package com.sohu.mtc.push.task.schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sohu.mtc.push.service.PushUserService;
import com.sohu.mtc.push.thread.IOSNewsPushThread;
import com.sohu.mtc.push.thread.WP7NewsPushThread;
import com.sohu.mtc.push.utils.MemcacheUtil;
import com.sohu.t.api.util.PushNewsUtil;

public class NewsPush{
	private static final Log	log	= LogFactory.getLog(NewsPush.class);
	private PushUserService		pushUserService;

	public void setPushUserService(PushUserService pushUserService)
	{
		this.pushUserService = pushUserService;
	}
	
	public void pushTerm()
	{
		log.debug("<<<NewsPush-Start-PushTerm>>>");
		try
		{
			Thread.sleep(1000 * 60);
		}
		catch(InterruptedException e1)
		{
			e1.printStackTrace();
		}
		List<Map<String,String>> mapList = PushNewsUtil.getTermInfoList();
		//若未获取正确数据，重试一次
		if(mapList.size() < 12 || !this.isLatestTerm(mapList.get(0).get("timestamp")))
		{
			try
			{
				Thread.sleep(1000 * 60 * 5);
				mapList = PushNewsUtil.getTermInfoList();
			}
			catch(InterruptedException e)
			{
				log.debug("<<<PushTerm Interrupted>>>" + e.getMessage());
			}
		}
		if(mapList.size() < 12 || !this.isLatestTerm(mapList.get(0).get("timestamp")))
		{
			return;
		}
		Map<String,String> map = mapList.get(0);//获取列表中最新一期的早晚报
		log.debug("<<<PushTerm Latest TermId>>>"+map.get("termId"));
		MemcacheUtil memClient = MemcacheUtil.getInstance();
		String termId = (String) memClient.getObjValue("pushed_termId");
		if(termId != null && termId.equals(map.get("termId")))
		{
			memClient.removeValue("push_termsummary_clear_"+termId);
			memClient.removeValue("push_termsummary_"+termId);
			return;
		}
		//缓存termId，防止新闻编辑发布新闻后又撤销重发，则根据termId判断，只推送一次
		memClient.putObjValue("pushed_termId", map.get("termId"), MemcacheUtil.DAY_TTL);
		map.put("content", PushNewsUtil.getLatestNewsTitle(map.get("termId")));
		memClient.putObjValue("push_termlist", mapList, MemcacheUtil.DAY_TTL);
		log.debug("Cache termlist success,timestamp=" + map.get("timestamp"));

		try
		{
			new IOSNewsPushThread(pushUserService,map).start();
			new WP7NewsPushThread(pushUserService,map).start();
		}
		catch(Exception e)
		{
			log.debug("push term error:" + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断最新一期的早晚报是否与当前时间吻合（1.是否是今天的 2.是否和当前时间所处的上午或下午一致）
	 * @param timestamp
	 * @return
	 */
	public boolean isLatestTerm(String timestamp)
	{
		boolean isLatest = false;
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		try
		{
			Date termDate = df.parse(timestamp);
			Date currdate = new Date();
			df = new SimpleDateFormat("yyyyMMdda");
			if(df.format(termDate).equals(df.format(currdate)))
			{
				isLatest = true;
			}
		}
		catch(ParseException e)
		{
			log.debug(e.getMessage());
			e.printStackTrace();
		}
		return isLatest;
	}
	
	
//	public void scheduledPush()
//	{
//		long accessTime = System.currentTimeMillis();
//		while(true)
//		{
//			// 如果任务执行超过两小时，则不再执行此任务
//			long spanTime = System.currentTimeMillis() - accessTime;
//			if(spanTime > 1000 * 60 * 60 * 2)
//			{
//				log.debug("<<<execute scheduledPush method over two hour>>>");
//				return;
//			}
//			// 如果请求不到新闻数据或请求的数据不完整，等20分钟再请求
//			List<Map<String,String>> mapList = PushNewsUtil.getTermInfoList();
//			if(mapList.size() < 12)
//			{
//				try
//				{
//					Thread.sleep(1000 * 60 * 20);
//					continue;
//				}
//				catch(InterruptedException e)
//				{
//					log.debug(e.getMessage());
//					continue;
//				}
//			}
//			Map<String,String> map = mapList.get(0);//获取列表中最新一期的早晚报
//			if(!this.isLatestTerm(map.get("timestamp")))
//			{
//				try
//				{
//					Thread.sleep(1000 * 60 * 20);
//					continue;
//				}
//				catch(InterruptedException e)
//				{
//					log.debug(e.getMessage());
//					continue;
//				}
//			}
//			MemcacheUtil.getInstance().putObjValue("push_termlist", mapList, MemcacheUtil.DAY_TTL);
//			log.debug("Scheduled cache termlist success,timestamp=" + map.get("timestamp") + ",title=" +map.get("title"));
//
//			try
//			{
//				new IOSNewsPushThread(pushUserService,map).start();
//				new WP7NewsPushThread(pushUserService,map).start();
//			}
//			catch(Exception e)
//			{
//				log.debug("Scheduled push news error:" + e);
//				e.printStackTrace();
//			}
//			break;
//		}
//	}
	
}
