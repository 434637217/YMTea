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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.modl.CustomerNature;
import com.imcore.tupianfengzhuang.util.JsonUtil;
import com.imcore.tupianfengzhuang.util.ToastUtil;

public class Customer extends Activity {

	private String token;
	private String userId;
	
	private List<CustomerNature> custList;
	
	private ListView listView;
	
	private int position =-1;
	
	private Button next;
	
	boolean f = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer);
		
		listView = (ListView) findViewById(R.id.cust_listview);
		
		next = (Button) findViewById(R.id.cust_red_btn);
		next.setOnClickListener(clickListener);
		
		SharedPreferences aaa = getSharedPreferences("do", MODE_PRIVATE);
		token = aaa.getString("token", "");
		userId = aaa.getString("userId","");
		
		new asy().execute();
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.cust_red_btn:
				
				CustomerNature c=custList.get(position);
				Intent intent = new Intent(Customer.this,Affirm.class);
				intent.putExtra("id", c.id);
				intent.putExtra("name", c.userName);
				startActivity(intent);
				break;
			}
		}
	};

	
	//异步解析客服列表
	class asy extends AsyncTask<Void,Void,String>{

		@Override
		protected String doInBackground(Void... params) {
			String url = "/user/employee/list.do";
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
					Log.i("cust", jsonData);
					custList = JsonUtil.toObjectList(jsonData, CustomerNature.class);
				} else {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}
		
		@Override
		protected void onPostExecute(String result) {
			listView.setAdapter(new listAdapter());
			super.onPostExecute(result);
		}
	}
	
	
	//listview适配器
	private class listAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return custList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return custList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int arg0, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh = null;
			if (view == null) {
				view = getLayoutInflater().inflate(
						R.layout.cust_list_nature, null);
				vh = new ViewHolder();
				vh.name = (TextView) view.findViewById(R.id.cust_list_name);
				vh.bt = (RadioButton) view.findViewById(R.id.cust_list_bt);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			final CustomerNature deta = custList.get(arg0);
			vh.name.setText(deta.getUserName());
			//单独的一项的监听
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Customer.this,OneCust.class);
					intent.putExtra("id",deta.getId());
					startActivity(intent);
				}
			});
			vh.bt.setId(arg0);
			vh.bt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
						if(isChecked){
							if(position!=-1){
								RadioButton rButton=(RadioButton) Customer.this.findViewById(position);
								if(rButton!=null){
									rButton.setChecked(false);
								}
							}
						
							position=buttonView.getId();
							
						}//isChecked
				}
			});
			if(position==arg0){
				vh.bt.setChecked(true);
			}else{
				vh.bt.setChecked(false);
			}
			return view;
		}
		
		class ViewHolder {
			TextView name;
			RadioButton bt;
		}
	}
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.customer, menu);
		return true;
	}

}
