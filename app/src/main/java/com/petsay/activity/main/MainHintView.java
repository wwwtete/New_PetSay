package com.petsay.activity.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.petsay.R;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/8/7
 * @Description
 */
public class MainHintView extends RelativeLayout implements View.OnClickListener {

    private ImageView mIvCustom;
    private View mTopView;
    private MainHintViewCallback mCallback;

    public MainHintView(Context context) {
        super(context);
        initView();
    }

    public MainHintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_main_hint,this);
        setBackgroundResource(R.color.transparent2);
        this.setFocusable(true);
        mTopView = findViewById(R.id.rl_top);
        mIvCustom = (ImageView) findViewById(R.id.iv_custom);
        this.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(mIvCustom.getVisibility() == VISIBLE) {
            this.setOnClickListener(null);
            AlphaAnimation animation = new AlphaAnimation(1,0);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    setVisibility(GONE);
                    if (mCallback != null)
                        mCallback.onClose(MainHintView.this);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setDuration(500);
           this.startAnimation(animation);
        }else {
            mTopView.setVisibility(GONE);
            mIvCustom.setVisibility(VISIBLE);
        }
    }

    public void setmCallback(MainHintViewCallback mCallback) {
        this.mCallback = mCallback;
    }


    public interface MainHintViewCallback{
        void onClose(MainHintView view);
    }

}
