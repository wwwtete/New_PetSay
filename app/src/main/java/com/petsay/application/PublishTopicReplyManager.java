package com.petsay.application;

import android.database.Observable;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.petsay.component.view.UploadTopicReplyView;
import com.petsay.network.base.NetCallbackInterface;
import com.petsay.network.base.PetSayError;
import com.petsay.network.net.TopicNet;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.ResponseBean;
import com.petsay.vo.forum.CreateTopicParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发表主题回复内容的Manager
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/4/7
 * @Description
 */
public class PublishTopicReplyManager extends Observable<PublishTopicReplyManager.PublishTopicReplyCallback> implements UploadTopicReplyView.UploadTopicReplyCallback, NetCallbackInterface {


    private static PublishTopicReplyManager mInstance;

    public static PublishTopicReplyManager getInstance(){
        if(mInstance == null)
            mInstance = new PublishTopicReplyManager();
        return mInstance;
    }

    private Map<String,List<UploadTopicReplyView>> mUploadViewMap;
    private TopicNet mNet;
    private String mKey;

    private PublishTopicReplyManager(){
        mUploadViewMap = new HashMap<String, List<UploadTopicReplyView>>();
        mNet = new TopicNet();
        mNet.setCallback(this);
    }

    public void addUploadView(UploadTopicReplyView uploadView,String key){
        List<UploadTopicReplyView> list;
        if(mUploadViewMap.containsKey(key))
            list = mUploadViewMap.get(key);
        else
            list = new ArrayList<UploadTopicReplyView>();
        list.add(uploadView);
        uploadView.setCallback(this);
        mUploadViewMap.put(key,list);
    }

    public List<UploadTopicReplyView> getUploadViewList(String key){
       return mUploadViewMap.get(key);
    }

    public List<UploadTopicReplyView> removeUploadViewList(String key){
        return mUploadViewMap.remove(key);
    }

    public boolean removeUploadView(UploadTopicReplyView view){
        if(view == null)
            return false;
        if(mUploadViewMap.containsKey(view.getKey())){
            mUploadViewMap.get(view.getKey()).remove(view);
        }
       return onRemoveViewByParent(view);
    }

    //TODO 暂未实现(如果token失效，尝试重新上传)
    public void retryUploadByTokenInvaild(UploadTopicReplyView uploadView){
//        if(mState < 0){
//            mWaitQueue.addFirst(uploadView);
//            getUploadToken();
//        }else {
//            uploadView.startUpload();
//        }
    }

    @Override
    public void onUpLoadFinish(UploadTopicReplyView view, Boolean isSuccess) {
        if(isSuccess){
            CreateTopicParams param  = view.getTopicParam();
//            String pics = JSONObject.toJSONString(param);
//            mNet.topicCreateTalk(param.topicId,param.petId,param.content,pics,view);
            mNet.topicCreateTalk(param,view);
        }
    }

    @Override
    public void onUploadCancel(UploadTopicReplyView view) {
        removeUploadView(view);
    }

    private boolean onRemoveViewByParent(UploadTopicReplyView view){
        if(view != null){
            ViewParent parent = view.getParent();
            if(parent != null && parent instanceof ViewGroup){
                try {
                    ((ViewGroup)parent).removeView(view);
                    parent.requestLayout();
                    return true;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 获取数据成功回调接口
     *
     * @param bean        服务器返回数据
     * @param requestCode 区分请求码
     */
    @Override
    public void onSuccessCallback(ResponseBean bean, int requestCode) {
        PublicMethod.log_d("创建讨论结果："+bean.getValue());
        UploadTopicReplyView view = (UploadTopicReplyView) bean.getTag();
        removeUploadView(view);
        view.publishTopicReplyFinish(true);
    }

    /**
     * 获取数据失败回调接口(也包括服务器返回500的错误)
     *
     * @param error       错误信息类
     * @param requestCode 请求码
     */
    @Override
    public void onErrorCallback(PetSayError error, int requestCode) {
        UploadTopicReplyView view = (UploadTopicReplyView) error.getTag();
        view.showRetryUploadView();
        view.publishTopicReplyFinish(false);
    }


    public interface PublishTopicReplyCallback{

    }


}
