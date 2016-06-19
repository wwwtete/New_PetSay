package com.petsay.component.view.postcard;

import com.petsay.R;
import com.petsay.component.view.postcard.ClothingTypeView.OnTagItemClickListener;
import com.petsay.vo.personalcustom.SpecDTO;
import com.petsay.vo.personalcustom.SpecValueDTO;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomGoodsSpecsView extends LinearLayout {

	private Context mContext;
	private TextView mTvSpecsName;
	private ClothingTypeView mClothingTypeView;
	private SpecValueDTO[] mSpecValueDTOs;
	private OnTagItemClickListener mOnTagItemClickListener;

	public CustomGoodsSpecsView(Context context) {
		super(context);
		mContext = context;
		inflate(context, R.layout.view_custom_goods_specs, this);
		initView();
	}

	private void initView() {
		mTvSpecsName = (TextView) findViewById(R.id.tv_specsName);
		mClothingTypeView = (ClothingTypeView) findViewById(R.id.view_clothingtype);
	}

	public void initData(SpecDTO specDTO, int tagBgRes, int selectedBgRes, int layoutWidth,OnTagItemClickListener onTagItemClickListener) {
		mOnTagItemClickListener=onTagItemClickListener;
		mClothingTypeView.setTagTextColor(Color.BLACK);
		mSpecValueDTOs=new SpecValueDTO[specDTO.getValues().size()];
		mTvSpecsName.setText(specDTO.getName());
		for (int i = 0; i < mSpecValueDTOs.length; i++) {
			mSpecValueDTOs[i]=specDTO.getValues().get(i);
			mSpecValueDTOs[i].setSpecId(specDTO.getId());
			
		}
		mClothingTypeView.setTags(mSpecValueDTOs, tagBgRes,selectedBgRes, layoutWidth);
//		点击tag选择服装不同规格
		mClothingTypeView.setOnTagItemClickListener(mOnTagItemClickListener);
	}

}
