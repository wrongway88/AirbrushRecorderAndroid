package com.example.airbrushrecorder;

import com.example.airbrushrecorder.data.FlightsDataSource;

import android.app.Activity;
import android.util.Log;

public class LoginHelper
{
	private static String TAG = "loginHelper";
	
	public void setLoginData(Activity activity, String mail, String password)
	{
		FlightsDataSource dataSource = new FlightsDataSource(activity);
		dataSource.open();
		dataSource.updateUserMail(mail);
		dataSource.updatePassword(password);
		dataSource.close();
	}
	
	public void login(Activity activity)
	{
		try
		{
			FlightsDataSource dataSource = new FlightsDataSource(activity);
			
			String mail = dataSource.getUserName();
			String password = dataSource.getPassWord();
			
			if(mail.length() == 0 || password.length() == 0)
			{
				Log.e(TAG, "mail or password is not set");
				return;
			}
			
			WebInterface webInterface = new WebInterface();
			int userId = webInterface.requestUserId(mail);
			
			String sessionData = webInterface.login(userId,  password);
			dataSource.updateCookie(sessionData);
		}
		catch(Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}
}
