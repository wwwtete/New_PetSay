package com.petsay.network.net;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.ProtocolManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商城接口
 */
public class AwardNet extends BaseNet {

	
	
	/**
	 * 获取奖品(活动)的列表
	 * @param startId
	 * @param petId
	 * @param pageSize
	 */
	public void awardActivityList(String startId,String petId,int pageSize,boolean isMore){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "awardActivity");
			object.put(ProtocolManager.OPTIONS, "list");
			object.put("startId", startId);
			object.put("petId", petId);
			object.put("pageSize", pageSize);
			execute(object, RequestCode.REQUEST_AWARDACTIVITYLIST,isMore);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取单个奖品详情
	 * @param id
	 * @param petId
	 */
	public void awardActivityOne(String id,String petId){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "awardActivity");
			object.put(ProtocolManager.OPTIONS, "one");
			object.put("id", id);
			object.put("petId", petId);
			execute(object, RequestCode.REQUEST_AWARDACTIVITYONE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 参加活动
	 * @param petId
	 * @param activityId
	 * @param headPortrait
	 */
	public void awardActivityJoin(String petId,String activityId,String headPortrait){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "awardActivity");
			object.put(ProtocolManager.OPTIONS, "join");
			object.put("petId", petId);
			object.put("activityId", activityId);
			object.put("headPortrait", headPortrait);
			execute(object, RequestCode.REQUEST_AWARDACTIVITYJOIN);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 我的任务列表
	 * @param petId
	 * @param startId
	 * @param pageSize
	 */
	public void awardActivityMyList(String petId,String startId,int pageSize,boolean isMore){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "awardActivity");
			object.put(ProtocolManager.OPTIONS, "myList");
			object.put("petId", petId);
			object.put("startId", startId);
			object.put("pageSize", pageSize);
			execute(object, RequestCode.REQUEST_AWARDACTIVITYMYLIST,isMore);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取我的任务的列表（这个是全部的）  ps相当于点击    “记录”  按钮
	 * @param petId
	 * @param startId
	 * @param pageSize
	 */
	public void awardActivityMyListAll(String petId,String startId,int pageSize,boolean isMore){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "awardActivity");
			object.put(ProtocolManager.OPTIONS, "myListAll");
			object.put("petId", petId);
			object.put("startId", startId);
			object.put("pageSize", pageSize);
			execute(object, RequestCode.REQUEST_AWARDACTIVITYMYLISTALL,isMore);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取我的单个任务详情   (目前设计好像奖品的详情跟我的任务详情的页面是一样的)，所以你们可以暂时不用这个，用上面那个代替
	 * @param id
	 */
	public void awardActivityMyOne(String id){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "awardActivity");
			object.put(ProtocolManager.OPTIONS, "myOne");
			object.put("id", id);
			execute(object, RequestCode.REQUEST_AWARDACTIVITYMYONE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取我的任务  下面“未完成”“已完成” 数字      基本信息
	 * @param petId
	 */
	public void awardActivityMyListInfo(String petId){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "awardActivity");
			object.put(ProtocolManager.OPTIONS, "myListInfo");
			object.put("petId", petId);
			execute(object, RequestCode.REQUEST_AWARDACTIVITYMYLISTINFO);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
