package com.petsay.application;

import com.petsay.component.view.UploadView;

/**
 * 发布故事管理器
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/8/2
 * @Description
 */
public class PublishStoryManager extends PublishManager {

    private static PublishStoryManager instance;

    public static PublishStoryManager getInstance(){
        if(instance == null){
            instance = new PublishStoryManager();
        }
        return instance;
    }

    public PublishStoryManager() {
        super();
    }

    @Override
    public void startUpload(UploadView uploadView) {
        super.startUpload(uploadView);
    }
}
