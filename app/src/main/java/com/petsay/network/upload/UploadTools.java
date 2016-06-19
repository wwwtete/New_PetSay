package com.petsay.network.upload;

import com.petsay.constants.Constants;
import com.petsay.utile.PublicMethod;
import com.qiniu_petsay.auth.JSONObjectRet;
import com.qiniu_petsay.io.IO;
import com.qiniu_petsay.io.PutExtra;
import com.qiniu_petsay.utils.InputStreamAt;
import com.qiniu_petsay.utils.QiniuException;

import org.json.JSONObject;

import java.io.File;

/**
 * @author wangw
 * 这是一个特殊的Service，没有继承BaseService，他是调用七牛提供的SDK，(非单例模式)
 * 注意： 调用上传方法必须是在主线程中调用，否则会抛出ExceptionInInitializerError
 */
public class UploadTools{
	
	private UploadServiceListener mListener;

	public UploadTools(){
		
	}
	
	/**
	 * 设置回调函数
	 */
	public void setUploadListener(UploadServiceListener listener){
		mListener = listener;
	}
	
	/**
	 * 向七牛服务器上传文件
	 */
	public void doUpload(byte[] data,final String path,final Object tag){
		IO.put(Constants.UPLOAD_TOKEN, path, new InputStreamAt(data), new PutExtra(), new JSONObjectRet() {
			
			@Override
			public void onProcess(long current, long total) {
				if(mListener != null)
					mListener.onProcess(current, total, tag);
			}
			
			@Override
			public void onFailure(QiniuException ex) {
				PublicMethod.log_d("-----------" + tag + ": 上传失败：失败原因：" + ex.getMessage());
				if(mListener != null)
					mListener.onUploadFinishCallback(false,"","", tag);
			}
			
			@Override
			public void onSuccess(JSONObject obj) {
				if(mListener != null){
					String hash = obj.optString("hash", "");
					mListener.onUploadFinishCallback(true,path,hash, tag);
				}
			}
		});
	}
	
	/**
	 * 向七牛服务器上传文件
	 * @param file
	 */
	public void doUpload(File file,final String path,final Object tag){
		IO.putFile(Constants.UPLOAD_TOKEN, path, file, new PutExtra(), new JSONObjectRet() {
			
			@Override
			public void onProcess(long current, long total) {
				if(mListener != null)
					mListener.onProcess(current, total, tag);
			}
			
			@Override
			public void onFailure(QiniuException ex) {
				PublicMethod.log_d("-----------" + tag + ": 上传失败：失败原因：" + ex.getMessage());
				if(mListener != null)
					mListener.onUploadFinishCallback(false, "","", tag);
			}
			
			@Override
			public void onSuccess(JSONObject obj) {
				if(mListener != null){
					String hash = obj.optString("hash", "");
					mListener.onUploadFinishCallback(true, path,hash, tag);
				}
			}
		});
	}
	
	public interface UploadServiceListener{
		
		public void onUploadFinishCallback(boolean isSuccess,String path,String hash,Object tag);
		
		public void onProcess(long current, long total,Object tag);
	}
	
	
	
}
