package com.example.airbrushrecorder;

import android.os.Bundle;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.view.View;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.airbrushrecorder.data.FlightsDataSource;
import com.example.airbrushrecorder.dialog.DialogDeleteFlight;
import com.example.airbrushrecorder.dialog.DialogSetLoginData;

import java.util.List;
import java.util.ArrayList;

import android.util.Log;

public class FileBrowser extends FragmentActivity implements DialogDeleteFlight.NoticeDialogListener, DialogSetLoginData.NoticeDialogListener
{
	private static String TAG = "FileBrowser";
	private Spinner _spinner = null;
	
	private ArrayList<Flight> _flights = null;
	
	private final OnItemSelectedListener m_itemSelectedListener = new OnItemSelectedListener()
	{
		@Override
		public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
		{
			displayMetaData();
	    }
		
		 @Override
	    public void onNothingSelected(AdapterView<?> parentView)
		{
			 
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_browser);
		
		listFiles();
	}
	
	private void listFiles()
	{
		try
		{
			FlightsDataSource dataSource = new FlightsDataSource(this);
			dataSource.open();
			_flights = dataSource.getFlights();
			//dataSource.printAllWaypoints();
			dataSource.close();
			
			List<String> list = new ArrayList<String>();
			for(int i = 0; i < _flights.size(); i++)
			{
				list.add(_flights.get(i).getDate().toString());
			}
			
			_spinner = (Spinner) findViewById(R.id.dropdown_file);
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			_spinner.setAdapter(dataAdapter);
			_spinner.setOnItemSelectedListener(m_itemSelectedListener);
			
			displayMetaData();
		}
		catch(Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}
	
	public void deleteSelectedFlight(View view)
	{
		try
		{
			DialogDeleteFlight dialog = new DialogDeleteFlight();
			dialog.show(this.getSupportFragmentManager(), TAG);
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	
	public void deleteSelectedFlight()
	{
		try
		{
			Flight flight = getSelectedFlight();
			if(flight != null)
			{
				FlightsDataSource dataSource = new FlightsDataSource(this);
				dataSource.open();
				dataSource.deleteFlight(flight.getId(), true);
				dataSource.close();
				
				listFiles();
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	
	public void submitSelectedFlight(View view)
	{
		try
		{
			Flight flight = getSelectedFlight();
			if(flight != null)
			{
				FlightsDataSource dataSource = new FlightsDataSource(this);
				dataSource.open();
				String sessionData = dataSource.getCookie();
				
				WebInterface wf = new WebInterface();
				
				LoginHelper loginHelper = new LoginHelper();
				if(sessionData.length() <= 0 || loginHelper.ipChanged(this))
				{
					if(loginHelper.login(this) == false)
					{
						DialogSetLoginData dialog = new DialogSetLoginData();
						dialog.show(this.getSupportFragmentManager(), TAG);
					}
					else
					{
						sessionData = dataSource.getCookie();
					}
				}
				
				dataSource.close();
				
				if(sessionData.length() > 0)
				{
					submitSelectedFlight(flight, sessionData);
				}
			}
		}
		catch(Exception e)
		{
			Log.d(TAG, e.toString());
		}
	}
	
	private void submitSelectedFlight(Flight flight, String sessionData)
	{
		WebInterface wf = new WebInterface();
		wf.postFlight(flight, sessionData);
	}
	
	private Flight getSelectedFlight()
	{
		if(_spinner != null && _flights != null)
		{
			Object selectedItem = _spinner.getSelectedItem();
			if(selectedItem == null)
				return null;
			
			String fileName = selectedItem.toString();
			
			for(int i = 0; i < _flights.size(); i++)
			{
				Flight flight = _flights.get(i);
				String date = flight.getDate().toString();
				
				if(fileName.equals(date))
				{
					return flight;
				}
			}
		}
		
		return null;
	}
		
	private void displayMetaData()
	{
		if(_spinner != null)
		{
			Flight flight = getSelectedFlight();
			
			if(flight == null)
			{
				TextView textView = (TextView)findViewById(R.id.textview_flight_data);
				textView.setText("No flight selected");
				return;
			}
			
			try
			{
				String date = "Date: " + flight.getDate().toString();
				String departure = "Departure: " + flight.getDeparture();
				String destination = "Destination: " + flight.getDestination();
				
				TextView textView = (TextView)findViewById(R.id.textview_flight_data);
				textView.setText(date + "\n" + departure + "\n" + destination + "\nWaypoints: " + flight.getWaypointCount());
			}
			catch(Exception e)
			{
				Log.e(TAG, e.getMessage());
			}
		}
	}
	
	public void backToMain(View view)
	{
		Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog)
	{
		//Log.d(TAG, "onDialogPositiveClick");
		deleteSelectedFlight();
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String mail, String password)
	{
		LoginHelper loginHelper = new LoginHelper();
		loginHelper.setLoginData(this, mail, password);
		
		if(loginHelper.login(this))
		{
			String sessionData = loginHelper.getSessionData(this);
			WebInterface wf = new WebInterface();
			wf.postFlight(getSelectedFlight(), sessionData);
		}
	}
	
	@Override
    public void onDialogNegativeClick(DialogFragment dialog)
    {
    	//Log.d(TAG, "onDialogNegativeClick");
    }
}
