package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.deta.Details;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.util.JsonUtil;

public class ShopFragment extends Fragment {

	private List<Details> bigList;
	private ListView lv;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		login();
		View view = inflater.inflate(R.layout.shop_listview, null);
		lv = (ListView) view.findViewById(R.id.shop_list_view);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				// Fragment之间传值，通过Bundle进行传递
//				Bundle b = new Bundle();
//				b.putInt("id", arg2 + 1);
//				ShopSub ss = new ShopSub();
//				ss.setArguments(b);
//				FragmentTransaction ft = getFragmentManager()
//						.beginTransaction();
				// ft.add(new ShopSub(), "shopSub");
//				ft.replace(getId(), ss);
//				ft.commit();
				// ft.replace(R., arg1)
				// ((MainActivity)getActivity()).change(b);

				
				Intent intent = new Intent(getActivity(),ShopActivity.class);
				intent.putExtra("id", arg2 + 1);
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
			return bigList.size() - 4;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return bigList.get(arg0);
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
			Details deta = bigList.get(position);
			vh.textView.setText(deta.getDisplayName());
			// vh.imgView.setImageBitmap(deta.getImageUrl());
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
		new getAsyTask(id).execute();
	}

	/**
	 * 异步加载解析商品类
	 * 
	 * @author Administrator
	 * 
	 */
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

					bigList = JsonUtil.toObjectList(jsonData, Details.class);
					for (int i = 0; i < bigList.size(); i++) {
						Log.i(json, bigList.get(i).displayName);
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

			lv.setAdapter(new listAdapter());
			super.onPostExecute(result);
		}
	}

}
