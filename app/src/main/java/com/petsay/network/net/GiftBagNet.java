package com.petsay.network.net;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.ProtocolManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangw on 2014/12/16.
 * 礼包服务接口
 */
public class GiftBagNet extends BaseNet {


    /**
     * 获取礼包选项卡的标题
     * @param petID
     */
    public void getGiftTabTitle(String petID){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"tab");
            object.put(ProtocolManager.OPTIONS,"giftBag");
            object.put("petId",petID);
            execute(object,RequestCode.REQUEST_GETGIFTTABTITLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取所有礼包
     * @param petID
     * @param pageIndex
     * @param pageSize
     * @param isMore 是否加载更多操作
     */
    public void getAllGiftBag(String petID,String code, int pageIndex, int pageSize,Object tag,boolean isMore){
        JSONObject jsonObject = getDefaultParams();
        try {
            jsonObject.put(ProtocolManager.COMMAND,"giftBag");
            jsonObject.put(ProtocolManager.OPTIONS,"all");
            jsonObject.put("petId",petID);
            jsonObject.put("code", code);
            jsonObject.put("pageIndex",pageIndex);
            jsonObject.put("pageSize",pageSize);
            execute(jsonObject, RequestCode.REQUEST_GETALLGIFBAG,isMore,tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取我的礼包
     * @param petID
     * @param pageIndex
     * @param pageSize
     */
    public void getMyGifBag(String petID, int pageIndex, int pageSize,boolean isMore){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"giftBag");
            object.put(ProtocolManager.OPTIONS,"mine");
            object.put("petId",petID);
            object.put("pageIndex",pageIndex);
            object.put("pageSize",pageSize);
            execute(object,RequestCode.REQUEST_GETMYGIFBAG,isMore);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取礼包详情
     * @param code
     */
    public void getBifBagDetail(String code){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"giftBag");
            object.put(ProtocolManager.OPTIONS,"detail");
            object.put("code",code);
            execute(object,RequestCode.REQUEST_GETBIFBAGDETAIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 领取礼包
     * @param petID
     * @param code
     */
    public void drawGifBag(String petID,String code){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"giftBag");
            object.put(ProtocolManager.OPTIONS,"draw");
            object.put("petId",petID);
            object.put("code",code);
            execute(object,RequestCode.REQUEST_DRAWGIFBAG);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
