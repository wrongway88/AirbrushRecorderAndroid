package com.airbrush.airbrushrecorder.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.DialogInterface;
import android.os.Bundle;

import com.airbrush.airbrushrecorder.DataStorage;
import com.airbrush.airbrushrecorder.Flight;
import com.airbrush.airbrushrecorder.R;
import com.airbrush.airbrushrecorder.WebInterface;
import com.airbrush.airbrushrecorder.data.FlightsDataSource;

public class DialogEditFlight extends DialogFragment
{
	private static String TAG = "DIALOG_EDIT_FLIGHT";
	
	private FragmentActivity m_activity = null;
	private int m_fightId = -1;
	
	public DialogEditFlight(FragmentActivity activity)
	{
		m_activity = activity;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		super.onCreateDialog(savedInstanceState);
		
		Bundle bundle = getArguments();
		m_fightId = bundle.getInt("flightId");
        FlightsDataSource dataSource = new FlightsDataSource(m_activity);
        dataSource.open();
		Flight flight = dataSource.getFlight(m_fightId);
		dataSource.close();
        
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View v = inflater.inflate(R.layout.dialog_enter_login_data, null);
		
		final AlertDialog d = builder.create();
		
		EditText inputDeparture = (EditText) v.findViewById(R.id.edit_text_departure);
    	EditText inputDestination = (EditText) v.findViewById(R.id.edit_text_destination);
    	EditText inputAirplaneType = (EditText) v.findViewById(R.id.edit_text_airplane_type);
		inputDeparture.setText(flight.getDeparture());
		inputDestination.setText(flight.getDestination());
		inputAirplaneType.setText(flight.getAirplaneType());
    	
		Button saveButton = d.getButton(R.id.button_save_flight);
		saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            	EditText inputDeparture = (EditText) v.findViewById(R.id.edit_text_departure);
            	EditText inputDestination = (EditText) v.findViewById(R.id.edit_text_destination);
            	EditText inputAirplaneType = (EditText) v.findViewById(R.id.edit_text_airplane_type);
            	
            	String departure = inputDeparture.getText().toString();
            	String destination = inputDestination.getText().toString();
            	String airplaneType = inputAirplaneType.getText().toString();
            	
            	FlightsDataSource dataSource = new FlightsDataSource(m_activity);
                dataSource.open();
        		Flight flight = dataSource.getFlight(m_fightId);
        		flight.setDeparture(departure);
        		flight.setDestination(destination);
        		flight.setAirplaneType(airplaneType);
        		dataSource.close();
        		
        		
            }
        });
		
		Button deleteButton = d.getButton(R.id.button_delete_flight);
		deleteButton.setOnClickListener(new View.OnClickListener()
		{	
			@Override
			public void onClick(View v)
			{
				DialogDeleteFlight dialog = new DialogDeleteFlight();
				dialog.show(m_activity.getSupportFragmentManager(), TAG);
			}
		});
		
		return d;
	}
	
	
}
