package com.example.airbrushrecorder.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.airbrushrecorder.ServicePathLog;
import com.example.airbrushrecorder.R;

public class FragmentRecorder extends Fragment
{
	private Boolean m_logging = false;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_recorder, container, false);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		Button button = (Button) getView().findViewById(R.id.button_toggle_logging);
		if(m_logging)
		{
			button.setText(R.string.button_start_logging);
		}
		else
		{
			button.setText(R.string.button_stop_logging);
		}
	}
	
	public void togglePathLogging(View view)
	{
		Button button = (Button) getView().findViewById(R.id.button_toggle_logging);
		
		if(m_logging)
		{
			stopPositionLogging();
			button.setText(R.string.button_start_logging);
		}
		else
		{
			startPositionLogging();
			button.setText(R.string.button_stop_logging);
		}
		
		m_logging = !m_logging;
	}
	
	private void startPositionLogging()
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
    }
	
	private void stopPositionLogging()
	{
		Intent intent = new Intent(getActivity(), ServicePathLog.class);
		getActivity().stopService(intent);
	}
}
