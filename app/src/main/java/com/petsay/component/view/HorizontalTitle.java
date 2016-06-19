package com.petsay.component.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petsay.utile.PublicMethod;

/**
 * Created by wangw on 2014/12/15.
 *
 * 横向滑动标题组件
 */
public class HorizontalTitle extends LinearLayout {

    private String[]  mTitles;
    private ClickTitleCallback mCallback;
    private TextView mCurrTxt;
    private int mSelectColor;
    private int mNormalColor;

    public HorizontalTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInitView();
    }

    private void onInitView() {
        mSelectColor = Color.parseColor("#7B7EE2");
        mNormalColor = Color.parseColor("#787878");
        this.setOrientation(HORIZONTAL);
    }

    public void setCallback(ClickTitleCallback callback){
        this.mCallback = callback;
    }

    public void setNormalColor(int color){
        if(getChildCount() > 0){
            for (int i = 0; i<getChildCount();i++){
                View view = getChildAt(i);
                if(view != null && view instanceof TextView && view != mCurrTxt){
                    ((TextView)view).setTextColor(color);
                }
            }
        }
        mNormalColor = color;
    }

    public void setSelectColor(int color){
        if(mCurrTxt != null){
            mCurrTxt.setTextColor(color);
        }
        mSelectColor = color;
    }

    public void initView(String[] titles,ClickTitleCallback callback){
        this.removeAllViews();
        mTitles = titles;
        if(titles != null && titles.length > 0){
            int temp = PublicMethod.getDisplayWidth(getContext())/3;
            int avgW = PublicMethod.getDisplayWidth(getContext())/titles.length;
            avgW = avgW < temp ? temp : avgW;

            for (int i = 0;i<titles.length;i++){
                String title = titles[i];
                TextView txt = new TextView(getContext());
                txt.setText(title);
                txt.setTag(i);
                txt.setGravity(Gravity.CENTER);
                if(i == 0) {
                    txt.setTextColor(mSelectColor);
                    txt.setTextSize(20);
                    mCurrTxt = txt;
                }else{
                    txt.setTextColor(mNormalColor);
                    txt.setTextSize(18);
                }

                txt.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mCurrTxt != null){
                            mCurrTxt.setTextColor(mNormalColor);
                            mCurrTxt.setTextSize(18);
                        }
                        TextView txt = (TextView) v;
                        txt.setTextSize(20);
                        txt.setTextColor(mSelectColor);
                        if(mCallback != null){
                            mCallback.onClickTitleCallback(txt.getText().toString(), (Integer) txt.getTag());
                        }
                        mCurrTxt = txt;
                    }
                });
                LayoutParams params = new LayoutParams(avgW,LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                addView(txt,params);
            }
        }
    }

    public interface ClickTitleCallback{
        public void onClickTitleCallback(String title,int position);
    }

}
