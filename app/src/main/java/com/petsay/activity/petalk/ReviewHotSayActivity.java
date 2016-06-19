package com.petsay.activity.petalk;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.petsay.R;
import com.petsay.activity.BaseActivity;
import com.petsay.component.gifview.AudioGifView;
import com.petsay.component.gifview.GifViewManager;
import com.petsay.component.gifview.ImageLoaderListener;
import com.petsay.component.view.ExProgressBar;
import com.petsay.component.view.Rotate3dAnimation;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.SayDataNet;
import com.petsay.application.UserManager;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.petalk.PetalkDecorationVo;
import com.petsay.vo.petalk.PetalkVo;

import java.util.LinkedList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author wangw
 * 审核新说说
 */
public class ReviewHotSayActivity extends BaseActivity implements OnClickListener,NetCallbackInterface {

    @InjectView(R.id.layout_content)
    private View mLayoutView;
    @InjectView(R.id.layout_backup_img)
    private View mLayoutBackupImg;
    @InjectView(R.id.img_backup_pet)
    private ImageView mimgBackupPet;

    @InjectView(R.id.layout_img)
    private View mLayoutImg;
    @InjectView(R.id.img_pet)
    private ImageView mImgPet;

    @InjectView(R.id.iv_flower)
    private ImageView mImgFlower;
    @InjectView(R.id.am_gif)
    private AudioGifView mGifView;
    @InjectView(R.id.tv_content)
    private TextView mTvContent;
    @InjectView(R.id.iv_down)
    private ImageView mImgDown;
    @InjectView(R.id.iv_up)
    private ImageView mImgUp;

    @InjectView(R.id.iv_flag)
    private ImageView mIvFlag;
    @InjectView(R.id.iv_backup_flag)
    private ImageView mIvBackupFlag;

    private SayDataNet mSayDataNet;
    private LinkedList<PetalkVo>  mSayList;
    private PetalkVo mCurSay;
    private Rotate3dAnimation mHidenAnimation;
    private Rotate3dAnimation mShowAnimation;
    private AnimationDrawable mFlowDrawable;
    private int mFlowerDuration;
    private Handler mHandler;
    private int mDisplayWidth;
    private DisplayImageOptions contentPetImgOptions;
    private ImageView mCurrImgPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_hotsay_layout);
        initView();
        mSayList = new LinkedList<PetalkVo>();
        mSayDataNet = new SayDataNet();
        mSayDataNet.setTag(this);
        mSayDataNet.setCallback(this);
        getSayList();
    }

    @Override
    protected void initView() {
        super.initView();
        initTitleBar("一起选热门");
        mTitleBar.setFinishEnable(true);
        mImgDown.setOnClickListener(this);
        mImgUp.setOnClickListener(this);
        mDisplayWidth = PublicMethod.getDisplayWidth(ReviewHotSayActivity.this);
        initGifView(mDisplayWidth);
        initFlowerView(mDisplayWidth);
        initCutAnim();
        initImageOptions();
        showLoading();
        setBtnClickable(false);
    }

    private void initImageOptions() {
        contentPetImgOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//		.showImageOnLoadingBackground(R.color.img_content_bg_color)
                .showImageOnLoading(R.drawable.pet1)
                .showImageForEmptyUri(R.drawable.pet1)
                .showImageOnFail(R.drawable.pet1)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(false)
                .resetViewBeforeLoading(true) // default
                .displayer(new FadeInBitmapDisplayer(500,true,true,false))
//		.displayer(new RoundedBitmapDisplayer(1000))
//		.displayer(new RoundedVignetteBitmapDisplayer(25, 0))
                .delayBeforeLoading(300)
                .build();
    }

    private void initGifView(int width){
        PublicMethod.initPetalkViewLayout(mImgPet, width);
        PublicMethod.initPetalkViewLayout(mimgBackupPet, width);

        ExProgressBar progressBar = (ExProgressBar)findViewById(R.id.pro_loaderpro);
        ImageLoaderListener imgPetListener = new ImageLoaderListener(progressBar);
        mImgPet.setTag(imgPetListener);
        mImgPet.setOnClickListener(this);

        ExProgressBar backupBar = (ExProgressBar) findViewById(R.id.pro_backup_loaderpro);
        ImageLoaderListener imgBackupPetListener = new ImageLoaderListener(backupBar);
        mimgBackupPet.setTag(imgBackupPetListener);
        mimgBackupPet.setOnClickListener(this);

        View playView = findViewById(R.id.img_play);
//        android.widget.RelativeLayout.LayoutParams playViewParams = (android.widget.RelativeLayout.LayoutParams) playView.getLayoutParams();
//        playViewParams.topMargin = (width - PublicMethod.getDiptopx(this, 100))/2 + PublicMethod.getDiptopx(this, 50);
//        playView.setLayoutParams(playViewParams);
        mGifView.setPlayBtnView(playView);

        ProgressBar bar = (ProgressBar) findViewById(R.id.playprogressbar);
        mGifView.setPlayProgressBar(bar);
    }

    private void initFlowerView(int width){
        mHandler = new Handler();
        mFlowDrawable = (AnimationDrawable) mImgFlower.getDrawable();
        for (int i = 0; i < mFlowDrawable.getNumberOfFrames(); i++) {
            mFlowerDuration += mFlowDrawable.getDuration(i);
        }
    }

    private void reseGifVIew(PetalkVo petalk){
        if(petalk == null){
            showToast("数据异常，请重试");
            showLoading();
            return;
        }
        int width = mDisplayWidth- PublicMethod.getDiptopx(this, 10);
        mTvContent.setText(petalk.getDescription());

        ImageLoaderListener mImageLoaderListener = null;
        if(mCurrImgPet == null){
            mCurrImgPet = mImgPet;
            updateFlag(mIvFlag,petalk);
            mImageLoaderListener = (ImageLoaderListener) mCurrImgPet.getTag();
            ImageLoaderHelp.displayImage(petalk.getPhotoUrl(), mCurrImgPet,contentPetImgOptions,mImageLoaderListener,mImageLoaderListener);
        }
        //执行预加载图片
        if(mCurrImgPet == mImgPet){
            preLoad(mimgBackupPet,mIvBackupFlag);
        }else {
            preLoad(mImgPet,mIvFlag);
        }

        mGifView.reset();
        if(petalk.isAudioModel()){
            mImageLoaderListener = (ImageLoaderListener) mCurrImgPet.getTag();
            mGifView.setImageLoaderListener(mImageLoaderListener);
            //初始化Gif的Item的布局
            LayoutParams params = (LayoutParams) mGifView.getLayoutParams();
            PetalkDecorationVo ad = petalk.getDecorations()[0];
            mGifView.initData(petalk, width, width);
            PublicMethod.updateGifItemLayout(width, width, ad, mGifView, params);
            boolean flag = GifViewManager.getInstance().getAllowAutoPlay();
            mGifView.setPlayBtnVisibility(!flag);
            if(flag)
                GifViewManager.getInstance().playGif(mGifView);
            else {
                mGifView.setPlayBtnVisibility(true);
            }
        }
    }

    private void updateFlag(ImageView ivFlag,PetalkVo petalk){
        if(petalk.getModel() == 0){
            ivFlag.setImageResource(R.drawable.audio_flag_icon);
        }else {
            ivFlag.setImageResource(R.drawable.image_flag_icon);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GifViewManager.getInstance().stopGif();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_pet:
            case R.id.img_backup_pet:
                GifViewManager.getInstance().pauseGif(mGifView);
                break;
            case R.id.iv_up:
                choiceSay(1);
                onNext(1);
                break;
            case R.id.iv_down:
                choiceSay(0);
                onNext(0);
                break;
        }
    }

    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        switch (requestCode) {
            case RequestCode.REQUEST_GETREVIEWHOTSAYLIST:
                List<PetalkVo> says;
                try {
                    says = JsonUtils.getList(bean.getValue(), PetalkVo.class);
                    onGetHotSayData(says);
                    setBtnClickable(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                closeLoading();
                break;
            case RequestCode.REQUEST_CHOICEHOTSAY:
                break;
        }
    }

    private void onGetHotSayData(List<PetalkVo> says) {
        mSayList.addAll(says);
        if(mCurSay == null){
            mCurSay = mSayList.pollFirst();
            reseGifVIew(mCurSay);
        }
    }

    private void onNext(int code) {
        mCurSay = mSayList.pollFirst();
        if(mSayList.size() == 1){
            getSayList();
        }
        if(mCurSay == null){
            showLoading();
        }else{
            mGifView.clearView();
            if(code == 1){
                startFlowerAnimation();
            }else{
                mLayoutView.startAnimation(mHidenAnimation);
            }
        }
    }

    private boolean preLoad(ImageView view,ImageView ivFlag){
        if(mSayList.size() > 0){
            PetalkVo vo = mSayList.getFirst();
            ImageLoaderListener listener = (ImageLoaderListener) view.getTag();
            ImageLoaderHelp.displayImage(vo.getPhotoUrl(),view,contentPetImgOptions,listener,listener);
            updateFlag(ivFlag, vo);
            return true;
        }
        return false;
    }

    private void startFlowerAnimation(){
        GifViewManager.getInstance().stopGif();
        mFlowDrawable.stop();
        mImgFlower.setVisibility(View.VISIBLE);
        setBtnClickable(false);
        mFlowDrawable.start();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFlowDrawable.stop();
                mLayoutView.startAnimation(mHidenAnimation);
            }
        }, mFlowerDuration);
    }

    private void nextPetalk(){
        setBtnClickable(true);
        if(mCurSay != null){
            reseGifVIew(mCurSay);
        }else {
            showLoading();
        }
    }

    private void setBtnClickable(boolean flag){
        mImgDown.setClickable(flag);
        mImgUp.setClickable(flag);
    }

    private void initCutAnim(){
        // 计算中心点
        final float centerX = mDisplayWidth / 2.0f;
        final float centerY = mDisplayWidth / 2.0f;
        mHidenAnimation= new Rotate3dAnimation(0, 90,centerX, centerY, 310.0f, true);
        mHidenAnimation.setDuration(300);
        mHidenAnimation.setFillAfter(true);
        mHidenAnimation.setInterpolator(new AccelerateInterpolator());
        mHidenAnimation.setAnimationListener(new CutViewListener(0));

        mShowAnimation = new Rotate3dAnimation(-90, 0, centerX, centerY, 310.0f, false);
        mShowAnimation.setDuration(300);
        mShowAnimation.setFillAfter(true);
        mShowAnimation.setInterpolator(new DecelerateInterpolator());
        mShowAnimation.setAnimationListener(new CutViewListener(1));
    }

    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        closeLoading();
        onErrorShowToast(error);
        if(requestCode == 500)
            setBtnClickable(false);
    }

    /**
     * 获取说说列表
     */
    private void getSayList() {
        mSayDataNet.getReviewHotSayList(UserManager.getSingleton().getActivePetId(), 10);
    }

    /**
     * 选择说说
     * @param code "0"：过；"1"：有趣；"2"：糟糕
     */
    private void choiceSay(int code){
        if(mCurSay != null)
            mSayDataNet.choiceHotSay(code, UserManager.getSingleton().getActivePetId(), mCurSay.getPetalkId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSayDataNet != null){
            mSayDataNet.cancelAll(this);
            mSayDataNet.setCallback(null);
            mSayDataNet = null;
        }
        if(mShowAnimation != null){
            mShowAnimation.cancel();
            mShowAnimation.setAnimationListener(null);
            mShowAnimation = null;
        }
        if(mHidenAnimation != null){
            mHidenAnimation.cancel();
            mHidenAnimation.setAnimationListener(null);
            mHidenAnimation = null;
        }
        mFlowDrawable = null;
    }

    class CutViewListener implements AnimationListener{

        private int mState;	//0：隐藏，1：显示

        public CutViewListener(int state){
            mState = state;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            setBtnClickable(false);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(mState == 0){	//隐藏动画结束
                onHidenAnimEnd();
            }else if(mState == 1) {
                nextPetalk();
            }
        }

        private void onHidenAnimEnd() {
            if(mCurrImgPet == mImgPet){
                mCurrImgPet = mimgBackupPet;
                mLayoutImg.setVisibility(View.INVISIBLE);
                mLayoutBackupImg.setVisibility(View.VISIBLE);
            }else {
                mCurrImgPet = mImgPet;
                mLayoutImg.setVisibility(View.VISIBLE);
                mLayoutBackupImg.setVisibility(View.INVISIBLE);
            }
            mImgFlower.setVisibility(View.GONE);
            mTvContent.setText(mCurSay.getDescription());
            mLayoutView.post(new Runnable() {
                @Override
                public void run() {
                    mLayoutView.startAnimation(mShowAnimation);
                }
            });
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    }

}
