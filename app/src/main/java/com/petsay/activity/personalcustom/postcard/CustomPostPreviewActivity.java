package com.petsay.activity.personalcustom.postcard;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.activity.personalcustom.pay.OrderConfirmActivity;
import com.petsay.application.UserManager;
import com.petsay.component.view.TitleBar;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.ProductNet;
import com.petsay.network.net.SayDataNet;
import com.petsay.utile.FileUtile;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.personalcustom.OrderDTO;
import com.petsay.vo.personalcustom.PostCardVo;
import com.petsay.vo.personalcustom.ProductDTO;
import com.petsay.vo.petalk.PetalkVo;

/**
 * 
 * @author GJ
 *明信片预览
 */
public class CustomPostPreviewActivity extends BaseActivity implements NetCallbackInterface,OnClickListener{
	private TitleBar mTitleBar;
	private RelativeLayout mLayoutCard;
	private PostCardView mCardView,mNextCardView;
	private SeekBar mSeekBar;
	private TextView mTvProgress;
	private ImageView mImgGoCustom;
	
	private SayDataNet mSayDataNet;
	private ProductNet mProductNet;
	
	private List<PetalkVo> mPetalkVos=new ArrayList<PetalkVo>();
	private final int pageSize=100;
	public static PostCardVo mPostCardVo;
	private int selectPosition=0;
	private boolean isFirstLoad=true;
	
	private int mTotalSayCount;
	
	private final int mPostBgWidth=750,mPostBgHeight=597;
	private int mDisplayWidth;
	private float downX=0,upX=0;
	private float  percentTop=0.768f,percentLeft=0.6868f;
	private TranslateAnimation mAnimLeftout,mAnimLeftin;
	private int mCardWidth,mCardHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);;
		setContentView(R.layout.activity_custom_postcard_preview);
		initPostCardVo();
		initView();
		setListener();
		initAnim();
		mSayDataNet=new SayDataNet();
		mSayDataNet.setCallback(this);
		mSayDataNet.setTag(this);
		netwrok(false);
		
		mProductNet=new ProductNet();
		mProductNet.setCallback(this);
		mProductNet.setTag(this);
		getProductDetail();
		
	}
	
	private void getProductDetail() {
		mProductNet.productDetailSpecsByCategory(1);
	}

	protected void initView(){
		super.initView();
		mTitleBar = (TitleBar) findViewById(R.id.titlebar);
		mLayoutCard = (RelativeLayout) findViewById(R.id.layout_card);
		initTitleBar(getIntent().getStringExtra("title")+"预览", true);
		mImgGoCustom=(ImageView) findViewById(R.id.img_gocustom);
        initPostPosition();
		mCardView = new PostCardView(CustomPostPreviewActivity.this,mPostCardVo,"mCardView");
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mCardWidth, mCardHeight);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mCardView.setLayoutParams(layoutParams);
		
		mNextCardView=new PostCardView(CustomPostPreviewActivity.this,mPostCardVo,"mNextCardView");
		mNextCardView.setLayoutParams(layoutParams);
		
		mLayoutCard.addView(mNextCardView);
		mLayoutCard.addView(mCardView);
		
		mSeekBar=(SeekBar) findViewById(R.id.seekbar);
		mTvProgress=(TextView) findViewById(R.id.tv_progress);
		mSeekBar.setClickable(false);
		mSeekBar.setEnabled(false);
		
		
	}
	
	@Override
	protected void initTitleBar(String title, boolean finsihEnable) {
		super.initTitleBar(title, finsihEnable);
		mTitleBar.setBgColor(Color.TRANSPARENT);
	}
	private void setListener(){
		mImgGoCustom.setOnClickListener(this);
//		ImageLoaderHelp.getImageLoader().loadImage(UserManager.getSingleton().getActivePetInfo().getHeadPortrait(),new ImageLoadingListener() {
//
//			@Override
//			public void onLoadingStarted(String arg0, View arg1) {}
//
//			@Override
//			public void onLoadingFailed(String arg0, View arg1,FailReason arg2) {
//			}
//
//			@Override
//			public void onLoadingComplete(String arg0,View arg1, Bitmap bitmap) {
//				mCardView.setHeaderBitamp(bitmap);
//			}
//
//			@Override
//			public void onLoadingCancelled(String arg0,View arg1) {}
//		});

		mCardView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					downX=event.getX();
					break;
				case MotionEvent.ACTION_UP:
					upX=event.getX();
					if (downX-upX>50) {
						turnPage(true);
					}else if (downX-upX<-50) {
						turnPage(false);
					}
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				default:
					break;
				}
				return true;
			}
		});
	}
	
	
	
	/**
	 * 联网获取数据
	 * @param pageIndex
	 * @param what
	 */
	private void netwrok(boolean isMore){
		String id = "";
		if (isMore) {
//			if (mPetalkVos!=null&&!mPetalkVos.isEmpty()) {
//				id = ((PetalkVo)mPetalkVos.get(mPetalkVos.size() - 1)).getPetalkId();
//				mSayDataNet.petalkList4Postcard(id, pageSize,isMore);
//			}
		}else {
			mSayDataNet.petalkList4Postcard( "",pageSize,isMore);
		}
	}

	@Override
	public void onSuccessCallback(ResponseBean bean, int requestCode) {
		closeLoading();
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKLIST4POSTCARD:
			try {
				List<PetalkVo> tempList;
				tempList = JsonUtils.parseList(bean.getValue(), PetalkVo.class);
				org.json.JSONObject json=new org.json.JSONObject(bean.getValue());
				mTotalSayCount=json.optInt("totalElements");
				if (mTotalSayCount>pageSize) {
					mTotalSayCount=pageSize;
				}
				mPetalkVos.addAll(tempList);
				initSeekbar();
				if (isFirstLoad&&!mPetalkVos.isEmpty()) {
					isFirstLoad=false;
					mCardView.setPetalkVo(mPetalkVos.get(0));
					for (int i = 0; i < tempList.size(); i++) {
						ImageLoaderHelp.getImageLoader().loadImage(tempList.get(i).getPhotoUrl(), ImageLoaderHelp.contentPetImgOptions, null);
					}
					if (mPetalkVos.size()>1) {
						mNextCardView.setPetalkVo(mPetalkVos.get(selectPosition));
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
			break;
		case RequestCode.REQUEST_ORDERCREATE:
			OrderDTO orderDTO=JsonUtils.resultData(bean.getValue(), OrderDTO.class);
			Intent intent =new Intent(this,OrderConfirmActivity.class);
			intent.putExtra("OrderDTO", orderDTO);
			startActivity(intent);
			break;
		default:
			break;
		}
		
	}
	@Override
	public void onErrorCallback(PetSayError error, int requestCode) {
		closeLoading();
		onErrorShowToast(error);
		switch (requestCode) {
		case RequestCode.REQUEST_PETALKLIST4POSTCARD:
			break;

		default:
			break;
		}
	}
	
	
	private void initPostCardVo(){
		if(null==mPostCardVo){
		mPostCardVo = JsonUtils.resultData(
				FileUtile.getStringFromFile(this, "postcardjson.json"),
				PostCardVo.class);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_gocustom:
			Intent intent=new Intent(this, CustomPostCardActivity.class);
			intent.putExtra("title", getIntent().getStringExtra("title"));
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}
	
	
	/**
	 * 翻页
	 * @param isNext
	 */
	private void turnPage(boolean isNext){
		if (isNext && selectPosition < mPetalkVos.size() - 1) {
			mCardView.startAnimation(mAnimLeftout);
		} else if (!isNext && selectPosition > 0) {
			mCardView.startAnimation(mAnimLeftin);
		}
	}
	
	private void initSeekbar() {
		mSeekBar.setEnabled(true);
		mSeekBar.setMax(mTotalSayCount);
		mSeekBar.setProgress(1);
		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				if (progress<1) {
					seekBar.setProgress(1);
				}else {
					mTvProgress.setText(progress + "/" + mTotalSayCount);
					mCardView.setPetalkVo(mPetalkVos.get(progress-1));
				}
			}
		});
		
	}
	
	private void initPostPosition(){
		mDisplayWidth=PublicMethod.getDisplayWidth(this);
		int dispalyHeight=mPostBgHeight*mDisplayWidth/mPostBgWidth;
		 mCardWidth=(int) (mDisplayWidth*percentTop);
		 mCardHeight=(int) (dispalyHeight*percentLeft);
	}
	
	private void initAnim(){
		mAnimLeftin=new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1.5f, Animation.RELATIVE_TO_SELF, 0, 
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		mAnimLeftin.setDuration(1000);
		mAnimLeftin.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				selectPosition--;
				mCardView.setPetalkVo(mPetalkVos.get(selectPosition));
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mSeekBar.setProgress(selectPosition+1);
				mNextCardView.setPetalkVo(mPetalkVos.get(selectPosition));
			}
		});
		
		mAnimLeftout=new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.5f, 
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		mAnimLeftout.setDuration(1000);
		mAnimLeftout.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				selectPosition++;
				mNextCardView.setPetalkVo(mPetalkVos.get(selectPosition));
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				mSeekBar.setProgress(selectPosition+1);
				mCardView.setPetalkVo(mPetalkVos.get(selectPosition));
			}
		});
	}
}
