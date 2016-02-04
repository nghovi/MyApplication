package com.nguyenhoangviet.vietnguyen.controllers;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.nguyenhoangviet.vietnguyen.controllers.Book.BookListFragment;
import com.nguyenhoangviet.vietnguyen.controllers.Note.NoteListFragment;
import com.nguyenhoangviet.vietnguyen.controllers.Task.TaskListFragment;
import com.nguyenhoangviet.vietnguyen.core.controller.Footer;
import com.nguyenhoangviet.vietnguyen.core.controller.MyActivity;
import com.nguyenhoangviet.vietnguyen.core.utils.MU;
import com.nguyenhoangviet.vietnguyen.models.Notice;
import com.nguyenhoangviet.vietnguyen.myapplication.R;
import com.nguyenhoangviet.vietnguyen.utils.GcmUtil;
//import com.facebook.AccessToken;
//import com.facebook.AccessTokenTracker;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.FacebookSdk;
//import com.facebook.Profile;
//import com.facebook.ProfileTracker;
//import com.facebook.appevents.AppEventsLogger;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;

public class MainActivity extends MyActivity implements View.OnClickListener{

	public Footer				footer;
	private SignUpInFragment	signUpInFragment;

	// private CallbackManager fbCallbackManager;
	// private ProfileTracker fbProfileTracker;
	// private Profile fbProfile;
	// private AccessTokenTracker fbAccessTokenTracker;
	// public LoginManager fbLoginManager;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// FacebookSdk.sdkInitialize(getApplicationContext());

		setContentView(R.layout.activity_main);
		onCreateFooter();

		// setUpFacebookCallbacks();

		if(mFragmentManager.getBackStackEntryCount() >= 1){ // change language case
			mFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}

		// Ensure that our profile is up to date
		// Profile.fetchProfileForCurrentAccessToken();
		// setProfile(Profile.getCurrentProfile());
		// AccessToken accessToken = AccessToken.getCurrentAccessToken();
		// for(String p : accessToken.getPermissions()){
		// MU.log("lsfslfasfsf ++++++++++ " + p);
		// }
		// if(accessToken == null || accessToken.isExpired()){
		// // gotoSignUpInFragment(); //todo
		// setAccessToken(accessToken);
		// gotoTaskListFragment();
		// }else if(!accessToken.isExpired()){
		// setAccessToken(accessToken);
		// gotoTaskListFragment();
		// }
		gotoTaskListFragment();
		bg = new Background(this);
		bg.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

		checkNotice();
	}

	private void checkNotice(){
		Bundle extra = getIntent().getExtras();
		if(extra != null){
			String noticeString = extra.getString(GcmUtil.BUNDLE_KEY_NOTICE);
			if(!MU.isEmpty(noticeString)){
				Notice notice = MU.convertToModel(noticeString, Notice.class);
				onClickFooterItem4();
			}
		}
	}

	private void gotoNoteListdFragment(){
		NoteListFragment noteListFragment = new NoteListFragment();
		replaceWithFragment(noteListFragment);
	}

	private void gotoSignUpInFragment(){
		footer.hide();
		signUpInFragment = new SignUpInFragment();
		replaceWithFragment(signUpInFragment);
	}

	private void onCreateFooter(){
		this.footer = new Footer(this);
		footer.setOnItemsClickListener(this);
		footer.checkFooterItem(3);
	}

	// private void setUpFacebookCallbacks(){
	// fbCallbackManager = CallbackManager.Factory.create();
	// fbLoginManager = LoginManager.getInstance();
	// // fbLoginManager.logInWithReadPermissions(this, Arrays.asList("user_photos"));
	// fbLoginManager.registerCallback(fbCallbackManager, new FacebookCallback<LoginResult>() {
	//
	// @Override
	// public void onSuccess(LoginResult loginResult){
	// Profile.fetchProfileForCurrentAccessToken();
	// }
	//
	// @Override
	// public void onError(FacebookException exception){
	// AccessToken.setCurrentAccessToken(null);
	// }
	//
	// @Override
	// public void onCancel(){
	// AccessToken.setCurrentAccessToken(null);
	// }
	// });
	//
	// fbProfileTracker = new ProfileTracker() {
	//
	// @Override
	// protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile){
	// AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();
	// }
	// };
	//
	// fbAccessTokenTracker = new AccessTokenTracker() {
	//
	// @Override
	// protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken){
	// Profile.fetchProfileForCurrentAccessToken();
	// setAccessToken(currentAccessToken);
	// gotoTaskListFragment();
	// }
	// };
	// }
	//
	// private void setProfile(Profile profile){
	// fbProfile = profile;
	// // fbProfile.
	// }
	//
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		// // fbCallbackManager.onActivityResult(requestCode, resultCode, data);
		// switch(requestCode){
		// case SettingFragment.INTENT_REQUEST_CODE_SEND_EMAIL:
		// Toast.makeText(this, getString(R.string.fragment_setting_thank_you), Toast.LENGTH_LONG).show();
		// break;
		// default:
		// break;
		// }
	}

	boolean	doubleBackToExitPressedOnce	= false;

	@Override
	public void onBackPressed(){
		if(getFragmentManager().getBackStackEntryCount() <= 1){
			setDoubleBackPressedToFinish();
		}else{
			backOneFragment();
		}
	}

	public void setDoubleBackPressedToFinish(){
		if(this.doubleBackToExitPressedOnce){
			finish();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, getString(R.string.main_activity_back_btn_guide), Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run(){
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
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
		gotoSettingFragment();
	}

	public void gotoSettingFragment(){
		emptyFragmentStack();
		SettingFragment frg = new SettingFragment();
		replaceWithFragment(frg);
	}

	public void onClickFooterItem2(){
		MU.log("Footer item 2 onClicked");
		gotoNoteListdFragment();
	}

	private void gotoPrimaryCardFragment(){
		PrimaryCardFragment primaryCardFragment = new PrimaryCardFragment();
		replaceWithFragment(primaryCardFragment);
	}

	public void onClickFooterItem3(){
		MU.log("Footer item 3 onClicked");
		gotoBookListFragment();
	}

	public void gotoBookListFragment(){
		emptyFragmentStack();
		BookListFragment frg = new BookListFragment();
		replaceWithFragment(frg);
	}

	public void onClickFooterItem4(){
		MU.log("Footer item 4 onClicked");
		gotoTaskListFragment();
	}

	public void gotoTaskListFragment(){
		emptyFragmentStack();
		TaskListFragment frg = new TaskListFragment();
		replaceWithFragment(frg);
	}

	@Override
	protected void onResume(){
		super.onResume();
		// facebook
		// Logs 'install' and 'app activate' App Events.
		// AppEventsLogger.activateApp(this.getApplicationContext(), Const.FACEBOOK_APP_ID);
		bg.startScheduledTasks();

	}

	@Override
	protected void onPause(){
		super.onPause();
		// Logs 'app deactivate' App Event.
		// AppEventsLogger.deactivateApp(this.getApplicationContext(), Const.FACEBOOK_APP_ID);
		bg.stopScheduledTasks();
	}

	@Override
	protected void onStop(){
		// emptyFragmentStack();
		super.onStop();
	}

	// @Override
	// protected void onSaveInstanceState(Bundle outState) {
	// //No call for super(). Bug on API Level > 11.
	// }

	@Override
	protected void onDestroy(){
		// int a = mFragmentManager.getBackStackEntryCount();
		// int x = a;
		// emptyFragmentStack();
		super.onDestroy();
	}
}
