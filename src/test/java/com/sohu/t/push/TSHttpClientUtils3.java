package com.sohu.t.push;

import java.util.HashMap;
import java.util.Map;

import com.sohu.mtc.push.model.IosMessage;
import com.sohu.mtc.push.service.PushUserService;
import com.sohu.mtc.push.strategy.PushStrategyOperator;
import com.sohu.mtc.push.strategy.impl.NewsPushStrategy;
import com.sohu.mtc.push.thread.WP7NewsPushThread;
import com.sohu.mtc.push.utils.BeanFactory;
import com.sohu.mtc.push.utils.SSLClient;
import com.sohu.mtc.push.utils.WindowsPhoneHttpUtil;



public class TSHttpClientUtils3{
	
	public static void main(String[] args)
	{
		IosMessage iosMessage = new IosMessage();
		iosMessage.setAlertMessage("this is title"+":"+"this is content");
		iosMessage.setNewspaperUrl("http://www.baidu.com");
		iosMessage.setnId("7ebf6b3a0c2cb048ba11ec07d4c5f6f5aa7727bf2297d1e21696dd1fe069fe2e");
		SSLClient.getInstance().sendMessage(iosMessage);
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("title", "this is title");
//		map.put("content", "this is content");
//		map.put("nid", "http://db3.notify.live.net/throttledthirdparty/01.00/AAGS92vaeQC3Qpk-6ar1ZMw6AgAAAAADCgAAAAQUZm52OkJCMjg1QTg1QkZDMkUxREQ");
//		WindowsPhoneHttpUtil.sendWP7News(map);
		System.out.println("over....");
	}
	
}
/**
 * 推送处理流程
 * 
 * @author jiakangliang
 */
class PushTask implements Runnable{

	int i;
	public PushTask(int i)
	{
		this.i = i;
		System.out.println(i);
	}
	public void run()
	{
		Map<String,String> map = new HashMap<String,String>();
		map.put("nid", "http://db3.notify.live.net/throttledthirdparty/01.00/AAEnvPkvzWbTQptK0M07wTpJAgAAAAADbQAAAAQUZm52OkJCMjg1QTg1QkZDMkUxREQ");
		map.put("title", "title");
		map.put("content", "content");
		WindowsPhoneHttpUtil.sendWP7News(map);
	}
}