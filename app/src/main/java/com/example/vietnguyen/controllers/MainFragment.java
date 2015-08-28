package com.example.vietnguyen.controllers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vietnguyen.core.controllers.MyFragment;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends MyFragment{
    private ProfilePictureView fbProfilePictureView;
    private TextView userNameView;


    public MainFragment(){
	}


    @Override
    public void onApiResponse(String url, JSONObject response) {
        switch(url) {
            case "":
                break;
            default:
                MU.log("hehehe url is ....... " + url);
                break;
        }
    }

    @Override
    public void onApiError(String url, String errorMsg) {

    }


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void buildLayout(){
		super.buildLayout();
        JSONObject param = new JSONObject();
        try {
            param.put("accessToken", "6af65192bde50a2849a69b2f634b41a38b6afc3b");
            param.put("userId", "72b88e38b9a4f1388b47959693673c34");
            param.put("apiKey", "23z37jp77fjxk45hnkyt8ud84g6tzugj");
            param.put("version","1");
            param.put("shopId", "00000000675");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://dev01clnt.shk.x.recruit.co.jp/api/shop/detail/";
        getApi(url, param);
    }
}
