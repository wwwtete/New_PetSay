package com.petsay.network.net;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.ProtocolManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 优惠券接口
 */
public class CouponNet extends BaseNet {

	/**
	 * 我的优惠卷--可用的优惠卷
	 * @param startId
	 * @param pageSize
	 */
	public void couponAvailableList(String startId,int pageSize){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "coupon");
			object.put(ProtocolManager.OPTIONS, "availableList");
			object.put("startId", startId);
			object.put("pageSize", pageSize);
			execute(object, RequestCode.REQUEST_COUPONAVAILABLELIST);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 我的优惠卷--过期和使用过的
	 * @param startId
	 * @param pageSize
	 */
	public void couponUnavailableList(String startId,int pageSize){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "coupon");
			object.put(ProtocolManager.OPTIONS, "unavailableList");
			object.put("startId", startId);
			object.put("pageSize", pageSize);
			execute(object, RequestCode.REQUEST_COUPONUNAVAILABLELIST);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  订单选择优惠卷--可用的优惠卷列表
	 * @param orderId
	 * @param startId
	 * @param pageSize
	 */
	public void couponOrderAvailableList(String orderId,String startId,int pageSize){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "coupon");
			object.put(ProtocolManager.OPTIONS, "orderAvailableList");
			object.put("orderId", orderId);
			object.put("startId", startId);
			object.put("pageSize", pageSize);
			execute(object, RequestCode.REQUEST_COUPONORDERAVAILABLELIST);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 优惠卷活动
	 * @param id
	 */
	public void couponActivity(String id){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "coupon");
			object.put(ProtocolManager.OPTIONS, "activity");
			object.put("id", id);
			execute(object, RequestCode.REQUEST_COUPONACTIVITY);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取优惠卷 -通过优惠卷活动
	 * @param id
	 */
	public void couponGetByActivity(String id){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "coupon");
			object.put(ProtocolManager.OPTIONS, "getByActivity");
			object.put("id", id);
			execute(object, RequestCode.REQUEST_COUPONGETBYACTIVITY);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取优惠卷 -通过优惠卷兑换码
	 * @param code
	 */
	public void couponGetByCode(String code){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "coupon");
			object.put(ProtocolManager.OPTIONS, "getByCode");
			object.put("code", code);
			execute(object, RequestCode.REQUEST_COUPONGETBYCODE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
