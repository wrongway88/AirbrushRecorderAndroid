package com.example.airbrushrecorder;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.airbrushrecorder.dialog.DialogCreateAccountResponse;
import com.example.airbrushrecorder.dialog.DialogDeleteFlight;
import com.example.airbrushrecorder.dialog.DialogEnterLoginData;
import com.example.airbrushrecorder.dialog.DialogCreateAccount;
import com.example.airbrushrecorder.dialog.DialogWifiOff;
import com.example.airbrushrecorder.fragments.FragmentRecorder;
import com.example.airbrushrecorder.fragments.FragmentFlightBrowser;

public class MainActivity extends FragmentActivity implements FragmentRecorder.OnToggleRecordingListener, FragmentFlightBrowser.OnFlightBrowserListener,
																DialogDeleteFlight.NoticeDialogListener, DialogEnterLoginData.NoticeDialogListener,
																DialogCreateAccount.NoticeDialogListener
{
	private static final String TAG = "MAIN";
	
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
	
	public void viewLogin(View view)
	{
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	public void viewCreateAccount(View view)
	{
		if(WebInterface.wifiAvailable(this))
		{
			DialogCreateAccount dialog = new DialogCreateAccount();
			dialog.show(this.getSupportFragmentManager(), TAG);
		}
		else
		{
			DialogWifiOff dialog = new DialogWifiOff();
			dialog.show(getSupportFragmentManager(), TAG);
		}
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
			if(view != null)
				fragment.deleteSelectedFlight(view); //this will trigger a dialog to confirm deletion (is that a real word?)
			else
				fragment.deleteSelectedFlight(); //this will immediately delete the selected flight
		}
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog)
	{
		deleteSelectedFlight(null);
	}
	
	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String mailAddress, String password)
	{
		LoginHelper loginHelper = new LoginHelper();
		//loginHelper.onDialogPositiveClick(dialog, mailAddress, password);
		loginHelper.onDialogPositiveClick(dialog, "McDobusch@gmx.net", "mgpö01");
		
		FragmentFlightBrowser fragment = (FragmentFlightBrowser) getSupportFragmentManager().findFragmentById(R.id.fragment_flight_browser);
		if(fragment != null)
		{
			fragment.submitSelectedFlight(null);
		}
	}
	
	@Override
    public void onDialogNegativeClick(DialogFragment dialog)
    {
		
    }

	@Override
	public void onDialogCreateClick(DialogFragment dialog, String name, String surname, String email, String password)
	{
		WebInterface webInterface = new WebInterface(this);
		Boolean success = webInterface.createAccount(name, surname, email, password);
		
		DialogCreateAccountResponse dialogResponse = new DialogCreateAccountResponse();
		Bundle bundle = new Bundle();
		bundle.putBoolean("success", success);
		dialogResponse.setArguments(bundle);
		dialog.show(this.getSupportFragmentManager(), TAG);
	}
}
