package com.nguyenhoangviet.vietnguyen.controllers;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragment;
import com.nguyenhoangviet.vietnguyen.core.model.User;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.network.UrlBuilder;
import com.nguyenhoangviet.vietnguyen.myapplication.R;

//import com.facebook.login.widget.LoginButton;

/**
 * Created by viet on 8/20/2015.
 */

public class SignUpInFragment extends MyFragment{

	// private LoginButton btnFacebookLogin;
	private boolean	isSigningUp	= false;

	public SignUpInFragment(){
	}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_sign_up_in, container, false);
	}

	@Override
	public void buildLayout(){
		super.buildLayout();

		// btnFacebookLogin = (LoginButton)getView().findViewById(R.id.btn_facebook_login);
		// btnFacebookLogin.setReadPermissions(Arrays.asList("user_status", "user_photos"));

		Button btnSignIn = getButton(R.id.btn_sign_up_in);
		btnSignIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				signIn();
			}
		});

		setOnClickFor(R.id.txt_sign_up, new View.OnClickListener() {

			@Override
			public void onClick(View view){
				updateLayoutToSignUp();
			}
		});

	}

	private void updateLayoutToSignUp(){
		this.isSigningUp = !this.isSigningUp;
		if(this.isSigningUp){
			setTextFor(R.id.btn_sign_up_in, getString(R.string.sign_up));
			setTextFor(R.id.txt_sign_up, getString(R.string.sign_in));
		}else{
			setTextFor(R.id.btn_sign_up_in, getString(R.string.sign_in));
			setTextFor(R.id.txt_sign_up, getString(R.string.sign_up));
		}
	}

	private void signIn(){
		String userName = getEditText(R.id.edt_fragment_sign_up_in_username).getText().toString();
		String passWord = getEditText(R.id.edt_fragment_sign_up_in_password).getText().toString();
		callApi(UrlBuilder.signIn(userName, passWord), new Api.OnApiSuccessObserver() {

			@Override
			public void onSuccess(JSONObject response){
				onSignInSucces(response);
			}

			@Override
			public void onFailure(JSONObject response){
				commonApiFailure(response);
			}
		});
	}

	private void onSignInSucces(JSONObject response){
		String token = response.optString("token");
		activity.loginUser = new User(token);
		activity.saveStringPreference(Const.PREF_KEY_TOKEN, token);
		((MainActivity)activity).gotoTaskListFragment();
	}

	private void signUp(){
		// JSONObject param = MU.buildJsonObj(Arrays.asList(""));
		// getApi(Const.SIGN_UP, param, new Api.OnCallApiListener() {
		//
		// @Override
		// public void onApiResponse(JSONObject response){
		//
		// }
		//
		// @Override
		// public void onApiError(String errorMsg){
		//
		// }
		// });
	}

}
