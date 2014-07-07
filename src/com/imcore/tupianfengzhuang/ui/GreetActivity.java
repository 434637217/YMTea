package com.imcore.tupianfengzhuang.ui;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.deta.Details;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.modl.TopImages;
import com.imcore.tupianfengzhuang.util.ConnectivityUtil;
import com.imcore.tupianfengzhuang.util.JsonUtil;
import com.imcore.tupianfengzhuang.why.MyApplicition;

public class GreetActivity extends Activity {
	private ImageView imageView;
	List<TopImages> topList;
	List<Details> shopList;
	// 当前已下载的图片个数
	private int mDownloadCount;
	private Object mLock = new Object();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_greet);
		imageView = (ImageView) findViewById(R.id.greed_view);

		if (ConnectivityUtil.isOnline(this) != false) {
			new ImageTask().execute();
		} else {
			Toast.makeText(GreetActivity.this, "当前无网络连接", Toast.LENGTH_SHORT)
					.show();
			GreetActivity.this.finish();
		}
	}

	class ImageTask extends AsyncTask<Void, Void, Void> {

		/**
		 * 根据图片地址进行解析
		 * 
		 */

		@Override
		protected Void doInBackground(Void... params) {
			String url = "/topline/list.do";
			RequestEntity entity = new RequestEntity(url, 0, null);
			try {
				String js = HttpHelper.execute(entity);
				ResponseJsonEntity rjs = ResponseJsonEntity.fromJSON(js);
				String data = rjs.getData();
				Log.i("data", data);
				Class<TopImages> clss = TopImages.class;
				topList = JsonUtil.toObjectList(data, clss);
				for (int i = 0; i < topList.size(); i++) {
					Log.i("dt", topList.get(i).getImageUrl());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			for (int i = 0; i < topList.size(); i++) {
				new dawlTask().execute(HttpHelper.DOMAIN + "/"
						+ topList.get(i).getImageUrl());
			}
			super.onPostExecute(result);
		}
	}
	
	

	class dawlTask extends AsyncTask<String, Void, Void> {
		private String imgUrl;

		@Override
		protected Void doInBackground(String... params) {
			imgUrl = params[0];
			boolean isSucc = ImageFetcher.downLoadImage(imgUrl);
			Log.i("img", isSucc + "");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			synchronized (mLock) {
				MyApplicition.tpList.add(imgUrl);
				mDownloadCount++;
				if (mDownloadCount >= topList.size()) {
					//根据SharedPreferences判断当前账号密码是否保存，如果有存直接跳转到首页，反之....
					SharedPreferences settings = getSharedPreferences("do", Context.MODE_PRIVATE);
					boolean tt = settings.getBoolean("fuck", false);
					if(!tt){
					Intent intent = new Intent(GreetActivity.this,
							DebarkActivity.class);
					GreetActivity.this.startActivity(intent);
					GreetActivity.this.finish();
					}else{
						Intent intent = new Intent(GreetActivity.this,
								MainActivity.class);
						GreetActivity.this.startActivity(intent);
						GreetActivity.this.finish();
					}
				}
			}

			super.onPostExecute(result);
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if(hasFocus == true){
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		}
		super.onWindowFocusChanged(hasFocus);
	}
	

}