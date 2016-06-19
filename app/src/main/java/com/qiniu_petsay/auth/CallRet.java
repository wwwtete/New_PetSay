package com.qiniu_petsay.auth;

import com.qiniu_petsay.utils.QiniuException;

public abstract class CallRet implements com.qiniu_petsay.utils.IOnProcess {
	public void onInit(int flag){}
	public abstract void onSuccess(byte[] body);
	public abstract void onFailure(QiniuException ex);
	public void onProcess(long current, long total){}
	public void onPause(Object tag){}
}
