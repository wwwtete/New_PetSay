package com.petsay.component.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Handler;

import com.petsay.utile.PublicMethod;

import java.io.File;
import java.io.FileInputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author wangw
 *	音频播放器
 */
public class ExAudioMediaPlayer {

	private MediaPlayer mPlayer;
	private Context mContext;
	private OnCompletionListener mOnCompletionListener;
	private OnPlayerProgressListener mProListener;
	private Timer mTimer;
	private boolean mIsPause;
	private TimerTask mProTask;
	private boolean mIsStoping;

	public ExAudioMediaPlayer(Context context){
		this.mContext = context;
	}

	public void setOnCompletionListener(OnCompletionListener listener){
		this.mOnCompletionListener = listener;
	}

	public void setOnProgressListener(OnPlayerProgressListener listener){
		this.mProListener = listener;
	}

	/**
	 * 播放本地文件
	 * @param uri
	 */
	public boolean playFile(Uri uri){
		if(uri == null)
			return false;
		try{
			return onInitMediaplayer(MediaPlayer.create(mContext, uri));
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean playFile(File file){
		if(file == null || !file.exists())
			return false;
		try{
			PublicMethod.log_d("当前要播放的文件：" + file.getAbsolutePath());
			MediaPlayer player = new MediaPlayer();
			FileInputStream in = new FileInputStream(file);
			player.setDataSource(in.getFD());
			player.prepare();
			return onInitMediaplayer(player);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean playResId(int resid){
		try {
			return onInitMediaplayer(MediaPlayer.create(mContext, resid));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean onInitMediaplayer(MediaPlayer player){
		if(player == null)
			return false;
		try {
			stopPlay();
			mPlayer = player;//new MediaPlayer();
			mPlayer.setOnErrorListener(mErrorListener);
			mPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					if(mOnCompletionListener != null)
						mOnCompletionListener.onCompletion(mp);	
					mIsPause = true;

				}
			});
			//			mPlayer.setDataSource(mContext, uri);
			//			mPlayer.prepare();
			mIsStoping = mIsPause = false;
			mProTask = getTimerTask();
			mPlayer.start();
			
			
			mTimer = new Timer();
			mTimer.schedule(mProTask, 0,500);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 暂停播放
	 */
	public void pausePlay(){
		if(mPlayer != null && mPlayer.isPlaying()){
			mIsPause = true;
			mPlayer.pause();
		}
	}

	/**
	 * 恢复播放
	 */
	public void resumePlay(){
		if(mPlayer != null && !mPlayer.isPlaying()){
			mIsPause = false;
			mPlayer.start();
		}
	}

	public void setLooping(boolean looping){
		if(mPlayer != null)
			mPlayer.setLooping(looping);
	}
	
	public boolean isLooping(){
		if(mPlayer != null)
			return mPlayer.isLooping();
		return false;
	}

	public void seekTo(int msec){
		if(mPlayer != null)
			mPlayer.seekTo(msec);
	}

	/**
	 * 停止播放并释放播放器资源
	 */
	public void stopPlay(){
		if(mPlayer != null){
			mPlayer.stop();
			//TODO 释放资源
			mPlayer.release();
			mPlayer = null;
		}

		if(mTimer != null){
			mTimer.cancel();
			mTimer = null;
			mProTask = null;
		}
		mIsStoping = true;
	}

	public boolean isPlaying(){
		if(mPlayer != null)
			return mPlayer.isPlaying();
		else
			return false;
	}
	
	public boolean isStoping(){
		return mIsStoping;
	}

	/**
	 * 获取播放总时长
	 * @return
	 */
	public int getDuration(){
		if(mPlayer != null){
			return mPlayer.getDuration();
		}
		return 0;
	}

	/**
	 * 获取当前播放时间
	 * @return
	 */
	public int getCurrentPosition(){
		if(mPlayer != null){
			return mPlayer.getCurrentPosition();
		}
		return 0;
	}

	private OnErrorListener mErrorListener = new OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			return false;
		}
	};

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(mProListener != null)
				mProListener.onProgressListener(getCurrentPosition());
		};
	};

	protected TimerTask getTimerTask(){
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if(!mIsPause)
					mHandler.sendEmptyMessage(100);
			}
		};
		return task;
	}

	public interface OnPlayerProgressListener{
		public void onProgressListener(int currentPosition);
	}
}
