package com.imcore.tupianfengzhuang.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.HttpMethod;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.util.ToastUtil;

public class EditActivity extends Activity implements android.view.View.OnClickListener{

	private ImageView imageView;
	private static final int REQUEST_CODE_CAMERA = 0;
	private static final int REQUEST_CODE_GALLARY = 1;
	private static final String APP_DIR = Environment
			.getExternalStorageDirectory() + File.separator + "SuiBian";
	private Uri captureImageUri;
	private Button save;
	Uri imageuri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		
		imageView = (ImageView) findViewById(R.id.edit_im);
		imageView.setOnClickListener( this);
		
		save = (Button) findViewById(R.id.edit_save);
		save.setOnClickListener(this);
	}

	
	private void getImage(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择图片")
				.setItems(R.array.choose_img_items, onDialogClickListenter)
				.show();
	}
	
	
private DialogInterface.OnClickListener onDialogClickListenter = new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch(which){
			case 0:
				computer();
				break;
			case 1:
				photo();
				break;
			
			}
		}
		
		
	};
	//拍照
	public void  computer(){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}
	
	public void photo(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, REQUEST_CODE_GALLARY);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK){
			return;
		}
		
		switch(requestCode){
		case REQUEST_CODE_CAMERA:
			Parcelable imgg = data.getParcelableExtra("data");
			Bitmap bm1 = (Bitmap) imgg;
			
			File dir = new File(APP_DIR);
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			String fileName = "图片_" + System.currentTimeMillis() + "png";
			File imgFile = new File(dir,fileName);
			
			if(!imgFile.exists()){
				OutputStream output = null;
				try {
					imgFile.createNewFile();
					output = new FileOutputStream(imgFile);
					bm1.compress(CompressFormat.PNG, 100, output);
					imageView.setImageBitmap(bm1);
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			break;
		case REQUEST_CODE_GALLARY:
			imageuri = data.getData();
			String pat = getRealPathByUri(imageuri);
			Bitmap map = BitmapFactory.decodeFile(pat);
			imageView.setImageBitmap(map);
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 根据媒体资源的Uri获得媒体文件的真实物理路径
	 * 
	 * @param uri
	 *            指定的资源uri
	 * @return 返回该资源的真实物理路径
	 */
	private String getRealPathByUri(Uri uri) {
		ContentResolver cr = getContentResolver();
		String[] projection = new String[] {MediaStore.Images.Media.DATA};
		Cursor c = cr.query(uri, projection, null, null, null);
		String realPath = "";
		if (c.moveToFirst()) {
			realPath = c.getString(c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
		}
		return realPath;
	}
	
	
	
	

	class asy extends AsyncTask<Void,Void,String>{

		@Override
		protected String doInBackground(Void... params) {
			String url = " /user/images/add.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("userId",146);
			map.put("avatarUrl",imageuri);
			RequestEntity entity = new RequestEntity(url, HttpMethod.GET, map);
			String json = "";
			try {
				json = HttpHelper.execute(entity);
				ResponseJsonEntity entity2 = ResponseJsonEntity.fromJSON(json);
				if (entity2.getStatus() == 200) {
					String jsonData = entity2.getData();
					Log.i("myFragement", jsonData);
				} else {
					ToastUtil.showToast(EditActivity.this, "服务器不可用");
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
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.edit_im:
			getImage();
			break;
		case R.id.edit_save:
			new asy().execute();
			ToastUtil.showToast(EditActivity.this, "服务器不可用");
			EditActivity.this.finish();
			break;
		}
		
	}

}
