package com.petsay.network.base;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.petsay.application.PetSayApplication;
import com.petsay.application.UserManager;
import com.petsay.constants.Constants;
import com.petsay.utile.ProtocolManager;
import com.petsay.utile.PublicMethod;
import com.petsay.utile.ToolsAesCrypt;
import com.petsay.vo.ResponseBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wangw
 * 基层Service
 */
public class BaseNet {

	private NetCallbackInterface mCallback;
	private Object mTag;
	
	public BaseNet(){
	}
	
	public BaseNet(NetCallbackInterface callback){
		setCallback(callback);
	}
	
	public void setCallback(NetCallbackInterface callback) {
		this.mCallback = callback;
	}
	
	public void removeCallback(){
		this.mCallback = null;
	}

	public VolleyManager getVolleyManager(){
		return VolleyManager.getInstance();
	}
	
	/**
	 * 设置tag标签用于取消请求
	 * @param tag
	 */
	public void setTag(Object tag){
		mTag = tag;
	}
	
	public Object getTag(){
		return mTag;
	}
	
	public void cancelAll(Object tag){
		if(tag != null)
			getVolleyManager().cancelAll(tag);
	}

    public void execute(JSONObject jsonObject,int requestCode, boolean isMore){
        execute(jsonObject,requestCode,isMore,"");
    }

	public void execute(JSONObject jsonObject,int requestCode){
		execute(jsonObject,requestCode,false,"");
	}

	public void execute(JSONObject jsonObject,int requestCode, final Object tag){
		execute(jsonObject,requestCode,false,tag);
	}

    public void execute(JSONObject jsonObject,final int requestCode, final boolean isMore, final Object tag){
        execute(jsonObject,requestCode,isMore,tag,false);
    }

    public void execute(String param,final int requestCode, final Object tag){
        execute(param,requestCode,false,tag,false,true);
    }

    /**
     * 执行Aes加密方式请求
     * @param jsonObject
     * @param requestCode
     */
    public void executeSecurity(JSONObject jsonObject,int requestCode){
        execute(jsonObject,requestCode,false,"",true);
    }

    /**
     * 执行一条请求
     * @param jsonObject
     * @param requestCode
     * @param isMore 当前的请求是否执行了加载更多操作
     * @param tag    当前区分同一个请求的操作tag
     * @param isEncrypt 是否使用AES加密
     */
    public void execute(JSONObject jsonObject,int requestCode,boolean isMore, Object tag,boolean isEncrypt){
//        if(PublicMethod.getAPNType(PetSayApplication.getInstance()) < 0){
//            onNetworkDisabled(requestCode,isMore,tag);
//        }else {
//            String key = UserManager.getSingleton().getDecryptKey();
//            String token = UserManager.getSingleton().getSeesionToken();
//            PublicMethod.log_e("token",token);
//            try {
//                jsonObject.put("sToken",token);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            String param = jsonObject.toString();
//            ResponseBeanRequest request = new ResponseBeanRequest(encrypt(param,isEncrypt), new Response.Listener<ResponseBean>() {
//
//                @Override
//                public void onResponse(ResponseBean response) {
//                    if(mCallback != null) {
//                        response.setTag(tag);
//                        response.setIsMore(isMore);
//                        mCallback.onSuccessCallback(response, requestCode);
//                    }
//                }
//            }, new Response.ErrorListener() {
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    PetSayError e = null;
//                    if (error instanceof PetSayError) {
//                        e = (PetSayError) error;
//                    }else {
//                        e = new PetSayError(PetSayError.CODE_ERROR);
//                    }
//                    e.setTag(tag);
//                    e.setIsMore(isMore);
//                    onErrorCallback(e,requestCode);
//                }
//            },isEncrypt ? key : null);
//            add(request);
//        }
        execute(jsonObject.toString(),requestCode,isMore,tag,isEncrypt,true);
    }
    /**
     * 
     * @param jsonObject
     * @param requestCode
     * @param isMore
     * @param tag    当前区分同一个请求的操作tag
     * @param isEncrypt 是否使用AES加密
     * @param hasToken 是否使用Token
     */
    public void executeNew(JSONObject jsonObject,int requestCode,boolean isMore, Object tag,boolean isEncrypt,boolean hasToken){
    	execute(jsonObject.toString(), requestCode,   isMore,   tag, isEncrypt, hasToken);
    }
    

    /**
     * 执行一条请求
     * @param param
     * @param requestCode
     * @param tag    当前区分同一个请求的操作tag
     * @param isEncrypt 是否使用AES加密
     * @param hasToken 是否使用Token
     */
	public void execute(String param,final int requestCode, final boolean isMore, final Object tag,boolean isEncrypt,boolean hasToken){
		if(PublicMethod.getAPNType(PetSayApplication.getInstance()) < 0){
            onNetworkDisabled(requestCode,false,tag);
		}else {
            String key = isEncrypt ? UserManager.getSingleton().getDecryptKey() : null;
            String url = getServerUrl(isEncrypt,hasToken);
			ResponseBeanRequest request = new ResponseBeanRequest(url,encrypt(param, isEncrypt), new Response.Listener<ResponseBean>() {
				
				@Override
				public void onResponse(ResponseBean response) {
					if(mCallback != null) {
                        response.setTag(tag);
                        response.setIsMore(isMore);
                        mCallback.onSuccessCallback(response, requestCode);
                    }
				}
			}, new Response.ErrorListener() {
				
				@Override
				public void onErrorResponse(VolleyError error) {
                   onError(error,tag,isMore,requestCode);
				}
			},key);
			add(request);
		}
	}

    private void onError(VolleyError error,Object tag,boolean isMore,int requestCode) {
        PetSayError e = null;
        if (error instanceof PetSayError) {
            e = (PetSayError) error;
            if(e.getCode() == PetSayError.CODE_SESSIONTOKEN_DISABLE || e.getCode() == PetSayError.CODE_PERMISSION_ERROR)
                UserManager.getSingleton().logout();
        }else {
            e = new PetSayError(PetSayError.CODE_ERROR);
        }
        e.setTag(tag);
        e.setIsMore(isMore);
        onErrorCallback(e, requestCode);
    }

    /**
     * 执行第一版的URL接口,不带SToken方式
     * @param jsonObject
     * @param requestCode
     * @param isMore
     * @param tag
     * @param isEncrypt
     */
    public void executeByV1(JSONObject jsonObject,final int requestCode, final boolean isMore, final Object tag,boolean isEncrypt){
        if(PublicMethod.getAPNType(PetSayApplication.getInstance()) < 0){
            onNetworkDisabled(requestCode,isMore,tag);
        }else {
            String param = jsonObject.toString();
            ResponseBeanRequest request = new ResponseBeanRequest(Constants.LOT_SERVER_V1+(isEncrypt ? "1" : "0"),encrypt(param, isEncrypt), new Response.Listener<ResponseBean>() {

                @Override
                public void onResponse(ResponseBean response) {
                    if(mCallback != null) {
                        response.setTag(tag);
                        response.setIsMore(isMore);
                        mCallback.onSuccessCallback(response, requestCode);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    PetSayError e = null;
                    if (error instanceof PetSayError) {
                        e = (PetSayError) error;
                    }else {
                        e = new PetSayError(PetSayError.CODE_ERROR);
                    }
                    e.setTag(tag);
                    e.setIsMore(isMore);
                    onErrorCallback(e,requestCode);
                }
            },isEncrypt ? Constants.KEY : null);
            add(request);
        }
    }

    /**
     * 加密
     * @param param
     * @param isEncrypt
     * @return
     */
    private String encrypt(String param,boolean isEncrypt){
        if(isEncrypt && !TextUtils.isEmpty(param)){
            try {
                return ToolsAesCrypt.Encrypt(param, Constants.KEY);
            } catch (Exception e) {
                e.printStackTrace();
                PublicMethod.log_e("BaseNet","参数加密异常");
//                onErrorCallback(new PetSayError(PetSayError.CODE_AESENCRYPT_ERROR),requestCode);
            }
        }
        return param;
    }

    /**
     * 获取服务器接口
     * @param isEncrypt
     * @param hasToken
     * @return
     */
    private String getServerUrl(boolean isEncrypt,boolean hasToken){
        String en = "&isEncrypt="+(isEncrypt ? "1":"0");
        String token = "";
        if(hasToken)
         token = "&sToken="+UserManager.getSingleton().getSeesionToken();
        return Constants.LOT_SERVER_V2+en+token;
    }

    private void onNetworkDisabled(int requestCode,boolean isMore, Object tag){
        PetSayError error = new PetSayError(PetSayError.CODE_NETWORK_DISABLED);
        error.setIsMore(isMore);
        error.setTag(tag);
        onErrorCallback(error, requestCode);
    }
	
	public void onErrorCallback(PetSayError error,int requestCode){
		if(mCallback != null){
				mCallback.onErrorCallback(error, requestCode);
		}
	}
	
	public <T> Request<T> add(Request<T> request){
		if(mTag != null){
			request.setTag(mTag);
		}
		return getVolleyManager().add(request);
	}
	
	public JSONObject getDefaultParams(){
		return ProtocolManager.getDefaultJsonProtocol();
	}

    public JSONObject getDefaultParams(String command,String option) throws JSONException {
        JSONObject object = ProtocolManager.getDefaultJsonProtocol();
        object.put(ProtocolManager.COMMAND,command);
        object.put(ProtocolManager.OPTIONS,option);
        return object;
    }
	
}
