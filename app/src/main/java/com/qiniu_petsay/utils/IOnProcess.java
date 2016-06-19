package com.qiniu_petsay.utils;

public interface IOnProcess {
	public void onProcess(long current, long total);
	public void onFailure(QiniuException ex);
}
