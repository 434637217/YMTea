package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.modl.Shop;
import com.imcore.tupianfengzhuang.ui.TestFragment.asy;
import com.imcore.tupianfengzhuang.util.JsonUtil;

public class ShopActivity extends ActionBarActivity {


	private List<Shop> sList;
	ActionBar actionBar;
	private ViewPager vPager;
	Map<Integer,TestFragment> tfMap;
	private static int sortInt = 0;
	private String[] prov = new String[] { "按价格升序排列", "按价格降序排列", "按上架时间升序排列",
			"按上架时间降序排列", "按销量降序排列", "按收藏降序排列" };
	private ButtonOnClick buttonOnClick = new ButtonOnClick();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop);
		vPager = (ViewPager) findViewById(R.id.viewpager);
		

		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		int id = getIntent().getIntExtra("id",0);
		new getAsyTask(id).execute();
	}
	
	private OnPageChangeListener onpager = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			getSupportActionBar().setSelectedNavigationItem(arg0);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		
	};
	

	private class getAsyTask extends AsyncTask<Void, Void, String> {
		private int myId;

		public getAsyTask(int id) {
			myId = id;
		}

		@Override
		protected String doInBackground(Void... params) {
			// 1.url
			String url = "/category/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", myId);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("resule", jsonData);

					sList = JsonUtil.toObjectList(jsonData, Shop.class);
					for (int i = 0; i < sList.size(); i++) {
						Log.i(json, sList.get(i).displayName);
					}
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return json;
		}
	

		@Override
		protected void onPostExecute(String result) {
			vp = new viewPager();
			tfMap = new HashMap<Integer, TestFragment>();
			vPager.setAdapter(vp);
			vPager.setOnPageChangeListener(onpager);
			for(int i = 0;i<sList.size();i++){
				Tab tab = actionBar.newTab();
				tab.setText(sList.get(i).getCategoryName());
				tab.setTabListener(tabListener);
				actionBar.addTab(tab);
			}
			super.onPostExecute(result);
		}
	}
	
	viewPager vp ;
	private TabListener tabListener = new TabListener() {
		
		@Override
		public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		}
		
		@Override
		public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
			vPager.setCurrentItem(arg0.getPosition());
		}
		
		@Override
		public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		}
		
	};
	
	private class  viewPager extends FragmentStatePagerAdapter{

		public viewPager() {
			super(getSupportFragmentManager());
		}

		@Override
		public Fragment getItem(int arg0) {
			Bundle b = new Bundle();
			b.putInt("give",sList.get(arg0).getId());
			TestFragment test = new TestFragment();
			test.setArguments(b);
			tfMap.put(arg0, test);
			return test;
		}

		@Override
		public int getCount() {
			return sList.size();
		}
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.shop, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {  
//        case android.R.id.home:  
//            // 当ActionBar图标被点击时调用  
//            
//            return true; 
		case R.id.line:
//        	TestFragment aa = (TestFragment) vp.getItem(vPager.getCurrentItem());
//        	aa.ls();
			tfMap.get(vPager.getCurrentItem()).ls();
			return true;
            
		case R.id.grid:
			tfMap.get(vPager.getCurrentItem()).lss();
			return true;
			
        case R.id.action_settings:
        	showSingleChoiceButton();
        	return true;
        	
        case R.id.menu_search:
        	tfMap.get(vPager.getCurrentItem()).showSingleScreenButton();
        	return true;
        	
        	
        default:
        	return super.onOptionsItemSelected(item);
        }  
	}
	
	//排序
		private void showSingleChoiceButton() {
			AlertDialog.Builder builder = new AlertDialog.Builder(ShopActivity.this);
			builder.setTitle("请选择排序方式");
			builder.setSingleChoiceItems(prov, sortInt, buttonOnClick);
			builder.show();
		}

		//对每一项进项监听
		//排序
		private class ButtonOnClick implements DialogInterface.OnClickListener

		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				sortInt = which;
				sort(which);
				dialog.dismiss();
			}
		}
		
		private void sort(int index) {// 排序
			switch (index) {
			case 0:// 按价格升序排序
				tfMap.get(vPager.getCurrentItem()).aab("price", 0);
				break;
			case 1:// 按价格降序排序
				tfMap.get(vPager.getCurrentItem()).aab("price", 1);
				break;
			case 2:// 按上架时间升序排序
				tfMap.get(vPager.getCurrentItem()).aab("updatedTime", 0);
				break;
			case 3:// 按上架时间降序排序
				tfMap.get(vPager.getCurrentItem()).aab("updatedTime", 1);
				break;
			case 4:// 按销量降序排序
				tfMap.get(vPager.getCurrentItem()).aab("saleTotal", 1);
				break;
			case 5:// 按收藏次数降序排序
				tfMap.get(vPager.getCurrentItem()).aab("favotieTotal", 1);
				break;
			}
		}
		
}
