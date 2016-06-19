package com.petsay.network.net;

import android.graphics.Color;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.vo.user.UserShippingAddressDTO;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/6/15
 * @Description 购物地址
 */
public class ShippingAddressNet extends BaseNet {


    /**
     * 获取单个收货地址
     * @param id
     */
    public void getOneAddress(String id){
        try {
            JSONObject object = getDefaultParams("one");
            object.put("id",id);
            execute(object, RequestCode.REQUEST_ONESHIPPINGADDRESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户所有收货地址
     */
    public void getListAddress(){
        try {
            JSONObject object = getDefaultParams("list");
            execute(object, RequestCode.REQUEST_LISTSHIPPINGADDRESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户默认收货地址
     */
    public void getDefaultAddress(){
        try {
            JSONObject object = getDefaultParams("defaultOne");
            execute(object, RequestCode.REQUEST_DEFAULTSHIPPINGADDRESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建收货地址
     * @param dto
     */
    public void createShippingAddress(UserShippingAddressDTO dto){
        editShippingAddress(dto, RequestCode.REQUEST_CREATESHIPPINGADDRESS,"create");
    }

    /**
     * 更新收货地址
     * @param dto
     */
    public void updateShippingAddress(UserShippingAddressDTO dto){
        editShippingAddress(dto, RequestCode.REQUEST_UPDATESHIPPINGADDRESS,"update");
    }

    private void editShippingAddress(UserShippingAddressDTO dto,int code,String option){
        try {
            JSONObject object = getDefaultParams(option);
            if(code == RequestCode.REQUEST_UPDATESHIPPINGADDRESS)
                object.put("id",dto.getId());
            object.put("address",dto.getAddress());
            object.put("mobile",dto.getMobile());
            object.put("city",dto.getCity());
            object.put("province",dto.getProvince());
            object.put("name",dto.getName());
            object.put("zipcode",dto.getZipcode());
            object.put("isDefault",dto.getIsDefault());
            execute(object, code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 创建收货地址
//     * @param address
//     * @param mobile
//     * @param city
//     * @param province
//     * @param name
//     * @param zipcode
//     * @param isDefalut
//     */
//    public void createShippingAddress(String address,String mobile,String city,String province,String name,String zipcode,boolean isDefalut){
//        try {
//            JSONObject object = getDefaultParams("create");
//            object.put("address",address);
//            object.put("mobile",mobile);
//            object.put("city",city);
//            object.put("province",province);
//            object.put("name",name);
//            object.put("zipcode",zipcode);
//            object.put("isDefalut",isDefalut);
//            execute(object, RequestCode.REQUEST_CREATESHIPPINGADDRESS);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    /**
//     * 更新收货地址
//     * @param id
//     * @param address
//     * @param mobile
//     * @param city
//     * @param province
//     * @param name
//     * @param zipcode
//     * @param isDefalut
//     */
//    public void updateShippingAddress(String id,String address,String mobile,String city,String province,String name,String zipcode,boolean isDefalut){
//        try {
//            JSONObject object = getDefaultParams("update");
//            object.put("id",id);
//            object.put("address",address);
//            object.put("mobile",mobile);
//            object.put("city",city);
//            object.put("province",province);
//            object.put("name",name);
//            object.put("zipcode",zipcode);
//            object.put("isDefalut",isDefalut);
//            execute(object, RequestCode.REQUEST_UPDATESHIPPINGADDRESS);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 设置默认地址
     * @param id
     */
    public void setDefaultAddress(String id){
        try {
            JSONObject object = getDefaultParams("setDefault");
            object.put("id",id);
            execute(object, RequestCode.REQUEST_SETDEFAULTSHIPPINGADDRESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除购物地址
     * @param id
     */
    public void deleteShippingAddress(String id){
        try {
            JSONObject object = getDefaultParams("delete");
            object.put("id",id);
            execute(object, RequestCode.REQUEST_DELETESHIPPINGADDRESS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject getDefaultParams(String option) throws JSONException {
        return getDefaultParams("shippingAddress",option);
    }
}
