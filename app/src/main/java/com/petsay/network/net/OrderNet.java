package com.petsay.network.net;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.ProtocolManager;
import com.petsay.vo.user.UserShippingAddressDTO;

/**
 * @author gaojian
 * 订单
 */
public class OrderNet extends BaseNet {

	
	/**
	 * 订单确认
	 * @param id
	 * @param payChannel
	 * @param note
	 * @param addressDTO
	 */
	 public void orderConfirm(String id,String couponId,String payChannel,String note,UserShippingAddressDTO addressDTO) {
			JSONObject obj = getDefaultParams();
			try {
				obj.put(ProtocolManager.COMMAND, "order");
				obj.put(ProtocolManager.OPTIONS, "confirm");
				obj.put("id", id);
				obj.put("couponId", couponId);
				obj.put("payChannel", payChannel);
				obj.put("note", note);
				obj.put("shippingName", addressDTO.getName());
				obj.put("shippingMobile", addressDTO.getMobile());
				obj.put("shippingZipcode", addressDTO.getZipcode());
				obj.put("shippingProvince", addressDTO.getProvince());
				obj.put("shippingCity", addressDTO.getCity());
				obj.put("shippingAddress", addressDTO.getAddress());
				execute(obj, RequestCode.REQUEST_ORDERCONFIRM);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	 
	 /**
	  * 订单创建<br>
	  * orderItems = [{
productId (产品的ID)
productUpdateTime; (产品的最后一次编辑的时间，   你们在获取商品的时候能够获得。     订单生成的时候，取价格等信息的时候，会先判断是否有时间差)
useCard；(true使用了m卡，false是没有使用m卡)
count；(此项商品数量)
List<OrderProductSpecDTO> specs;(规格信息)
}]
<br>
注解：（其中value，如果有商品的specValueId返回则为这个值）
specs = [{id, value},{},{}]
	  * @param orderItems
	  */
	public void orderCreate(String orderItems) {
		JSONObject obj = getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "order");
			obj.put(ProtocolManager.OPTIONS, "create");
			JSONArray jsonArray = new JSONArray(orderItems);
			obj.put("orderItems", jsonArray);
			execute(obj, RequestCode.REQUEST_ORDERCREATE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 待支付
     * @param startId
     * @param pageSize
     * @param isMore
     */
    public void toPayList(String startId,int pageSize,boolean isMore){
        orderList(RequestCode.REQUEST_TOPAYLIST,"toPayList",startId,pageSize,isMore);
    }

    /**
     * 待发货
     * @param startId
     * @param pageSize
     * @param isMore
     */
    public void toShipList(String startId,int pageSize,boolean isMore){
        orderList(RequestCode.REQUEST_TOSHIPLIST,"toShipList",startId,pageSize,isMore);
    }

    /**
     * 待收货
     * @param startId
     * @param pageSize
     * @param isMore
     */
    public void toReceiveList(String startId,int pageSize,boolean isMore){
        orderList(RequestCode.REQUEST_TORECEIVELIST,"toReceiveList",startId,pageSize,isMore);
    }

    /**
     * 已完成
     * @param startId
     * @param pageSize
     * @param isMore
     */
    public void finishedList(String startId,int pageSize,boolean isMore){
        orderList(RequestCode.REQUEST_FINISHEDLIST,"finishedList",startId,pageSize,isMore);
    }

    private void orderList(int code,String option,String startId,int pageSize,boolean isMore){
        try {
            JSONObject object = getDefaultParams(option);
            object.put("id",startId);
            object.put("pageSize",pageSize);
            execute(object,code,isMore);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 订单详情
     * @param id
     */
    public void orderDetails(String id){
        try {
            JSONObject object = getDefaultParams("one");
            object.put("id",id);
            execute(object,RequestCode.REQUEST_ORDERDETAILS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 确认收货
     * @param id
     */
    public void receiveProduct(String id){
        try {
            JSONObject object = getDefaultParams("receiveProduct");
            object.put("id",id);
            execute(object,RequestCode.REQUEST_RECEIVEPRODUCT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消订单
     * @param id
     */
    public void cancelOrder(String id){
        try {
            JSONObject object = getDefaultParams("cancelOrder");
            object.put("id",id);
            execute(object,RequestCode.REQUEST_CANCELORDER);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected JSONObject getDefaultParams(String option) throws JSONException {
        return getDefaultParams("order",option);
    }

}
