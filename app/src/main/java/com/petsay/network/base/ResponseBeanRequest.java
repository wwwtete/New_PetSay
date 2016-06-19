package com.petsay.network.base;

import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.petsay.constants.Constants;
import com.petsay.utile.PetsayLog;
import com.petsay.utile.ProtocolManager;
import com.petsay.utile.ToolsAesCrypt;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * @author wangw
 * 向服务器请求数据的Request，返回数据结构必须是ReponseBean格式
 */
public class ResponseBeanRequest extends JsonRequest<ResponseBean> {

	private String mDecryptKey;
    private String mRequestParams;
//	public ResponseBeanRequest(JSONObject params,Listener<ResponseBean> listener,ErrorListener errorListener,String decryptKey){
//		super(Method.POST, Constants.LOT_SERVER_V2 + (TextUtils.isEmpty(decryptKey) ? "0" : "1"), params.toString(), listener, errorListener);
//		initValue(decryptKey);
//	}
//
//	public ResponseBeanRequest(String params,Listener<ResponseBean> listener,ErrorListener errorListener,String decryptKey){
//		super(Method.POST, Constants.LOT_SERVER_V2+ (TextUtils.isEmpty(decryptKey) ? "0" : "1"), params, listener,errorListener);
//		initValue(decryptKey);
//	}

	public ResponseBeanRequest(String url,String params,Listener<ResponseBean> listener,ErrorListener errorListener,String decryptKey){
		super(Method.POST, url, params, listener,errorListener);
		initValue(decryptKey,params);
	}

	public ResponseBeanRequest(String url,JSONObject params,Listener<ResponseBean> listener, ErrorListener errorListener, String decryptKey){
		super(Method.POST, url, params.toString(), listener, errorListener);
		initValue(decryptKey,params.toString());
	}

	private void initValue(String decryptKey,String params){
		setRetryPolicy(new DefaultRetryPolicy(Constants.NET_CONNECTTIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		this.mDecryptKey = decryptKey;
        this.mRequestParams = params;
	}

    @Override
	protected Response<ResponseBean> parseNetworkResponse(
			NetworkResponse response) {
		try {
			String jsonString = new String(response.data,HttpHeaderParser.parseCharset(response.headers));
            if(!TextUtils.isEmpty(mDecryptKey)){
                jsonString = ToolsAesCrypt.Decrypt(jsonString,mDecryptKey);
            }
			ResponseBean bean = JsonUtils.resultData(jsonString, ResponseBean.class);
            PetsayLog.d("param=%s", mRequestParams);
            PetsayLog.d("response=%s",jsonString);
            if(bean.getError() == ProtocolManager.SUCCESS_CODE){
				return Response.success(bean, HttpHeaderParser.parseCacheHeaders(response));
			}else {
                PetsayLog.e("ResponseBeanRequest=%s", jsonString);
				return Response.error(new PetSayError(bean));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
            PetsayLog.e(e, "ResponseBeanRequest=");
			Response.error(new PetSayError(e, PetSayError.CODE_STRINGENCOD));
		} catch (Exception e) {
            e.printStackTrace();
            Response.error(new PetSayError(e, PetSayError.CODE_AES_Decrypt_ERROR));
            PetsayLog.e(e, "ResponseBeanRequest=");
        }
        PetsayLog.e("ResponseBeanRequest出现未知异常");
		return Response.error(new PetSayError(response, PetSayError.CODE_PARSEERROR));
	}

}
