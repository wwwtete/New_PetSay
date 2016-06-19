package com.petsay.component.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/12
 * @Description
 */
public class ExHintView extends ImageView implements Animation.AnimationListener {

    private Animation mShowAnimation;
    private CycleInterpolator mInter;
    private Animation mHidenAnim;
    private ExHintViewCallback mCallback;

    private int mDistance = 20;

    public ExHintView(Context context) {
        super(context);
        initView();
    }

    public ExHintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ExHintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void setCallback(ExHintViewCallback callback){
        this.mCallback = callback;
    }

    private void initView(){
        mInter = new CycleInterpolator(3.0f);
        mHidenAnim =new AlphaAnimation(1.0f,0.0f);
        mHidenAnim.setDuration(300);
        mHidenAnim.setAnimationListener(this);
        mHidenAnim.setFillAfter(true);
    }

    /**
     * @param isVertical
     */
    public void show(boolean isVertical){
        stopAnimation();
        if(isVertical){
            mShowAnimation = new  TranslateAnimation(0,0,0,mDistance);
        }else {
            mShowAnimation = new  TranslateAnimation(0,mDistance,0,0);
        }

        mShowAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                ViewHelper.setAlpha(ExHintView.this,1.0f);
                setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hiden();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mShowAnimation.setDuration(3000);
        mShowAnimation.setInterpolator(mInter);
        mShowAnimation.setRepeatCount(1);
        startAnimation(mShowAnimation);
    }

    public void stopAnimation(){
        if(mShowAnimation != null){
            mShowAnimation.cancel();
            mShowAnimation = null;
            clearAnimation();
        }
    }

    public void hiden(){
//         stopAnimation();
         this.startAnimation(mHidenAnim);
    }

    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        mDistance = distance;
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        this.setVisibility(GONE);
        if(mCallback != null){
            mCallback.onHidenAnimationFinish(this);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public interface ExHintViewCallback{
        public void onHidenAnimationFinish(ExHintView view);
    }

}
