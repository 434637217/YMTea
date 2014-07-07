package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.modl.ImageAlone;
import com.imcore.tupianfengzhuang.modl.ShopBz;
import com.imcore.tupianfengzhuang.ui.ShoppingCart.subkey;
import com.imcore.tupianfengzhuang.util.JsonUtil;

public class PackingActivity extends Activity {
	
	private TextView bz;
	private TextView money;
	private TextView whit;
	
	private Gallery gall;
	private Gallery gallTwo;
	private ShopBz shopBz;
	
	private Button tj;
	long  iid;
	String wc;
	String userId;
	String token;
	private List<ShopBz> mProductImagesTwo;
	private List<ImageAlone> imageAlone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_packing);
		
		SharedPreferences aaa = getSharedPreferences("do", MODE_PRIVATE);
		 token= aaa.getString("token", "");
		 userId = aaa.getString("userId","");
		
		bz = (TextView) findViewById(R.id.bz);
		money = (TextView) findViewById(R.id.money);
		whit = (TextView) findViewById(R.id.textView1);
		
		gall = (Gallery) findViewById(R.id.gally);
		gallTwo = (Gallery) findViewById(R.id.galleryTwo);
		gallTwo.setOnItemClickListener(onItent);
		
		tj = (Button) findViewById(R.id.red_btn);
		tj.setOnClickListener(onclick);
		
		iid = getIntent().getIntExtra("mid",0);
		new asyTask(iid).execute();
	}
	
	//底部按钮监听
	private OnClickListener onclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.red_btn:
				Log.i("shopBz", shopBz.productId+shopBz.sku);
				new asy(shopBz.getProductId(),shopBz.getSku(), userId, token, 1).execute();
				break;
				
			default:
				break;
			}
			
		}
	};
	
	
	//添加购物车提示框
	protected void dialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
		builder.setMessage("添加成功")  
		       .setCancelable(false)  
		       .setPositiveButton("再逛逛", new DialogInterface.OnClickListener() {  
		           public void onClick(DialogInterface dialog, int id) {  
		                PackingActivity.this.finish();
		           }  
		       })  
		       .setNegativeButton("下蛋去", new DialogInterface.OnClickListener() {  
		           public void onClick(DialogInterface dialog, int id) {  
		        	   Intent intent = new Intent(PackingActivity.this,ShoppingCart.class);
		        	   String ab = mProductImagesTwo.get(0).getSku();
		        	   long bx = mProductImagesTwo.get(0).getProductId();
		        	   intent.putExtra("iid",bx);
		        	   intent.putExtra("sku",ab);
		        	   startActivity(intent);
		           }  
		       });  
		AlertDialog alert = builder.create();  
		alert.show();
		 }
	
	class asy extends AsyncTask<String,Void,String>{
		private long id ;
		private String sku;
		private long quantity;
		
		public asy(long id,String sku,String userId,String token,long quantity){
			this.id = id;
			this.sku = sku;
			this.quantity=quantity;
		}

		@Override
		protected String doInBackground(String... arg0) {
			String url = "/orderitem/update.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("productId",id );
			map.put("sku",sku);
			map.put("quantity", quantity);
			map.put("sub_total",1);
			map.put("token",token);
			map.put("userId",userId);
			RequestEntity entity = new RequestEntity(url, HttpMethod.POST, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("gouwuche", jsonData);
				} else {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog();
		}
		
	}

	
	//小gallery监听事件
	private OnItemClickListener onItent = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
//			System.out.println("productImages:"  + bz.productImages);
//			wc = bz.getSku();
//			mProductImages = JsonUtil.toObjectList(bz.productImages, ShopBz.class);
			shopBz=mProductImagesTwo.get(arg2);
			gall.setAdapter(new Image(shopBz));
			Select(arg2);
		}
	};
	
	private void Select(int position){
		bz.setText(mProductImagesTwo.get(position).packing);
		money.setText("￥" + String.valueOf(mProductImagesTwo.get(position).retailPrice));
		whit.setText(mProductImagesTwo.get(position).weight + "g");
		gallTwo.setAdapter(new Images());
	}
	
	
	//异步加载相关数据任务
	class asyTask extends AsyncTask<Void,Void,String>{
		private long b;
		public asyTask(long id){
			b = id;
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			String url = "/product/item/list.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("productId",b );
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("baozhuangxuanze", jsonData);
//					shopBz = JsonUtil.toObject(jsonData, ShopBz.class);
					mProductImagesTwo = JsonUtil.toObjectList(jsonData, ShopBz.class);
				} else {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}
			
		@Override
		protected void onPostExecute(String result) {
			
			bz.setText(mProductImagesTwo.get(0).packing);
			money.setText("￥" + String.valueOf(mProductImagesTwo.get(0).retailPrice));
			whit.setText(mProductImagesTwo.get(0).weight + "g");
			//gallery设置适配器
			shopBz=mProductImagesTwo.get(0);
			gall.setAdapter(new Image(shopBz));
			gallTwo.setAdapter(new Images());
			super.onPostExecute(result);
		}
	}
	
	//小的gallery适配器
			class Images extends BaseAdapter{
				
				@Override
				public int getCount() {
					return mProductImagesTwo.size();
				}

				@Override
				public Object getItem(int arg0) {
					return mProductImagesTwo.get(arg0);
				}

				@Override
				public long getItemId(int arg0) {
					return mProductImagesTwo.get(arg0).id;
				}

				@Override
				public View getView(int arg0, View arg1, ViewGroup arg2) {
					View view = getLayoutInflater().inflate(R.layout.imageshop, null);
					ImageView im = (ImageView) view.findViewById(R.id.imstwo);
					 ImageFetcher fetcher = new ImageFetcher();
						fetcher.fetch(HttpHelper.DOMAIN + "/" + mProductImagesTwo.get(arg0).imageUrl,
								im);
					return view;
				}
				
				class viewHolder{
					ImageView imageview;
				}
			}
			
//	顶部gallary适配器
		class Image extends BaseAdapter{
			public Image(ShopBz p){
				imageAlone=JsonUtil.toObjectList(p.productImages, ImageAlone.class);
			}
			
		@Override
		public int getCount() {
			return imageAlone.size();
		}

		@Override
		public Object getItem(int arg0) {
			return imageAlone.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return imageAlone.get(arg0).id;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = getLayoutInflater().inflate(R.layout.image, null);
			ImageView im = (ImageView) view.findViewById(R.id.ims);
			 ImageFetcher fetcher = new ImageFetcher();
				fetcher.fetch(HttpHelper.DOMAIN + "/" + imageAlone.get(arg0).imageUrl,
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
		getMenuInflater().inflate(R.menu.packing, menu);
		return true;
	}

}
