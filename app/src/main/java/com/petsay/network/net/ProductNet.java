package com.petsay.network.net;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.ProtocolManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/6/10
 * @Description 商品接口
 */
public class ProductNet extends BaseNet {

    /**
     * 根据商品类别获取商品详情
     * @param id
     */
    public void getProductDetail(String id){
        try {
            JSONObject object = getDefaultParams("product","detail");
            object.put("id",id);
            execute(object, RequestCode.REQUEST_GETPRODUCTDETAIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 根据类别获取商品信息的接口(返回数据格式跟1一样，用于类别只有一种商品，当前状态下，明信片和日记用到)<br>
     * 明信片categoryId =1<br>日记 categoryId =2<br>服装categoryId =3
     * @param categoryId
     */
    public void productDetailSpecsByCategory(int categoryId){
    	 try {
             JSONObject object = getDefaultParams("product","detailSpecsByCategory");
             object.put("categoryId",categoryId);
             execute(object, RequestCode.REQUEST_PRODUCTDETAILSPECSBYCATEGORY);
         } catch (JSONException e) {
             e.printStackTrace();
         }
    }
    
    /**
     * 获取商品列表的接口（当前状态下只有服装用到）
     * @param categoryId
     * @param pageIndex
     * @param pageSize
     * @param isMore
     */
    public void productListByCategory(int categoryId,int pageIndex,int pageSize,boolean isMore){
   	 try {
            JSONObject object = getDefaultParams("product","listByCategory");
            object.put("categoryId",categoryId);
            object.put("pageIndex",pageIndex);
            object.put("pageSize",pageSize);
            execute(object, RequestCode.REQUEST_PRODUCTLISTBYCATEGORY,isMore);
        } catch (JSONException e) {
            e.printStackTrace();
        }
   }
    
    /**
     * 获取商品所有信息（包含规格信息，例如到定制页面）的接口
     * @param id （id信息从list中获取）
     */
    public void productDetailSpecs(String id){
      	 try {
               JSONObject object = getDefaultParams("product","detailSpecs");
               object.put("id",id);
               execute(object, RequestCode.REQUEST_PRODUCTDETAILSPECS);
           } catch (JSONException e) {
               e.printStackTrace();
           }
      }


}
