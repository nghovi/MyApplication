package com.example.vietnguyen.controllers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.myapplication.R;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends MyFragment implements View.OnClickListener
{

	public int[]	buttonIds;

	public MainFragment(){
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void buildLayout(){
        super.buildLayout();
		buttonIds = new int[]{R.id.btn_list, R.id.btn_form, R.id.btn_setting};
		switchButtonTextColor(R.id.btn_list);
        Button btnList = (Button) getView(R.id.btn_list);
        Button btnForm = (Button) getView(R.id.btn_form);
        Button btnSetting = (Button) getView(R.id.btn_setting);
        activity.registerOnClickListener(Arrays.asList((View)btnList, btnForm, btnSetting), this);
	}

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btn_list:
                onClickList();
                break;
            case R.id.btn_form:
                onClickForm();
                break;
            case R.id.btn_setting:
                onClickSetting();
                break;
            default:
                break;
        }
    }

	public void onClickSignOut(View view){
		// delete session id
		// SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		// SharedPreferences.Editor editor = pref.edit();
		// editor.remove("session_id");
		//
		// Intent intent = new Intent(this, SignIn.class);
		// startActivity(intent);
	}

	public void onClickList(){
		switchButtonTextColor(R.id.btn_list);
		ListFragment listFragment = new ListFragment();
		activity.replaceWithFragment(listFragment);
	}

	public void onClickForm(){
		switchButtonTextColor(R.id.btn_form);
		FormFragment form_fragment = new FormFragment();
		activity.replaceWithFragment(form_fragment);
	}

	public void onClickSetting(){
		switchButtonTextColor(R.id.btn_setting);
		SettingFragment setting_fragment = new SettingFragment();
		activity.replaceWithFragment(setting_fragment);
	}

	private void switchButtonTextColor(int buttonId){
		for(int i = 0; i < buttonIds.length; i++){
			Button btn = (Button)getView().findViewById(buttonIds[i]);
			if(buttonId == buttonIds[i]){
				btn.setTextColor(Color.RED);
			}else{
				btn.setTextColor(Color.BLACK);
			}
		}
	}


}
