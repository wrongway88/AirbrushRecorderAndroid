package com.example.airbrushrecorder;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.airbrushrecorder.data.FlightsDataSource;

public class MainActivity extends Activity
{
	private Boolean m_logging = false;
	
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		Button button = (Button) findViewById(R.id.button_toggle_logging);
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
		Button button = (Button) findViewById(R.id.button_toggle_logging);
		
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
		
		FlightsDataSource dataSource = new FlightsDataSource(this);
		dataSource.open();
		dataSource.updateIp("foo");
		dataSource.getIp();
		dataSource.close();
		
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
	
	private void startPositionLogging()
    {
		EditText departureField = (EditText)findViewById(R.id.edit_text_departure);
		String departure = departureField.getText().toString();
		EditText arrivalField = (EditText)findViewById(R.id.edit_text_destination);
		String arrival = arrivalField.getText().toString();
		EditText airplaneTypeField = (EditText)findViewById(R.id.edit_text_airplane_type);
		String airplaneType = airplaneTypeField.getText().toString(); 
		
    	Intent intent = new Intent(this, ServicePathLog.class);
    	intent.putExtra(getString(R.string.log_departure), departure);
		intent.putExtra(getString(R.string.log_destination), arrival);
		intent.putExtra(getString(R.string.log_airplane_type), airplaneType);
		startService(intent);
    }
	
	private void stopPositionLogging()
	{
		Intent intent = new Intent(this, ServicePathLog.class);
		stopService(intent);
	}
}
