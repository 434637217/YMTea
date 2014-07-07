package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.modl.OrderGallery;
import com.imcore.tupianfengzhuang.modl.OrderRecord;
import com.imcore.tupianfengzhuang.util.JsonUtil;

public class OrderRecAcivity extends Activity {
	
	private List<OrderRecord> ord;
	
	private String token;
	private String userId;
	
	private ListView listView;
	
	private List<OrderGallery> gallList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_rec_acivity);
		
		listView = (ListView) findViewById(R.id.order_listview);

		SharedPreferences aaa = getSharedPreferences("do", MODE_PRIVATE);
		token = aaa.getString("token", "");
		userId = aaa.getString("userId","");
		new subkey().execute();
	}

	//异步解析记录内容
	class subkey extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... arg0) {
			
			
			String url = "/order/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("type",1);
			map.put("token",token);
			map.put("userId",userId);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("order", jsonData);
					ord = JsonUtil.toObjectList(jsonData, OrderRecord.class);
					
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
				return ord.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return ord.get(arg0);
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
							R.layout.order_listview_details, null);
					vh = new ViewHolder();
					vh.time = (TextView) view.findViewById(R.id.time);
					vh.order_sl = (TextView) view.findViewById(R.id.order_sl);
					vh.order_moner = (TextView) view.findViewById(R.id.order_moner);
					vh.amount = (TextView) view.findViewById(R.id.amount);
					vh.gallery = (Gallery) view.findViewById(R.id.order_gallery);
					vh.name = (TextView) view.findViewById(R.id.order_name);
					view.setTag(vh);
				} else {
					vh = (ViewHolder) view.getTag();
				}
				gallList = JsonUtil.toObjectList(ord.get(position).orderItems, OrderGallery.class);
				OrderRecord deta = ord.get(position);
				vh.time.setText(deta.getOrderDate());
				
				int tem = 0;
				for(int i = 0; i<=gallList.size();i++){
					tem++;
				}
				vh.order_sl.setText(tem+"");
				vh.order_moner.setText("￥" + String.valueOf(deta.getTotalAmount()));
				vh.amount.setText(String.valueOf(deta.getId()));
				vh.gallery.setAdapter(new Images());
				vh.name.setText(gallList.get(0).getItemName());
				return view;
			}

			class ViewHolder {
				TextView time;
				TextView order_sl;
				TextView order_moner;
				TextView amount;
				Gallery gallery;
				TextView name;
			}
		}
		
		class Images extends BaseAdapter{
			
			@Override
			public int getCount() {
				return gallList.size();
			}

			@Override
			public Object getItem(int arg0) {
				return gallList.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return gallList.get(arg0).id;
			}

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				View view = getLayoutInflater().inflate(R.layout.order_gallery_image, null);
				ImageView im = (ImageView) view.findViewById(R.id.order_gallery_image);
				 ImageFetcher fetcher = new ImageFetcher();
					fetcher.fetch(HttpHelper.DOMAIN + "/" + gallList.get(arg0).imageUrl,
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
		getMenuInflater().inflate(R.menu.order_rec_acivity, menu);
		return true;
	}

}
