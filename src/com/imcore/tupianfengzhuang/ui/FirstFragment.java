package com.imcore.tupianfengzhuang.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.ui.TestFragment.asy;
import com.imcore.tupianfengzhuang.why.MyApplicition;

public class FirstFragment extends Fragment {


	private ViewPager advPager = null;

	private int position = 0;
	private ImageButton b1;
	private ImageButton b2;
	private ImageButton b3;
	private ImageButton b4;
	private Handler handler;
	
	private ImageButton store;
	private EditText editText;
	private Button xl;

	private static int sortInt = 0;
	private ButtonOnClick buttonOnClick = new ButtonOnClick();
	private String[] prov = new String[] { "产品", "新闻", "用户" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.first_fragment, null);

		editText = (EditText) view.findViewById(R.id.first_eidtext);
		editText.clearFocus(); 
		
		xl = (Button) view.findViewById(R.id.first_seek);
		xl.setOnClickListener(clickListener);
		
		b1 = (ImageButton) view.findViewById(R.id.xpss);
		b2 = (ImageButton) view.findViewById(R.id.rxph);
		b3 = (ImageButton) view.findViewById(R.id.cyzx);
		b4 = (ImageButton) view.findViewById(R.id.lxmd);
		
		store = (ImageButton) view.findViewById(R.id.lxmd);
		store.setOnClickListener(clickListener);
		advPager = (ViewPager) view.findViewById(R.id.adv_pager);
		advPager.setAdapter(new ViewPagerAdapter());
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				
				advPager.setCurrentItem(msg.arg1);
			}
		};

		//开启线程来实现自动切换
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {

						position++;
						if (position == MyApplicition.tpList.size()) {
							position = 0;
						}
						Message msg = new Message();
						msg.arg1 = position;
						handler.sendMessage(msg);
						Thread.sleep(3000 );
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		Bundle agrs = getArguments();
		String value = agrs.getString("title");

		return view;
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
			case R.id.lxmd:
				Intent intent = new Intent(getActivity(),StoresActivity.class);
				startActivity(intent);
				break;
				
			case R.id.first_seek:
				showSingleChoiceButton();
				
				break;
			}
			
		}
	};
	
	
	//显示图片的适配器
	class ViewPagerAdapter extends FragmentStatePagerAdapter {

		public ViewPagerAdapter() {
			super(getActivity().getSupportFragmentManager());
		}

		@Override
		public Fragment getItem(int arg0) {
			ImageO io = new ImageO();

			Bundle bl = new Bundle();
			bl.putInt("position", arg0);
			io.setArguments(bl);
			return io;
		}

		@Override
		public int getCount() {
			return MyApplicition.tpList.size();
		}

	}
	
	private void showSingleChoiceButton() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("请选搜索类型");
		builder.setSingleChoiceItems(prov, sortInt, buttonOnClick);
		builder.show();
	}

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
	
	private void sort(int index) {
		switch (index) {
		case 0:
			sc(editText.getText().toString(),1);
			break;
		case 1:
			sc(editText.getText().toString(),2);
			break;
		case 2:
			sc(editText.getText().toString(),3);
			break;
		}
	}
	
	public void sc(String a,Integer b){
		Intent intent = new Intent(getActivity(),SeekDetailsActivity.class);
		intent.putExtra("st",a);
		intent.putExtra("in",b);
		startActivity(intent);
		editText.setText(" ");
	}
}
