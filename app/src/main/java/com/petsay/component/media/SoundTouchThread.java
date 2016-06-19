package com.petsay.component.media;

import java.io.File;
import java.io.FileOutputStream;
import java.util.AbstractQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.example.soundtouchdemo.JNISoundTouch;
import com.example.soundtouchdemo.WaveHeader;
import com.petsay.utile.PublicMethod;

/**
 * @author wangw
 *	SoundTouch音频转换
 */
public class SoundTouchThread extends Thread {

	public static final int CONVERT_SUCCESS = 800;

	private AbstractQueue<short[]> mData;
	private LinkedList<byte[]> mWavDatas;
	private JNISoundTouch mSoundtouch = new JNISoundTouch();
	private File mFile;
	private MediaListener mListener;
	private boolean misOriginal = false;


	public SoundTouchThread(AbstractQueue<short[]> data,File file,MediaListener listener){
		this.mData = data;
		mWavDatas = new LinkedList<byte[]>();
		this.mFile = file;
		mListener = listener;
	}

//	/**
//	 * 保存原始的声音
//	 */
//	public void originalSound(){
//		misOriginal = true;
//		mSoundtouch.setSampleRate(16000);
//		mSoundtouch.setChannels(1);
//		mSoundtouch.setPitchSemiTones(0);
//		mSoundtouch.setRateChange(0);
//		mSoundtouch.setTempoChange(0);
//	}

	/**
	 * 设置变声参数
	 * @param pitchSemiTones
	 * @param rateChange
	 * @param tempoChange
	 */
	public void transcoding(int pitchSemiTones,float rateChange,float tempoChange){
		mSoundtouch.setSampleRate(16000);
		mSoundtouch.setChannels(1);
		mSoundtouch.setPitchSemiTones(pitchSemiTones);
		mSoundtouch.setRateChange(rateChange);
		mSoundtouch.setTempoChange(tempoChange);
	}

	/**
	 * 设置变声参数
	 * @param sampleRate
	 * @param channels
	 * @param pitchSemiTones
	 * @param rateChange
	 * @param tempoChange
	 */
	public void transcoding(int sampleRate,int channels,int pitchSemiTones,float rateChange,float tempoChange){
		mSoundtouch.setSampleRate(sampleRate);
		mSoundtouch.setChannels(channels);
		mSoundtouch.setPitchSemiTones(pitchSemiTones);
		mSoundtouch.setRateChange(rateChange);
		mSoundtouch.setTempoChange(tempoChange);
	}

	@Override
	public void run() {
		if(mData == null)
			return;
		mWavDatas.clear();
		short[] buffer;
		for (short[] data : mData) {
			try{
				if(data != null){
						mSoundtouch.putSamples(data, data.length);
						do {
							buffer = mSoundtouch.receiveSamples();
							byte[] bytes = PublicMethod.shortToByteSmall(buffer);					
							mWavDatas.add(bytes);
						} while (buffer.length > 0);
					}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		saveFile(mWavDatas);
	}

	private void saveFile(LinkedList<byte[]> datas){
		int fileLength = 0;
		for(byte[] bytes: datas){
			fileLength += bytes.length;
		}
		FileOutputStream out = null;
		try {
			WaveHeader header = new WaveHeader(fileLength);
			byte[] headers = header.getHeader();	
			if(mFile.exists())
				mFile.delete();
			// 保存文件
			out = new FileOutputStream(mFile);
			out.write(headers);

			for(byte[] bytes: datas){
				out.write(bytes,0,bytes.length);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try{
				if(out != null){
					out.close();
					out = null;
				}
				mWavDatas.clear();
				mWavDatas = null;
				mSoundtouch = null;
			}catch(Exception e){
			}
		}
		mListener.onTranscodingFinishCallback(mFile);
	}


}
