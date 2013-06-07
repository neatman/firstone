package com.sohu.mtc.push.utils;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BeanFactory {

	private static ClassPathXmlApplicationContext context;
	private static final Object synclock = new Object();
	private static BeanFactory beanFactory;

	private BeanFactory() {
		context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });
	}

	public static void loadContext() {
		if(beanFactory == null)
		{
			synchronized (synclock) {
				if (beanFactory == null)
					beanFactory = new BeanFactory();
			}
		}
	}

	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

}
