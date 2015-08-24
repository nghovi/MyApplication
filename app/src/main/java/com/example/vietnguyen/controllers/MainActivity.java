package com.example.vietnguyen.controllers;

import android.os.Bundle;
import android.view.View;

import com.example.vietnguyen.core.controllers.Footer;
import com.example.vietnguyen.core.controllers.MyActivity;
import com.example.vietnguyen.core.utils.MU;
import com.example.vietnguyen.myapplication.R;

public class MainActivity extends MyActivity implements View.OnClickListener{

	public Footer	footer;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MainFragment mainFragment = new MainFragment();
		replaceWithFragment(mainFragment);
		onCreateFooter();
	}

	private void onCreateFooter(){
		this.footer = new Footer(this);
		footer.setOnItemsClickListener(this);
		footer.checkFooterItem(0);
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
}
