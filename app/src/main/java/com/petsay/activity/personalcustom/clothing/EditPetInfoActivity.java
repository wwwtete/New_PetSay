package com.petsay.activity.personalcustom.clothing;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.application.UserManager;
import com.petsay.cache.DataFileCache;
import com.petsay.component.view.TitleBar;
import com.petsay.component.view.TitleBar.OnClickBackListener;
import com.petsay.component.view.postcard.ImageTextView;
import com.petsay.component.wheelview.GenderWheelView;
import com.petsay.component.wheelview.PetWheelView;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.utile.ToastUtiles;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.personalcustom.OrderProductSpecDTO;

/**
 * 
 * @author GJ
 *编辑宠物信息
 */
public class EditPetInfoActivity extends BaseActivity implements NetCallbackInterface,OnClickListener{
	private TitleBar mTitleBar;
	
	private InputView mViewVariety; 
	
	private InputView mViewGender;
	private InputView mViewBust,mViewNeckCf,mViewBodyLength,mViewBodyWeight;
	
	
	
	private LinearLayout mLayoutWheel;
	private RelativeLayout mRLayoutWheel;
	private Button mBtnOk,mBtnCancle,mBtnSave;
	
	private PetWheelView petWheelView;
	private GenderWheelView genderWheelView;
	
	private ArrayList<OrderProductSpecDTO> mPetinfoSpecDTOs;
	
	private String clothingShape="";
	
	//版式
	private ImageTextView mViewClothingShape1,mViewClothingShape2,mViewClothingShape3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_petinfo);
		mPetinfoSpecDTOs=(ArrayList<OrderProductSpecDTO>) getIntent().getSerializableExtra("list");
		initView();
		setListener();
//		initData();
		setInfoData();
	}
	
	
	private void setListener() {
		mBtnOk.setOnClickListener(this);
		mBtnSave.setOnClickListener(this);
		mBtnCancle.setOnClickListener(this);
        mViewVariety.setOnEdClickListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction()==MotionEvent.ACTION_DOWN) {
					petWheelView = new PetWheelView(getApplicationContext());
					mLayoutWheel.removeAllViews();
					mLayoutWheel.addView(petWheelView);
					mRLayoutWheel.setVisibility(View.VISIBLE);
					return true;
				}
				return false;
			}
		});
        
		mViewGender.setOnEdClickListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					genderWheelView = new GenderWheelView(getApplicationContext(),true);
					mLayoutWheel.removeAllViews();
					mLayoutWheel.addView(genderWheelView);
					mRLayoutWheel.setVisibility(View.VISIBLE);
					return true;
				}
				return false;
			}
		});
        
        mViewClothingShape1.setOnClickListener(this);
        mViewClothingShape2.setOnClickListener(this);
        mViewClothingShape3.setOnClickListener(this);
	}

	protected void initView(){
		super.initView();
		mTitleBar=(TitleBar) findViewById(R.id.titlebar);
		initTitleBar("爱宠信息",true);		
		mLayoutWheel=(LinearLayout) findViewById(R.id.layout_wheel);
	    mRLayoutWheel=(RelativeLayout) findViewById(R.id.rlayout_wheel);
	    mBtnOk=(Button) findViewById(R.id.btn_ok);
	    mViewClothingShape1=(ImageTextView) findViewById(R.id.view_clothingShape1);
	    mViewClothingShape2=(ImageTextView) findViewById(R.id.view_clothingShape2);
	    mViewClothingShape3=(ImageTextView) findViewById(R.id.view_clothingShape3);
		mViewVariety=(InputView) findViewById(R.id.view_variety);
		mViewGender=(InputView) findViewById(R.id.view_gender);
		mViewBodyLength=(InputView) findViewById(R.id.view_bodyLength);
		mViewBodyWeight=(InputView) findViewById(R.id.view_bodyWeight);
		mViewBust=(InputView) findViewById(R.id.view_bust);
		mViewNeckCf=(InputView) findViewById(R.id.view_neckCf);
		mBtnCancle=(Button) findViewById(R.id.btn_cancle);
		mBtnSave=(Button) findViewById(R.id.btn_save);
	}
	
	private void setInfoData(){
		
		if (null==mPetinfoSpecDTOs||mPetinfoSpecDTOs.isEmpty()) {
			mPetinfoSpecDTOs=new ArrayList<OrderProductSpecDTO>();
			return ;
		}else {
			for (int i = 0; i < mPetinfoSpecDTOs.size(); i++) {
				OrderProductSpecDTO dto=mPetinfoSpecDTOs.get(i);
				if (dto.getId().equals("bust")) {
					//胸围
					mViewBust.setText(dto.getValue());

				} else if (dto.getId().equals("neckCf")) {
					//颈围
					mViewNeckCf.setText(dto.getValue());

				} else if (dto.getId().equals("bodyLength")) {
	                //身长
					mViewBodyLength.setText(dto.getValue());
					
				} else if (dto.getId().equals("variety")) {
					//品种
					mViewVariety.setText(dto.getValue());

				} else if (dto.getId().equals("bodyWeight")) {
					//体重
					mViewBodyWeight.setText(dto.getValue());

				} else if (dto.getId().equals("gender")) {
					//性别
					mViewGender.setText(dto.getValue());

				} else if (dto.getId().equals("clothingShape")) {
					clothingShape=dto.getValue();
					//版式
					if (dto.getValue().equals("宽松")) {
						mViewClothingShape1.setSelectBackgroundResource(R.drawable.clothing_type_selected);
						mViewClothingShape2.setBackgroundResource(R.drawable.clothing_type_default);
						mViewClothingShape3.setBackgroundResource(R.drawable.clothing_type_default);
					}else if (dto.getValue().equals("合身")) {
						mViewClothingShape2.setSelectBackgroundResource(R.drawable.clothing_type_selected);
						mViewClothingShape1.setBackgroundResource(R.drawable.clothing_type_default);
						mViewClothingShape3.setBackgroundResource(R.drawable.clothing_type_default);
					}else {
						mViewClothingShape3.setSelectBackgroundResource(R.drawable.clothing_type_selected);
						mViewClothingShape1.setBackgroundResource(R.drawable.clothing_type_default);
						mViewClothingShape2.setBackgroundResource(R.drawable.clothing_type_default);
					}

				}
			}	
		}
	}

	
	
	
	@Override
	protected void initTitleBar(String title, boolean finsihEnable) {
		super.initTitleBar(title, false);
		mTitleBar.setOnClickBackListener(new OnClickBackListener() {
			
			@Override
			public void OnClickBackListener() {
				onFinish();
			}
		});
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKUSERLIST:
			break;
		default:
			break;
		}
		
	}
	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			if (mLayoutWheel.getChildAt(0)==petWheelView) {
				int[] index=petWheelView.getSelectIndex();
				mViewVariety.setText(Constants.petTypes.get(index[0]).getTypeName()+" "+Constants.petTypes.get(index[0]).getName()[index[1]]);
			}else if (mLayoutWheel.getChildAt(0)==genderWheelView) {
				mViewGender.setText(genderWheelView.getSelectedGender());
			}
			 mRLayoutWheel.setVisibility(View.GONE);
             break;
		case R.id.view_clothingShape1:
			if (!mViewClothingShape1.isSelected()) {
				clothingShape=mViewClothingShape1.getText();
				mViewClothingShape1.setSelectBackgroundResource(R.drawable.clothing_type_selected);
				mViewClothingShape2.setBackgroundResource(R.drawable.clothing_type_default);
				mViewClothingShape3.setBackgroundResource(R.drawable.clothing_type_default);
			}
			break;
		case R.id.view_clothingShape2:
			if (!mViewClothingShape2.isSelected()) {
				clothingShape=mViewClothingShape2.getText();
				mViewClothingShape2.setSelectBackgroundResource(R.drawable.clothing_type_selected);
				mViewClothingShape1.setBackgroundResource(R.drawable.clothing_type_default);
				mViewClothingShape3.setBackgroundResource(R.drawable.clothing_type_default);
			}
			break;
		case R.id.view_clothingShape3:
			if (!mViewClothingShape3.isSelected()) {
				clothingShape=mViewClothingShape3.getText();
				mViewClothingShape3.setSelectBackgroundResource(R.drawable.clothing_type_selected);
				mViewClothingShape1.setBackgroundResource(R.drawable.clothing_type_default);
				mViewClothingShape2.setBackgroundResource(R.drawable.clothing_type_default);
			}
			break;
		case R.id.btn_save:
			String variety=mViewVariety.getText();
			String bust=mViewBust.getText();
			String bodyLength=mViewBodyLength.getText();
			String bodyWeight=mViewBodyWeight.getText();
			String neckCf=mViewNeckCf.getText();
			String gender=mViewGender.getText();
			
			
			if (variety.trim().equals("") || bust.trim().equals("")
					|| bodyLength.trim().equals("")
					|| bodyWeight.trim().equals("") || neckCf.trim().equals("")
					|| gender.trim().equals("")
					|| clothingShape.trim().equals("")) {
				ToastUtiles.showDefault(getApplicationContext(), "请完善信息");

			}else {
				mPetinfoSpecDTOs.clear();
				setListInfo("bust", bust);
				setListInfo("neckCf", neckCf);
				setListInfo("bodyLength", bodyLength);
				setListInfo("variety", variety);
				setListInfo("bodyWeight",bodyWeight);
				setListInfo("gender", gender);
				setListInfo("clothingShape", clothingShape);
				DataFileCache.getSingleton().asyncSaveData("petinfo_"+ UserManager.getSingleton().getActivePetId(), mPetinfoSpecDTOs);
				finish();
			}
			break;
		case R.id.btn_cancle:
			finish();
			break;
		default:
			break;
		}
	}
	
	private void setListInfo(String id,String value){
		OrderProductSpecDTO dto =new OrderProductSpecDTO();
		dto.setId(id);
		dto.setValue(value);
		mPetinfoSpecDTOs.add(dto);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			onFinish();
			return true;
		}
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}
	
	private void onFinish() {
		if ( mRLayoutWheel.getVisibility()==View.VISIBLE) {
			 mRLayoutWheel.setVisibility(View.GONE);
		}else {
			finish();
		}

	}
}
