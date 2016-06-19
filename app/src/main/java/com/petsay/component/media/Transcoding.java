package com.petsay.component.media;

import java.io.File;
import java.util.AbstractQueue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.petsay.constants.Constants;
import com.petsay.utile.FileUtile;

/**
 * @author wangw
 *	声音转换包装类
 */
public class Transcoding {

	/**自定义一个线程池管理转换声音线程*/
	//	protected ExecutorService mSoundTouchThreadPool = Executors.newFixedThreadPool(1);
	private MediaListener mListener;
	private Map<String, File> mFileMap;
	private String mFilePath;
	private Context mContext;
	private String mId;

	public Transcoding(String id,String filePath,Context context,MediaListener listener){
		this.mId = id;
		this.mContext = context;
		this.mFilePath = FileUtile.getPath(context, filePath+File.separator+id+File.separator);
		this.mListener = listener;
		mFileMap = new HashMap<String, File>();
	}

	/**
	 * 转换原声
	 * @param fileName
	 * @param data
	 */
	//	public void originalSound(String fileName,AbstractQueue<short[]> data){
	//		File file = getFileByTime(fileName);
	//		SoundTouchThread thread = getThread(data,file);
	//		thread.originalSound();
	//		thread.start();
	//	}

	/**
	 * 设置变声参数
	 * @param pitchSemiTones
	 * @param rateChange
	 * @param tempoChange
	 */
	public void transcoding(String fileName,AbstractQueue<short[]> data,int pitchSemiTones,float rateChange,float tempoChange){
		File file = getFileByTime(mId + fileName);
		if(file.exists()){
			mediaListener.onTranscodingFinishCallback(file);
		}else {
			SoundTouchThread thread = getThread(data,file);
			thread.transcoding(pitchSemiTones, rateChange, tempoChange);
			thread.start();
		}
	}

	/**
	 * 清空列表
	 */
	public void clearFiles(){
		Iterator<Entry<String, File>> iterator = mFileMap.entrySet().iterator();
		while(iterator.hasNext()){
			File file = iterator.next().getValue();
			if(file.exists())
				file.delete();
		}
	}

	protected SoundTouchThread getThread(AbstractQueue<short[]> data,File file){
		return new SoundTouchThread(data, file, mediaListener);
	}

	protected File getFileByTime(String name){
		File file= null;
		if(mFileMap.containsKey(name))
			return mFileMap.get(name);
		else{
			file = new File(mFilePath,name+".wav");
			mFileMap.put(name, file);
		}
		return file;
	}

	private MediaListener mediaListener = new MediaListener() {

		@Override
		public void onTranscodingFinishCallback(File file) {
			mListener.onTranscodingFinishCallback(file);
		}
	};



}
