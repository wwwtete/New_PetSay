package com.petsay.activity.story;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.petsay.R;
import com.petsay.application.PublishStoryManager;
import com.petsay.component.view.UploadView;
import com.petsay.constants.Constants;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PetsayLog;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.story.StoryImageItem;
import com.petsay.vo.story.StoryItemVo;
import com.petsay.vo.story.StoryParams;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/8/2
 * @Description
 */
public class UploadStoryView extends UploadView {


    private Bitmap mCover;
    private StoryParams mParams;
    private Map<String,byte[]> mUploadData;
    private int mCoverStatus;
    private int maxw,maxh;
    private int mMaxSize = 512;

    public UploadStoryView(Context context,StoryParams params,Bitmap cover) {
        super(context);
        this.mParams = params;
        this.mCover = cover;
         maxw= PublicMethod.getDisplayWidth(getContext());
        maxh= PublicMethod.getDisplayHeight(getContext());
        mIvThumbnail.setImageBitmap(mCover);
    }

    @Override
    public void startUpload() {
        if(doUpload(mCover,-1)){
            mCoverStatus = 1;
        }else {
            showToast("故事封面不能为空");
            return;
        }
        mUploadCount ++;
        mUploadData = new HashMap<String, byte[]>();
        for(int i=0;i<mParams.items.size();i++){
            StoryItemVo itemVo = mParams.items.get(i);
            if(itemVo.getType() == StoryItemVo.TYPE_IMAGE){
                onUploadStoryImageItem((StoryImageItem) itemVo);
            }
        }
    }

    private void onUploadStoryImageItem(StoryImageItem itemVo) {
        if(!TextUtils.isEmpty(itemVo.getImageUrl()) && itemVo.getImageStatus() < 1){
            if(itemVo.getImageStatus() == 0)
                mUploadCount++;
           uploadBmp(itemVo);
        }
        if(!TextUtils.isEmpty(itemVo.getAudioUrl()) && itemVo.getAudioStatus() < 1){
            File audioFile = new File(itemVo.getAudioUrl());
            String audioPath = PublicMethod.getServerUploadPath(Constants.CONTENT_AUDIO) + audioFile.getName();
            itemVo.setAudioStatus(doUpload(audioFile,audioPath,itemVo) ? 1 : -1);
            if(itemVo.getAudioStatus() == 0)
                mUploadCount++;
        }else if(itemVo.getAudioStatus() == 0) {
            itemVo.setAudioStatus(2);
        }
    }

    private boolean uploadBmp(StoryImageItem item){
        String path = item.getImageUrl();
        byte[] data = mUploadData.get(path);
        if(data == null){
            Bitmap bmp;
            try {
                bmp = FileUtile.loadImageBySdCard(Bitmap.Config.RGB_565, 1, maxw, maxh, path);
                item.setScaleWH(bmp.getWidth()*100.0f / bmp.getHeight()/100.0f);
            }catch (Exception e){
                item.setImageStatus(-1);
                PublicMethod.showToast(getContext(),"加载图片失败，请重试");
                return false;
            }
            data = FileUtile.compressBitmapOutputByte(bmp, mMaxSize);
            mUploadData.put(path,data);
            if (bmp != null) {
                bmp.recycle();
            }
            bmp = null;
        }
        String server_path =PublicMethod.getServerUploadPath(Constants.CONTENT_IMG) + new File(item.getImageUrl()).getName();
        mService.doUpload(data, server_path, item);
        item.setImageStatus(1);
        return true;
    }

    @Override
    public void onUploadFinishCallback(boolean isSuccess, String path, String hash, Object tag) {
        if(mCancelUpload)
            return;
        PetsayLog.e("[onUploadFinishCallback] isSuccess => %s | path => %s",isSuccess,path);
        StoryImageItem item;
        if(tag instanceof StoryImageItem){
            item = (StoryImageItem) tag;
            String serverPath = Constants.DOWNLOAD_SERVER + path;
            if(path.contains(Constants.CONTENT_AUDIO)){
                item.setAudioStatus( isSuccess ? 2 : -1);
                item.setAudioUrl(serverPath);
            }else {
                item.setImageUrl(serverPath);
                item.setImageStatus( isSuccess ? 2 : -1);
            }
        }else {
            if(isSuccess)
                mParams.photoUrl = mParams.thumbUrl = Constants.DOWNLOAD_SERVER + path;
            mCoverStatus = isSuccess ? 2 : -1;
        }
        if(isSuccess){
            mUploadSuccessCount ++;
            onUploadFinish();
        }else {
            onShowRetryUploadView();
        }
    }

    private void onUploadFinish() {
        if(!mCancelUpload && checkAllUploadSuccess() && mListener != null){
            mListener.onUploadFinish(this,checkAllUploadSuccess());
        }
    }

    @Override
    public void uploadFail() {
        onShowRetryUploadView();
    }

    @Override
    protected void onRetryUpload() {

        if(PublicMethod.getAPNType(getContext()) < 0){
            PublicMethod.showToast(getContext(), R.string.network_disabled);
            return;
        }

        if(checkAllUploadSuccess()){
            onUpLoadFinish();
            return;
        }

        if(TextUtils.isEmpty(Constants.UPLOAD_TOKEN)){
            onShowUploadingView();
            PublishStoryManager.getInstance().retryUploadByTokenInvaild(this);
            return;
        }

        onShowUploadingView();
        int size = mParams.items.size();
        for (int i=0;i<size;i++){
            StoryItemVo itemVo = mParams.items.get(i);
            if(itemVo.getType() == StoryItemVo.TYPE_IMAGE){
                int imgStatus = ((StoryImageItem) itemVo).getImageStatus();
                int audioStatus = ((StoryImageItem) itemVo).getAudioStatus();
                if(imgStatus < 1 || audioStatus < 1)
                    onUploadStoryImageItem((StoryImageItem) itemVo);
            }
        }

        onChangeProgressBar(100/mUploadCount*mUploadSuccessCount);
    }

    public StoryParams getPublishParams() {
        return mParams;
    }

    @Override
    public void release() {
        mUploadData = null;
        if(mCover != null) {
            mCover.recycle();
            mCover = null;
        }
    }

    private void onUpLoadFinish(){
        if(!mCancelUpload && mListener != null && checkAllUploadSuccess()){
            mListener.onUploadFinish(this, checkAllUploadSuccess());
        }
    }

    public boolean checkAllUploadSuccess(){
        int size = mParams.items.size();
        for (int i=0;i<size;i++){
            StoryItemVo itemVo = mParams.items.get(i);
            if(itemVo.getType() == StoryItemVo.TYPE_IMAGE){
                int imgStatus = ((StoryImageItem) itemVo).getImageStatus();
                int audioStatus = ((StoryImageItem) itemVo).getAudioStatus();
                if(imgStatus < 2 || audioStatus < 2){
                    return false;
                }
            }
        }
        return mCoverStatus == 2;
//        return mUploadCount == mUploadSuccessCount;
    }

}
