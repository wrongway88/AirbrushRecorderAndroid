package com.example.airbrushrecorder;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.util.InetAddressUtils;

public class WebInterface
{
	private static String TAG = "WebInterface";
	private static String ADDRESS_FLIGHT = "http://airbrush.nucular-bacon.com/api/flight";
	private static String ADDRESS_USER = "http://airbrush.nucular-bacon.com/api/user";
	private static String ADDRESS_SESSION = "http://airbrush.nucular-bacon.com/api/session";
	//private HttpURLConnection _connection = null;
	
	private class AsyncHttpRequest extends AsyncTask<String, Integer, String>
	{
		@Override
		protected String doInBackground(String...params)
		{
			String result = "";
			
			if(params.length == 3)
				result = postData(params[0], params[1], params[2]);
			else if(params.length == 1)
				result = getData(params[0]);
			
			return result;
		}
		
		public String postData(String address, String data, String cookie)
		{
			URL url;
			HttpURLConnection connection;
			
			String result = "";
			
			//Log.d(TAG, data);
			
			try
			{
				url = new URL(address);
				
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Accept", "application/json");
				
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				
				if(cookie.length() > 0)
				{
					connection.setRequestProperty("Cookie", cookie);
				}
				
				DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
				
				wr.write(data.getBytes());
				wr.flush();
				wr.close();
				
				InputStream is = connection.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				String line;
				StringBuffer response = new StringBuffer(); 
		      while((line = rd.readLine()) != null)
		      {
		        response.append(line);
		        response.append('\r');
		      }
		      rd.close();
		      
		      result = response.toString();
		      Log.d(TAG, result);
			}
			catch(ClientProtocolException e)
			{
				Log.e(TAG, "Error: " + e);
			}
			catch(IOException e)
			{
				Log.e(TAG, "Error: " + e);
			}
			
			return result;
		}
		
		public String getData(String address)
		{
			URL url;
			HttpURLConnection connection;
			
			String result = "";
			
			try
			{
				url = new URL(address);
				
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				connection.setRequestProperty("Accept", "application/json");
				
				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);
				
				InputStream is = connection.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				String line;
				StringBuffer response = new StringBuffer(); 
		      while((line = rd.readLine()) != null)
		      {
		        response.append(line);
		        response.append('\r');
		      }
		      rd.close();
		      
		      result = response.toString();
		      
		      //Log.d(TAG + "get", result);
			}
			catch(ClientProtocolException e)
			{
				Log.e(TAG, "Error: " + e);
			}
			catch(IOException e)
			{
				Log.e(TAG, "Error: " + e);
			}
			
			return result;
		}
	}
	
	public WebInterface()
	{
	}
	
	public void postFlight(Flight flight, String cookie)
	{
		new AsyncHttpRequest().execute(ADDRESS_FLIGHT, flight.serializeToHttp(), cookie);
	}
	
	public String login(int userId, String password)
	{
		String result = "";
		
		try
		{
			String postData = "id=" + userId + "&password=" + password;
			String response = new AsyncHttpRequest().execute(ADDRESS_SESSION, postData, "").get();
			
			JSONObject object = new JSONObject(response);
			result = object.getString("sessionkey");
		}
		catch (InterruptedException e)
		{
			Log.e(TAG, e.toString());
		}
		catch (ExecutionException e)
		{
			Log.e(TAG, e.toString());
		}
		catch (JSONException e)
		{
			Log.e(TAG, e.toString());
		}
		
		return result;
	}
	
	public int requestUserId(String mailAddress)
	{
		int result = -1;
		String response = "";
		
		String address = ADDRESS_USER;
		address += "?email=" + mailAddress;
		
		try
		{
			response = new AsyncHttpRequest().execute(address).get();
			
			JSONArray jsonArray = new JSONArray(response);
			
			for(int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject object = jsonArray.getJSONObject(i);
				result = object.getInt("id");
			}
		}
		catch (InterruptedException e)
		{
			Log.e(TAG, e.toString());
		}
		catch (ExecutionException e)
		{
			Log.e(TAG, e.toString());
		}
		catch(JSONException e)
		{
			Log.e(TAG, e.toString());
		}
		
		return result;
	}
	
	//http://stackoverflow.com/questions/6064510/how-to-get-ip-address-of-the-device
	public static String getIPAddress(boolean useIPv4)
	{
        try
        {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces)
            {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                
                for (InetAddress addr : addrs)
                {
                    if (!addr.isLoopbackAddress())
                    {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        if (useIPv4)
                        {
                            if (isIPv4) 
                                return sAddr;
                        }
                        else
                        {
                            if (!isIPv4)
                            {
                                int delim = sAddr.indexOf('%');
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
        	Log.e(TAG, ex.toString());
        }
        
        return "";
    }
}
