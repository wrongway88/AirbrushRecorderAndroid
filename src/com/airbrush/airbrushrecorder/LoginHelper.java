package com.airbrush.airbrushrecorder;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.airbrush.airbrushrecorder.data.FlightsDataSource;
import com.airbrush.airbrushrecorder.dialog.DialogEnterLoginData;
import com.airbrush.airbrushrecorder.dialog.DialogWifiOff;

public class LoginHelper
{
	private static String TAG = "LOGIN_HELPER";
	
	public boolean setLoginData(Activity activity, String mail, String password)
	{
		try
		{
			if(WebInterface.wifiAvailable(activity))
			{
				return setLoginDataOnline(activity, mail, password);
			}
			else
			{
				setLoginDataOffline(activity, mail, password);
			}
		}
		catch(Exception e)
		{
			Log.e(TAG, e.toString());
		}
		
		return true;
	}
	
	private void setLoginDataOffline(Activity activity, String mail, String password)
	{
		FlightsDataSource dataSource = new FlightsDataSource(activity);
		dataSource.open();
		dataSource.updateUserMail(mail);
		dataSource.updatePassword(password);
		dataSource.updateCookie(""); //force login on next upload
		dataSource.close();
	}
	
	private boolean setLoginDataOnline(Activity activity, String mail, String password)
	{
		if(WebInterface.wifiAvailable(activity))
		{
			String ip = WebInterface.getIPAddress(false);
			
			//this checks whether login data is correct
			int userId = getUserId(mail, activity);
			if(userId <= -1)
			{
				Log.e(TAG, "setLoginDataOnline: failed to get id");
				return false;
			}
			
			String sessionData = getSessionData(userId, password, activity);
			if(sessionData.length() <= 0)
			{
				Log.e(TAG, "setLoginDataOnline: failed to get session data - " + userId + " // " + password);
				return false;
			}
			
			FlightsDataSource dataSource = new FlightsDataSource(activity);
			dataSource.open();
			dataSource.updateUserMail(mail);
			dataSource.updatePassword(password);
			dataSource.updateIp(ip);
			dataSource.updateCookie(sessionData); //force login on next upload
			dataSource.close();
			
			return true;
		}
		
		return false;
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

	public boolean onDialogPositiveClick(DialogFragment dialog, String mailAddress, String password)
	{
		return setLoginData(dialog.getActivity(), mailAddress, password);
	}

	public void onDialogNegativeClick(DialogFragment dialog)
	{
		
	}
}
