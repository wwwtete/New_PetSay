package com.petsay.component.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;

/**
 * 话题列表头view
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/31
 * @Description
 */
public class TopicListHeadView extends RelativeLayout {


    private ImageView mIv_bg;
    private ImageView mIv_superscript;
    private TextView mTv_title;

    public TopicListHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TopicListHeadView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(),R.layout.topiclist_head_view,this);
        bindViews();
    }

    private void bindViews() {
        mIv_bg = (ImageView) findViewById(R.id.iv_bg);
        mIv_superscript = (ImageView) findViewById(R.id.iv_superscript);
        mTv_title = (TextView) findViewById(R.id.tv_title);
    }

    /**
     * 设置背景图片
     * @param url
     */
    public void setImageUrl(String url){
        ImageLoaderHelp.displayContentImage(url,mIv_bg,new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                mIv_bg.setImageResource(R.drawable.pet1);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if(bitmap != null){
                    mIv_bg.setImageBitmap(bitmap);
                }else {
                    mIv_bg.setImageResource(R.drawable.pet1);
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        },null);
//        ImageLoaderHelp.displayImage(url,mIv_bg);
    }

    /**
     * 设置角标图片资源ID
     * @param resId
     */
    public void setSuperscriptResId(int resId){
        mIv_superscript.setImageResource(resId);
    }

    /**
     * 设置标题
     * @param title
     */
    public void setContent(String title){
        this.mTv_title.setText(title);
    }



}
