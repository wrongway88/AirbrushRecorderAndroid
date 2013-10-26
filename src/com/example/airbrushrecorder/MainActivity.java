package com.example.airbrushrecorder;

import android.os.Bundle;
//import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

import com.example.airbrushrecorder.fragments.FragmentRecorder;
import com.example.airbrushrecorder.fragments.FragmentFlightBrowser;

public class MainActivity extends FragmentActivity implements FragmentRecorder.OnToggleRecordingListener, FragmentFlightBrowser.OnFlightBrowserListener
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	public void viewFlights(View view)
	{
		Intent intent = new Intent(this, FileBrowser.class);
		startActivity(intent);
	}
	
	public void viewLogin(View view)
	{
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	@Override
	public void togglePathLogging(View view)
	{
		FragmentRecorder fragment = (FragmentRecorder) getSupportFragmentManager().findFragmentById(R.id.fragment_recorder);
		
		if(fragment != null)
		{
			fragment.togglePathLogging(view);
		}
	}
	
	@Override
	public void submitSelectedFlight(View view)
	{
		FragmentFlightBrowser fragment = (FragmentFlightBrowser) getSupportFragmentManager().findFragmentById(R.id.fragment_flight_browser);
		
		if(fragment != null)
		{
			fragment.submitSelectedFlight(view);
		}
	}
	
	@Override
	public void deleteSelectedFlight(View view)
	{
		FragmentFlightBrowser fragment = (FragmentFlightBrowser) getSupportFragmentManager().findFragmentById(R.id.fragment_flight_browser);
		
		if(fragment != null)
		{
			fragment.deleteSelectedFlight(view);
		}
	}
}
