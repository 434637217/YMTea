package com.imcore.tupianfengzhuang.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.tupianfengzhuang.R;
import com.ft.TwoCodeActivity;
import com.google.zxing.ui.CaptureActivity;
import com.imcore.tupianfengzhuang.util.ToastUtil;



public class MoreFragment extends Fragment{
	
	private RelativeLayout out;
	private RelativeLayout id;  
	private RelativeLayout we;
	private RelativeLayout update;
	private RelativeLayout dimen;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.morefragment,null);
		
		out = (RelativeLayout) view.findViewById(R.id.more_out);
		out.setOnClickListener(clickListener);
		id = (RelativeLayout) view.findViewById(R.id.more_id);
		id.setOnClickListener(clickListener);
		we = (RelativeLayout) view.findViewById(R.id.more_we);
		we.setOnClickListener(clickListener);
		update = (RelativeLayout) view.findViewById(R.id.more_update);
		update.setOnClickListener(clickListener);
		dimen = (RelativeLayout) view.findViewById(R.id.more_dimen);
		dimen.setOnClickListener(clickListener);
		
		
		return view;
	}
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.more_out:
				showTips();
				break;
				
			case R.id.more_id:
				Intent intent = new Intent(getActivity(),DebarkActivity.class);
				startActivity(intent);
				break;
				
			case R.id.more_we:
				ToastUtil.showToast(getActivity(), "服务器不可用");
				break;
				
			case R.id.more_update:
				ToastUtil.showToast(getActivity(), "服务器不可用");
				break;
				
			case R.id.more_dimen:
				Intent intent2 = new Intent(getActivity(),CaptureActivity.class);
				startActivity(intent2);
			default:
				break;
			}
		}
	};
	//退出程序
	private void showTips() {

		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("提醒")
				.setMessage("是否退出程序")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_MAIN);  
			            intent.addCategory(Intent.CATEGORY_HOME);  
			            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
			            startActivity(intent);  
				    android.os.Process.killProcess(android.os.Process.myPid());
					}

				}).setNegativeButton("取消",

				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create(); // 创建对话框
		alertDialog.show(); // 显示对话框
	}
	

}
