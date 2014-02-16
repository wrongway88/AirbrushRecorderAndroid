package com.airbrush.airbrushrecorder.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.content.DialogInterface;
import android.os.Bundle;

import com.airbrush.airbrushrecorder.WebInterface;
import com.airbrush.airbrushrecorder.R;

public class DialogEnterLoginData extends DialogFragment
{
	private static String TAG = "DIALOG_ENTER_LOGIN_DATA";
	
	public interface NoticeDialogListener
	{
        public void onDialogPositiveClick(DialogFragment dialog, String mailAddress, String password);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	
	private NoticeDialogListener m_listener;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
	{
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_set_login_data);
        builder.setMessage(R.string.dialog_set_login_data_message);
        
        final EditText inputMail = new EditText(getActivity());
        final EditText inputPassword = new EditText(getActivity());
        
        inputMail.setHint(R.string.edit_text_email);
        inputPassword.setHint(R.string.edit_text_password);
        
        inputMail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        
        layout.addView(inputMail);
        layout.addView(inputPassword);
        
        builder.setView(layout);
        
        builder.setPositiveButton(R.string.dialog_save, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
            	String mail = inputMail.getText().toString();
            	String password = inputPassword.getText().toString();
            	
            	password = WebInterface.saltPassword(password, mail);
        		password = WebInterface.toHash(password);
            	
            	m_listener.onDialogPositiveClick(DialogEnterLoginData.this, mail, password);
            }
        });
 
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
            	m_listener.onDialogNegativeClick(DialogEnterLoginData.this);
            }
        });
        
        return builder.create();
    }

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		
		try
		{
			m_listener = (NoticeDialogListener)activity;
		}
		catch (Exception e)
		{
			Log.e(TAG, activity.toString() + " must implement NoticeDialogListener");
		}
	}
}
