package com.petsay.application;

import android.database.Observable;
import android.text.TextUtils;

import com.alipay.mobilesecuritysdk.model.Upload;
import com.google.inject.Singleton;
import com.petsay.component.view.UploadView;
import com.petsay.component.view.publishtalk.UploadPetalkView;
import com.petsay.constants.Constants;
import com.petsay.database.DBManager;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.UploadTokenNet;
import com.petsay.utile.FileUtile;
import com.petsay.vo.petalk.DraftboxVo;
import com.petsay.vo.petalk.PublishTalkParam;
import com.petsay.vo.ResponseBean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 发布说说管理器
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/3/3
 * @Description
 */
@Singleton
public class PublishTalkManager extends  PublishManager {

    private static PublishTalkManager instance;

    public static PublishTalkManager getInstance(){
        if(instance == null){
            instance = new PublishTalkManager();
        }
        return instance;
    }

    private List<UploadPetalkView> mUploadingList;



    public PublishTalkManager(){
        super();
        mUploadingList = new ArrayList<UploadPetalkView>();
    }

    @Override
    public void startUpload(UploadView uploadView){
        super.startUpload(uploadView);
        mUploadingList.add((UploadPetalkView) uploadView);
    }


    /**
     * 检查草稿箱的Item是否正在上传
     * @param vo
     * @return
     */
    public UploadPetalkView checkUploading(DraftboxVo vo){
        if(mUploadingList.isEmpty() || vo == null)
            return null;
        for(int i = 0;i<mUploadingList.size();i++){
            if(vo.getId() == mUploadingList.get(i).getPublishParam().id){
                return mUploadingList.get(i);
            }
        }
        return null;
    }

    /**
     * 从上传队列中删除，但不删除本地草稿箱数据
     * @param uploadView
     * @return
     */
    public boolean removeUploadView(UploadPetalkView uploadView){
        if(!mUploadingList.isEmpty()){
            return mUploadingList.remove(uploadView);
        }
        return false;
    }

    /**
     * 删除本地草稿箱中的数据
     */
    public void deleteLocalData(PublishTalkParam param){
        if(param == null)
            return;
        DBManager dbManager = DBManager.getInstance();
        if(dbManager.isOpen()){
            if(param.model == 0 && !param.decorations.isEmpty()) {
                long pid = param.decorations.get(0).id;
                if (pid > 0) {
                    dbManager.getDaoSession().getDecorationPositionDao()
                            .deleteByKey(pid);
                }
            }
            if(param.id > 0) {
                DraftboxVo vo = dbManager.getDaoSession().getDraftboxVoDao().load(param.id);
                if(vo.getModel() == 0 ) {
                    FileUtile.deleteFile(vo.getAudioPath());
                }
                FileUtile.deleteFile(vo.getPhotoPath());
                FileUtile.deleteFile(vo.getThumbPath());
                dbManager.getDaoSession().getDraftboxVoDao()
                        .deleteByKey(vo.getId());
            }
        }
        param.recycle();
        param.release();
        if(!mObservers.isEmpty()){
            for (int i = 0;i<mObservers.size();i++){
                mObservers.get(i).onDeleteLocalData(param);
            }
        }
        return;
    }

}
