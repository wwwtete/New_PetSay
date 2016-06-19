package com.petsay.component.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.constants.Constants;
import com.petsay.network.upload.UploadTools;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToastUtiles;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/8/2
 * @Description
 */
public abstract class UploadView extends RelativeLayout implements UploadTools.UploadServiceListener, View.OnClickListener {

    protected UploadTools mService;
    protected View mView;
    protected ImageView mIvThumbnail;
    protected ProgressBar mUploadBar;
    protected ProgressBar mLoadingBar;
    protected View mRetryView;
    protected ImageView mImgCancel;
    protected ImageView mImgReset;
    protected View mErrorTip;

    protected UploadViewListener mListener;
    protected boolean mCancelUpload;
    protected int mUploadCount;
    protected int mUploadSuccessCount;

    public UploadView(Context context) {
        super(context);
        onCreateView();
    }

    protected View onCreateView() {
        mService = new UploadTools();
        mService.setUploadListener(this);
        mView = inflate(getContext(),R.layout.upload_view, this);
        mIvThumbnail = (ImageView) mView.findViewById(R.id.iv_thumbnail);
        mUploadBar = (ProgressBar) mView.findViewById(R.id.uploadprogressbar);
        mUploadBar.setProgress(0);
        mLoadingBar = (ProgressBar) mView.findViewById(R.id.loding);
        mRetryView = mView.findViewById(R.id.layout_cancelandreset);
        mImgCancel = (ImageView) mView.findViewById(R.id.iv_cancel);
        mImgReset = (ImageView) mView.findViewById(R.id.iv_reset);
        mErrorTip = mView.findViewById(R.id.ll_errortip);
        return mView;
    }

    protected boolean doUpload(Bitmap bmp,Object tag){
        if(bmp != null){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG,100,out);
            String path = PublicMethod.getServerUploadPath(Constants.CONTENT_IMG)+System.currentTimeMillis()+".png";
            mService.doUpload(out.toByteArray(),path, tag);
            return true;
        }else {
            PublicMethod.log_d("上传图片为null，请检查上传图片");
            return false;
        }
    }

    protected boolean doUpload(File file,String serverPath,Object tag){
        if(file != null && file.exists()){
            mService.doUpload(file,serverPath, tag);
            return true;
        }else {
            PublicMethod.log_d("上传文件不存在,请检查上传图片");
            return false;
        }
    }


    @Override
    public void onUploadFinishCallback(boolean isSuccess, String path, String hash, Object tag) {

    }

    @Override
    public void onProcess(long current, long total, Object tag) {
        float percent = (float) ((current*10000.0f/total)/100.0f)*(mUploadCount*1.0f/10);
        onChangeProgressBar((int) (mUploadBar.getMax()/mUploadCount*mUploadSuccessCount+percent));
    }

    protected void onChangeProgressBar(int per) {
        mUploadBar.setProgress(per);
    }

    public void setOnUploadListener(UploadViewListener listener){
        this.mListener = listener;
    }

    public void showToast(String msg){
        ToastUtiles.showDefault(getContext(),msg);
    }

    protected void onShowRetryUploadView(){
        mUploadBar.setVisibility(GONE);
        mLoadingBar.setVisibility(View.GONE);

        mErrorTip.setVisibility(VISIBLE);
        mRetryView.setVisibility(View.VISIBLE);
        mImgCancel.setOnClickListener(this);
        mImgReset.setOnClickListener(this);
    }

    protected void onShowUploadingView(){
        mRetryView.setVisibility(View.GONE);
        mErrorTip.setVisibility(GONE);
        mUploadBar.setVisibility(VISIBLE);
        mLoadingBar.setVisibility(View.VISIBLE);
    }

    public abstract void startUpload();

    public abstract void uploadFail();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancel:
               onCancelUpload();
                break;

            case R.id.iv_reset:
                onRetryUpload();
                break;
        }
    }

    protected void onCancelUpload() {
        mCancelUpload = true;
        mService.setUploadListener(null);
        if(mListener != null){
            mListener.onCancelUpload(this);
        }
    }

    protected abstract void onRetryUpload();

    public interface UploadViewListener{
        public void onUploadFinish(UploadView view,boolean isSuccess);
        public void onCancelUpload(UploadView view);
    }

    public abstract void release();

}
