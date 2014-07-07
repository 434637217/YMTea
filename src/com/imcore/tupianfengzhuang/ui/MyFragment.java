package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.modl.UserDetails;
import com.imcore.tupianfengzhuang.util.JsonUtil;
import com.imcore.tupianfengzhuang.util.ToastUtil;

public class MyFragment  extends Fragment{
	
	private String token;
	private String userId;
	
	private UserDetails myList;
	
	private ImageView myImage;
	private TextView userName;
	private TextView grade;
	private TextView totalPoint;
	
	private RelativeLayout shopp;
	private RelativeLayout order;
	private RelativeLayout collect;
	private RelativeLayout push;
	
	private Button edit;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.myfragment,null);
		myImage = (ImageView) view.findViewById(R.id.myfragment_imView);
		userName = (TextView) view.findViewById(R.id.myfragment_rex);
		grade = (TextView) view.findViewById(R.id.myfragment_grade);
		totalPoint = (TextView) view.findViewById(R.id.myfragment_integral);
		
		edit = (Button) view.findViewById(R.id.myfragment_compile);
		edit.setOnClickListener(clickListener);
		shopp = (RelativeLayout) view.findViewById(R.id.my_cshopp);
		shopp.setOnClickListener(clickListener);
		order = (RelativeLayout) view.findViewById(R.id.indent);
		order.setOnClickListener(clickListener);
		collect = (RelativeLayout) view.findViewById(R.id.my_collect);
		collect.setOnClickListener(clickListener);
		push = (RelativeLayout) view.findViewById(R.id.my_push);
		push.setOnClickListener(clickListener);
		
		SharedPreferences aaa = getActivity().getSharedPreferences("do", Context.MODE_PRIVATE);
		token = aaa.getString("token", "");
		userId = aaa.getString("userId","");
		new asy().execute();
		
		return view;
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
			case R.id.my_cshopp:
				Intent intent = new Intent(getActivity(),ShoppingCart.class);
				startActivity(intent);
				break;
			
			case R.id.indent:
				Intent intent1 = new Intent(getActivity(),OrderRecAcivity.class);
				startActivity(intent1);
				break;
				
			case R.id.my_push:
				ToastUtil.showToast(getActivity(), "服务器不可用");
				break;
				
			case R.id.myfragment_compile:
			Intent intent2 = new Intent(getActivity(),EditActivity.class);
			startActivity(intent2);
				break;
			
			case R.id.my_collect:
				ToastUtil.showToast(getActivity(), "服务器不可用");
				break;
				
			}
		}
	};
	
	
	
	//异步解析个人详情
	class asy extends AsyncTask<Void,Void,String>{

		@Override
		protected String doInBackground(Void... params) {
			String url = " /user/get.do";
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
					Log.i("myFragement", jsonData);
					myList = JsonUtil.toObject(jsonData, UserDetails.class);
				} else {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}
		
		@Override
		protected void onPostExecute(String result) {
			userName.setText(myList.getUserName());
			grade.setText("等级：" + String.valueOf(myList.getGrade()));
			totalPoint.setText("积分：" + String.valueOf(myList.getTotalPoint()));
			ImageFetcher fetcher = new ImageFetcher();
			fetcher.fetch(HttpHelper.DOMAIN + "/" + myList.getAvatarUrl(),
					myImage);
			
			super.onPostExecute(result);
		}
	}

}
