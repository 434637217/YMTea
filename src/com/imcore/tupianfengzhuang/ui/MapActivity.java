package com.imcore.tupianfengzhuang.ui;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.tupianfengzhuang.R;
import com.example.tupianfengzhuang.R.drawable;
import com.example.tupianfengzhuang.R.id;
import com.example.tupianfengzhuang.R.layout;

public class MapActivity extends Activity {

	private static final String MAP_KEY="CfluU8FacC0thCm7kr1GLqLX";
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private long jd;
	private long wd;
	private String nm;
	private TextView name;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_map);
		
		name = (TextView) findViewById(R.id.db);
		
		mMapView=(MapView) findViewById(R.id.bmapView);
		mBaiduMap=mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通地图
		
		jd = getIntent().getLongExtra("jd",0);
		wd = getIntent().getLongExtra("wd",0);
		nm = getIntent().getStringExtra("name");
		
		name.setText(nm);
		initView();
	}
	
	private void initView(){
		
		//定义Marker坐标点
		LatLng point=new LatLng(DoubleFormat(wd), DoubleFormat(jd));
		//构建Marker图标
		BitmapDescriptor descriptor=BitmapDescriptorFactory.fromResource(R.drawable.map_marker_red);
		//构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option=new MarkerOptions().position(point).icon(descriptor);
		//在地图上添加Marker并显示
		mBaiduMap.addOverlay(option);
	}
	
	private double DoubleFormat(double d){
		DecimalFormat df=new DecimalFormat("0.000000");
		return Double.parseDouble(df.format(d));
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	


}
