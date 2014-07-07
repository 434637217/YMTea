package com.imcore.tupianfengzhuang.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.modl.TeaDetails;
import com.imcore.tupianfengzhuang.util.JsonUtil;
import com.imcore.tupianfengzhuang.util.ToastUtil;

public class TestFragment extends Fragment implements OnClickListener {
	
	private ProgressDialog pd1;
	private GridView gView;
	private ListView lView;
	private List<TeaDetails> teaList = new ArrayList<TeaDetails>();
	private int bb;
	private Button list;
	private Button grid;

	private Button px;
	private Button sx;
	private static int sortInt = 0;
	private static int screenInt = 0;
	private String[] prov = new String[] { "按价格升序排列", "按价格降序排列", "按上架时间升序排列",
			"按上架时间降序排列", "按销量降序排列", "按收藏降序排列" };
	private String[]screen = new String[]{"默认","0-1000","1000-2000","2000-3000",">3000"};
	private ButtonOnClick buttonOnClick = new ButtonOnClick();
	private ButtonOnClickTwo but = new ButtonOnClickTwo();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.shop_big_big, null);
		gView = (GridView) view.findViewById(R.id.shop_gridview);
		gView.setOnItemClickListener(onItem);
		lView = (ListView) view.findViewById(R.id.shop_big_listview);
		lView.setOnItemClickListener(onItem);

//		list = (Button) view.findViewById(R.id.shop_title_list);
//		list.setOnClickListener(this);
//		grid = (Button) view.findViewById(R.id.shop_title_grid);
//		grid.setOnClickListener(this);
//
//		px = (Button) view.findViewById(R.id.btn_product_sort);
//		px.setOnClickListener(this);
//		sx = (Button) view.findViewById(R.id.btn_product_shaixuan);
//		sx.setOnClickListener(this);

		
		// 获取数据
		Bundle b = getArguments();
		bb = b.getInt("give");
		new getAsyTask(bb).execute();
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.shop_title_list:
//			gView.setVisibility(View.GONE);
//			lView.setVisibility(View.VISIBLE);
//			break;
//		case R.id.shop_title_grid:
//			gView.setVisibility(View.VISIBLE);
//			lView.setVisibility(View.GONE);
//			break;
//		case R.id.btn_product_sort:
//			showSingleChoiceButton();
//			break;
//		case R.id.btn_product_shaixuan:
//			showSingleScreenButton();
//			break;
//
//		default:
//			break;
		}
	}
	
	//临时隐藏和显示布局
	public void ls(){
		gView.setVisibility(View.GONE);
		lView.setVisibility(View.VISIBLE);
	}
	public void lss(){
		gView.setVisibility(View.VISIBLE);
		lView.setVisibility(View.GONE);
	}
	
	//跳转商品详情页面的监听
	private OnItemClickListener onItem = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(getActivity(),ProductDetailsActivity.class);
			TeaDetails deta = teaList.get(arg2);
			intent.putExtra("id", deta.id);
			if(deta.id != 0){
			startActivity(intent);
			}else{
				ToastUtil.showToast(getActivity(), "商品未上架");
			}
		}
		
	};

	/*
	 * 异步加载解析商品类
	 * 
	 * @author Administrator
	 */
	private class getAsyTask extends AsyncTask<Void, Void, String> {

		private int myId;

		public getAsyTask(int id) {
			myId = id;
		}

		@Override
		protected String doInBackground(Void... params) {
			// 1.url
			String url = "/category/products.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", myId);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("tea", jsonData);

					teaList = JsonUtil.toObjectList(jsonData, TeaDetails.class);
					for (int i = 0; i < teaList.size(); i++) {
						Log.i(json, teaList.get(i).getAltName());
					}
				} else {
					//
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			gView.setAdapter(new gridAdapter());
			lView.setAdapter(new listAdapter());
			super.onPostExecute(result);
		}
	}

	/**
	 * gridView适配器
	 */

	private class gridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return teaList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return teaList.get(arg0);
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
						R.layout.shop_big_mintue, null);
				vh = new ViewHolder();
				vh.imgView = (ImageView) view
						.findViewById(R.id.shop_big_imageview);
				vh.name = (TextView) view.findViewById(R.id.shop_big_name);
				vh.cost = (TextView) view.findViewById(R.id.shop_big_cost);
				vh.sales = (TextView) view.findViewById(R.id.shop_big_sales);
				vh.collect = (TextView) view
						.findViewById(R.id.shop_big_collect);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			TeaDetails deta = teaList.get(position);
			vh.name.setText(deta.getProductName());
			vh.cost.setText(deta.getPrice() + "");
			vh.sales.setText(deta.getSaleTotal() + "");
			vh.collect.setText(deta.getFavotieTotal() + "");
			ImageFetcher fetcher = new ImageFetcher();
			fetcher.fetch(HttpHelper.DOMAIN + "/" + deta.getImageUrl(),
					vh.imgView);
			return view;
		}

		class ViewHolder {
			ImageView imgView;
			TextView name;
			TextView cost;
			TextView sales;
			TextView collect;

		}
	}

	/**
	 * listView适配器
	 */

	private class listAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return teaList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return teaList.get(arg0);
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
						R.layout.shop_big_list, null);
				vh = new ViewHolder();
				vh.imgView = (ImageView) view
						.findViewById(R.id.shop_big_imageview);
				vh.name = (TextView) view.findViewById(R.id.shop_big_name);
				vh.cost = (TextView) view.findViewById(R.id.shop_big_cost);
				vh.sales = (TextView) view.findViewById(R.id.shop_big_sales);
				vh.collect = (TextView) view
						.findViewById(R.id.shop_big_collect);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			TeaDetails deta = teaList.get(position);
			vh.name.setText(deta.getProductName());
			vh.cost.setText(deta.getPrice() + "");
			vh.sales.setText(deta.getSaleTotal() + "");
			vh.collect.setText(deta.getFavotieTotal() + "");
			ImageFetcher fetcher = new ImageFetcher();
			fetcher.fetch(HttpHelper.DOMAIN + "/" + deta.getImageUrl(),
					vh.imgView);
			return view;
		}

		class ViewHolder {
			ImageView imgView;
			TextView name;
			TextView cost;
			TextView sales;
			TextView collect;

		}
	}

	// 设置弹出框内容
	//排序
	private void showSingleChoiceButton() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("请选择排序方式");
		builder.setSingleChoiceItems(prov, sortInt, buttonOnClick);
		builder.show();
	}
	
	//筛选的弹出框内容
	public void showSingleScreenButton() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("请选择筛选方式");
		builder.setSingleChoiceItems(screen, screenInt, but);
		builder.show();
	}
	
	//对每一项进项监听
	//排序
	private class ButtonOnClick implements DialogInterface.OnClickListener

	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			sortInt = which;
			sort(which);
			dialog.dismiss();
		}
	}
	
	
	//对筛选的每一项进行监听
	
	private class ButtonOnClickTwo implements DialogInterface.OnClickListener

	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			screenInt = which;
			new anew(which).execute();
			dialog.dismiss();
		}
	}
	
	private void sort(int index) {// 排序
		switch (index) {
		case 0:// 按价格升序排序
			new asy("price", 0).execute();
			break;
		case 1:// 按价格降序排序
			new asy("price", 1).execute();
			break;
		case 2:// 按上架时间升序排序
			new asy("updatedTime", 0).execute();
			break;
		case 3:// 按上架时间降序排序
			new asy("updatedTime", 1).execute();
			break;
		case 4:// 按销量降序排序
			new asy("saleTotal", 1).execute();
			break;
		case 5:// 按收藏次数降序排序
			new asy("favotieTotal", 1).execute();
			break;
		}
	}
	
	public void aab(String a ,int b){
		new asy(a, b).execute();
	}
		// 排序后重新加载并按顺序显示
	class asy extends AsyncTask<String,Void, String>{
		private String ord;
		private int des;
		
		public  asy(String orderBy, int desc) {
			ord = orderBy;
			des = desc;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			String url = "/category/products.do";
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("id", bb);
			args.put("orderBy", ord);
			args.put("desc", des);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, args);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("resule", jsonData);

					teaList = JsonUtil.toObjectList(jsonData, TeaDetails.class);

					for (int i = 0; i < teaList.size(); i++) {
						Log.i(json, teaList.get(i).altName);
					}
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			gView.setAdapter(new gridAdapter());
			lView.setAdapter(new listAdapter());
			super.onPostExecute(result);
		}
	}
	
	//筛选后重新加载
	class anew extends AsyncTask<String,Void, String>{
		private int scr;
		
		public  anew(int position) {
			scr = position;
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			String url = "/category/products.do";
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("id", bb);
			//key要跟文档中的key对应起来
			args.put("filterId", scr);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, args);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("resule", jsonData);
					teaList = JsonUtil.toObjectList(jsonData, TeaDetails.class);
					for (int i = 0; i < teaList.size(); i++) {
						Log.i(json, teaList.get(i).altName);
					}
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			gView.setAdapter(new gridAdapter());
			lView.setAdapter(new listAdapter());
			super.onPostExecute(result);
		}
	}
}

