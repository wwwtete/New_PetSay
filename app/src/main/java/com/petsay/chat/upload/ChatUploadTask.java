package com.petsay.chat.upload;

import android.content.Context;
import android.net.Uri;

import com.emsg.sdk.client.android.asynctask.IUpLoadTask;
import com.emsg.sdk.client.android.asynctask.TaskCallBack;
import com.petsay.constants.Constants;
import com.petsay.utile.PublicMethod;
import com.qiniu_petsay.auth.JSONObjectRet;
import com.qiniu_petsay.io.IO;
import com.qiniu_petsay.io.PutExtra;
import com.qiniu_petsay.utils.FileUri;

import org.json.JSONObject;

import java.io.File;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/22
 * @Description
 */
public class ChatUploadTask implements IUpLoadTask {

    private String mUploadToken;
    private Context mContext;
    private String mKey;

    public ChatUploadTask(Context context,String uploadToken){
        this.mUploadToken = uploadToken;
        this.mContext = context;
    }


    /**
     * 上传uri文件
     *
     * @param uri
     * @param callback
     */
    public void upload(Uri uri, TaskCallBack callback) {
        File file = FileUri.getFile(mContext, uri);
        upload(file, callback);
    }

    /**
     * 上传文件
     *
     * @param file
     * @param callback
     */
    public void upload(File file, final TaskCallBack callback) {
//        String uptoken = TokenGenerator.getAccesskey(context, "emsg");
//        PutExtra extra = new PutExtra();
//        extra.params = new HashMap<String, String>();
//
//        IO.putFile(uptoken, key, file, extra, new JSONObjectRet() {
//
//            @Override
//            public void onFailure(QiniuException obj) {
//                callback.onFailure();
//            }
//
//            @Override
//            public void onSuccess(JSONObject obj) {
//                callback.onSuccess(key);
//            }
//        });

//        mKey = Constants.CHAT_SERVER_AUDIO+UUID.randomUUID().toString() + "_" + file.getName(); // 自动生成key
        mKey = Constants.CHAT_SERVER_AUDIO +  file.getName();
        IO.putFile(Constants.UPLOAD_TOKEN, mKey, file, new PutExtra(), new JSONObjectRet() {

            @Override
            public void onProcess(long current, long total) {
            }

            @Override
            public void onFailure(com.qiniu_petsay.utils.QiniuException ex) {
                PublicMethod.log_e("-----------" + mKey + ": 上传失败：失败原因：", ex.getMessage());
                if (callback != null)
                    callback.onFailure();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                if(callback != null){
                    callback.onSuccess(mKey);
                }
            }
        });

    }
}
