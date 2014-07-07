package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
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
import com.imcore.tupianfengzhuang.modl.StoreNature;
import com.imcore.tupianfengzhuang.util.JsonUtil;

public class StoresActivity extends Activity {

	private List<StoreNature> sNature;
	private ListView listView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		
		listView = (ListView) findViewById(R.id.store_listview);
		
		new subkey().execute();
	}
	

	
	
	//异步解析相关现时的数据
	class subkey extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... arg0) {
			
			
			String url = "/store/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("zidingdan", jsonData);
					sNature = JsonUtil.toObjectList(jsonData, StoreNature.class);
					for(StoreNature s:sNature){
						Log.i("aa",s.toString());
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
			listView.setAdapter(new listAdapter());
			super.onPostExecute(result);
		}
	}
	
	//适配器
		private class listAdapter extends BaseAdapter {

			@Override
			public int getCount() {
				return sNature.size();
			}

			@Override
			public Object getItem(int arg0) {
				return sNature.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = convertView;
				ViewHolder vh = null;
				if (view == null) {
					view = getLayoutInflater().inflate(
							R.layout.store_details, null);
					vh = new ViewHolder();
					vh.imgView = (ImageView) view
							.findViewById(R.id.store_image);
					vh.name = (TextView) view.findViewById(R.id.store_name);
					vh.site = (TextView) view.findViewById(R.id.store_details);
					vh.btn = (Button) view.findViewById(R.id.btn_map);
					vh.store = (Button) view.findViewById(R.id.btn_stite);
					view.setTag(vh);
				} else {
					vh = (ViewHolder) view.getTag();
				}
				final StoreNature deta = sNature.get(position);
				System.out.println(deta.latitude + "-" + deta.longitude + "==" + deta.id);
				vh.name.setText(deta.getName());
				vh.site.setText(deta.getAddress() + "");
				ImageFetcher fetcher = new ImageFetcher();
				fetcher.fetch(HttpHelper.DOMAIN + "/" + deta.getPictureUrl(),
						vh.imgView);
				vh.btn.setTag(deta);
				vh.btn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						StoreNature d = (StoreNature)arg0.getTag();
						Intent intent = new Intent(StoresActivity.this,MapActivity.class);
						intent.putExtra("jd",d.getLongitude());
						intent.putExtra("wd",d.getLatitude());
						Log.i("latitude", d.latitude+"");
						intent.putExtra("name",d.getName());
						startActivity(intent);
					}
				});
				vh.store.setTag(deta);
				vh.store.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						StoreNature a = (StoreNature)arg0.getTag();
						Intent intent = new Intent(StoresActivity.this,ServiceDetailsActivity.class);
						intent.putExtra("store_id",a.getId());
						Log.i("storeId", a.id+"");
						intent.putExtra("names",a.getName());
						startActivity(intent);
						
					}
				});
				
				return view;
			}

			class ViewHolder {
				ImageView imgView;
				TextView name;
				TextView site;
				Button btn;
				Button store;
			}
		}
		
	

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.store, menu);
		return true;
	}

}

