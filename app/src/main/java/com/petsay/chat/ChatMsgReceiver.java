package com.petsay.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.emsg.sdk.EmsgConstants;
import com.emsg.sdk.beans.EmsMessage;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.chat.ChatMsgEntity;

import java.util.Date;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/5
 * @Description
 */
public class ChatMsgReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        EmsMessage message = intent.getParcelableExtra("message");
        PublicMethod.log_d("action ="+ action + "  |  msg = "+message.toString());
        if(message == null)
            return;
        ChatMsgEntity entity = new ChatMsgEntity();
        entity.setDate(new Date());
        entity.setIsComMeg(true);
        entity.setStates(1);
        entity.setPetId(getID(message.getmAccFrom()));
        entity.setAccFromId(getID(message.getmAccFrom()));
        entity.setAccToId(getID(message.getmAccTo()));
        if(message.getContentLength() != null){
            entity.setMediaTime(Integer.parseInt(message.getContentLength()));
        }
//        entity.setDate(DateUtils.getFormatTime());
//        entity.setName(message.getmAccFrom());
//        entity.setMsgType(true);
        String type = message.getContentType();
        if (type != null) {
            entity.setType(type);
        } else {
            entity.setType(EmsgConstants.MSG_TYPE_FILETEXT);
        }
        entity.setText(message.getContent());

        ChatMsgManager.getInstance().receiveChartMsg(entity);
    }

    private String getID(String str){
        if(TextUtils.isEmpty(str))
            return str;
        else{
            return str.substring(0,str.indexOf("@"));
        }
    }
}
