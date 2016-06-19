package com.petsay.network.download;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.petsay.utile.HttpDownloadUtile;
import com.petsay.utile.HttpDownloadUtile.DownloadProgressCallback;
import com.petsay.utile.PublicMethod;

import java.io.File;

/**
 * @author wangw
 * 下载的异步任务
 */
public class DownloadTask extends AsyncTask<String, Long, File> {

	private Object mWhat;
	private String mSavePath;
	private String mDownLoadURL;
	private DownloadTaskCallback mCallback;
	private DownloadProgressCallback mProgressCallback;
	private Object mTag;
	
	public DownloadTask(Object what,String savePath){
		this.mWhat = what;
		this.mSavePath = savePath;
	}

	public void setCallback(DownloadTaskCallback callback){
		this.mCallback = callback;
	}
	
	public void setDownloadProgressCallback(DownloadProgressCallback callback){
		mProgressCallback = callback;
	}
	
	/**
	 * 设置下载标志用于取消
	 * @param tag
	 */
	public void setTag(Object tag){
		mTag = tag;
	}
	
	public Object getTag(){
		return mTag;
	}
	
	@Override
	protected void onProgressUpdate(Long... values) {
		PublicMethod.log_d("下载进度：所在线程：" + Thread.currentThread().getName() + "总大小：" + values[0] + " 已下载资源:" + values[1]);
		super.onProgressUpdate(values);
		if(mProgressCallback != null){
			mProgressCallback.onUpdateProgress(values[0], values[1]);
		}
	}
	
	@Override
	protected File doInBackground(String... params) {
		mDownLoadURL = params[0];
		if(TextUtils.isEmpty(mDownLoadURL))
			return null;
		
		if(mProgressCallback == null){
			return HttpDownloadUtile.download(mDownLoadURL, mSavePath);
		}else {
			return HttpDownloadUtile.download(mDownLoadURL, mSavePath,new DownloadProgressCallback() {
				@Override
				public void onUpdateProgress(long total, long current) {
					publishProgress(total,current);
				}
			});
		}
		
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		if(mCallback != null){
			mCallback.onCancelCallback(this,mDownLoadURL, mWhat);
		}
	}
	
	@Override
	protected void onPostExecute(File result) {
		if(mCallback != null){
			mCallback.onDownloadFinishCallback(this,result != null, mDownLoadURL, result, mWhat);
		}
		super.onPostExecute(result);
	}
	
	public interface DownloadTaskCallback{
		public void onDownloadFinishCallback(DownloadTask task,boolean isSuccess,String url,File file,Object what);
		
		public void onCancelCallback(DownloadTask task,String url,Object what);
	}



}
