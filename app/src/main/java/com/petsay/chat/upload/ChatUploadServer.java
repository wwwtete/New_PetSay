package com.petsay.chat.upload;

import android.content.Context;

import com.emsg.sdk.client.android.asynctask.qiniu.QiNiuFileServerTarget;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/22
 * @Description
 */
public class ChatUploadServer extends QiNiuFileServerTarget {

    public ChatUploadServer(Context mContext) {
        super(mContext);
    }

    @Override
    public String getAudioUrlPath(String content) {
//        return Constants.DOWNLOAD_SERVER + content;
        return  content;
    }
}
