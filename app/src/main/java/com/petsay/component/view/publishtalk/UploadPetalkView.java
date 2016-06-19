package com.petsay.component.view.publishtalk;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.petsay.R;
import com.petsay.application.PublishTalkManager;
import com.petsay.component.view.UploadView;
import com.petsay.constants.Constants;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PublishTalkParam;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * @author wangw
 * 显示上传进度的控件
 */
public class UploadPetalkView extends UploadView {

    private static final String TAG_BG = "photo";
    private static final String TAG_THUM="thumb";
    private static final String TAG_AUDIO = "audio";

//    private View mView;
//    private ImageView mIvThumbnail;
//    private ProgressBar mUploadBar;
    /**上传状态：-1：未上传  0：上传中  1：上传成功 2：上传失败*/
    private int mBg_state = -1;
    /**上传状态：-1：未上传  0：上传中  1：上传成功 2：上传失败*/
    private int mAudio_state = -1;
    /**上传状态：-1：未上传  0：上传中  1：上传成功 2：上传失败*/
    private int mThumb_state = -1;
    private File mAudioFile;
    private Bitmap mPhoto;
    private File mPhotoFile;
    private Bitmap mThumb;
    private PublishTalkParam mParam;

    private String mBgPath;
    private String mVoicePath;
    private String mThumbPath;
//    private UploadTools mService;

//    private ProgressBar mLoadingBar;
//    private View mRetryView;
//    private ImageView mImgCancel;
//    private ImageView mImgReset;
//    private View mErrorTip;

    /**上传状态：-1：未上传 0：上传中：1上传成功： 2：上传失败*/
    private int mUploadStatus =-1;


    public UploadPetalkView(Context context){
        super(context);
    }

    @Override
    protected View onCreateView() {
        mBg_state = mAudio_state = mThumb_state = -1;
        return super.onCreateView();
    }


    public void setPhoto(Bitmap bmp){
        mPhoto = bmp;
    }

    public void setPhotoFile(File file){
        mPhotoFile = file;
    }

    public void setPetThumb(Bitmap bmp){
        if(bmp != null){
            mIvThumbnail.setImageBitmap(bmp);
        }
        this.mThumb = bmp;
    }

    public void setAudioFile(File file){
        this.mAudioFile = file;
    }

    public String getBitmapPath(){
        return mBgPath;
    }

    public String getVoicePath(){
        return mVoicePath;
    }

    public String getThumPath(){
        return mThumbPath;
    }

    public void setPublishParam(PublishTalkParam param){
        this.mParam = param;
    }

    public PublishTalkParam getPublishParam(){
        return mParam;
    }

    @Override
    public void startUpload(){
        if(mUploadStatus != 0 && mThumb != null && (mPhoto != null || mPhotoFile != null)){
            mUploadStatus = 0;
            uploadPhoto();
            uploadAudio();
            uploadThumb();
        }else {
            PublicMethod.showToast(getContext(), "上传异常");
        }
    }

    private void uploadThumb() {
        if(uploadBmp(mThumb, TAG_THUM)){
            mThumb_state = 0;
        }else {
            mThumb_state = 2;
            onShowRetryUploadView();
        }
    }

    private void uploadAudio() {
        if(mParam != null && mParam.model == 0) {
            String audioPath = PublicMethod.getServerUploadPath(Constants.CONTENT_AUDIO) + mAudioFile.getName();
            if (uploadFile(mAudioFile, audioPath, TAG_AUDIO)) {
                mAudio_state = 0;
            } else {
                mAudio_state = 2;
                onShowRetryUploadView();
            }
        }else {
            mAudio_state = 1;
        }
    }

    private void uploadPhoto(){
        if(mPhoto != null){
            if(uploadBmp(mPhoto,TAG_BG)){
                mBg_state = 0;
            }else {
                mBg_state = 2;
                onShowRetryUploadView();
            }
        }else if(mPhotoFile != null) {
            String path = PublicMethod.getServerUploadPath(Constants.CONTENT_IMG)+mPhotoFile.getName();
            if(uploadFile(mPhotoFile,path, TAG_BG)){
                mBg_state = 0;
            }else {
                mBg_state = 2;
                onShowRetryUploadView();
            }
        }else {
            PublicMethod.showToast(getContext(),"上传图片失败，请重试！");
        }
    }

    @Override
    public void uploadFail(){
        onShowRetryUploadView();
        mUploadStatus = 2;
        mAudio_state = mBg_state =  mThumb_state = 2;
    }

//    private void onUpLoad(Bitmap bg,Bitmap thumb,final File file) {
//        mUploading = true;
//        mBg_state = mThumb_state = 0;
//        mThumb = thumb;
//        uploadBmp(mThumb, TAG_THUM);
//        mPhoto = bg;
//        uploadBmp(mPhoto, TAG_BG);
//        if (mParam.model == 1) {
//            mAudio_state = 1;
//        } else {
//            mAudio_state = 0;
//            mAudioFile = file;
//            uploadFile(file);
//        }
//    }

    private boolean uploadBmp(Bitmap bmp,String tag){
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

    private boolean uploadFile(File file,String serverPath,String tag){
        if(file != null && file.exists()){
            mService.doUpload(file,serverPath, tag);
            return true;
        }else {
            PublicMethod.log_d("上传文件不存在,请检查上传图片");
            return false;
        }
    }

    protected void onChangeProgressBar(int per){
        PublicMethod.log_d("当前上传进度" + per);
        mUploadBar.setProgress(per);
    }

    public boolean upLoadFinish(){
        return mAudio_state > 0 && mBg_state > 0 && mThumb_state > 0;
    }

    public boolean uploadSuccess(){
        return mAudio_state == 1 && mBg_state == 1 && mThumb_state == 1;
    }

    /**
     * 上传状态：-1：未上传 0：上传中：1上传成功： 2：上传失败
     * @return
     */
    public int getUploadStatus(){
       if(uploadSuccess()){
           mUploadStatus = 1;
       }else if(mAudio_state == 2 || mBg_state == 2 || mThumb_state == 2){
           mUploadStatus = 2;
       }
        return mUploadStatus;
    }

    @Override
    public void onUploadFinishCallback(boolean isSuccess,String path, String hash,
                                       Object tag) {
        //是否取消上传
        if(mCancelUpload)
            return;

        if(TAG_AUDIO.equals(tag)){
            onAudioUploadFinish(isSuccess);
            mVoicePath = path;
        }else if(TAG_BG.equals(tag)){
            onBgUploadFinish(isSuccess);
            mBgPath = path;
        }else if(TAG_THUM.equals(tag)){
            onThumbUploadFinish(isSuccess);
            mThumbPath = path;
        }

        if(mListener != null && upLoadFinish()){
            mUploadStatus = uploadSuccess() ? 1 : 2;
            mListener.onUploadFinish(this, uploadSuccess());
        }
    }

    private void onAudioUploadFinish(boolean isSuccess){
        mAudio_state = isSuccess ? 1 : 2;
        if(!isSuccess){
            onShowRetryUploadView();
        }
//        else if(mAudioFile != null && isSuccess && mAudioFile.exists()){
//            FileUtile.deleteDir(new File(mAudioFile.getParent()));
//        }
    }

    private void onBgUploadFinish(boolean isSuccess){
        mBg_state = isSuccess ? 1 : 2;
        if(!isSuccess){
            onShowRetryUploadView();
        }
    }

    private void onThumbUploadFinish(boolean isSuccess){
        mThumb_state = isSuccess ? 1 : 2;
        if(!isSuccess){
            onShowRetryUploadView();
        }
    }

    @Override
    public void onProcess(long current, long total, Object tag) {
        float percent = (float) ((current*10000/total)/ 100/3);
        onChangeProgressBar(mUploadBar.getProgress()/2+(int)percent);
    }

    public void release(){
        if(mThumb != null){
            mIvThumbnail.setImageBitmap(null);
            mThumb.recycle();
            mThumb = null;
        }

        if(mPhoto != null){
            mPhoto.recycle();
            mPhoto = null;
        }
        if(mParam != null){
            mParam.release();
            mParam = null;
        }
    }

    public void onRetryUpload() {
        if(getUploadStatus() != 2){
            return;
        }
        mUploadStatus = 0;
        if(PublicMethod.getAPNType(getContext()) < 0){
            PublicMethod.showToast(getContext(), R.string.network_disabled);
            return;
        }

        if(TextUtils.isEmpty(Constants.UPLOAD_TOKEN)){
            onShowUploadingView();
            PublishTalkManager.getInstance().retryUploadByTokenInvaild(this);
            return;
        }

        int temp = 0;
        if(mThumb_state == 1)
            temp ++;
        if(mBg_state == 1)
            temp ++;
        if(mAudio_state == 1)
            temp ++;


        onChangeProgressBar(100 / 3 * temp);
        onShowUploadingView();

        if(mThumb_state != 1){
            uploadThumb();
        }

        if(mBg_state != 1){
            uploadPhoto();
        }

        if(mAudio_state != 1){
            uploadAudio();
        }
    }

    @Override
    protected void onShowRetryUploadView() {
        mUploadStatus = -1;
        super.onShowRetryUploadView();
    }

    @Override
    protected void onShowUploadingView() {
        mUploadStatus = 0;
        super.onShowUploadingView();
    }
}
