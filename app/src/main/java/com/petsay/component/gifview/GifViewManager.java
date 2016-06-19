package com.petsay.component.gifview;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.petsay.application.PetSayApplication;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.component.gifview.AudioGifView.GifViewCallback;
import com.petsay.constants.Constants;
import com.petsay.utile.PublicMethod;

/**
 * @author wangw
 * Gif播放管理器(全局单例)
 */
public class GifViewManager implements ImageLoadingListener,GifViewCallback{

	public static GifViewManager mInstance;
	public synchronized static GifViewManager getInstance(){
		if(mInstance == null){
			mInstance = new GifViewManager();
		}
		return mInstance;
	}

	private AudioGifView mCurrGifView;//当前的GIf动画View
	private ImageLoaderListener mListener;
	public GifViewManager(){
	}

	/**
	 * 开始播放Gif
	 * @param gif
	 */
	public void playGif(AudioGifView gif){
		if(gif == null || (gif != null && gif.getImageLoaderListener() == null))
			return;
		if(gif != mCurrGifView){
			stopGif();
			mCurrGifView = gif;
			if(mCurrGifView != null){
				onStartPlayGif();
			}
		}else {
			mCurrGifView = gif;
			if(gif.getImageLoaderListener().getDownloadState() == 1 && gif.checkReadyedSuccess()){
				gif.pause();
			}else {
				onStartPlayGif();
			}
		}
	}

	protected void onStartPlayGif(){
		mListener = mCurrGifView.getImageLoaderListener();
		mCurrGifView.setPlayBtnVisibility(false);
		if(mCurrGifView != null && mListener != null){
			//1.判断背景图片是否已下载完成，如果未下载完成则添加监听
			switch (mListener.getDownloadState()) {
			case 0:	//下载中
				mListener.setListener(this);
				break;
			case 1:	//下载完成
				onPlayGif();
				break;
			default:
				break;
			}
		}
	}

	protected void onPlayGif(){
        if(mCurrGifView.getData().isAudioModel()) {
            mCurrGifView.showLoading(true);
            mCurrGifView.setGifViewCallback(this);
            mCurrGifView.playGif();
        }else {
            mCurrGifView.showLoading(false);
        }
	}

	/**
	 * 停止播放Gif
	 */
	public void stopGif(){
		if(mCurrGifView != null){
			//删除监听
			if(mListener != null)
				mListener.removeListener();
			mListener = null;
			mCurrGifView.stopGif();
			mCurrGifView.release();
		}
	}

	public void pauseGif(AudioGifView gif){
		if(gif != null)
			playGif(gif);
	}
	
	public void pauseGif(){
		if(mCurrGifView != null){
			mCurrGifView.pause();
		}
	}

    public void pauseGif(boolean pause){
        pauseGif(mCurrGifView,pause);
    }
	
	public void pauseGif(AudioGifView gif,boolean pause){
		if(gif != null){
			gif.pause(pause);
		}
	}

	public void release(){
		stopGif();
	}

	/*
	@Override
	public void onStarted(String uri) {
	}

	@Override
	public void onSuccess(String uri) {
		PublicMethod.log_d("当前的地址："+mCurrGifView.getPicassoCallback().getDownloadUrl());
		PublicMethod.log_d("完成的地址："+uri);
		if(mCurrGifView.getPicassoCallback().getDownloadUrl().equals(uri)){
			mCurrGifView.playGif();
			if(mCallback != null)
				mCallback.removeCallback();
		}
	}
	@Override
	public void onError(String uri) {
	}

	@Override
	public void onCancelled(String uri) {
	}
	 */
	@Override
	public void onLoadingStarted(String imageUri, View view) {
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if(mCurrGifView.getImageLoaderListener().getDownloadUrl().equals(imageUri)){
			onPlayGif();
			if(mListener != null){
				mListener.hidenProgressBar();
				mListener.removeListener();
			}
		}
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
	}

	@Override
	public void onReadyedFinish(boolean audioFlag, boolean decorationFlag) {
		if(mCurrGifView != null && mCurrGifView.checkReadyedSuccess()){
			if(mListener != null){
				mListener.hidenProgressBar();
			}
		}
	}

	/**
	 * 获取是否自动播放
	 * @return
	 */
	public boolean getAllowAutoPlay(){
		if(Constants.PLAY_GIF_MODE == -1){
			Constants.PLAY_GIF_MODE = SharePreferenceCache.getSingleton(PetSayApplication.getInstance()).getPlayMode();
		}
		int mode = Constants.PLAY_GIF_MODE;
		int type = PublicMethod.getAPNType(PetSayApplication.getInstance());
		//如果没有网络则不允许自动播放
		if(type == -1)
			return false;
		//如果是自动播放
		if(mode == 2)
			return true;
		//如果是wifi下，并且播放模式未设置的则默认自动播放
		if(type == 1 && (mode == -1 || mode == 1))
			return true;

		return false;
	}

}
