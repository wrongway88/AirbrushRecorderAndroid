package com.example.airbrushrecorder;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.airbrushrecorder.data.FlightsDataSource;
import com.example.airbrushrecorder.dialog.DialogEnterLoginData;
import com.example.airbrushrecorder.dialog.DialogWifiOff;

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
	
	public boolean login(FragmentActivity activity)
	{
		try
		{
			if(WebInterface.wifiAvailable(activity) == false)
			{
				DialogWifiOff dialog = new DialogWifiOff();
				dialog.show(activity.getSupportFragmentManager(), TAG);
				return false;
			}
			
			FlightsDataSource dataSource = new FlightsDataSource(activity);
			dataSource.open();
			
			String mail = dataSource.getUserName();
			String password = dataSource.getPassWord();
			
			if(mail.length() == 0 || password.length() == 0)
			{
				DialogEnterLoginData dialog = new DialogEnterLoginData();
				dialog.show(activity.getSupportFragmentManager(), TAG);
				
				//Log.e(TAG, "mail or password is not set");
				return false;
			}
			
			int userId = getUserId(mail, activity);
			String sessionData = getSessionData(userId, password, activity);
			
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
	
	private int getUserId(String mailAddress, Activity activity)
	{
		int userId = -1;
		
		if(mailAddress.length() == 0)
		{
			Log.e(TAG, "mail is not set");
			return userId;
		}
		
		WebInterface webInterface = new WebInterface(activity);
		userId = webInterface.requestUserId(mailAddress);
		
		return userId;
	}
	
	private String getSessionData(int userId, String password, Activity activity)
	{
		String sessionData = "";
		
		if(userId < 0 || password.length() == 0)
		{
			Log.e(TAG, "userId and/or password not set");
			return sessionData;
		}
		
		WebInterface webInterface = new WebInterface(activity);
		sessionData = webInterface.login(userId,  password);
		
		return sessionData;
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

	public void onDialogPositiveClick(DialogFragment dialog, String mailAddress, String password)
	{
		int userId = getUserId(mailAddress, dialog.getActivity());
		password = WebInterface.saltPassword(password, mailAddress);
		
		Log.d(TAG, password);
		
		password = WebInterface.toHash(password);
		
		Log.d(TAG, password);
		
		String sessionData = getSessionData(userId, password, dialog.getActivity());
		
		Log.d(TAG, sessionData);
		
		if(sessionData.length() > 0)
		{
			FlightsDataSource dataSource = new FlightsDataSource(dialog.getActivity());
			dataSource.open();
			dataSource.updateCookie(sessionData);
			dataSource.updateUserMail(mailAddress);
			dataSource.updatePassword(password);
			dataSource.close();
		}
		else
		{
			Log.e(TAG, "Failed to login");
		}
	}

	public void onDialogNegativeClick(DialogFragment dialog)
	{
		
	}
}
