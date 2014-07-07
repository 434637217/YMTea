package com.imcore.tupianfengzhuang.ui;

import com.example.tupianfengzhuang.R;
import com.example.tupianfengzhuang.R.layout;
import com.example.tupianfengzhuang.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SubmitActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.submit, menu);
		return true;
	}

}
