package com.petsay.activity.global.guide.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.activity.main.MainActivity;
import com.petsay.cache.SharePreferenceCache;

public class GuideView extends RelativeLayout{

	private Activity mActivity;
	private ImageView imgPet,imgGuide1,imgGuide2,imgGuide3;
	private RelativeLayout mLayoutGuide;
//	private ImageView imgPlay;
	private Animation anim;
	private boolean isPlay = false;
	private int mFlag;
	private boolean mIsFromStart;
//private String[] soundPaths={"welcomesound1.mp3","welcomesound2.mp3","welcomesound3.mp3"};
	public GuideView(Activity activity,int flag,boolean isFromStart) {
		super(activity);
		mActivity=activity;
		mFlag=flag;
		mIsFromStart=isFromStart;
		inflate(mActivity, R.layout.guide_layout1, this);
		initView();
	}
	
	private void initView(){
		imgPet = (ImageView) findViewById(R.id.img_pet);
//		imgPlay=(ImageView) findViewById(R.id.img_play);
		imgGuide1=(ImageView) findViewById(R.id.guide1_title);
		imgGuide2=(ImageView) findViewById(R.id.guide1_img2);
		imgGuide3=(ImageView) findViewById(R.id.guide1_img1);
		mLayoutGuide=(RelativeLayout) findViewById(R.id.layout_guide);
//		int animRes=R.anim.guide1_anim;
		switch (mFlag) {
		case 1:
//			animRes=R.anim.guide1_anim;
			mLayoutGuide.setBackgroundColor(Color.rgb(0x85, 0xcb, 0xfc));
			imgPet.setImageResource(R.drawable.guide1_pic);
			imgGuide1.setImageResource(R.drawable.guide1_title);
			imgGuide2.setImageResource(R.drawable.guide1_point);
			imgGuide3.setVisibility(View.VISIBLE);
			imgGuide3.setImageResource(R.drawable.guide1_img1);
			break;
		case 2:
			mLayoutGuide.setBackgroundColor(Color.rgb(0xf7, 0xac, 0xe4));
			imgPet.setImageResource(R.drawable.guide2_pic);
			imgGuide1.setImageResource(R.drawable.guide2_title);
			imgGuide2.setImageResource(R.drawable.guide2_point);
			imgGuide3.setVisibility(View.GONE);
			break;
		case 3:
//			animRes=R.anim.guide3_anim;
			mLayoutGuide.setBackgroundColor(Color.rgb(0xfe, 0xbf, 0x56));
			imgPet.setImageResource(R.drawable.guide3_pic);
			imgGuide1.setImageResource(R.drawable.guide3_title);
			imgGuide2.setImageResource(R.drawable.guide3_point);
			imgGuide3.setVisibility(View.VISIBLE);
			imgGuide3.setImageResource(R.drawable.guide3_img1);
			break;
		case 4:
			mLayoutGuide.setBackgroundColor(Color.rgb(0x92, 0xd7, 0x60));
			imgPet.setImageResource(R.drawable.guide4_pic);
			imgGuide1.setImageResource(R.drawable.guide4_title);
			imgGuide2.setImageResource(R.drawable.guide4_point);
			imgGuide3.setVisibility(View.VISIBLE);
			imgGuide3.setImageResource(R.drawable.guide4_img1);
			break;
		case 5:
			mLayoutGuide.setBackgroundColor(Color.rgb(0x9f, 0xa2, 0xf1));
			imgPet.setImageResource(R.drawable.guide5_pic);
			imgGuide1.setImageResource(R.drawable.guide5_title);
			imgGuide2.setImageResource(R.drawable.guide5_point);
			imgGuide3.setVisibility(View.VISIBLE);
			imgGuide3.setImageResource(R.drawable.guide5_img1);
			break;
		}
		anim =AnimationUtils.loadAnimation(getContext(), R.anim.bottom_in);

	}
}
