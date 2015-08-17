package com.example.vietnguyen.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.core.controllers.MyActivity;
import com.example.vietnguyen.myapplication.R;

public class SignUpInActivity extends MyActivity{

	public final static String	PASSWORD	= "comp.example.vietnguyen.myapplication.password";
	public final static String	EMAIL		= "comp.example.vietnguyen.myapplication.mail_address";
	private final static int	SESSION_ID	= 9999;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);
	}

	/*
	 * Sign in validation here, but skip now
	 */
	public void onClickSignIn(View view){

		EditText edtMail = (EditText)findViewById(R.id.mail_address);
		String mail = edtMail.getText().toString();
		EditText edtPass = (EditText)findViewById(R.id.pass_word);
		String pass = edtPass.getText().toString();
		if(isAuthorized(mail, pass)){
			saveIntPreference(Const.PREF_KEY_SESSION_ID, 1);
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		}else{
			MU.log("Authorization failed!");
		}

	}

	public boolean isAuthorized(String email, String password){
		return true;
	}
}
