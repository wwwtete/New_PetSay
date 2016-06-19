package com.petsay.component.media;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangw
 * 适合播放短音频的播放器
 */
public class ExSoundPool implements OnLoadCompleteListener {

	private SoundPool mSoundPool;
	private Map<Integer, Integer> mMap;
	private Context mContext;
	private int mCurrSoundID = -1;
	private boolean mLooping;
	public ExSoundPool(Context context){
		mContext = context;
		mLooping = true;
		mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 0);
		mSoundPool.setOnLoadCompleteListener(this);
		mMap = new HashMap<Integer, Integer>();
	}
	
	protected int load(int resId){
		return mSoundPool.load(mContext, resId, 1);
	}
	
	public void play(int resId){
		stop();
		int id;
//		if(mMap.containsKey(resId) && mMap.get(key)){
//			id = mMap.get(resId);
//			onPlay(id);
//		}else {
			id = load(resId);
//			mMap.put(id, resId);
//		}
	}
	
	public void setLoop(boolean flag){
		mLooping = flag;
	}
	
	protected void onPlay(int soundID){
		mCurrSoundID = soundID;
		mSoundPool.play(soundID, 1, 1, 1, mLooping ? -1 : 1, 1);
		mSoundPool.setLoop(soundID, -1);
	}
	
	public void stop(){
		try {
			if(mCurrSoundID !=  -1){
				mSoundPool.unload(mCurrSoundID);
				mSoundPool.stop(mCurrSoundID);
//				mMap.remove(mSoundPool);
//				mMap.remove()
			}
//			mCurrSoundID = -1;
		} catch (Exception e) {
		}
	}
	
	public void rerelease(){
		try {
			stop();
			mSoundPool.release();
			mSoundPool.setOnLoadCompleteListener(null);
			mSoundPool = null;
		} catch (Exception e) {
		}
	}

	@Override
	public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
		onPlay(sampleId);
	}
	
	
}
