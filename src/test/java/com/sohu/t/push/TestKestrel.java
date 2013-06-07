package com.sohu.t.push;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.sohu.mtc.push.utils.MemcacheUtil;

import net.rubyeye.xmemcached.exception.MemcachedException;

public class TestKestrel {
	// static MemcachedClient client = KestrelQueueFactory.getInstance();
	static String notificationId = "2T3xPsBs+MKLyx6UxwoFnIRc7Cxm9iQU7oWKb52b3W9zlayfMT7YfXLmMKJ2ZDj3+Epda8HVZe7omBfXCamMsN27cQrhl8D0U1KQ5WrQnXr31YqUbHJbIOkv0UXo9ZPc";
	static SimpleDateFormat sf = new SimpleDateFormat("yyyy MM DD");

	/**
	 * @param args
	 * @throws MemcachedException
	 * @throws InterruptedException
	 * @throws TimeoutException
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception
	{
//		MemcacheUtil.getInstance().putObjValue("blockNames", "微闻联播,名人观点,囧事段子,狐说八道,加油雅安", 0);
//		System.out.println("ok~~~");
//		MemcacheUtil.getInstance().removeValue("push_termsummary_26161");
//		MemcacheUtil.getInstance().removeValue("push_termsummary_clear_33326");
//		MemcacheUtil.getInstance().removeValue("expression_iphone22099123124");
//		MemcacheUtil.getInstance().putObjValue("ttest", null, 0);
		Object obj = MemcacheUtil.getInstance().getObjValue("push_termlist_new");
//		MemcacheUtil.getInstance().putObjValue("push_termlist", obj, 0);
//		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("C:\\Users\\jiakangliang\\Desktop\\termList.txt"));
//		oos.writeObject(obj);
//		oos.flush();
//		oos.close();
//		ObjectInputStream ois = new ObjectInputStream(TestKestrel.class.getResourceAsStream("/termList.txt"));
//		List<Map<String,String>> obj = (List<Map<String, String>>) ois.readObject();
//		ois.close();
		System.out.println("ok~~"+obj);
//		System.out.println("ok"+PushNewsUtil.getLatestNewsTitle("62970"));
//		MemcacheUtil.getInstance().removeValue("config_2b770e50e43d392e75120f205b0f94de2864fb568c31934fd1f8c7d56c35969e");
//		MemcacheUtil.getInstance().removeValue("config_8ffbac4313c3f500a59d11dc5fa42f543663f05cc8762aaace153c3d64f60aaf");
//		Object obj = MemcacheUtil.getInstance().getObjValue("config_8ffbac4313c3f500a59d11dc5fa42f543663f05cc8762aaace153c3d64f60aaf");
//		try
//		{
////			
//			MemcachedClient kestrelClient = KestrelQueueUtil.getInstance(Constants.KESHOST_MicroBlog);
//////			String reg = "user_prefix^374763068^iphone^2b770e50e43d392e75120f205b0f94de2864fb568c31934fd1f8c7d56c35969e";
//			String pus = "{uid:374763068,mt:0,cm:0,fd:1,dm:0}";
//////			for(int i=0;i<50;i++)
//////			{
//				kestrelClient.set(Constants.QUEUE_NAME, KestrelQueueUtil.QUEUE_TTL, pus);
//////				Object obj = kestrelClient.get(Constants.QUEUE_NAME);
//				System.out.println("ok");
//////			}
////			while(true)
////			{
////				Object obj = kestrelClient.get(Constants.QUEUE_NAME);
////				if(obj != null)
////				{
////					System.out.println("ok-->"+obj);
////				}
////			}
//		}
//		catch(Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

//	public static void getMessage() throws TimeoutException, InterruptedException, MemcachedException {
//		for (int i = 0; i < 1000; i++) {
//			Object object = KestrelQueueFactory.getInstance(Constants.KESHOST_Local).get(Constants.QUEUE_NAME);
//
//			System.out.println(">>>>>>>" + object);
//		}
//	}
//
//	public static void sendMessage() throws TimeoutException, InterruptedException, MemcachedException, IOException {
//		PushUser user = new PushUser();
//
//		// user.setClientId("android");
//		// user.setnId(notificationId);
//		// user.setUserId("dongyansheng1@gmail.com");
//		// user.setLastLoginDate(sf.format(new Date()));
//		// user.setCreateDate(sf.format(new Date()));
//		// KestrelQueueFactory.getInstance().set(Constants.QUEUE_NAME, 0, user);
//		for (int i = 0; i < 10; i++) {
//			String json = "{uid:22758064,mt:@me,cm:aaa,fd:zhang,dm:hello 22758064}";
//			KestrelQueueFactory.getInstance(Constants.KESHOST_Local).set(Constants.QUEUE_NAME, 0, json);
//
//		}
//	}

	public static List<String> testJdbc()
	{
		List<String> nidList = new ArrayList<String>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.106.129:3306/api","mysql","123");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from tmp_nid_table");
			while(rs.next())
			{
				String nid = rs.getString(1);
				nidList.add(nid);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(conn != null)
				{
					conn.close();
				}
				if(stmt != null)
				{
					stmt.close();
				}
				if(rs != null)
				{
					rs.close();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return nidList;
	}
	
}
