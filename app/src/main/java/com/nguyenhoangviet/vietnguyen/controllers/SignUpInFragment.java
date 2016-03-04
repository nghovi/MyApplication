package com.nguyenhoangviet.vietnguyen.controllers;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.nguyenhoangviet.vietnguyen.Const;
import com.nguyenhoangviet.vietnguyen.core.controller.MyFragment;
import com.nguyenhoangviet.vietnguyen.core.network.Api;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.myapplication.R;
//import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

/**
 * Created by viet on 8/20/2015.
 */

public class SignUpInFragment extends MyFragment{

//	private LoginButton	btnFacebookLogin;
	private boolean		isSigningUp	= false;

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

		JSONObject param = new JSONObject();
		try{
			param.put("accessToken", "6af65192bde50a2849a69b2f634b41a38b6afc3b");
			param.put("userId", "72b88e38b9a4f1388b47959693673c34");
			param.put("apiKey", "23z37jp77fjxk45hnkyt8ud84g6tzugj");
			param.put("version", "1");
			param.put("shopId", "00000000675");
		}catch(JSONException e){
			e.printStackTrace();
		}

		String url = "http://dev01clnt.shk.x.recruit.co.jp/api/shop/detail/";
//		getApi(url, param, new Api.OnCallApiListener() {
//
//			@Override
//			public void onApiResponse(JSONObject response){
//
//			}
//
//			@Override
//			public void onApiError(String errorMsg){
//
//			}
//		});
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
		JSONObject param = MU.buildJsonObj(Arrays.asList(""));
//		getApi(Const.SIGN_IN, param, new Api.OnCallApiListener() {
//
//			@Override
//			public void onApiResponse(JSONObject response){
//
//			}
//
//			@Override
//			public void onApiError(String errorMsg){
//
//			}
//		});
	}

	private void signUp(){
//		JSONObject param = MU.buildJsonObj(Arrays.asList(""));
//		getApi(Const.SIGN_UP, param, new Api.OnCallApiListener() {
//
//			@Override
//			public void onApiResponse(JSONObject response){
//
//			}
//
//			@Override
//			public void onApiError(String errorMsg){
//
//			}
//		});
	}

}
