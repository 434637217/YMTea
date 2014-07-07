package com.imcore.tupianfengzhuang.ui;

import com.example.tupianfengzhuang.R;
import com.imcore.tupianfengzhuang.image.ImageFetcher;
import com.imcore.tupianfengzhuang.image.StorageHelper;
import com.imcore.tupianfengzhuang.util.DisplayUtil;
import com.imcore.tupianfengzhuang.why.MyApplicition;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageO extends Fragment {

	private ImageView mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.viewpager, null);
		mView = (ImageView) view.findViewById(R.id.viewpager);

		Bundle bd = getArguments();

		int position = bd.getInt("position");
		setImage(position);
		return view;
	}

	/**
	 * 
	 * @param position
	 */
	private void setImage(int position) {
		new ImageFetcher().fetch(MyApplicition.tpList.get(position), mView);
	}
}
