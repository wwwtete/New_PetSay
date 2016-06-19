package com.petsay.network.base;

import com.petsay.vo.ResponseBean;

/**
 * @author wangw
 * 连接服务器回调接口
 */
public interface NetCallbackInterface {

	/**
	 * 获取数据成功回调接口
	 * @param bean 服务器返回数据
	 * @param requestCode 区分请求码
	 */
	public void onSuccessCallback(ResponseBean bean,int requestCode);
	
	/**
	 * 获取数据失败回调接口(也包括服务器返回500的错误)
	 * @param error	错误信息类
	 * @param requestCode	请求码
	 */
	public void onErrorCallback(PetSayError error,int requestCode);
	
}
