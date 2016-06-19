package com.petsay.component.media;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.widget.Toast;

import com.petsay.utile.PetsayLog;

public class MediaPlayManager {
	private static MediaPlayManager _instance;
	private MediaPlayer mPlayer;
	private CommentRecordPlayerView mRecordPlayerView;
	private OnPlayerStateChangedListener mChangedListener;
	public static MediaPlayManager getSingleton(){
		if (null==_instance) {
			_instance=new MediaPlayManager();
		}
		return _instance;
	}

	public void play(Context context,String audioPath){
		File file = new File(audioPath);
		if (file.exists()) {
			try {
                stopAudio();
                mPlayer = MediaPlayer.create(context, Uri.fromFile(file));
				mPlayer.start();
				if (null!=mChangedListener) {
					mChangedListener.onPlayStart();
				}
			} catch (Exception e) {
                PetsayLog.e("播放文件失败："+e.getMessage());
			}
		} else {
			Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void setOnPlayerStateChangedListener(OnPlayerStateChangedListener changedListener){
		this.mChangedListener=changedListener;
	}

	public void play(Context context,String audioPath,CommentRecordPlayerView view){
		if(mPlayer != null && mRecordPlayerView != null && view == mRecordPlayerView){
			if(mPlayer.isPlaying()){
				pause();
				
				return;
			}else{
				mPlayer.start();
				
			}
		}else {
			stopAudio();
			play(context, audioPath);
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					if(mRecordPlayerView != null){
						mRecordPlayerView.stopAnimation();
					}
				}
			});
		}
		
		this.mRecordPlayerView = view;
		if(mRecordPlayerView != null){
			mRecordPlayerView.playAnimation();
		}
	}
	
	public void pause(){
		if(mPlayer != null ){
            if(mPlayer.isPlaying()){
			    mPlayer.pause();
			    if (null!=mChangedListener) {
					mChangedListener.onPlayPause();
				}
            }
            else
                resumePlay();
		}
		if(mRecordPlayerView != null){
			mRecordPlayerView.stopAnimation();
		}
	}

    /**
     * 恢复播放
     */
    public void resumePlay(){
        if(mPlayer != null && !mPlayer.isPlaying()){
            mPlayer.start();
            if (null!=mChangedListener) {
				mChangedListener.onPlayStart();
			}
        }
    }

	public void playByAssets(Context context,String audioPath){
		if (null==mPlayer) {
			mPlayer=new MediaPlayer();
		}

		try {
			AssetManager am = context.getResources().getAssets();
			AssetFileDescriptor fileDescriptor = am.openFd(audioPath);
			if (mPlayer.isPlaying()) {
				mPlayer.stop();
				mPlayer.release();
			}
			mPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),fileDescriptor.getLength());
			mPlayer.prepare();
			mPlayer.start();
			if (null!=mChangedListener) {
				mChangedListener.onPlayStart();
			}
		} catch (IOException e) {
			// PetsayLog.e(LOG_TAG, "播放失败");
		}
	}


	public void stopAudio(){
		if (null!=mPlayer) {
			mPlayer.stop();
			if (null!=mChangedListener) {
				mChangedListener.onPlayStop();
			}
			mPlayer.release();
			mPlayer=null;
		}
		if(mRecordPlayerView != null){
			mRecordPlayerView.stopAnimation();
		}
	}
	
	public interface OnPlayerStateChangedListener{
		void onPlayStop();
		void onPlayPause();
		void onPlayStart();
	}
}
