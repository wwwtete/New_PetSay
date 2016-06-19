package com.petsay.utile.task;

import java.io.File;

import com.petsay.utile.FileUtile;

import android.os.AsyncTask;

/**
 * @author wangw
 * 删除本地缓存Task
 */
public class ClearCacheTask extends AsyncTask<String, Void, Boolean> {

	private ClearCacheTaskListener mListener;
	
	public void setOnListener(ClearCacheTaskListener listener){
		this.mListener = listener;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if(params.length > 0){
			for (int i = 0; i < params.length; i++) {
				String path = params[i];
				File file = new File(path);
				if(file.exists()){
					FileUtile.deleteDir(file);
				}
				file = null;
			}
		}
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(mListener != null){
			mListener.onClearCacheCallback(result);
		}
		super.onPostExecute(result);
	}

	public interface ClearCacheTaskListener{
		public void onClearCacheCallback(boolean flag);
	}
	
}
