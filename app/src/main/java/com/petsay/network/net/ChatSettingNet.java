package com.petsay.network.net;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.ProtocolManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/26
 * @Description 聊天设置服务器接口
 */
public class ChatSettingNet extends BaseNet {

    /**
     * 获取聊天设置
     * @param petID
     */
    public void getChatSetting(String petID){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"setting");
            object.put(ProtocolManager.OPTIONS,"PSL");
            object.put("petId",petID);
            execute(object,RequestCode.REQUEST_GETCHATSETTING);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改宠物说设置
     * @param petID
     * @param val 0: 所有人 1：仅我关注的人
     */
    public void modifyChatSetting(String petID,int val){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"setting");
            object.put(ProtocolManager.OPTIONS,"PSM");
            object.put("petId",petID);
            object.put("key","mesg");
            object.put("val",val);
            execute(object,RequestCode.REQUEST_MODIFYCHATSETTING);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取聊天黑名单
     * @param currPetId 当前宠物的ID
     * @param pageIndex
     * @param pageSize
     */
    public void getBlackList(String currPetId,int pageIndex,int pageSize,boolean isMore){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"setting");
            object.put(ProtocolManager.OPTIONS,"CBL");
            object.put("petId",currPetId);
            object.put("currPetId",currPetId);
            object.put("pageIndex",pageIndex);
            object.put("pageSize",pageSize);
            execute(object, RequestCode.REQUEST_GETBLACKLIST,isMore);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除黑名单
     * @param petID
     * @param currPetId
     */
    public void deleteBlack(String petID,String currPetId){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"setting");
            object.put(ProtocolManager.OPTIONS,"CBD");
            object.put("petId",petID);
            object.put("currPetId",currPetId);
            execute(object,RequestCode.REQUEST_DELETEBLACK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加黑名单
     * @param petID
     * @param currPetId
     */
    public void addBlack(String petID,String currPetId){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"setting");
            object.put(ProtocolManager.OPTIONS,"CBA");
            object.put("petId",petID);
            object.put("currPetId",currPetId);
            execute(object,RequestCode.REQUEST_ADDBLACK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否允许聊天
     * @param petID
     * @param currPetId
     */
    public void canChat(String petID,String currPetId){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"setting");
            object.put(ProtocolManager.OPTIONS,"CCT");
            object.put("petId",petID);
            object.put("currPetId",currPetId);
            execute(object,RequestCode.REQUEST_CANCHAT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
