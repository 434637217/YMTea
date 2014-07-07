package com.imcore.tupianfengzhuang.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.modl.Shop;

public class MainActivity extends FragmentActivity {
	
	private FragmentTabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTabHost = (FragmentTabHost) findViewById(R.id.tab_host);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.frgmt_container);

		View indicator1 = getIndicatorView(R.drawable.tab_indicator, "首页");
		TabSpec spec1 = mTabHost.newTabSpec("shouye");
		spec1.setIndicator(indicator1);
		Bundle args1 = new Bundle();
		args1.putString("title", "shouye");
		mTabHost.addTab(spec1, FirstFragment.class, args1);
		
		View indicator2 = getIndicatorView(R.drawable.tab_shop, "商城");
		TabSpec spec2 = mTabHost.newTabSpec("shop");
		spec2.setIndicator(indicator2);
		Bundle args2 = new Bundle();
		args2.putString("title", "shop");
		mTabHost.addTab(spec2, ShopFragment.class, args2);
		
		View indicator3 = getIndicatorView(R.drawable.tab_my, "我的");
		TabSpec spec3 = mTabHost.newTabSpec("my");
		spec3.setIndicator(indicator3);
		Bundle args3 = new Bundle();
		args3.putString("title", "my");
		mTabHost.addTab(spec3, MyFragment.class, args3);
		
		View indicator4 = getIndicatorView(R.drawable.tab_more, "更多");
		TabSpec spec4 = mTabHost.newTabSpec("gengduo");
		spec4.setIndicator(indicator4);
		Bundle args4 = new Bundle();
		args4.putString("title", "gengduo");
		mTabHost.addTab(spec4, MoreFragment.class, args4);
		
	}
	public void change(Bundle b){
//		FragmentManager fm = getSupportFragmentManager();
//		
//		FragmentTransaction ft = fm.beginTransaction();
//		
//		fm.putFragment(b, "a", new ShopSub());
//		ft.replace(1, new ShopSub());
		
	}
	private View getIndicatorView(int drawableId, String title) {
		View view = getLayoutInflater().inflate(R.layout.view_tab_indicator,
				null);
		ImageView imgIcon = (ImageView) view.findViewById(R.id.img_icon);
		imgIcon.setImageResource(drawableId);
		TextView tvTitle = (TextView) view.findViewById(R.id.tab_title);
		tvTitle.setText(title);

		return view;
	}

@Override
protected void onDestroy() {
	mTabHost = null;
	super.onDestroy();
}

}
