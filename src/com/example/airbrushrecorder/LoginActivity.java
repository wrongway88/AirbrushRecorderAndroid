package com.example.airbrushrecorder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.airbrushrecorder.LoginHelper;

public class LoginActivity extends Activity
{
	private static String TAG = "LoginActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void setLoginData(View view)
	{
		EditText mailField = (EditText)findViewById(R.id.edit_text_user_name);
		String name = mailField.getText().toString();
		EditText passwordField = (EditText)findViewById(R.id.edit_text_password);
		String password = passwordField.getText().toString();
		
		LoginHelper helper = new LoginHelper();
		helper.setLoginData(this,  name,  password);
		helper.login(this);
	}
}
