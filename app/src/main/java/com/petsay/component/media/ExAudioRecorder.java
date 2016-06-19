package com.petsay.component.media;

import java.util.AbstractQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.petsay.component.media.RecordingThread.OnVolumeChangeListener;

/**
 * @author wangw
 * 
 *  扩展录音程序
 *
 */
public class ExAudioRecorder {

	private RecordingThread mRecording;
	private AbstractQueue<short[]> mSourceData;
	private OnVolumeChangeListener mChangeListener;
	public ExAudioRecorder(){
		mSourceData = new ConcurrentLinkedQueue<short[]>();
	}


	public void startRecord(){
		mRecording = new RecordingThread(mSourceData);
		if(mChangeListener != null)
			mRecording.setOnVolumeChangeListener(mChangeListener);
		mRecording.start();
	}

	public void setOnVolumeChangeListener(OnVolumeChangeListener listener){
		mChangeListener = listener;
	}

	public AbstractQueue<short[]> stopRecord(){
		if(mRecording != null){
			mRecording.stopRecording();
			mRecording = null;
		}
		return mSourceData;
	}

	public AbstractQueue<short[]> getSourceData(){
		stopRecord();
		return mSourceData;
	}
	
	/**
	 * 清空
	 */
	public void clear(){
		mSourceData.clear();
	}

	/**
	 * 释放资源
	 */
	public void release(){
		clear();
		mSourceData = null;
	}



}
