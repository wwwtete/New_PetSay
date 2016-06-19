package com.petsay.activity.petalk.publishtalk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.petsay.component.customview.module.BasicSurfaceViewModule;
import com.petsay.component.customview.module.EditSurfaceViewModule;
import com.petsay.component.customview.module.MultiframeModule;
import com.petsay.component.view.publishtalk.DecorationItemView;
import com.petsay.component.view.publishtalk.EditRecordView;
import com.petsay.constants.Constants;
import com.petsay.utile.FileUtile;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.task.UnZipTask;
import com.petsay.vo.petalk.PublishTalkParam;
import com.petsay.vo.decoration.DecorationBean;
import com.petsay.vo.decoration.DecorationTitleBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/30
 * @Description
 */
public class MultiFunEditActivity extends BaseEditActivity {


    protected MultiframeModule mMouthModule;
    protected DecorationBean mMouthBean;
    private EditRecordView mRecordView;

    @Override
    protected void initGroupTitleView() {
        if(mRootData != null) {
            List<DecorationTitleBean> beans = mRootData.getChildren();
            mDtGroupview.setGroupData(beans);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mRecordView != null)
            mRecordView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mRecordView != null)
            mRecordView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mRecordView != null)
            mRecordView.onDestroy();
    }

    @Override
    public void onClickDecoratoiinGroupTitle(DecorationTitleBean bean) {
        if(bean.getType().equals(TYPE_MOUTH)){
            if(mMouthBean == null) {
                List<DecorationBean> beans = getUsullyQueue(bean.getType());
                if(beans != null && beans.size() > 0)
                    mDtUsuallyview.setUsuallyDecorationData(beans, beans.get(0).getId(),bean.getType());
            }else {
                mDtUsuallyview.setUsuallyDecorationData(getUsullyQueue(bean.getType()),mMouthBean.getId(),bean.getType());
            }
        }else {
            super.onClickDecoratoiinGroupTitle(bean);
        }
    }

    @Override
    protected boolean onAddDecoration(DecorationBean bean) {
        stopGif();
        if(TYPE_MOUTH.equals(bean.getType())) {
           return onAddMouth(bean);
        }else {
            return super.onAddDecoration(bean);
        }
    }

    private boolean onAddMouth(DecorationBean bean) {
        stopGif();
        mMouthBean = bean;
        if(mMouthModule == null){
            initMouthView(bean);
            return true;
        }
        mMouthModule.setTag(bean);
        initMouthFrame();
        mEditView.setModuleFocus(mMouthModule);
        return true;
    }

    /**
     * 初始嘴型视图
     */
    private void initMouthView(DecorationBean bean) {
        File file = initMouthFrame();
        if(file == null)
            return;
        oninitMouthView(file,bean);
    }

    private void oninitMouthView(File file,DecorationBean bean){
        File[] files = file.listFiles();
        if(files == null || files.length == 0){
            showToast("该嘴下载异常，请重新选择");
            return;
        }
        String bmpPath = "";
        for(File f:files){
            if(f.exists() && f.isFile()){
               bmpPath = f.getAbsolutePath();
                break;
            }
        }
        if(TextUtils.isEmpty(bmpPath)){
            showToast("该嘴型下载异常，请重新选择");
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(bmpPath);
        if(bitmap == null){
            showToast("该嘴型存在异常，请重新选择");
            return;
        }
        mMouthModule = new MultiframeModule(bitmap, mControlBmp);
        Matrix mx = mMouthModule.getMatrix();
        int dw = PublicMethod.getDisplayWidth(this);
        mMouthModule.setMaxHeight(dw);
        mMouthModule.setMaxWidth(dw);
        int dx = dw/4;
        int dy = dw/4;
        float sx =bitmap.getWidth()*100.0f/(dw/2)/100.0f;//tw*100.0f/bitmap.getWidth()/100.0f;//
        float sy =bitmap.getHeight()*100.0f/(dw/2)/100.0f;//tw*100.0f/bitmap.getHeight()/100.0f; //
        if(sx > 1 || sy > 1){
            sx = (dw/2*100.0f)/bitmap.getWidth()/100.0f;//bitmap.getWidth()*100.0f/tw/100.0f;//
            sy = (dw/2*100.0f)/bitmap.getHeight()/100.0f;//bitmap.getHeight()*100.0f/tw/100.0f;//
        }
        mx.postScale(sx, sy);
        mx.postTranslate(dx, dy);
        addSurfaceModule(mMouthModule);
        //设置饰品缩放最大值及最小值
        EditSurfaceViewModule.MAX_BMP = dw - 100;
        EditSurfaceViewModule.MIN_BMP = PublicMethod.getDiptopx(MultiFunEditActivity.this, 50);
        mMouthModule.setTag(bean);
        initMouthFrame();
        mEditView.setModuleFocus(mMouthModule);
        closeLoading();
    }

    /**
     * 初始化动画的帧
     */
    private File initMouthFrame(){
        if(mMouthBean == null)
            return null;
        String outputPath = FileUtile.getUnzipFilePath(this, mMouthBean.getFileName());//FileUtile.getPath(this, Constants.SDCARD_DECORATE_UNZIP+mMouthBean.getFileName()+File.separator);
        String fileName = FileUtile.getFileNameByUrl(mMouthBean.getUrl());
        File file = new File(outputPath);
        if(file.exists() && file.list().length == mMouthBean.getFileCount()){
            if(mMouthModule == null){
                return file;
            }else {
                addFrame(file);
                return file;
            }
        }else {
            InputStream in = null;
            File targetDirs = new File(outputPath);
            try{
                if(mMouthBean.isAssetsed()){
                    in = getResources().getAssets().open(Constants.ZIP+fileName);
                }else if(mMouthBean.isDownloaded()) {
                    String zipDirs = FileUtile.getPath(MultiFunEditActivity.this, Constants.SDCARD_DECORATE_ZIP);
                    File zipFile = new File(zipDirs,fileName);
                    if(zipFile.exists())
                        in = new FileInputStream(zipFile);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            if(in != null)
                unzip(in,targetDirs);
            return null;
        }
    }

    /**
     * 添加帧动画
     * @param fileDirs
     */
    private void addFrame(File fileDirs){
        if(mMouthModule == null){
            initMouthView(mMouthBean);
            return;
        }
        mMouthModule.clearFrame();
        File[] files = fileDirs.listFiles();
        boolean flag = false;
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
            if(bmp == null || bmp.isRecycled())
                continue;
            if(!flag){
                mMouthModule.changeView(bmp);
                mEditView.refreshView();
                flag = true;
            }
            mMouthModule.addFrame(bmp);
        }
    }

    /**
     * 解压表情包
     */
    private void unzip(InputStream in,File targetDirs){
        final UnZipTask thread = new UnZipTask(in, targetDirs);
        thread.setUnzipListener(new UnZipTask.UnzipListener() {

            @Override
            public void onUnzipFinishListener(File outDirs,boolean success) {
                if(success){
                    if(mMouthModule == null) {
                        oninitMouthView(outDirs,mMouthBean);
//                        initMouthView(mMouthBean);
                    }else{
                        addFrame(outDirs);
                    }
                    thread.removeUnzipListener();
                }else {
                    showToast("解压资源包失败");
                }
            }
        });
        thread.execute();
    }

    @Override
    protected void onDownloadDecorationCallback(boolean isSuccess, DecorationItemView view) {
        super.onDownloadDecorationCallback(isSuccess, view);
        if(isSuccess && mMouthModule == null && TYPE_MOUTH.equals(view.getData().getType())){
            onAddMouth(view.getData());
        }
    }

    private void playGif(){
        if(mMouthModule == null)
            return;
        if(mMouthModule.getFrameCount() > 0){
            mEditView.playGif();
        }else {
            initMouthFrame();
            mEditView.playGif();
        }
    }

    protected void stopGif(){
        if(mEditView != null)
            mEditView.stopGif();
    }

    @Override
    protected void showMoreView(String type) {
        stopGif();
        super.showMoreView(type);
    }

    @Override
    public void onTouchModule(BasicSurfaceViewModule module, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(module != null)// && module == mMouthModule)
                    stopGif();
                break;
            case MotionEvent.ACTION_UP:
                if(module == null || !mEditView.getFocusable())
                    onClickPetBackGround();
                break;
        }
    }

    /**
     * 点击图片背景
     */
    public void onClickPetBackGround(){
      if(mEditView.getFocusable()){
            playGif();
        }
    }

    @Override
    protected void onNext() {
        boolean flag = updatePublishVale();
        showLoading();
        if(flag){
            showRecordView();
        }else {
            closeLoading();
            showToast("编辑失败，请联系客服");
        }
    }

    private void showRecordView() {
        stopGif();
        if(mRecordView == null){
            mRecordView = new EditRecordView(this);
            mRecordView.setGravity(View.GONE);
            mLayoutRoot.addView(mRecordView);
        }
        compositeImages(false);
        if(PublishParam.editImg != null) {
            mRecordView.updateView(PublishParam);
            mRecordView.onShow();
        }else {
            showToast("合成图片失败");
        }
        closeLoading();
    }

    public boolean updatePublishVale(){
        if(mMouthModule == null)
            return false;
        if(PublishParam == null){
            PublishParam = new PublishTalkParam();
        }
        try{
            PublishParam.mouth = (DecorationBean)mMouthModule.getTag();
            PublishParam.petId = getActivePetId();
            PublishParam.model = 0;
//            DecorationPosition dp = null;
//            if(PublishParam.decorations != null && PublishParam.decorations.size()>0)
//                dp = PublishParam.decorations.get(0);
//            if(dp == null){
//                dp = new DecorationPosition();
//                PublishParam.decorations.add(dp);
//            }
//            dp.setDecorationId(((DecorationBean) mMouthModule.getTag()).getId());
//            dp.setWidth((double) mMouthModule.getWidthScale());
//            dp.setHeight((double) mMouthModule.getHeightScale());
//            dp.setCenterX((double) mMouthModule.getCenterX());
//            dp.setCenterY((double) mMouthModule.getCenterY());
//            dp.setRotationX((double) 0);//mMouthModule.getRotationX();
//            dp.setRotationY((double) 0);//mMouthModule.getRotationY();
//            dp.setRotationZ(mMouthModule.getRotationZ());

            PublishTalkParam.Position dp = null;
            if(PublishParam.decorations != null && PublishParam.decorations.size()>0)
                dp = PublishParam.decorations.get(0);
            if(dp == null){
                dp = PublishParam.new Position();
                PublishParam.decorations.add(dp);
            }
            dp.decorationId = ((DecorationBean)mMouthModule.getTag()).getId();
            dp.width = mMouthModule.getWidthScale();
            dp.height = mMouthModule.getHeightScale();
            dp.centerX = mMouthModule.getCenterX();
            dp.centerY = mMouthModule.getCenterY();
            dp.rotationX = 0;//mMouthModule.getRotationX();
            dp.rotationY = 0;//mMouthModule.getRotationY();
            dp.rotationZ = mMouthModule.getRotationZ();
        }catch(Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(mRecordView !=null && mRecordView.isShow()){
                mRecordView.showCustomMenu();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
