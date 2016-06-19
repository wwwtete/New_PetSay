package com.petsay.component.media;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * @author wangw
 *
 */
public class VideoPlayer extends SurfaceView implements Callback {
	
	private MediaPlayer mPlayer;
	private AssetFileDescriptor mDescriptor;
	private VideoPlayerListener mListener;
	
	public VideoPlayer(Context context){
		super(context);
		initViews();
	}
	

	public VideoPlayer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
	}

	private void initViews() {
		getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		getHolder().addCallback(this);
	}
	
	public void setOnListener(VideoPlayerListener listener){
		mListener = listener;
	}
	
	public void play(AssetFileDescriptor descriptor){
		this.mDescriptor = descriptor;
	}
	
	private int mResid;
	public void play(int id){
		this.mResid = id;
	}
	
	protected void initPlayer(){
		stop();
		try {
			mPlayer = new MediaPlayer();//MediaPlayer.create(getContext(), mResid);//
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					if(mListener != null){
						mListener.onCompletion(mp);
					}
				}
			});
			mPlayer.setDataSource(mDescriptor.getFileDescriptor(),mDescriptor.getStartOffset(),mDescriptor.getLength());
			mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.setDisplay(getHolder());
			mPlayer.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					mPlayer.start();
				}
			});
			mPlayer.prepare();
//			mPlayer.start();
//			mPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				mDescriptor.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop(){
		if(mPlayer != null){
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	public void pause(){
		if(mPlayer != null){
			mPlayer.pause();
		}
	}


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(mDescriptor != null){
			initPlayer();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stop();
	}
	
	public interface VideoPlayerListener{
		public void onCompletion(MediaPlayer mp);
	}

}
