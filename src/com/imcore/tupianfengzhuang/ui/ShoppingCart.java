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
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.modl.CartShopp;
import com.imcore.tupianfengzhuang.modl.CartTwo;
import com.imcore.tupianfengzhuang.modl.ShopBz;
import com.imcore.tupianfengzhuang.util.JsonUtil;
import com.imcore.tupianfengzhuang.util.ToastUtil;

public class ShoppingCart extends Activity {

	String token;
	String userId;
	
	private CartShopp zList;
	private List<CartTwo>bList;
	
	private ImageView imageview;
	
	private ListView lisview;
	private ListView listViewT;
	
	private TextView tx1;
	private TextView tx2;
	
	private Button redact;
	private Button add;
	private Button shopp;
	
	private boolean Eit = false;
	long shu;
	
	long di;
	String de;
	
	private TextView cs;
	
	private long detaId;
	private long detaOreaId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);
		
		lisview = (ListView) findViewById(R.id.shopping_cart_listview);
		listViewT = (ListView) findViewById(R.id.shopping_cart_listviewtwo);
		
		
		tx1 = (TextView) findViewById(R.id.original);
		tx2 = (TextView) findViewById(R.id.vip);
		
		redact = (Button) findViewById(R.id.redact);
		redact.setOnClickListener(onListener);
		shopp=(Button) findViewById(R.id.red_btn);
		shopp.setOnClickListener(onListener);
		
		di = getIntent().getLongExtra("iid",0);
		de = getIntent().getStringExtra("sku");
		
		SharedPreferences aaa = getSharedPreferences("do", MODE_PRIVATE);
		token = aaa.getString("token", "");
		userId = aaa.getString("userId","");
		
		new subkey().execute();
	}

	//各按钮的监听事件
	private OnClickListener onListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.redact:
				if(Eit == false){
					redact.setText("完成");
				lisview.setVisibility(View.GONE);
				listViewT.setVisibility(View.VISIBLE);
				Eit = true;
				}else {
					redact.setText("编辑");
					lisview.setVisibility(View.VISIBLE);
					listViewT.setVisibility(View.GONE);
					Eit = false;
				}
				break;
				
			case R.id.red_btn:
				if(Eit==false){
					Intent intent = new Intent(ShoppingCart.this,Customer.class);
					startActivity(intent);
				}else{
					ToastUtil.showToast(ShoppingCart.this,"编辑未完成");
				}
			default:
				break;
			}
			
		}
	};
	
	//解析添加入口
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
			super.onPostExecute(result);
			new subkey().execute();
		}
	}
	
	//解析子项列表
	class subkey extends AsyncTask<String,Void,String>{

		@Override
		protected String doInBackground(String... arg0) {
			
			
			String url = "/orderitem/list.do";
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
					Log.i("zidingdan", jsonData);
					zList = JsonUtil.toObject(jsonData, CartShopp.class);
					bList = JsonUtil.toObjectList(zList.orderItems, CartTwo.class);
					for(CartTwo abc:bList){
						Log.i("aa",abc.imageUrl);
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
			lisview.setAdapter(new listAdapter());
			listViewT.setAdapter(new listRedactAdapter());
			tx1.setText("原价：" +  "￥" + zList.getTotalAmount());
			tx2.setText("会员价："+  "￥" + zList.getDiscount());
			super.onPostExecute(result);
		}
	}
	
	
	
	//未編輯的适配器
	private class listAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return bList.get(arg0);
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
				view = getLayoutInflater().inflate(
						R.layout.shopping_cart, null);
				vh = new ViewHolder();
				vh.imgView = (ImageView) view
						.findViewById(R.id.shoppping_cart_image);
				vh.name = (TextView) view.findViewById(R.id.shopping_cart_d_name);
				vh.cost = (TextView) view.findViewById(R.id.shopping_cart_x_name);
				vh.sales = (TextView) view.findViewById(R.id.shopping_cart_name);
				vh.collect = (TextView) view
						.findViewById(R.id.shopping_cart_js);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			CartTwo deta = bList.get(position);
			vh.name.setText(deta.getProductName());
			vh.cost.setText(deta.getItemName() + "");
			vh.sales.setText("￥" + deta.getRetailPrice() + "");
			vh.collect.setText("共"+ deta.getQuantity() + "件");
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
	
	
	//编辑状态下的适配器
	private class listRedactAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return bList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder vh;
			if (view == null) {
				view = getLayoutInflater().inflate(
						R.layout.read_listview_two, null);
				vh = new ViewHolder();
				vh.imgView = (ImageView) view
						.findViewById(R.id.shoppping_cart_image_redact);
				vh.name = (TextView) view.findViewById(R.id.shopping_cart_d_name_redact);
				vh.cost = (TextView) view.findViewById(R.id.shopping_cart_x_name_redact);
				vh.sales = (TextView) view.findViewById(R.id.shopping_cart_name_redact);
				vh.collect = (TextView) view
						.findViewById(R.id.shu);
				vh.add = (Button) view.findViewById(R.id.add);
				vh.minus= (Button) view.findViewById(R.id.minus);
				vh.delete = (Button) view.findViewById(R.id.delete);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			final CartTwo deta = bList.get(position);
			detaId = deta.id;
			detaOreaId = deta.orderId;
			//增加商品数量，每次+1
			vh.add.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					new asy(deta.productId, deta.sku,userId,token, 1).execute();
					vh.collect.setText(String.valueOf(deta.quantity));
				}
			});
			
			//删减商品数量，每次-1
			vh.minus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(deta.quantity > 1){
					new asy(deta.productId, deta.sku,userId,token, -1).execute();
					vh.collect.setText(String.valueOf(deta.quantity));
					} else if(deta.quantity == 1){
						dialog(deta);
					}
				}
			});
			
			//删除商品
			vh.delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					dialog(deta);
					
				}
			});
			
			vh.name.setText(deta.getProductName());
			vh.cost.setText(deta.getItemName() + "");
			vh.sales.setText("￥" + deta.getRetailPrice() + "");
			vh.collect.setText(String.valueOf(deta.getQuantity()));
			vh.add.setTag(vh.collect);
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
			Button add;
			Button minus;
			Button delete;

		}
	}
	
	
	//删除子订单相关解析
	class Delete extends AsyncTask<String,Void,String>{
		private long id;
		private long orderId;
		 public Delete (long id,long orderId){
			 this.id = id;
			 this.orderId = orderId;
		 }
		
		@Override
		protected String doInBackground(String... arg0) {
			
			
			
			String url = "/orderitem/delete.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id",id);
			map.put("orderId",orderId);
			map.put("token",token);
			map.put("userId",userId);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("delete", jsonData);
					
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
			new subkey().execute();
		}
	}
	
	//删除商品提示框
	protected void dialog(final CartTwo c) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
		builder.setMessage("确认删除该商品？")  
		       .setCancelable(false)  
		       .setPositiveButton("取消", new DialogInterface.OnClickListener() {  
		           public void onClick(DialogInterface dialog, int id) { 
		               dialog.dismiss();
		           }  
		       })  
		       .setNegativeButton("确认", new DialogInterface.OnClickListener() {  
		           public void onClick(DialogInterface dialog, int id) { 
		        	   new Delete(c.id,c.orderId).execute();
		           }  
		       });  
		AlertDialog alert = builder.create();  
		alert.show();
		 }
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.shopping_cart, menu);
		return true;
	}

}
