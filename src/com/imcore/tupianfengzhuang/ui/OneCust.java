package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.modl.CustImage;
import com.imcore.tupianfengzhuang.modl.OneCustnature;
import com.imcore.tupianfengzhuang.util.JsonUtil;

public class OneCust extends Activity {
	
	private List<CustImage> custImage;
	private OneCustnature oneList;
	private long myId ;
	
	private TextView name;
	private TextView age;
	private TextView store;
	private TextView phone;
	
	private Gallery gallery;
	
	private Button go;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one_cust);
		name = (TextView) findViewById(R.id.one_cust_name);
		age = (TextView) findViewById(R.id.age);
		store = (TextView) findViewById(R.id.store);
		phone = (TextView) findViewById(R.id.phone_c);
				
		gallery=(Gallery) findViewById(R.id.one_cust_gallery);
		
		go = (Button) findViewById(R.id.one_cust_returns);
		go.setOnClickListener(clickListener);
		
		myId = getIntent().getLongExtra("id",0);
		Log.i("dadeid",myId + "");
		new asy().execute();
	}

	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.one_cust_returns:
				OneCust.this.finish();
			}
		}
	};
	
	//异步解析数据
	class asy extends AsyncTask<Void,Void,String>{

		@Override
		protected String doInBackground(Void... arg0) {
			String url = "/store/employee/get.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id",myId);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("cc", jsonData);
					oneList = JsonUtil.toObject(jsonData, OneCustnature.class);
					custImage = JsonUtil.toObjectList(oneList.getUserPictures(),CustImage.class);
					for(CustImage a : custImage){
					Log.i("iamge", a.getPictureUrl());
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
			name.setText(oneList.getUserName());
			age.setText(String.valueOf(oneList.getAge()));
			store.setText(oneList.getCompany());
			phone.setText(oneList.getPhoneNumber());
			
			gallery.setAdapter(new Image());
			super.onPostExecute(result);
		}
	}
	
	//gallery适配器
	class Image extends BaseAdapter{
		
		@Override
		public int getCount() {
			return custImage.size();
		}

		@Override
		public Object getItem(int arg0) {
			return custImage.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return custImage.get(arg0).getId();
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = getLayoutInflater().inflate(R.layout.cust_image, null);
			ImageView im = (ImageView) view.findViewById(R.id.cust_image_image);
			 ImageFetcher fetcher = new ImageFetcher();
				fetcher.fetch(HttpHelper.DOMAIN + "/" + custImage.get(arg0).getPictureUrl(),
						im);
			return view;
		}
		
		class viewHolder{
			ImageView imageview;
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.one_cust, menu);
		return true;
	}

}
