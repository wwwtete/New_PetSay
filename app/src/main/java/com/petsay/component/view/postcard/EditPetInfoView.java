package com.petsay.component.view.postcard;

import java.util.ArrayList;
import java.util.List;

import com.petsay.R;
import com.petsay.activity.personalcustom.clothing.EditPetInfoActivity;
import com.petsay.application.UserManager;
import com.petsay.cache.DataFileCache;
import com.petsay.vo.personalcustom.OrderProductSpecDTO;
import com.petsay.vo.personalcustom.ProductDTO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EditPetInfoView extends RelativeLayout implements android.view.View.OnClickListener{
	
	private TextView mTvAddInfo,mTvEdit;
	private LinearLayout mLayoutInfo;
	private TextView mTvClothingShape,mTvGender,mTvBodyWeight,mTvVariety,mTvBodyLength,mTvNeckCf,mTvBust;
	
	private ArrayList<OrderProductSpecDTO> mPetinfoSpecDTOs;
	private Context mContext;
	public EditPetInfoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext=context;
		initView();
	}
	
	private void initView(){
		inflate(mContext, R.layout.view_edit_petinfo, this);
		mTvAddInfo=(TextView) findViewById(R.id.tv_add);
		mTvEdit=(TextView) findViewById(R.id.tv_edit);
		mLayoutInfo=(LinearLayout) findViewById(R.id.layout_info);
		mTvAddInfo.setOnClickListener(this);
		mTvEdit.setOnClickListener(this);
		mTvBodyWeight=(TextView) findViewById(R.id.tv_bodyWeight);
		mTvBodyLength=(TextView) findViewById(R.id.tv_bodyLength);
		mTvBust=(TextView) findViewById(R.id.tv_bust);
		mTvClothingShape=(TextView) findViewById(R.id.tv_clothingShape);
		mTvGender=(TextView) findViewById(R.id.tv_gender);
		mTvNeckCf=(TextView) findViewById(R.id.tv_neckCf);
		mTvVariety=(TextView) findViewById(R.id.tv_variety);
	}
	
	public void setInfoData(ArrayList<OrderProductSpecDTO> petinfoSpecDTOs){
		mPetinfoSpecDTOs=petinfoSpecDTOs;
		if (null==mPetinfoSpecDTOs||mPetinfoSpecDTOs.isEmpty()) {
			mLayoutInfo.setVisibility(View.GONE);
			mTvEdit.setVisibility(View.GONE);;
			mTvAddInfo.setVisibility(View.VISIBLE);
			mPetinfoSpecDTOs=new ArrayList<OrderProductSpecDTO>();
			return ;
		}else {
			mLayoutInfo.setVisibility(View.VISIBLE);
			mTvEdit.setVisibility(View.VISIBLE);;
			mTvAddInfo.setVisibility(View.GONE);
			for (int i = 0; i < mPetinfoSpecDTOs.size(); i++) {
				OrderProductSpecDTO dto=mPetinfoSpecDTOs.get(i);
				if (dto.getId().equals("bust")) {
					//胸围
					mTvBust.setText(dto.getValue());

				} else if (dto.getId().equals("neckCf")) {
					//颈围
					mTvNeckCf.setText(dto.getValue());

				} else if (dto.getId().equals("bodyLength")) {
	                //身长
					mTvBodyLength.setText(dto.getValue());
					
				} else if (dto.getId().equals("variety")) {
					//品种
					mTvVariety.setText(dto.getValue());

				} else if (dto.getId().equals("bodyWeight")) {
					//体重
					mTvBodyWeight.setText(dto.getValue());

				} else if (dto.getId().equals("gender")) {
					//性别
					mTvGender.setText(dto.getValue());

				} else if (dto.getId().equals("clothingShape")) {
					//版式
					mTvClothingShape.setText(dto.getValue());

				}
			}	
		}
	}
	
//	private void initPetinfoData() {
//		try {
//			mPetinfoSpecDTOs=(ArrayList<OrderProductSpecDTO>) DataFileCache.getSingleton().loadObject("petinfo_"+UserManager.getSingleton().getActivePetId());
//		} catch (Exception e) {
//			e.printStackTrace();
//			mLayoutInfo.setVisibility(View.GONE);
//			mTvEdit.setVisibility(View.GONE);;
//			mTvAddInfo.setVisibility(View.VISIBLE);
//			mPetinfoSpecDTOs=new ArrayList<OrderProductSpecDTO>();
//			return ;
//		};
//		
//		
//	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_add:
			Intent intent=new Intent(mContext,EditPetInfoActivity.class);
			intent.putExtra("list", mPetinfoSpecDTOs);
			mContext.startActivity(intent);
			break;
		case R.id.tv_edit:
			intent=new Intent(mContext,EditPetInfoActivity.class);
			intent.putExtra("list", mPetinfoSpecDTOs);
			mContext.startActivity(intent);
			break;
		default:
			break;
		}
		
	}
	
	

}
