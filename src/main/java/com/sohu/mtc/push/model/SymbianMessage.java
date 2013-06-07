package com.sohu.mtc.push.model;

public class SymbianMessage extends Message{

	private static final long	serialVersionUID	= -7028676719395002320L;
	public static final String	host[]				= { "nnapi.ovi.com", "nnapi.ovi.com.cn" };
	public String				serviceId;
	public String				serviceSecret;
	private int					pushTimes;

	public int getPushTimes()
	{
		return pushTimes;
	}

	public void setPushTimes(int pushTimes)
	{
		this.pushTimes = pushTimes;
	}

	public String getServiceId()
	{
		return serviceId;
	}

	public void setServiceId(String serviceId)
	{
		this.serviceId = serviceId;
	}

	public String getServiceSecret()
	{
		return serviceSecret;
	}

	public void setServiceSecret(String serviceSecret)
	{
		this.serviceSecret = serviceSecret;
	}

}
