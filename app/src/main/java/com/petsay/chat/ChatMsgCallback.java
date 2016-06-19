package com.petsay.chat;


import com.petsay.vo.chat.ChatMsgEntity;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/8
 * @Description
 */
public interface ChatMsgCallback {

    public void onReceiveChatMsg(ChatMsgEntity entity);

}
