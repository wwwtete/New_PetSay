package com.petsay.component.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.petsay.R;
import com.petsay.application.PublishTopicReplyManager;
import com.petsay.application.UploadTokenManager;
import com.petsay.constants.Constants;
import com.petsay.network.upload.UploadTools;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.forum.CreateTopicParams;
import com.petsay.vo.forum.PicDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 回复主题图片上传View
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/4/7
 * @Description
 */
public class UploadTopicReplyView extends RelativeLayout implements UploadTools.UploadServiceListener, View.OnClickListener {

    private UploadTools mService;
    private UploadTopicReplyCallback mCallback;
    private PublishTopicReplyListener mListener;
    private ProgressBar mLoadingBar;
    private View mRetryView;
    private ImageView mImgCancel;
    private ImageView mImgReset;
    private View mErrorTip;
    private View mView;
    private ImageView mIvThumbnail;
    private ProgressBar mUploadBar;

    private CreateTopicParams mParam;
    private boolean mCancelUpload = false;
    private int mUploadCount = 0;
    private int mMaxSize = 512;
    private int mUploadSuccessCount = 0;
    /**
     * 上传状态Map：0：为上传,-1上传失败，1:上传中,2:上传成功
     */
    private Map<String,Integer> mUploadStateMap;
    private Map<String,byte[]> mUploadData;

    public UploadTopicReplyView(Context context) {
        super(context);
        initView();
    }

    public UploadTopicReplyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mService = new UploadTools();
        mService.setUploadListener(this);
        View mView = inflate(getContext(), R.layout.upload_view, this);
        mIvThumbnail = (ImageView) mView.findViewById(R.id.iv_thumbnail);
        mUploadBar = (ProgressBar) mView.findViewById(R.id.uploadprogressbar);
        mUploadBar.setProgress(0);
        mLoadingBar = (ProgressBar) mView.findViewById(R.id.loding);
        mRetryView = mView.findViewById(R.id.layout_cancelandreset);
        mImgCancel = (ImageView) mView.findViewById(R.id.iv_cancel);
        mImgReset = (ImageView) mView.findViewById(R.id.iv_reset);
        mErrorTip = mView.findViewById(R.id.ll_errortip);
    }

    /**
     * 开始上传
     * @param params
     */
    public void startUpload(CreateTopicParams params){
        mUploadCount = params.selectPicMap.size();
        mCancelUpload = false;
        mParam = params;
        mUploadStateMap = new HashMap<String, Integer>(mUploadCount);
        mUploadData = new HashMap<String, byte[]>(mUploadCount);
        Set<Map.Entry<String,PicDTO>> sets = mParam.selectPicMap.entrySet();
        Bitmap thum = null;
        for (Map.Entry<String,PicDTO> entry:sets){
            String key = entry.getKey();
            mUploadStateMap.put(key, 0);
//            uploadBmpCore(entry.getValue(), key);
//            uploadBmpCore(entry.getKey(),key);
            uploadBmp(entry.getKey());
//            if(thum == null) {
//                thum = entry.getValue();
//            }
        }
        //设置缩略图
        if(thum == null){
            Iterator<Map.Entry<String,PicDTO>> iterator =sets.iterator();
            while (thum ==  null && iterator.hasNext()){
                byte[] data = mUploadData.get(iterator.next().getKey());
                if(data != null)
                    thum = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        }
        mIvThumbnail.setImageBitmap(thum);

    }

    private boolean uploadBmp(String path){
        byte[] data = mUploadData.get(path);
        if(data == null){
            Bitmap bmp;
                int maxw = PublicMethod.getDisplayWidth(getContext());
                int maxh = PublicMethod.getDisplayHeight(getContext());
                try {
                    bmp = FileUtile.loadImageBySdCard(Bitmap.Config.RGB_565,1,maxw,maxh,path);
                    PicDTO dto = new PicDTO();
                    String server_path =PublicMethod.getServerUploadPath(Constants.CONTENT_IMG) + UUID.randomUUID().toString()+".jpg";
                    dto.setPic(server_path);
                    dto.setScaleWH(bmp.getWidth()*100.0f / bmp.getHeight()/100.0f);
                    mParam.selectPicMap.put(path,dto);
                }catch (Exception e){
                    mUploadStateMap.put(path,-1);
                    PublicMethod.showToast(getContext(),"加载图片失败，请重试");
                    return false;
                }
            data = FileUtile.compressBitmapOutputByte(bmp, mMaxSize);
            mUploadData.put(path,data);
            if (bmp != null)
                bmp.recycle();
            bmp = null;
        }
        String server_path = mParam.selectPicMap.get(path).getPic();
        mUploadStateMap.put(path,1);
        mService.doUpload(data, server_path, path);
        return true;
    }

    @Deprecated
    private boolean uploadBmpCore(Bitmap bmp, String tag){
        if(bmp != null){
            mUploadStateMap.put(tag,1);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int quality = 100;
            int size = FileUtile.getBitmapSize(bmp);
            if(size > 1024*1024 *2){
                quality = 30;
            }else if(size > 1024*1024){
                quality = 50;
            }
            long start = System.currentTimeMillis();
            bmp.compress(Bitmap.CompressFormat.JPEG,quality,out);
            long end = System.currentTimeMillis();
            PublicMethod.log_d("压缩图片耗时："+((end-start)/1000));
            start = System.currentTimeMillis();
            String path = PublicMethod.getServerUploadPath(Constants.CONTENT_IMG)+ UUID.randomUUID().toString()+".png";
            end = System.currentTimeMillis();
            PublicMethod.log_d("path耗时：" + ((end - start) / 1000));
            mService.doUpload(out.toByteArray(),path, tag);
            try {
//                bmp.recycle();
//                bmp = null;
                if(out != null)
                    out.close();
                out = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }else {
            mUploadStateMap.put(tag,-1);
            PublicMethod.log_d("上传图片为null，请检查上传图片");
            return false;
        }
    }

    public void cancelUpload(){

    }

    @Override
    public void onUploadFinishCallback(boolean isSuccess, String path, String hash, Object tag) {
        String key = tag.toString();
        if(isSuccess) {
            mUploadStateMap.put(key, 2);
//            Bitmap bmp = mParam.selectPicMap.get(key);
//            PicDTO dto = new PicDTO();
//            dto.setPic(Constants.DOWNLOAD_SERVER+path);
//            dto.setScaleWH(bmp.getWidth()*100.0f / bmp.getHeight()/100.0f);
            PicDTO dto = mParam.selectPicMap.get(key);
            if(dto != null) {
                dto.setPic(Constants.DOWNLOAD_SERVER + path);
                mParam.pictures.add(dto);
            }
//            if(bmp != null){
//                bmp.recycle();
//            }
            mParam.selectPicMap.put(key, null);
            mUploadSuccessCount ++;
        }else {
            mUploadStateMap.put(key,-1);
            showRetryUploadView();
        }

        onUpLoadFinish();
    }

    private void onUpLoadFinish(){
        if(!mCancelUpload && mCallback != null && checkAllUploadFinish()){
            mCallback.onUpLoadFinish(this,checkAllUploadSuccess());
        }
    }

    /**
     * 检查是否上传完成
     * @return
     */
    public boolean checkAllUploadFinish(){
        if(mUploadStateMap == null)
            return false;
       Set<Map.Entry<String,Integer>> sets = mUploadStateMap.entrySet();
        for (Map.Entry<String,Integer> entry:sets){
            if(entry.getValue() == 1){
                return false;
            }
        }
        return  true;
    }

    /**
     * 检查是否全部上传成功
     * @return
     */
    public boolean checkAllUploadSuccess(){
        if(mUploadStateMap == null)
            return false;
        Set<Map.Entry<String,Integer>> sets = mUploadStateMap.entrySet();
        for (Map.Entry<String,Integer> entry:sets){
            if(entry.getValue() != 2){
                return false;
            }
        }
        return  true;
    }

    @Override
    public void onProcess(long current, long total, Object tag) {
        float percent = (float) ((current*10000.0f/total)/100.0f)*(mUploadCount*1.0f/10);
        onChangeProgressBar((int) (mUploadBar.getMax()/mUploadCount*mUploadSuccessCount+percent));
    }


    public void showRetryUploadView(){
        mUploadBar.setVisibility(GONE);
        mLoadingBar.setVisibility(View.GONE);

        mErrorTip.setVisibility(VISIBLE);
        mRetryView.setVisibility(View.VISIBLE);
        mImgCancel.setOnClickListener(this);
        mImgReset.setOnClickListener(this);
    }

    public void showUploadingView(){
        mRetryView.setVisibility(View.GONE);
        mErrorTip.setVisibility(GONE);
        mUploadBar.setVisibility(VISIBLE);
        mLoadingBar.setVisibility(View.VISIBLE);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_cancel:
                onCancelUpload();
                break;
            case R.id.iv_reset:
                onRetryUpload();
                break;
        }
    }

    private void onRetryUpload() {
       if(checkAllUploadSuccess()) {
            onUpLoadFinish();
           return;
       }
        if(PublicMethod.getAPNType(getContext()) < 0){
            PublicMethod.showToast(getContext(), R.string.network_disabled);
            return;
        }

        if(!UploadTokenManager.getInstance().checkvalid()){
            showUploadingView();
            PublishTopicReplyManager.getInstance().retryUploadByTokenInvaild(this);
            return;
        }

        showUploadingView();
        int temp = 0;
        Set<Map.Entry<String,Integer>> sets = mUploadStateMap.entrySet();
        for (Map.Entry<String,Integer> entry:sets){
            if(entry.getValue() == 0 || entry.getValue() == -1){
//                uploadBmpCore(mParam.selectPicMap.get(entry.getKey()), entry.getKey());
                uploadBmp(entry.getKey());
            }else {
                temp ++;
            }
        }
        onChangeProgressBar(100 / mUploadCount * temp);

    }

    protected void onChangeProgressBar(int per){
        PublicMethod.log_d("onChangeProgressBar:"+per);
        mUploadBar.setProgress(per);
    }

    private void onCancelUpload() {
        mCancelUpload = true;
        if(mCallback != null) {
            mCallback.onUploadCancel(this);
        }
    }


    public void setCallback(UploadTopicReplyCallback callback){
        this.mCallback = callback;
    }

    public void setPublishListener(PublishTopicReplyListener listener){
        this.mListener = listener;
    }

    public void publishTopicReplyFinish(boolean isSuccess){
        if(mListener != null){
            mListener.onPublishTopicReplyFinish(isSuccess,this);
        }
    }

    public CreateTopicParams getTopicParam(){
        return mParam;
    }

    public String getKey(){
        if(mParam != null)
            return mParam.topicId;
        return "";
    }

    public interface UploadTopicReplyCallback{
        public void onUpLoadFinish(UploadTopicReplyView view,Boolean isSuccess);
        public void onUploadCancel(UploadTopicReplyView view);
    }

    public interface PublishTopicReplyListener {
        public void onPublishTopicReplyFinish(boolean isSuccess,UploadTopicReplyView view);
    }

}
