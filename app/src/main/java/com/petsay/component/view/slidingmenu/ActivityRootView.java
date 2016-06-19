package com.petsay.component.view.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.petsay.utile.PublicMethod;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/5/7
 * @Description Activity的根视图
 */
public class ActivityRootView extends ViewGroup {

    private View mContent;

    /*是否拦截事件*/
    private boolean mInterceptTouch;

    public ActivityRootView(Context context) {
        super(context);
    }

    public ActivityRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setContent(View content){
        if(mContent != null){
            this.removeView(mContent);
        }
        mContent = content;
        addView(mContent);
    }

    public View getContent() {
        return mContent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(0,widthMeasureSpec);
        int height = getDefaultSize(0,heightMeasureSpec);
        setMeasuredDimension(width,height);

        final int contentWidht = getChildMeasureSpec(widthMeasureSpec,0,width);
        final int contentHeight = getChildMeasureSpec(heightMeasureSpec,0,height);
        mContent.measure(contentWidht,contentHeight);
    }

    /**
     * {@inheritDoc}
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = r - l;
        int height = b - t;
        mContent.layout(0,0,width,height);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return super.onInterceptTouchEvent(ev);
        return  mInterceptTouch;
    }

    /**
     * 是否拦截Touch事件
     * @return
     */
    public boolean ismInterceptTouch() {
        return mInterceptTouch;
    }

    /**
     * 设置是否拦截事件
     * @param mInterceptTouch
     */
    public void setInterceptTouch(boolean mInterceptTouch) {
        this.mInterceptTouch = mInterceptTouch;
    }



}
