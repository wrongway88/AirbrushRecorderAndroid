package com.airbrush.airbrushrecorder;

import java.util.Calendar;
import java.lang.Thread;

import com.airbrush.airbrushrecorder.data.FlightsDataSource;
import com.airbrush.airbrushrecorder.R;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.os.PowerManager;

public class ServicePathLog extends Service
{
	private String TAG = "ServicePathLog";
	
	private LocationManager m_locationManager = null;
	private LocationProvider m_locationProvider = null;
	private int m_startTime = 0;
	
	private String m_departure = "";
	private String m_destination = "";
	private String m_airplaneType = "";
	
	private Flight _flight = null;
	
	private LogWriter _logWriter = null;
	
	private PowerManager.WakeLock _wakeLock = null;
	
	private Thread.UncaughtExceptionHandler _uncaughtExceptionHandler = null;
	
	private int _dbId = -1;
	
	private Thread.UncaughtExceptionHandler _exceptionHandler = new Thread.UncaughtExceptionHandler()
	{
		@Override
		public void uncaughtException(Thread thread, Throwable ex)
		{
			try
			{
				if(_wakeLock != null)
				{
					if(_wakeLock.isHeld())
					{
						_wakeLock.release();
					}
				}
			}
			catch(Exception e)
			{
				Log.e(TAG, "Uncaught Exception: " + ex.getMessage());
			}
			finally
			{
				Thread.setDefaultUncaughtExceptionHandler(_uncaughtExceptionHandler);
				_uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), ex);
			}
		}
	};
	
	private ServicePathLog _this = null;
	
	private final LocationListener m_locationListener = new LocationListener()
	{
		@Override
		public void onProviderEnabled(String provider)
		{
		}
		
		@Override
	    public void onLocationChanged(Location location)
		{
			Calendar c = Calendar.getInstance();
			int t = (c.get(Calendar.HOUR_OF_DAY) * 60 * 60) + (c.get(Calendar.MINUTE) * 60) + c.get(Calendar.SECOND);
			t -= m_startTime;
			
			//_flight.addWaypoint(new Flight.Waypoint(t, location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getSpeed()));
			
			if(_logWriter != null)
			{
				/*
				String wp = "{";
				wp += "\"t\":" + t;
				wp += "\"lat\":" + location.getLatitude();
				wp += "\"long\":" + location.getLongitude();
				wp += "\"alt\":" + location.getAltitude();
				wp += "\"speed\":" + location.getSpeed();
				wp += "},";
				*/
				
				//_logWriter.writeToFile(wp + ",");
			}
			
			if(_this != null)
			{
				FlightsDataSource dataSource = new FlightsDataSource(_this);
				dataSource.open();
				dataSource.createWaypoint(_dbId, t, location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getSpeed());
				dataSource.close();
			}
		}
		
		@Override
		public void onProviderDisabled(String provider)
		{
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}
	};
	
	
	
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
	
	@Override
	public void onCreate()
	{
		_this = this;
		
		_uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(_exceptionHandler);
	}
	
	@Override
	public void onStart(Intent intent, int startId)
	{
		m_departure = intent.getStringExtra(getString(R.string.log_departure));
		m_destination = intent.getStringExtra(getString(R.string.log_destination));
		m_airplaneType = intent.getStringExtra(getString(R.string.log_airplane_type));
		
		PowerManager p = (PowerManager) getSystemService(Context.POWER_SERVICE);
		_wakeLock = p.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
		_wakeLock.acquire();
		
		setupLocationProvider();
	}
	
	@Override
	public void onDestroy()
	{
		try
		{
			if(_wakeLock.isHeld())
				_wakeLock.release();
		}
		catch(Error error)
		{
			Log.e(TAG, error.getMessage());
		}

		stopLocationProvider();
	}
	
	protected void setupLocationProvider()
	{
		try
		{
			if(m_locationManager == null)
			{
				m_locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			}
			
			boolean gpsEnabled = m_locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			if(!gpsEnabled)
			{
				this.stopSelf();
			}
			
			m_locationProvider = m_locationManager.getProvider(LocationManager.GPS_PROVIDER);
			m_locationProvider.getName();
			
			m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 2, m_locationListener);
			
			initFlight();
			Calendar c = Calendar.getInstance();
			m_startTime = (c.get(Calendar.HOUR_OF_DAY) * 60 * 60) + (c.get(Calendar.MINUTE) * 60) + c.get(Calendar.SECOND);
			//String date = c.get(Calendar.YEAR) + "_" + c.get(Calendar.MONTH) + "_" + c.get(Calendar.DAY_OF_MONTH) + "-"
			//			+ c.get(Calendar.HOUR_OF_DAY) + "_" + c.get(Calendar.MINUTE) + "_" + c.get(Calendar.SECOND);
		}
		catch(Exception e)
		{
			Log.e(TAG, e.getMessage());
		}
	}
	
	private void stopLocationProvider()
	{
		if(m_locationManager != null)
		{
			m_locationManager.removeUpdates(m_locationListener);
		}
	}
	
	/*
	private void openLogFile(String date)
	{
		_logWriter = new LogWriter();
		_logWriter.openFile("log_" + date + ".txt");
		
		if(_flight != null)
		{
			_logWriter.writeToFile("\"departure\":\"" + _flight.getDeparture() + "\",");
			_logWriter.writeToFile("\"destination\":\"" + _flight.getDestination() + "\",");
			_logWriter.writeToFile("\"date\":\"" + _flight.getDate().serialize() + "\",");
			_logWriter.writeToFile("\"airplanetype\":\"" + _flight.getAirplaneType() + "\",");
			_logWriter.writeToFile("\"waypoints\":[");
		}
	}
	
	private void closeLogFile()
	{
		if(_logWriter != null)
		{
			_logWriter.writeToFile("]}");
			_logWriter.closeFile();
		}
	}
	*/
	
	private void initFlight()
	{
		_flight = new Flight();
		
		Calendar c = Calendar.getInstance();
		//String date = c.get(Calendar.DAY_OF_MONTH) + "." + c.get(Calendar.MONTH) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
		
		_flight.setDate(new Flight.Date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
										c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)));
		
		_flight.setDeparture(m_departure);
		_flight.setDestination(m_destination);
		_flight.setAirplaneType(m_airplaneType);
		
		FlightsDataSource dataSource = new FlightsDataSource(this);
		dataSource.open();
		_dbId = dataSource.createFlight(_flight.getDate().toString(), _flight.getDeparture(), _flight.getDestination(), _flight.getAirplaneType());
		dataSource.close();
	}
	
	/*
	private void writeFlight()
	{
		FlightsDataSource dataSource = new FlightsDataSource(this);
		dataSource.open();
		int id = dataSource.createFlight(_flight.getDate().toString(), _flight.getDeparture(), _flight.getDestination(), _flight.getAirplaneType());
		
		//dataSource.createWaypoint(id, 0, 0.0, 0.0, 300.0, 0.0f);
		
		
		for(int i = 0; i < _flight.getWaypointCount(); i++)
		{
			Flight.Waypoint waypoint = _flight.getWaypoint(i);
			
			if(waypoint != null)
			{
				dataSource.createWaypoint(id, waypoint._t, waypoint._latitude, waypoint._longitude, waypoint._altitude, waypoint._speed);
			}
		}
		
		
		//dataSource.createWaypoint(flightId, timeStamp, latitude, longitude, altitude, speed)
		dataSource.close();
	}
	*/
}
