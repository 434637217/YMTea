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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.modl.ConmmentDetails;
import com.imcore.tupianfengzhuang.util.JsonUtil;

public class CommentActivity extends Activity {
	
	private String token;
	private String userId;

	private long id;
	private List<ConmmentDetails>list;
	private ListView listView;
	private EditText details;
	private Button tj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		listView = (ListView) findViewById(R.id.comment_listview);
		details = (EditText) findViewById(R.id.comment_eidttext);
		tj = (Button) findViewById(R.id.commentt_tj);
		tj.setOnClickListener(clickListener);
		
		SharedPreferences aaa = getSharedPreferences("do", MODE_PRIVATE);
		token = aaa.getString("token", "");
		userId = aaa.getString("userId","");
		
		id=getIntent().getLongExtra("id",0);
		
		
		new subkey().execute();
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
			case R.id.commentt_tj:
				new review().execute();
				break;
			}
		}
	};
	
	//异步解析相关现时的数据
		class subkey extends AsyncTask<String,Void,String>{

			@Override
			protected String doInBackground(String... arg0) {
				String url = "/product/comments/list.do";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id",id);
				RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
				String json = "";
				try {
					json = HttpHelper.execute(entity);
					ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
					if (entity2.getStatus() == 200) {
						String jsonData = entity2.getData();
						Log.i("comment", jsonData);
						list = JsonUtil.toObjectList(jsonData, ConmmentDetails.class);
						for(ConmmentDetails s:list){
							Log.i("comment",s.toString());
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
		
		//评论产品
		class review extends AsyncTask<String,Void,String>{

			@Override
			protected String doInBackground(String... arg0) {
				
				
				String url = "/product/comments/add.do";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id",id);
				map.put("comment", details.getText().toString());
				map.put("token",token);
				map.put("userId",userId);
				RequestEntity entity = new RequestEntity(url, HttpMethod.POST, map);
				String json = "";
				try {
					json = HttpHelper.execute(entity);
					ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
					if (entity2.getStatus() == 200) {
						String jsonData = entity2.getData();
//						}
						
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
				details.setText("");
				new subkey().execute();
				listView.setAdapter(new listAdapter());
			}
		}
		
		//适配器
				private class listAdapter extends BaseAdapter {

					@Override
					public int getCount() {
						return list.size();
					}

					@Override
					public Object getItem(int arg0) {
						return list.get(arg0);
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
									R.layout.comment, null);
							vh = new ViewHolder();
							vh.name = (TextView) view.findViewById(R.id.comment_name);
							vh.matter = (TextView) view.findViewById(R.id.comment_matter);
							vh.time = (TextView) view.findViewById(R.id.comment_time);
							view.setTag(vh);
						} else {
							vh = (ViewHolder) view.getTag();
						}
						ConmmentDetails deta = list.get(position);
						vh.name.setText(deta.getUserName());
						vh.matter.setText(deta.getComments());
						vh.time.setText(deta.getCreatedTime());
						
						return view;
					}

					class ViewHolder {
						TextView name;
						TextView matter;
						TextView time;
					}
				}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment, menu);
		return true;
	}

}
