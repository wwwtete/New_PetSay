package com.petsay.component.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/4/3
 * @Description
 */
public class ImageSuperscriptView extends RelativeLayout implements View.OnClickListener {

    private ImageView mIvContent;
    private ImageView mIvTRMark;
    private ImageSuperscriptCallback mCallback;

    public ImageSuperscriptView(Context context) {
        super(context);
        initView();
    }

    public ImageSuperscriptView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.superscrpt_view,this);
        mIvContent = (ImageView) findViewById(R.id.iv_content);
        mIvTRMark = (ImageView) findViewById(R.id.iv_mark_tr);
    }

    public void setCallback(ImageSuperscriptCallback callback){
        mIvTRMark.setOnClickListener(this);
        mCallback = callback;
    }

    public void setTopRightMarkResId(int resId){
        mIvTRMark.setVisibility(VISIBLE);
        mIvTRMark.setImageResource(resId);
    }

    public void setImageContentURL(String url){
        ImageLoaderHelp.displayImage(url,mIvContent);
    }

    public void setImageContentBitmap(Bitmap bmp){
        mIvContent.setImageBitmap(bmp);
    }

    /**
     * 获取图片内容获取右上角角标ImageView
     * @return
     */
    public ImageView getmContent() {
        return mIvContent;
    }

    /**
     * 获取右上角角标ImageView
     * @return
     */
    public ImageView getmTopRightMark() {
        return mIvTRMark;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(mCallback != null)
            mCallback.onClickSuperscriptView(this, (ImageView) v);
    }

    public interface ImageSuperscriptCallback{
        /**
         * 点击角标事件
         * @param view
         * @param markView  点击角标的View
         */
        public void onClickSuperscriptView(ImageSuperscriptView view, ImageView markView);
    }



}
