package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.modl.ProductDetails;
import com.imcore.tupianfengzhuang.modl.SeekPress;
import com.imcore.tupianfengzhuang.modl.SeekProduct;
import com.imcore.tupianfengzhuang.ui.ProductDetailsActivity.Image;
import com.imcore.tupianfengzhuang.util.JsonUtil;
import com.imcore.tupianfengzhuang.util.ToastUtil;

public class SeekDetailsActivity extends Activity {
	
	private ListView listView;
	private String keys;
	private Integer types;
	private List<SeekProduct> prList;
	private List<SeekPress>preList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seek_details);
		
		ToastUtil.showToast(SeekDetailsActivity.this,"服务器异常");
		SeekDetailsActivity.this.finish();
		listView = (ListView) findViewById(R.id.seek_details_listview);
		
		keys = getIntent().getStringExtra("st");
		types = getIntent().getIntExtra("in",0);
		
		new asyTask().execute();
	}
	
	//解析所需内容
		class asyTask extends AsyncTask<Void,Void,String>{
			
			@Override
			protected String doInBackground(Void... arg0) {
				String url = "/search/keyword.do";
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("keyword", keys);
				map.put("type", types);
				RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
				String json = "";
				try {
					json = HttpHelper.execute(entity);
					ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
					if (entity2.getStatus() == 200) {
						String jsonData = entity2.getData();
						Log.i("seek", jsonData);
						
						if(types ==1){
						prList = JsonUtil.toObjectList(jsonData, SeekProduct.class);
						for(SeekProduct s:prList){
							Log.i("ss",s + "");
						}
						}else if(types == 2){
							preList=JsonUtil.toObjectList(jsonData, SeekPress.class);
						}
						
					} else {
						ToastUtil.showToast(SeekDetailsActivity.this,"服务器不可用");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return json;
			}
				
			@Override
			protected void onPostExecute(String result) {
				
				super.onPostExecute(result);
			}
			
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.seek_details, menu);
		return true;
	}

}
