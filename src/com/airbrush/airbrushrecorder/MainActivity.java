package com.airbrush.airbrushrecorder;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.airbrush.airbrushrecorder.LoginHelper;
import com.airbrush.airbrushrecorder.data.FlightsDataSource;
import com.airbrush.airbrushrecorder.dialog.DialogCreateAccount;
import com.airbrush.airbrushrecorder.dialog.DialogDeleteFlight;
import com.airbrush.airbrushrecorder.dialog.DialogEnterLoginData;
import com.airbrush.airbrushrecorder.dialog.DialogUploadFlightResponse;
import com.airbrush.airbrushrecorder.dialog.DialogWifiOff;
import com.airbrush.airbrushrecorder.dialog.DialogLogFlightResponse;
import com.airbrush.airbrushrecorder.fragments.FragmentFlightBrowser;
import com.airbrush.airbrushrecorder.fragments.FragmentRecorder;
import com.airbrush.airbrushrecorder.R;

public class MainActivity extends FragmentActivity implements FragmentRecorder.OnToggleRecordingListener, FragmentFlightBrowser.OnFlightBrowserListener,
																DialogDeleteFlight.NoticeDialogListener, DialogEnterLoginData.NoticeDialogListener,
																DialogUploadFlightResponse.NoticeDialogListener, DialogCreateAccount.NoticeDialogListener
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
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.id.menu_change_account:
        	viewChangeAccount();
            break;
        case R.id.menu_create_account:
        	viewCreateAccount();
        	break;
        }
 
        return true;
    }
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		displayAccountData();
		
		Boolean showCreateDialog = DataStorage.getInstance().getBoolean(getString(R.string.data_view_create_account));
		if(showCreateDialog != null &&  showCreateDialog == true)
		{
			viewCreateAccount();
		}
		
		Boolean showChangeDialog = DataStorage.getInstance().getBoolean(getString(R.string.data_view_change_account));
		if(showChangeDialog != null && showChangeDialog == true)
		{
			viewChangeAccount();
		}
	}
	
	public void viewChangeAccount()
	{
		DialogEnterLoginData dialog = new DialogEnterLoginData(this);
		dialog.show(getSupportFragmentManager(), TAG);
	}
	
	public void viewCreateAccount()
	{
		if(WebInterface.wifiAvailable(this))
		{
			DialogCreateAccount createAccountDialog = new DialogCreateAccount(this);
			createAccountDialog.show(this.getSupportFragmentManager(), TAG);
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
	public void onAccountDataSet()
	{
		displayAccountData();
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog)
	{}
	
	@Override
	public void onDialogDeleteClick(DialogFragment dialog)
	{
		deleteSelectedFlight(null);
	}
	
	@Override
	public void onSuccess(DialogFragment dialog, String email, String password)
	{
		LoginHelper loginHelper = new LoginHelper();
		loginHelper.setLoginData(this, email, password);
		displayAccountData();
	}
	
	@Override
	public void onLoggingSuccess()
	{
		FragmentFlightBrowser flightBrowser = (FragmentFlightBrowser) getSupportFragmentManager().findFragmentById(R.id.fragment_flight_browser);
		flightBrowser.updateFlightList();
	}

	@Override
	public void onLoggingFail()
	{
		DialogLogFlightResponse dialog = new DialogLogFlightResponse();
		dialog.show(getSupportFragmentManager(), TAG);
	}
	
	private void displayAccountData()
	{
		String name = "";
		
		FlightsDataSource dataSource = new FlightsDataSource(this);
		dataSource.open();
		name = dataSource.getUserName();
		dataSource.close();
		
		displayAccountData(name);
	}
	
	private void displayAccountData(String name)
	{
		TextView textView = (TextView)findViewById(R.id.textview_account_data);
		
		String message = getString(R.string.textView_account_data_title);
		
		if(name.length() <= 0)
		{
			message += "\n" + getString(R.string.textView_account_data_default);
		}
		else
		{
			message += "\n" + name;
		}
		
		textView.setText(message);
	}
}
