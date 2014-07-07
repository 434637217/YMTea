package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.modl.CartShopp;
import com.imcore.tupianfengzhuang.modl.CartTwo;
import com.imcore.tupianfengzhuang.util.JsonUtil;

public class Affirm extends Activity {

	private TextView tx;
	private TextView vip;
	
	private CartShopp zList;
	private List<CartTwo>bList;
	private String token;
	private String userId;
	
	private ListView listView;
	
	private TextView custname;
	
	private long id;
	private String name;
	
	private Button refer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affirm);
		
		tx = (TextView) findViewById(R.id.affirm_original);
		vip = (TextView) findViewById(R.id.affirm_vip);
		
		listView = (ListView) findViewById(R.id.affirm_listview);
		
		custname = (TextView) findViewById(R.id.affirm_cust);
		
		refer = (Button) findViewById(R.id.affirm_red_btn);
		refer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new Refer().execute();
			}
		});
		
		id = getIntent().getLongExtra("id",0);
		name = getIntent().getStringExtra("name");
		
		custname.setText("指定客服:" +"           " + name);
		
		SharedPreferences aaa = getSharedPreferences("do", MODE_PRIVATE);
		token = aaa.getString("token", "");
		userId = aaa.getString("userId","");
		new subkey().execute();
	}
	
	
	//解析子项列表
	class subkey extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... arg0) {
			
			
			String url = "/orderitem/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("token",token);
			map.put("userId",userId);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("zidingdan", jsonData);
					zList = JsonUtil.toObject(jsonData, CartShopp.class);
					bList = JsonUtil.toObjectList(zList.orderItems, CartTwo.class);
					for(CartTwo abc:bList){
						Log.i("aa",abc.imageUrl);
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
			tx.setText("原价：" +  "￥" + zList.getTotalAmount());
			vip.setText("会员价："+  "￥" + zList.getDiscount());
			listView.setAdapter(new listAdapter());
			super.onPostExecute(result);
			
		}
	}
	
	//listview适配器
	private class listAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return bList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh = null;
			if (view == null) {
				view = getLayoutInflater().inflate(
						R.layout.affirm_return, null);
				vh = new ViewHolder();
				vh.imgView = (ImageView) view
						.findViewById(R.id.affirm_image);
				vh.name = (TextView) view.findViewById(R.id.affirm_name);
				vh.synthesize = (TextView) view.findViewById(R.id.affirm_ks);
				vh.sales = (TextView) view.findViewById(R.id.affirm_money);
				vh.collect = (TextView) view
						.findViewById(R.id.affirm_js);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			CartTwo deta = bList.get(position);
			vh.name.setText(deta.getItemName() + "");
			vh.synthesize.setText(deta.getPacking() + " " + deta.getWeight()+"g");
			vh.sales.setText("￥" + deta.getRetailPrice() + "");
			vh.collect.setText("共"+ deta.getQuantity() + "件");
			ImageFetcher fetcher = new ImageFetcher();
			fetcher.fetch(HttpHelper.DOMAIN + "/" + deta.getImageUrl(),
					vh.imgView);
			return view;
		}

		class ViewHolder {
			ImageView imgView;
			TextView name;
			TextView synthesize;
			TextView sales;
			TextView collect;

		}
	}
	
	//提交异步线程
	class Refer extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... params) {

			String url = "/order/commit.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId",userId);
			map.put("employeeId", id);
			map.put("customerId", userId);
			map.put("token",token);
			map.put("id", zList.id);
			
			RequestEntity entity = new RequestEntity(url, HttpMethod.POST, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("tijiao", jsonData);
					
				} else {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Intent intent = new Intent(Affirm.this,SubmitActivity.class);
			startActivity(intent);
		}
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.affirm, menu);
		return true;
	}

}
