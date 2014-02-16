package com.airbrush.airbrushrecorder.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.airbrush.airbrushrecorder.Flight;
import com.airbrush.airbrushrecorder.LoginHelper;
import com.airbrush.airbrushrecorder.WebInterface;
import com.airbrush.airbrushrecorder.data.FlightsDataSource;
import com.airbrush.airbrushrecorder.dialog.DialogDeleteFlight;
import com.airbrush.airbrushrecorder.dialog.DialogUploadFlightResponse;
import com.airbrush.airbrushrecorder.dialog.DialogWifiOff;
import com.airbrush.airbrushrecorder.fragments.FragmentRecorder.OnToggleRecordingListener;
import com.airbrush.airbrushrecorder.R;

public class FragmentFlightBrowser extends Fragment
{
	private static String TAG = "FLIGHT_BROWSER";
	private Spinner _spinner = null;
	
	private ArrayList<Flight> _flights = null;
	
	private OnFlightBrowserListener m_listener;
	
	public interface OnFlightBrowserListener //well, thats a stupid name...
	{
		public void submitSelectedFlight(View view);
		public void deleteSelectedFlight(View view);
	}
	
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
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		if(activity instanceof OnToggleRecordingListener)
		{
			m_listener = (OnFlightBrowserListener) activity;
		}
		else
		{
			throw new ClassCastException(activity.toString() + "must implement OnFlightBrowserListener");
		}
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		return inflater.inflate(R.layout.fragment_flight_browser, container, false);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
		listFiles();
	}
	
	public void updateFlightList()
	{
		listFiles();
	}
	
	private void listFiles()
	{
		//Log.d(TAG, "list files");
		
		try
		{
			FlightsDataSource dataSource = new FlightsDataSource(this.getActivity());
			dataSource.open();
			_flights = dataSource.getFlights();
			//dataSource.printAllWaypoints();
			dataSource.close();
			
			List<String> list = new ArrayList<String>();
			for(int i = 0; i < _flights.size(); i++)
			{
				list.add(_flights.get(i).getDate().toString());
			}
			
			_spinner = (Spinner) getView().findViewById(R.id.dropdown_file);
			Button submitButton = (Button) getView().findViewById(R.id.button_submit_flight);
			Button deleteButton = (Button) getView().findViewById(R.id.button_delete_flight);
			
			if(list.isEmpty() == false)
			{
				ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, list);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				//_spinner.setVisibility(View.VISIBLE);
				_spinner.setAdapter(dataAdapter);
				_spinner.setOnItemSelectedListener(m_itemSelectedListener);
				
				submitButton.setVisibility(View.VISIBLE);
				deleteButton.setVisibility(View.VISIBLE);
			}
			else
			{
				//_spinner.setVisibility(View.GONE);
				submitButton.setVisibility(View.GONE);
				deleteButton.setVisibility(View.GONE);
			}
			
			if(list.size() > 1)
			{
				_spinner.setVisibility(View.VISIBLE);
			}
			else
			{
				_spinner.setVisibility(View.GONE);
			}
			
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
			dialog.show(this.getActivity().getSupportFragmentManager(), TAG);
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
				FlightsDataSource dataSource = new FlightsDataSource(this.getActivity());
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
				if(WebInterface.wifiAvailable(getActivity()))
				{
					FlightsDataSource dataSource = new FlightsDataSource(this.getActivity());
					dataSource.open();
					String sessionData = dataSource.getCookie();
					dataSource.close();
					
					LoginHelper loginHelper = new LoginHelper();
					if(sessionData.length() <= 0 || loginHelper.ipChanged(this.getActivity()))
					{
						loginHelper.login(getActivity());
						
						dataSource.open();
						sessionData = dataSource.getCookie();
						dataSource.close();
					}
					
					if(sessionData.length() > 0)
					{
						submitSelectedFlight(flight, sessionData);
					}
					else
					{
						DialogUploadFlightResponse dialog = new DialogUploadFlightResponse();
						Bundle bundle = new Bundle();
						bundle.putBoolean("success", false);
						bundle.putBoolean("loginSuccess", false);
						dialog.setArguments(bundle);
						dialog.show(getFragmentManager(), TAG);
					}
				}
				else
				{
					DialogWifiOff dialog = new DialogWifiOff();
					dialog.show(getChildFragmentManager(), TAG);
				}
			}
		}
		catch(Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}
	
	private void submitSelectedFlight(Flight flight, String sessionData)
	{	
		if(WebInterface.wifiAvailable(getActivity()))
		{
			WebInterface wf = new WebInterface(getActivity());
			Boolean success = wf.postFlight(flight, sessionData);
			
			DialogUploadFlightResponse dialog = new DialogUploadFlightResponse();
			Bundle bundle = new Bundle();
			bundle.putBoolean("success", success);
			dialog.setArguments(bundle);
			dialog.show(getFragmentManager(), TAG);
		}
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
			TextView textView = (TextView)getView().findViewById(R.id.textview_flight_data);
			
			if(_flights.isEmpty())
			{
				textView.setText(getString(R.string.textView_flight_data_no_records));
				
				return;
			}
			
			Flight flight = getSelectedFlight();
			
			if(flight == null)
			{
				textView.setText(getString(R.string.textView_flight_data_default));
				return;
			}
			
			try
			{
				String date = getString(R.string.textView_flightBrowser_date) + flight.getDate().toString();
				String departure = getString(R.string.textView_flightBrowser_departure) + flight.getDeparture();
				String destination = getString(R.string.textView_flightBrowser_destination) + flight.getDestination();
				
				textView.setText(date + "\n" + departure + "\n" + destination + "\n" + getString(R.string.textView_flightBrowser_waypoints) + flight.getWaypointCount());
			}
			catch(Exception e)
			{
				Log.e(TAG, e.getMessage());
			}
		}
	}
	
	public void onDialogPositiveClick(DialogFragment dialog)
	{
		deleteSelectedFlight();
	}
}
