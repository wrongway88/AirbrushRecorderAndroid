package com.example.airbrushrecorder;

import com.example.airbrushrecorder.data.FlightsDataSource;

import android.app.Activity;
import android.util.Log;

public class LoginHelper
{
	private static String TAG = "loginHelper";
	
	public void setLoginData(Activity activity, String mail, String password)
	{
		String ip = WebInterface.getIPAddress(false);
		
		FlightsDataSource dataSource = new FlightsDataSource(activity);
		dataSource.open();
		dataSource.updateUserMail(mail);
		dataSource.updatePassword(password);
		dataSource.updateIp(ip);
		dataSource.close();
	}
	
	public boolean login(Activity activity)
	{
		try
		{
			FlightsDataSource dataSource = new FlightsDataSource(activity);
			dataSource.open();
			
			String mail = dataSource.getUserName();
			String password = dataSource.getPassWord();
			
			if(mail.length() == 0 || password.length() == 0)
			{
				Log.e(TAG, "mail or password is not set");
				return false;
			}
			
			WebInterface webInterface = new WebInterface();
			int userId = webInterface.requestUserId(mail);
			
			String sessionData = webInterface.login(userId,  password);
			dataSource.updateCookie(sessionData);
			
			dataSource.close();
			
			if(sessionData.length() > 0)
				return false;
			else
				return true;
		}
		catch(Exception e)
		{
			Log.e(TAG, e.toString());
		}
		
		return false;
	}
	
	public boolean ipChanged(Activity activity)
	{
		String ip = WebInterface.getIPAddress(false);
		
		FlightsDataSource dataSource = new FlightsDataSource(activity);
		dataSource.open();
		String oldIp = dataSource.getIp();
		dataSource.close();
		
		return (ip != oldIp);
	}
	
	public String getSessionData(Activity activity)
	{
		String result = "";
		
		FlightsDataSource dataSource = new FlightsDataSource(activity);
		dataSource.open();
		result = dataSource.getCookie();
		dataSource.close();
		
		return result;
	}
}
