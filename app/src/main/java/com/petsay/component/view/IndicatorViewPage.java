package com.petsay.component.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.utile.PublicMethod;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/6/10
 * @Description 带有指示器的ViewPage
 */
public class IndicatorViewPage extends RelativeLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPage;
    private LinearLayout mLlIndicators;
    private IndicatorAdapter mAdapter;
    private Drawable mUnfocused;
    private Drawable mFocuse;

    public IndicatorViewPage(Context context) {
        super(context);
        initView();
    }

    public IndicatorViewPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.indicator_viewpage,this);

        setUnfocused(getResources().getDrawable(R.drawable.page_indicator_unfocused));
        setFocuse(getResources().getDrawable(R.drawable.page_indicator_focused));
        mViewPage = (ViewPager) findViewById(R.id.vPager);
        mViewPage.setOnPageChangeListener(this);
        mLlIndicators = (LinearLayout) findViewById(R.id.ll_indicator);
    }



    public void setData(String[] imageURLs){
        if(imageURLs == null)
            return;

        ImageView[] imgs = new ImageView[imageURLs.length];
        mLlIndicators.removeAllViews();
        int size = PublicMethod.getDiptopx(getContext(),8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size,size);
        int marg = PublicMethod.getDiptopx(getContext(),3);
        params.setMargins(marg, marg, marg, marg);
        for (int i=0;i<imageURLs.length;i++){
            ImageView img = new ImageView(getContext());
            ImageLoaderHelp.displayContentImage(imageURLs[i],img);
            imgs[i] = img;
            View ind = new View(getContext());
            mLlIndicators.addView(ind,params);
        }

        mLlIndicators.setVisibility(imageURLs.length > 1 ? VISIBLE : GONE);
        mAdapter = new IndicatorAdapter(imgs);
        mViewPage.setAdapter(mAdapter);
        updateSelectState(0);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int index) {
        updateSelectState(index);
    }

    private void updateSelectState(int index){
        for (int i = 0; i < mLlIndicators.getChildCount(); i++) {
            View view = mLlIndicators.getChildAt(i);
            if (i==index) {
                view.setBackgroundDrawable(mFocuse);
            }else {
                view.setBackgroundDrawable(mUnfocused);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }


    class IndicatorAdapter extends PagerAdapter{

        public ImageView[] mImgs;

        public IndicatorAdapter(ImageView[] imgs){
            this.mImgs = imgs;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImgs[position]);
            return mImgs[position];
//            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImgs[position]);
//            super.destroyItem(container, position, object);
        }

        @Override
        public int getCount() {
            if(mImgs == null)
                return 0;
            else
                return mImgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }

    public void setFocuse(Drawable mFocuse) {
        this.mFocuse = mFocuse;
    }

    public void setUnfocused(Drawable mUnfocused) {
        this.mUnfocused = mUnfocused;
    }

    public Drawable getUnfocused() {
        return mUnfocused;
    }

    public Drawable getFocuse() {
        return mFocuse;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return mViewPage.dispatchTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
}
