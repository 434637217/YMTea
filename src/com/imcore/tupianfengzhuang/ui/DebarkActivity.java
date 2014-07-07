package com.imcore.tupianfengzhuang.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.http.HttpHelper;
import com.imcore.tupianfengzhuang.http.RequestEntity;
import com.imcore.tupianfengzhuang.http.ResponseJsonEntity;
import com.imcore.tupianfengzhuang.modl.User;
import com.imcore.tupianfengzhuang.util.JsonUtil;
import com.imcore.tupianfengzhuang.util.TextUtil;
import com.imcore.tupianfengzhuang.util.ToastUtil;

public class DebarkActivity extends Activity {

	private Button debark;
	private Button login;
	private EditText us;
	private EditText pw;
	private ImageView imView;

	private ProgressDialog pd1;

	public static final String SETTING_INFOS = "SETTING_Infos";
	public static final String NAME = "NAME";
	public static final String PASSWORD = "PASSWORD";
	SharedPreferences settings;
	 final String INITIALIZED = "initialized";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_deng_lu);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		debark = (Button) findViewById(R.id.debark);
		us = (EditText) findViewById(R.id.yhm_view);
		pw = (EditText) findViewById(R.id.mm_view);
		imView = (ImageView) findViewById(R.id.forget_mm);

		
		settings = getSharedPreferences("do", MODE_PRIVATE); // 获取一个SharedPreferences对象
		String name = settings.getString(NAME, ""); // 取出保存的NAME
		String password = settings.getString(PASSWORD, ""); // 取出保存的PASSWORD
		us.setText(name); // 将取出来的用户名赋予field_name
		pw.setText(password); // 将取出来的密码赋予filed_pass
		
		
		debark.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				doLogin();
				pd1 = new ProgressDialog(DebarkActivity.this);
				pd1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pd1.setTitle("提示");
				pd1.setMessage("正在玩命加载");
				pd1.show();
			}
		});
	}

	private void doLogin() {
		String userName = us.getText().toString();
		String password = pw.getText().toString();

		if (!validateInput(userName, password)) {
			Toast.makeText(DebarkActivity.this, "错批了", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		new DoLoginAsyncTask(userName, password).execute();

	}

	// 判斷輸入是否合法
	private boolean validateInput(String userName, String password) {

		return true;
	}

	class DoLoginAsyncTask extends AsyncTask<Void, Void, String> {
		private String mUserName;
		private String mPassword;

		public DoLoginAsyncTask(String uName, String pwd) {
			mUserName = uName;
			mPassword = pwd;
		}

		@Override
		protected String doInBackground(Void... arg0) {
			String url = "/passport/login.do";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("phoneNumber", mUserName);
			map.put("password", mPassword);
			map.put("client", "android");
			RequestEntity entity = new RequestEntity(url, map);
			String jason = "";
			try {
				jason = HttpHelper.execute(entity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jason;
		}

		@Override
		protected void onPostExecute(String result) {
			if (TextUtil.isEmptyString(result)) {
				// 错误提示
				return;
			}
			ResponseJsonEntity entity = ResponseJsonEntity.fromJSON(result);
			if (entity.getStatus() == 200) {
				// 处理正确的响应数据
				String jsonData = entity.getData();
				Log.i("json", jsonData);
				
				User user = JsonUtil.toObject(jsonData, User.class);
				
				
				Intent intent = new Intent(DebarkActivity.this,
						MainActivity.class);
				startActivity(intent);
				DebarkActivity.this.finish();

				settings = getSharedPreferences("do", Context.MODE_PRIVATE); // 首先获取一个SharedPreferences对象
				//存入一个boolean 用来判断账号密码保存状态，SharedPreferences的信息存入Editor
				Editor editor = settings.edit();
				editor.putString(NAME, us.getText().toString());
				editor.putString(PASSWORD, pw.getText().toString());
				editor.putString("token",user.token);
				System.out.println("userId:" + user.id);
				editor.putString("userId",String.valueOf(user.id));
				editor.putBoolean("fuck", true);
				editor.commit();
				
			} else {
				// 做错误提示
				pd1.dismiss();
				ToastUtil.showToast(DebarkActivity.this, entity.getMessage());
				pw.setText(null);
			}
			super.onPostExecute(result);
		}
	}

}
