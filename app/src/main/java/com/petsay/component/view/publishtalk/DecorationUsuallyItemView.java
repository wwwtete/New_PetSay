package com.petsay.component.view.publishtalk;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.application.ImageLoaderOptionsManager;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.decoration.DecorationBean;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/2
 * @Description
 */
public class DecorationUsuallyItemView extends RelativeLayout implements DecorationItemView{

    private ImageView mImg;
    private View mBar;
    private DecorationBean mBean;

    public DecorationUsuallyItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.decoration_usually_list_item,this);
        int padding = PublicMethod.getDiptopx(getContext(),2);
        this.setPadding(padding,padding,padding,padding);
        mImg = (ImageView) findViewById(R.id.iv_icon);
        mBar =  findViewById(R.id.bar_loading);
//        mImg = new ImageView(getContext());
//        mImg.setPadding(padding, padding, padding, padding);
//        addView(mImg);
//        mBar = new ProgressBar(getContext());
//        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        params.addRule(RelativeLayout.CENTER_VERTICAL);
//        mBar.setVisibility(View.GONE);
//        addView(mBar, params);
    }

    public void updateView(DecorationBean bean){
        mBean = bean;
        ImageLoaderHelp.displayImage(bean.getThumbnail(), mImg, ImageLoaderOptionsManager.getmDecorationOptions());
    }

    public DecorationBean getData(){
        return mBean;
    }

    public void showLoading(boolean flag){
        if(flag) {
            mBar.setVisibility(VISIBLE);

        }else {
            mBar.setVisibility(GONE);
        }
    }

    @Override
    public void setDownloadFaile() {
        showLoading(false);
    }

    @Override
    public void setDownloadSuccess() {
        showLoading(false);
    }

    @Override
    public void resetView() {
       showLoading(false);
    }


}
