package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.tupianfengzhuang.R;
import com.example.tupianfengzhuang.R.layout;
import com.example.tupianfengzhuang.R.menu;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.modl.ServiceDetails;
import com.imcore.tupianfengzhuang.modl.StoreNature;
import com.imcore.tupianfengzhuang.util.JsonUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ServiceDetailsActivity extends Activity {
	
	private long ids;
	private String name;
	
	private TextView title;
	
	private List<ServiceDetails> sNature;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_details);
		
		title = (TextView) findViewById(R.id.service_taitle);
		listView = (ListView) findViewById(R.id.service_listview);
		
		ids = getIntent().getLongExtra("store_id",0);
		name = getIntent().getStringExtra("names");
		
		title.setText(name);
		
		new subkey().execute();
	}

	//异步解析相关现时的数据
		class subkey extends AsyncTask<String,Void,String>{

			@Override
			protected String doInBackground(String... arg0) {
				
				
				String url = "/store/employee/list.do";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id",ids );
				RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
				String json = "";
				try {
					json = HttpHelper.execute(entity);
					ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
					if (entity2.getStatus() == 200) {
						String jsonData = entity2.getData();
						Log.i("mdgy", jsonData);
						sNature = JsonUtil.toObjectList(jsonData, ServiceDetails.class);
						for(ServiceDetails s:sNature){
							Log.i("cc",s + "");
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
								R.layout.service_details, null);
						vh = new ViewHolder();
						vh.imgView = (ImageView) view
								.findViewById(R.id.service_image);
						vh.name = (TextView) view.findViewById(R.id.service_name);
						vh.site = (TextView) view.findViewById(R.id.service_jobTitle);
						vh.con = (TextView) view .findViewById(R.id.service_company);
						view.setTag(vh);
					} else {
						vh = (ViewHolder) view.getTag();
					}
					ServiceDetails deta = sNature.get(position);
					vh.name.setText("姓 名:" + "   " + deta.getName());
					vh.site.setText("职 位:" + "   " + deta.getJobTitle());
					vh.con.setText("店 名:" + "   " + deta.getCompany());
					ImageFetcher fetcher = new ImageFetcher();
					fetcher.fetch(HttpHelper.DOMAIN + "/" + deta.getAvatarUrl(),
							vh.imgView);
					return view;
				}

				class ViewHolder {
					ImageView imgView;
					TextView name;
					TextView site;
					TextView con;
				}
			}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.service_details, menu);
		return true;
	}

}
