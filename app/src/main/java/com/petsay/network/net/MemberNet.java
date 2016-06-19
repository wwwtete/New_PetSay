package com.petsay.network.net;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.ProtocolManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wangw on 2014/12/16.
 *
 * 会员接口
 */
public class MemberNet extends BaseNet {


    /**
     * 获取总积分
     * @param petID
     */
    public void getScoreTotal(String petID){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"petScore");
            object.put(ProtocolManager.OPTIONS,"total");
            object.put("petId",petID);
            execute(object, RequestCode.REQUEST_GETSCORETOTAL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取积分详情列表
     * @param petID
     * @param startId
     * @param pageSize
     * @param isMore
     */
    public void getScoreDetail(String petID,String startId,int pageSize,boolean isMore){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"petScore");
            object.put(ProtocolManager.OPTIONS,"detail");
            object.put("petId",petID);
            object.put("startId",startId);
            object.put("pageSize",pageSize);
            execute(object, RequestCode.REQUEST_GETSCOREDETAIL,isMore);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 可参与活动列表(登陆后访问，获取签到状态)
     * @param petId
     */
    public void activityPartake(String petId){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"activity");
            object.put(ProtocolManager.OPTIONS,"partake");
            object.put("petId",petId);
            execute(object, RequestCode.REQUEST_ACTIVITYPARTAKE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 签到
     * @param petId
     */
    public void activitySignIn(String petId){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"activity");
            object.put(ProtocolManager.OPTIONS,"signIn");
            object.put("petId",petId);
            object.put("code","signInNormal");
            execute(object, RequestCode.REQUEST_ACTIVITYSIGNIN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 签到日历
     * @param petId
     * @param pageSize
     */
    public void activitySignCal(String petId,int pageSize){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"activity");
            object.put(ProtocolManager.OPTIONS,"signCal");
            object.put("petId",petId);
            object.put("pageSize",pageSize);
            execute(object, RequestCode.REQUEST_ACTIVITYSIGNCAL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取宠物金币明细
     * @param petID
     * @param startId
     * @param pageSize
     * @param isMore
     */
    public void getCoinDetail(String petID,String startId,int pageSize,boolean isMore){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"petCoin");
            object.put(ProtocolManager.OPTIONS,"detail");
            object.put("petId",petID);
            object.put("startId",startId);
            object.put("pageSize",pageSize);
            execute(object,RequestCode.REQUEST_GETCOINDETAIL,isMore);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 等级规则
     */
    public void getGradeRule(){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"petGrade");
            object.put(ProtocolManager.OPTIONS,"rule");
            execute(object,RequestCode.REQUEST_GETGRADERULE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取宠物等级
     * @param petID
     */
    public void getPetGrade(String petID){
        JSONObject object = getDefaultParams();
        try {
            object.put(ProtocolManager.COMMAND,"petGrade");
            object.put(ProtocolManager.OPTIONS,"pet");
            object.put("petId",petID);
            execute(object,RequestCode.REQUEST_GETPETGRADE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

	/**
	 * 403_获取宠物金币
	 * 
	 * @param petId
	 * @param code
	 */	
	public void petCoin(String petId) {
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "petCoin");
			object.put(ProtocolManager.OPTIONS, "total");
			object.put("petId", petId);
			execute(object, RequestCode.REQUEST_PETCOIN);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
