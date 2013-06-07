package com.sohu.mtc.push;

import com.sohu.mtc.push.thread.IdleHttpConnMonitorThread;
import com.sohu.mtc.push.thread.MicroblogClientThread;
import com.sohu.mtc.push.thread.SohuNewsClientThread;
import com.sohu.mtc.push.thread.WP7PinUserClientThread;
import com.sohu.mtc.push.utils.BeanFactory;
import com.sohu.mtc.push.utils.HttpClientUtil;

public class PushServer{

	public static void main(String[] args)
	{
		BeanFactory.loadContext();
		MicroblogClientThread microblogClientThread = new MicroblogClientThread();
//		SohuNewsClientThread sohuNewsClientThread = new SohuNewsClientThread();
		WP7PinUserClientThread pinUserPushThread = new WP7PinUserClientThread();
		IdleHttpConnMonitorThread connMonitorThread = new IdleHttpConnMonitorThread(HttpClientUtil.getConnManager());
		microblogClientThread.start();
//		sohuNewsClientThread.start();
		pinUserPushThread.start();
		connMonitorThread.start();
	}
}
