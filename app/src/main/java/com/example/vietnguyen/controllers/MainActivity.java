package com.example.vietnguyen.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.vietnguyen.core.Const;
import com.example.vietnguyen.core.controllers.Footer;
import com.example.vietnguyen.core.controllers.MyActivity;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class MainActivity extends MyActivity implements View.OnClickListener{

	public Footer				footer;
	private SignUpInFragment	signUpInFragment;
	private MainFragment		mainFragment;
	private CallbackManager		fbCallbackManager;
	private ProfileTracker		fbProfileTracker;
	private Profile				fbProfile;
	private AccessTokenTracker	fbAccessTokenTracker;
	public LoginManager			fbLoginManager;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getApplicationContext());

		setContentView(R.layout.activity_main);
        onCreateFooter();

		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if(accessToken == null){
			signUpInFragment = new SignUpInFragment();
			replaceWithFragment(signUpInFragment);
            footer.hide();
		}else{
            footer.show();
			mainFragment = new MainFragment();
			replaceWithFragment(mainFragment);
		}
		setUpFacebookCallbacks();
		// Ensure that our profile is up to date
		Profile.fetchProfileForCurrentAccessToken();
		setProfile(Profile.getCurrentProfile());
	}

	private void onCreateFooter(){
		this.footer = new Footer(this);
		footer.setOnItemsClickListener(this);
		footer.checkFooterItem(0);
	}

	private void setUpFacebookCallbacks(){
		fbCallbackManager = CallbackManager.Factory.create();
		fbLoginManager = LoginManager.getInstance();
		fbLoginManager.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {

			@Override
			public void onSuccess(LoginResult loginResult){
				Profile.fetchProfileForCurrentAccessToken();
			}

			@Override
			public void onError(FacebookException exception){
				AccessToken.setCurrentAccessToken(null);
			}

			@Override
			public void onCancel(){
				AccessToken.setCurrentAccessToken(null);
			}
		});

		fbProfileTracker = new ProfileTracker() {

			@Override
			protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile){
				AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();
			}
		};

		fbAccessTokenTracker = new AccessTokenTracker() {

			@Override
			protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken){
				Profile.fetchProfileForCurrentAccessToken();
			}
		};
	}

	private void setProfile(Profile profile){
		fbProfile = profile;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		fbCallbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View view){
		switch(view.getId()){
		case R.id.lnr_footer_item1:
			onClickFooterItem1();
			break;
		case R.id.lnr_footer_item2:
			onClickFooterItem2();
			break;
		case R.id.lnr_footer_item3:
			onClickFooterItem3();
			break;
		case R.id.lnr_footer_item4:
			onClickFooterItem4();
			break;
		default:
			break;
		}
	}

	public void onClickFooterItem1(){
		MU.log("Footer item 1 onClicked");
		replaceWithFragment(new PrimaryCardFragment());
	}

	public void onClickFooterItem2(){
		MU.log("Footer item 2 onClicked");
		replaceWithFragment(new FormFragment());
	}

	public void onClickFooterItem3(){
		MU.log("Footer item 3 onClicked");
		replaceWithFragment(new SettingFragment());
	}

	public void onClickFooterItem4(){
		MU.log("Footer item 4 onClicked");
	}

	@Override
	protected void onResume(){
		super.onResume();

		// facebook
		// Logs 'install' and 'app activate' App Events.
		// todo why dont work ? don't instantiate before ?
		// AppEventsLogger.activateApp(this.getApplicationContext(), Const.FACEBOOK_APP_ID);
	}

	@Override
	protected void onPause(){
		super.onPause();

		// Logs 'app deactivate' App Event.
		// AppEventsLogger.deactivateApp(this.getApplicationContext(), Const.FACEBOOK_APP_ID);
	}
}
