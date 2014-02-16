package com.airbrush.airbrushrecorder.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import com.airbrush.airbrushrecorder.ServicePathLog;
import com.airbrush.airbrushrecorder.dialog.DialogGPSOff;
import com.airbrush.airbrushrecorder.R;

public class FragmentRecorder extends Fragment
{
	private Boolean m_logging = false;
	private OnToggleRecordingListener m_listener;
	private static String TAG = "FragmentRecorder";
	
	public interface OnToggleRecordingListener
	{
		public void togglePathLogging(View view);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		this.setRetainInstance(true); //so that the fragment is not recreated on orientation change (or other events), needed to keep track of logging state
		
		return inflater.inflate(R.layout.fragment_recorder, container, false);
	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		if(activity instanceof OnToggleRecordingListener)
		{
			m_listener = (OnToggleRecordingListener) activity;
		}
		else
		{
			throw new ClassCastException(activity.toString() + "must implement OnToggleRecordingListener");
		}
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		//Log.d(TAG, "on resume: " + m_logging);
		
		setLayoutLogging(m_logging);
	}
	
	public void togglePathLogging(View view)
	{
		//Button button = (Button) getView().findViewById(R.id.button_toggle_logging);
		
		if(m_logging)
		{
			stopPositionLogging();
			//button.setText(R.string.button_start_logging);
		}
		else
		{
			startPositionLogging();
			//button.setText(R.string.button_stop_logging);
		}
	}
	
	private void startPositionLogging()
    {
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			EditText departureField = (EditText) getView().findViewById(R.id.edit_text_departure);
			String departure = departureField.getText().toString();
			EditText arrivalField = (EditText) getView().findViewById(R.id.edit_text_destination);
			String arrival = arrivalField.getText().toString();
			EditText airplaneTypeField = (EditText) getView().findViewById(R.id.edit_text_airplane_type);
			String airplaneType = airplaneTypeField.getText().toString();
			
	    	Intent intent = new Intent(getActivity(), ServicePathLog.class);
	    	intent.putExtra(getString(R.string.log_departure), departure);
			intent.putExtra(getString(R.string.log_destination), arrival);
			intent.putExtra(getString(R.string.log_airplane_type), airplaneType);
			getActivity().startService(intent);
			
			setLayoutLogging(true);
			
			m_logging = !m_logging;
		}
		else
		{
			DialogGPSOff dialog = new DialogGPSOff();
			dialog.show(this.getFragmentManager(), TAG);
		}
    }
	
	private void stopPositionLogging()
	{
		setLayoutLogging(false);
		
		m_logging = !m_logging;
		
		Intent intent = new Intent(getActivity(), ServicePathLog.class);
		getActivity().stopService(intent);
	}
	
	private void setLayoutLogging(Boolean logging)
	{
		EditText departureField = (EditText) getView().findViewById(R.id.edit_text_departure);
		EditText arrivalField = (EditText) getView().findViewById(R.id.edit_text_destination);
		EditText airplaneTypeField = (EditText) getView().findViewById(R.id.edit_text_airplane_type);
		
		TextView indicator = (TextView) getView().findViewById(R.id.textview_log_indicator);
		
		Button button = (Button) getView().findViewById(R.id.button_toggle_logging);
		
		if(logging)
		{
			String departure = departureField.getText().toString();
			String arrival = arrivalField.getText().toString();
			String airplane = airplaneTypeField.getText().toString();
			
			departureField.setFocusable(false);
			arrivalField.setFocusable(false);
			airplaneTypeField.setFocusable(false);
			
			departureField.setVisibility(View.GONE);
			arrivalField.setVisibility(View.GONE);
			airplaneTypeField.setVisibility(View.GONE);
			
			Resources resource = getResources();
			if(departure.length() <= 0) departure = resource.getString(R.string.textView_logging_na);
			if(arrival.length() <= 0) arrival = resource.getString(R.string.textView_logging_na);
			if(airplane.length() <= 0) airplane = resource.getString(R.string.textView_logging_na);
			
			String message = resource.getString(R.string.textView_logging_logging);
			message += ("\n" + resource.getString(R.string.textView_logging_departure) + " " + departure);
			message += ("\n" + resource.getString(R.string.textView_logging_arrival) + " " + arrival);
			message += ("\n" + resource.getString(R.string.textView_logging_airplaneType) + " " + airplane);
			
			indicator.setText(message);
			indicator.setVisibility(View.VISIBLE);
			
			button.setText(R.string.button_stop_logging);
		}
		else
		{
			departureField.setFocusableInTouchMode(true);
			arrivalField.setFocusableInTouchMode(true);
			airplaneTypeField.setFocusableInTouchMode(true);
			
			departureField.setVisibility(View.VISIBLE);
			arrivalField.setVisibility(View.VISIBLE);
			airplaneTypeField.setVisibility(View.VISIBLE);

			indicator.setVisibility(View.GONE);
			
			button.setText(R.string.button_start_logging);
		}
	}
}
