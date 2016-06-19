package com.petsay.component.gifview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.petsay.constants.Constants;
import com.petsay.network.download.DownloadTask;
import com.petsay.network.download.DownloadTask.DownloadTaskCallback;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.task.UnZipTask;
import com.petsay.utile.task.UnZipTask.UnzipListener;
import com.petsay.vo.decoration.DecorationBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wangw
 *
 */
public class BaseGifView extends ImageView {

    private static final int ONDOWNLOADFINISH = 900;
    //	protected static final List<BitmapDrawable> mBmpList = Collections.synchronizedList(new ArrayList<BitmapDrawable>());
    protected List<BitmapDrawable> mBmpList;
    private String mCurrFileName;
    private String mFileUrl;
    private int mFrameCount;
    private String mFileType;
    protected boolean mBmpReadyed = false;
    /**当前状态：-1：未初始化  0：初始化中  1：播放Gif 2：stop 3：显示第一帧动画*/
    protected int mStep = -1;
    //	protected DownloadService mService;
    /**上次播放gif文件的名称*/
    protected String mPreFileName;
    protected Timer mTimer;
    protected int mFrameIndex;



    public BaseGifView(Context context){
        super(context);
        initViews();
    }

    public BaseGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    protected void initViews() {
        mBmpList = new ArrayList<BitmapDrawable>();
    }

    public void initData(DecorationBean bean){
        initData(bean.getUrl(),bean.getFileCount(),bean.getFileType());
    }

    public void initData(String fileUrl,int frameCount,String fileType){
        this.mFileUrl = fileUrl;
        this.mFrameCount = frameCount;
        this.mFileType = fileType;
        this.mCurrFileName = FileUtile.getFileNameByUrl(mFileUrl);
    }

    public void reset(){
        stopGif();
        mBmpReadyed =false;
        mFrameIndex = 0;
        mStep = 0;
        mStep = -1;
        this.mFileUrl = "";
        this.mFrameCount = -1;
        this.mFileType = "";
        this.mCurrFileName = FileUtile.getFileNameByUrl(mFileUrl);
        this.setBackgroundDrawable(null);
        cleareList();
    }

    public boolean checkEnabled(){
        return !TextUtils.isEmpty(mFileUrl) && !TextUtils.isEmpty(mFileType) && mFrameCount > 0;
    }

    /**
     * 清空列表，释放内存
     */
    public void cleareList(){
        int size = mBmpList.size();
        for (int i = 0; i < size; i++) {
            BitmapDrawable drawable = mBmpList.get(i);
            if(drawable != null && drawable.getBitmap() != null){
                if(drawable != null && drawable.getBitmap() != null){
                    //					drawable.getBitmap().recycle();
                    //					drawable.setCallback(null);
                    //TODO 释放内存采用recycle可能会异常
                    new SoftReference<BitmapDrawable>(drawable);
                }
                drawable = null;
            }
        }
        mBmpList.clear();
    }

    /**
     * 验证文件是否已下载或解压
     */
    private void verifyFile() {
        String outputPath = FileUtile.getUnzipFilePath(getContext(), mCurrFileName);//FileUtile.getPath(getContext(), Constants.SDCARD_DECORATE_UNZIP+mDecorationBean.getFileName()+File.separator);
        File file = new File(outputPath);
        boolean flag = file.exists() && file.list().length == mFrameCount;
        if(flag){
            addFrame(file);
        }else {
            //检查资源包是否下载
            boolean assetsHas = FileUtile.assetsHasFile(getContext(), Constants.DECORATE+mFileType, mCurrFileName);
            String filePath = FileUtile.getPath(getContext(), Constants.SDCARD_DECORATE+mFileType);
            boolean sdCardHas = FileUtile.sdCardHasFile(filePath, mCurrFileName);
            try {
                if(assetsHas){
                    Unzip(getResources().getAssets().open(Constants.ZIP+mCurrFileName));
                }else if(sdCardHas){
                    String zipDirs = filePath+mCurrFileName;
                    Unzip(new FileInputStream(new File(zipDirs)));
                }else {
                    downloadZip(mFileUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载资源包
     * @param url
     */
    protected void downloadZip(String url){
        if(PublicMethod.getAPNType(getContext()) > 0){
            String target = FileUtile.getPath(getContext(), Constants.SDCARD_DECORATE+mFileType);
            //		mService.download(url, target, this);
            DownloadTask task = new DownloadTask(this, target);
            task.setCallback(new DownloadTaskCallback() {
                @Override
                public void onDownloadFinishCallback(DownloadTask task,boolean isSuccess, String url,
                                                     File file, Object what) {
                    if(!isSuccess){
                        PublicMethod.showToast(getContext(), "资源包下载失败,重试中！");
                        downloadZip(mFileUrl);
                    }else {
                        try {
                            Unzip(new FileInputStream(file));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelCallback(DownloadTask task,String url, Object what) {
                }
            });
            task.execute(url);
        }
    }


    /**
     * 解压资源包
     * @param in
     */
    protected void Unzip(InputStream in){
        String outputPath = FileUtile.getUnzipFilePath(getContext(), mCurrFileName);//FileUtile.getPath(getContext(), Constants.SDCARD_DECORATE_UNZIP+mDecorationBean.getFileName()+File.separator);
        UnZipTask thread = new UnZipTask(in, new File(outputPath));
        thread.setUnzipListener(new UnzipListener() {

            @Override
            public void onUnzipFinishListener(File outDirs,boolean success) {
                if(success)
                    addFrame(outDirs);
            }
        });
        thread.execute();

    }

    /**
     * 显示第一帧图片
     */
    public void showFirstFrame(){
            mStep = 3;
        if(checkEnabled()) {
            onPrepare();
        }
    }

    public void playGif(){
        mStep = 1;
        if(checkEnabled()) {
            onPrepare();
        }
    }

    protected void onPrepare(){
        if (mCurrFileName.equals(mPreFileName) &&
                mBmpList.size() == mFrameCount) {
            mBmpReadyed = true;
            onReadyedFinish();
        } else {
            verifyFile();
            mPreFileName = mCurrFileName;
        }
    }

    /**
     * 停止播放Gif
     */
    public void stopGif(){
        mStep = 2;
        stopAnimation();
    }

    @SuppressLint("NewApi")
    protected void addFrame(File fileDirs){
        cleareList();
        File[] files = fileDirs.listFiles();
        if(fileDirs == null || files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            BitmapDrawable drawable = getDrawable(f);
            if(i == 0){
                setBackgroundDrawable(drawable);
            }
            mBmpList.add(drawable);
        }
        mBmpReadyed = true;
        onReadyedFinish();
    }

    private BitmapDrawable getDrawable(File file){
        BitmapDrawable drawable = new BitmapDrawable(BitmapFactory.decodeFile(file.getAbsolutePath()));
        return drawable;
    }

    /**
     * 准备完成
     */
    protected void onReadyedFinish(){
        if(mBmpReadyed && mBmpList.size() > 0){
            setBackgroundDrawable(mBmpList.get(0));
            if(mStep == 1) {
                startAnimation();
            }
        }
    }

    /**
     * 开始播放动画
     */
    protected void startAnimation(){
        if(mTimer != null)
            stopAnimation();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask(){

            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                msg.what = 5000;
                msg.sendToTarget();
            }
        }, 0,200);

    }

    protected void stopAnimation(){
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    protected Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 5000:
                    onChangeBackground();
                    break;
            }

        };
    };

    protected void onChangeBackground(){
        if(mBmpList.size() <= 0)
            return;
        mFrameIndex++;
        mFrameIndex = mFrameIndex >= mBmpList.size() ? 0 : mFrameIndex;
        //		PublicMethod.log_d("当前下标 ===="+mFrameIndex + "   |  总共大小 == "+mBmpList.size());
        BitmapDrawable drawable = mBmpList.get(mFrameIndex);
        if(drawable != null ){
            setBackgroundDrawable(drawable);
        }else {
            PublicMethod.log_d("=============Background为Null===================");
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        if(d != null && ((BitmapDrawable)d).getBitmap() != null && !((BitmapDrawable)d).getBitmap().isRecycled())
            super.setBackgroundDrawable(d);
        else {
            super.setBackgroundDrawable(null);
        }
    }

//    @Override
//    public void onDownloadFinishCallback(boolean isSuccess,String url, File file, View view) {
//        onDownloadZipFinish(isSuccess,file);
//    }
//
//    /**
//     * 处理下载zip包
//     * @param isSuccess
//     * @param file
//     */
//    protected void onDownloadZipFinish(boolean isSuccess,File file){
//        Message msg = mHandler.obtainMessage();
//        msg.arg1 = isSuccess ? 1 : -1;
//        msg.what = ONDOWNLOADFINISH;
//        msg.obj = file;
//        msg.sendToTarget();
//    }

    public void release(){
        cleareList();
        stopGif();
    }



}
