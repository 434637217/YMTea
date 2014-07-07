package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.image.StorageHelper;
import com.imcore.tupianfengzhuang.modl.ProductDetails;
import com.imcore.tupianfengzhuang.modl.ProductImage;
import com.imcore.tupianfengzhuang.util.JsonUtil;
import com.imcore.tupianfengzhuang.util.ToastUtil;

public class ProductDetailsActivity extends Activity {
	private TextView shopName;
	private TextView describe;
	private TextView money;
	private TextView pack;
	private TextView stylenumber;
	private Button wrap;
	
	private Gallery gall;
	private List<ProductImage> mProductImages;
	ProductDetails produ;
	
	private RelativeLayout details;
	private Button coment;
	private Button collect;
	private Button share;
	
	int ids;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		
		ShareSDK.initSDK(this,"23d25cf02c87");
		
		shopName = (TextView) findViewById(R.id.shop_name);
		describe = (TextView) findViewById(R.id.describe);
		describe.setOnClickListener(onClick);
		money = (TextView) findViewById(R.id.money);
		pack = (TextView) findViewById(R.id.box);
		stylenumber = (TextView) findViewById(R.id.stylenumber);
		details = (RelativeLayout) findViewById(R.id.comment);
		details.setOnClickListener(onClick);
		coment = (Button) findViewById(R.id.product_coment);
		coment.setOnClickListener(onClick);
		collect = (Button) findViewById(R.id.product_collectt);
		collect.setOnClickListener(onClick);
		share = (Button) findViewById(R.id.product_recommend);
		share.setOnClickListener(onClick);
		
		gall = (Gallery) findViewById(R.id.gally);
		
		wrap = (Button) findViewById(R.id.product_red_btn);
		wrap.setOnClickListener(onClick);
		
		
		ids = getIntent().getIntExtra("id",0);
		new asyTask(ids).execute();
		
		Button bt = (Button) findViewById(R.id.returns);
		bt.setOnClickListener(onClick);
		
	}
	
	private OnClickListener onClick = new OnClickListener() {
		boolean flag = true;
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.returns:
				ProductDetailsActivity.this.finish();
				break;
			case R.id.describe:
				if(flag){
			        flag = false;
			        describe.setEllipsize(null); // 展开
			        describe.setSingleLine(flag);
			        }else{
			          flag = true;
			          describe.setEllipsize(TextUtils.TruncateAt.END); // 收缩
			          describe.setSingleLine(flag);
			        	}
			     break;
			          
			case R.id.product_red_btn:
				Intent intent = new Intent(ProductDetailsActivity.this,PackingActivity.class);
				int pro = produ.getId();
				intent.putExtra("mid",pro);
				startActivity(intent);
				break;
				
			case R.id.comment:
				Cz();
				break;
				
			case R.id.product_coment:
				Cz();
				break;
				
			case R.id.product_collectt:
				long id = ids;
				new collectTask(id).execute();
				break;
				
			case R.id.product_recommend:
				showOnekeyshare();
				break;
				
			
			    }
			}
		};
	
		public void Cz(){
			Intent intents = new Intent(ProductDetailsActivity.this,CommentActivity.class);
			long com = produ.getId();
			intents.putExtra("id", com);
			startActivity(intents);
		};
	
		public void showOnekeyshare() {
	        OnekeyShare oks = new OnekeyShare();
	        
	        // 分享时Notification的图标和文字
	        oks.setNotification(R.drawable.ic_launcher, 
	        getString(R.string.app_name));
	        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
	        oks.setTitle(getString(R.string.share));
	        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
	        oks.setTitleUrl("http://sharesdk.cn");
	        // text是分享文本，所有平台都需要这个字段
	        oks.setText("");
	        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
	        oks.setImagePath(StorageHelper.APP_DIR_IMAGE);
	        // imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、
	        // 微信的两个平台、Linked-In支持此字段
	        oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
	        // url仅在微信（包括好友和朋友圈）中使用
	        oks.setUrl("http://sharesdk.cn");
	        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
	        oks.setComment(getString(R.string.share));
	        // site是分享此内容的网站名称，仅在QQ空间使用
	        oks.setSite(getString(R.string.app_name));
	        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
	        oks.setSiteUrl("http://sharesdk.cn");
	        // venueName是分享社区名称，仅在Foursquare使用
	        oks.setVenueName("Southeast in China");
	        // venueDescription是分享社区描述，仅在Foursquare使用
	        oks.setVenueDescription("This is a beautiful place!");
	        // latitude是维度数据，仅在新浪微博、腾讯微博和Foursquare使用
	        oks.setLatitude(23.122619f);
	        // longitude是经度数据，仅在新浪微博、腾讯微博和Foursquare使用
	        oks.setLongitude(113.372338f);
	        oks.show(this);
	}
		
	//商品名字详情等异步解析
	class asyTask extends AsyncTask<Void,Void,String>{
		private int a;
		
		public asyTask(int id){
			a = id;
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			String url = "/product/get.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", a);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("xiangqing", jsonData);

//					proList = JsonUtil.toObjectList(jsonData, ProductDetails.class);
					produ = JsonUtil.toObject(jsonData, ProductDetails.class);
				} else {
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return json;
		}
			
		@Override
		protected void onPostExecute(String result) {
			
			shopName.setText(produ.productName);
			describe.setText(produ.shortDesc);
			money.setText(String.valueOf(produ.price));
			pack.setText(produ.packing);
			stylenumber.setText(String.valueOf(produ.productItemSize));
			
			//gallery设置适配器
			gall.setAdapter(new Image(produ));
			
			super.onPostExecute(result);
		}
		
	}
	
	//收藏异步解析
	class collectTask extends AsyncTask<Void,Void,String>{
		private long a;
		
		public collectTask(long id){
			a = id;
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			String url = "/user/favorite/add.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", a);
			map.put("userId", 146);
			map.put("userId", 1);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("xiangqing", jsonData);

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
			ToastUtil.showToast(ProductDetailsActivity.this,"收藏成功");
		}
		
	}
	
	
	//gallery适配器
	class Image extends BaseAdapter{
		
		public Image(ProductDetails p){
			mProductImages=JsonUtil.toObjectList(p.productImages, ProductImage.class);
			for(ProductImage pi:mProductImages){
				Log.i("productImage", pi.imageUrl);
			}
		}
		@Override
		public int getCount() {
			return mProductImages.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mProductImages.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return mProductImages.get(arg0).id;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = getLayoutInflater().inflate(R.layout.image, null);
			ImageView im = (ImageView) view.findViewById(R.id.ims);
			 ImageFetcher fetcher = new ImageFetcher();
				fetcher.fetch(HttpHelper.DOMAIN + "/" + mProductImages.get(arg0).imageUrl,
						im);
			return view;
		}
		
		class viewHolder{
			ImageView imageview;
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.product_details, menu);
		return true;
	}

}

