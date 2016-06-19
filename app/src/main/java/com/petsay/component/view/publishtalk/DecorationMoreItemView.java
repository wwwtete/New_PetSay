package com.petsay.component.view.publishtalk;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.petsay.R;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.application.ImageLoaderOptionsManager;
import com.petsay.vo.decoration.DecorationBean;
import com.petsay.vo.decoration.DecorationDataManager;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/6
 * @Description
 */
public class DecorationMoreItemView extends LinearLayout implements DecorationItemView {

    private DecorationBean mBean;
    private ImageView ivThumbnail;
    private FrameLayout barLoading;
    private ImageView ivStatus;

    public DecorationMoreItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.decoration_more_item_view,this);
        this.setOrientation(VERTICAL);
        findViews();
    }

    private void findViews() {
        ivThumbnail = (ImageView)findViewById( R.id.iv_thumbnail );
        barLoading = (FrameLayout)findViewById( R.id.bar_loading );
        ivStatus = (ImageView)findViewById( R.id.iv_status );
    }


    @Override
    public void updateView(DecorationBean bean) {
        this.mBean = bean;
        ImageLoaderHelp.displayImage(bean.getThumbnail(), ivThumbnail, ImageLoaderOptionsManager.getmDecorationOptions());
        setDownloadStatus();
    }

    @Override
    public DecorationBean getData() {
        return mBean;
    }

    @Override
    public void showLoading(boolean flag) {
        if(flag)
            barLoading.setVisibility(VISIBLE);
        else
            barLoading.setVisibility(GONE);
    }

    @Override
    public void setDownloadFaile() {
        showLoading(false);
        setDownloadStatus();
    }

    @Override
    public void setDownloadSuccess() {
        showLoading(false);
        setDownloadStatus();
    }

    @Override
    public void resetView() {
        showLoading(false);
        setDownloadStatus();
    }

    private void setDownloadStatus(){
        if(mBean != null ){
            if(mBean.isDownloaded() || mBean.isAssetsed() || DecorationDataManager.getInstance(getContext()).checkFileDownload(mBean))
                ivStatus.setImageResource(R.drawable.decoration_use_status);
            else
                ivStatus.setImageResource(R.drawable.decoration_download_status);
        }
    }

    public void setWidht(int widht){
        View view = findViewById(R.id.temp);
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        params.width = widht;
        view.setLayoutParams(params);

        ViewGroup.LayoutParams params1  = ivThumbnail.getLayoutParams();
        params1.width = widht;
        ivThumbnail.setLayoutParams(params1);

    }
}
