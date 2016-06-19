package com.petsay.network.base;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.petsay.vo.ResponseBean;

/**
 * @author wangw
 * 异常类
 */
public class PetSayError extends ParseError {

	/**未知错误*/
	public static final int CODE_ERROR = -1;
	/**网络连接不可用*/
	public static final int CODE_NETWORK_DISABLED = 8000;
	/**Token失效*/
	public static final int CODE_SESSIONTOKEN_DISABLE = 503;
    /**需要登录权限*/
    public static final int CODE_PERMISSION_ERROR = 504;
	/**字符串转换异常*/
	public static final int CODE_STRINGENCOD = 8100;
	/**解析数据异常错误*/
	public static final int CODE_PARSEERROR = 8200;
    /**Aes解密异常*/
    public static final int CODE_AES_Decrypt_ERROR = 8300;
    /**Aes加密异常*/
    public static final int CODE_AESENCRYPT_ERROR = 8400;

//	private int mCode = CODE_ERROR;
	private ResponseBean mBean;
//    /**当前的请求是否执行加载更多操作*/
//    private boolean mIsMore;
//    private Object mTag;

    public PetSayError(NetworkResponse networkResponse,int code){
		super(networkResponse);
		initValue(CODE_ERROR);
	}
	
	public PetSayError(ResponseBean bean){
		this.mBean = bean;
	}
	
	public PetSayError(Throwable cause,int code){
		super(cause);
		initValue(code);
	}
	
	public PetSayError(int code){
		initValue(code);
	}

	private void initValue(int code){
		if(mBean != null)
			mBean.setError(code);
		else
			mBean = new ResponseBean(code);
	}
	
	public ResponseBean getResponseBean(){
		return mBean;
	}

	public String getMessage(){
		return mBean.getMessage();
	}
	
	/**
	 * 错误码
	 * @return
	 */
	public int getCode(){
		return mBean.getError();
	}

    /**
     * @return 用于区分当前的请求是否加载更多操作，默认是false,
     * true:执行刷新操作  false：执行加载更多操作
     */
    public boolean isIsMore() {
        return mBean.isIsMore();
    }

    /**
     * 用于区分当前的请求是否加载更多操作，默认是true,
     * true:执行刷新操作  false：执行加载更多操作
     * @param isMore 默认为false
     */
    public void setIsMore(boolean isMore) {
		mBean.setIsMore(isIsMore());
    }

    public Object getTag() {
        return mBean.getTag();
    }

    public void setTag(Object tag) {
        mBean.setTag(tag);
    }
	
	
}
