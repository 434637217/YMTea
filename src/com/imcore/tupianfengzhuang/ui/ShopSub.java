package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.imcore.tupianfengzhuang.modl.Shop;
import com.imcore.tupianfengzhuang.util.JsonUtil;

public class ShopSub extends Fragment {

	private List<Shop> sList;
	private ListView lView;
	private Button bt;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		login();
		View view = inflater.inflate(R.layout.shopsub, null);
		lView = (ListView) view.findViewById(R.id.shopsub_listview);
		bt = (Button) view.findViewById(R.id.go);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),ShopFragment.class);
				startActivity(intent);
				
			}
		});

		return view;
	}

	// ListView适配器
	private class listAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return sList.get(arg0);
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
				view = getActivity().getLayoutInflater().inflate(
						R.layout.shop_listview_son, null);
				vh = new ViewHolder();
				vh.imgView = (ImageView) view.findViewById(R.id.shop_image);
				vh.textView = (TextView) view.findViewById(R.id.shop_text_son);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			Shop deta = sList.get(position);
			vh.textView.setText(deta.getCategoryName());
			ImageFetcher fetcher = new ImageFetcher();
			fetcher.fetch(HttpHelper.DOMAIN + "/" + deta.getImageUrl(),
					vh.imgView);
			return view;
		}

		class ViewHolder {
			ImageView imgView;
			TextView textView;
		}
	}

	private void login() {
		int id = 0;
		new getAsyTask().execute();
	}

	/**
	 * 异步加载解析商品类
	 * 
	 * @author Administrator
	 * 
	 */
	private class getAsyTask extends AsyncTask<Void, Void, String> {

		//接收传递的参数
		Bundle b = getArguments();
		int as = b.getInt("id");
		@Override
		protected String doInBackground(Void... params) {
			// 1.url
			String url = "/category/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", as);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("shop", jsonData);

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

			lView.setAdapter(new listAdapter());
			super.onPostExecute(result);
		}
	}
	
}
