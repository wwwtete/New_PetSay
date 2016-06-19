package com.petsay.component.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.petsay.R;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/8/6
 * @Description 功能提示PoupWindow
 */
public class HintPopupWindow implements ExHintView.ExHintViewCallback {

    private Context mContext;
    private ExHintView mHint;
    private PopupWindow mPpWindow;
    private RelativeLayout mView;

    public HintPopupWindow(Context context) {
        this.mContext = context;
        initView();
    }

    private void initView() {
//        mView = LayoutInflater.from(mContext).inflate(R.layout.view_hint,null);
//        mHint = (ExHintView) mView.findViewById(R.id.iv_hint);
        mView = new RelativeLayout(mContext);
        mHint = new ExHintView(mContext);
        mHint.setCallback(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = params.bottomMargin = mHint.getDistance();
        mView.addView(mHint, params);

        mPpWindow = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPpWindow.setFocusable(false);
        mPpWindow.setOutsideTouchable(false);
        mPpWindow.setBackgroundDrawable(new BitmapDrawable());

    }

    public void show(View parentView,int resId){
      show(parentView,Gravity.TOP,resId);
    }


    public void show(View parentView,int gravity,int resId){
        int[] locs = new int[2];
        parentView.getLocationOnScreen(locs);
       switch (gravity){
           case Gravity.BOTTOM:
               locs[1] += parentView.getHeight();
               break;
           case Gravity.RIGHT:
               locs[0] += parentView.getWidth();
               break;
           case Gravity.TOP:
               locs[1] = locs[1] - parentView.getHeight();
               break;
       }
        show(parentView,resId,locs[0],locs[1]);
    }


    public void show(View parentView,int resId,int x,int y){
        mHint.setImageResource(resId);
        mPpWindow.showAtLocation(parentView, Gravity.NO_GRAVITY, x, y);
        mHint.show(true);
    }

    @Override
    public void onHidenAnimationFinish(ExHintView view) {
        mPpWindow.dismiss();
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener onDismissListener){
        if(mPpWindow != null)
            mPpWindow.setOnDismissListener(onDismissListener);
    }

}
