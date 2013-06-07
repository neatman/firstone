package com.sohu.mtc.push.model;

import org.json.JSONException;
import org.json.JSONObject;

public class IosMessage extends Message{

	private static final long	serialVersionUID	= 6863697454150460515L;
	private String				alertMessage;
	private String				newspaperUrl;
	
	public String getContent()
	{
		JSONObject json = new JSONObject();
		try
		{
			json.put("fs", this.getNewFansCount());
			json.put("tm", this.getNewAtCount());
			json.put("pm", this.getNewPrivateMsgCount());
			json.put("rm", this.getNewCommentCount());
		}
		catch(JSONException e)
		{
			e.printStackTrace();
		}
		return json.toString();
	}

	public String getAlertMessage()
	{
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage)
	{
		this.alertMessage = alertMessage;
	}

	public String getNewspaperUrl()
	{
		return newspaperUrl;
	}

	public void setNewspaperUrl(String newspaperUrl)
	{
		this.newspaperUrl = newspaperUrl;
	}

}
