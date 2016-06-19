package com.petsay.component.gifview;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.petsay.component.view.ExProgressBar;

/**
 * @author wangw
 *
 */
public class ImageLoaderListener implements ImageLoadingProgressListener,ImageLoadingListener {

	
	private ExProgressBar mProgressBar;
	private int mDownloadState = -3;
	private ImageLoadingListener mListener;
	private String mDowloadUrl;
	
	public ImageLoaderListener(ExProgressBar bar){
		this.mProgressBar = bar;
	}
	
	public void reset(){
		removeListener();
		mDownloadState = -3;
		mDowloadUrl = "";
	}
	
	public String getDownloadUrl(){
		return mDowloadUrl;
	}
	
	public void removeListener() {
		mListener = null;
	}

	/**
	 * ImageLoader下载状态
	 * -3:未初始化
	 * -2:取消下载
	 * -1:下载失败
	 * 0:下载中
	 * 1:下载完成
	 * @return
	 */
	public int getDownloadState(){
		return mDownloadState;
	}
	
	public void setListener(ImageLoadingListener listener){
		this.mListener = listener;
	}
	
	public ImageLoadingListener getListener(){
		return mListener;
	}
	
	@Override
	public void onLoadingStarted(String imageUri, View view) {
		mDownloadState = 0;
		mDowloadUrl = imageUri;
		if(mListener != null )
			mListener.onLoadingStarted(imageUri, view);
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		mDownloadState = -1;
		hidenProgressBar();
		if(mListener != null)
			mListener.onLoadingFailed(imageUri, view, failReason);
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		mDownloadState = 1;
		hidenProgressBar();
		if(mListener != null){
			mListener.onLoadingComplete(imageUri, view, loadedImage);
		}
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		mDownloadState = -2;
		hidenProgressBar();
		if(mListener != null){
			mListener.onLoadingCancelled(imageUri, view);
		}
	}

	@Override
	public void onProgressUpdate(String imageUri, View view, int current,
			int total) {
		mDownloadState = 0;
		showProgressBar();
//		updateProgressBar((current*100/total/100));
		
	}
	
	public void hidenProgressBar(){
		if(mProgressBar != null){
			mProgressBar.hiden();
		}
	}
	
	public void showProgressBar() {
		if(mProgressBar != null && mProgressBar.getVisibility() == View.GONE){
			mProgressBar.show();
		}
	}
	
	public void updateProgressBar(float progress) {
		if(mProgressBar != null){
			mProgressBar.updateProgress(progress);
		}
	}

}
