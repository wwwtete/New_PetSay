package com.petsay.network.net;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.ProtocolManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 商城接口
 */
public class ShopNet extends BaseNet {

	/**
	 * 701_试用商品推荐
	 * 
	 * @param petId
	 */
	public void goodsTrialTop2(String petId) {
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "goods");
			object.put(ProtocolManager.OPTIONS, "trialTop2");
			object.put("petId", petId);
			execute(object, RequestCode.REQUEST_GOODSTRIALTOP2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 702_试用商品列表
	 * @param petId
	 * @param pageIndex
	 * @param pageSize
	 */
	public void goodsTrialList(String petId,int pageIndex,int pageSize,boolean isMore) {
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "goods");
			object.put(ProtocolManager.OPTIONS, "trialList");
			object.put("petId", petId);
			object.put("pageIndex", pageIndex);
			object.put("pageSize", pageSize);
			execute(object, RequestCode.REQUEST_GOODSTRIALLIST, isMore);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 *703_兑换商品列表
	 * @param petId
	 * @param pageIndex
	 * @param pageSize
	 */
	public void goodsExchangeList(String petId, int pageIndex, int pageSize,boolean isMore) {
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "goods");
			object.put(ProtocolManager.OPTIONS, "exchangeList");
			object.put("petId", petId);
			object.put("pageIndex", pageIndex);
			object.put("pageSize", pageSize);
			execute(object, RequestCode.REQUEST_GOODSEXCHANGELIST,isMore);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 *706_订单列表
	 * @param petId
	 * @param pageIndex
	 * @param pageSize
	 */
	public void goodsOrderList(String petId, int pageIndex, int pageSize,boolean isMore) {
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "goods");
			object.put(ProtocolManager.OPTIONS, "orderList");
			object.put("petId", petId);
			object.put("pageIndex", pageIndex);
			object.put("pageSize", pageSize);
			execute(object, RequestCode.REQUEST_GOODSORDERLIST,isMore);
//			execute(jsonObject, requestCode, isMore);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 商品详情
	 * 
	 * @param petId
	 * @param code
	 */	
	public void goodsDetail(String petId, String code) {
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "goods");
			object.put(ProtocolManager.OPTIONS, "goodsDetail");
			object.put("petId", petId);
			object.put("code", code);
			execute(object, RequestCode.REQUEST_GETSCOREDETAIL);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 705_创建订单
	 * 
	 * @param petId
	 * @param code
	 */	
	public void goodsOrderCreate(String petId, String code) {
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "goods");
			object.put(ProtocolManager.OPTIONS, "orderCreate");
			object.put("petId", petId);
			object.put("code", code);
			execute(object, RequestCode.REQUEST_GOODSORDERCREATE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	

}
