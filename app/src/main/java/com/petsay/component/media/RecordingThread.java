package com.petsay.component.media;

import java.util.AbstractQueue;

import com.petsay.utile.PublicMethod;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * @author wangw
 *	录音的线程
 */
public class RecordingThread extends Thread {


	private static int FREQUENCY = 16000;
	private static int CHANNEL = AudioFormat.CHANNEL_IN_MONO;
	private static int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private static int bufferSize = AudioRecord.getMinBufferSize(FREQUENCY, CHANNEL, ENCODING);
	private static final int TIMER_INTERVAL = 120;
	private AbstractQueue<short[]> mLinked;
	private AudioRecord mRecord;
	//	private int bufferSize;
	private int bSamples = 16;
	private int nChannels = 1;
	private OnVolumeChangeListener mListener;
	private boolean setToStopped = false;

	public RecordingThread(AbstractQueue<short[]> linked){
	
		
		
		this.mLinked = linked;
		//		int framePeriod = FREQUENCY * TIMER_INTERVAL / 1000;
		//		bufferSize = framePeriod * 2 * bSamples * nChannels / 8;
		//		bufferSize = framePeriod * 2 * bSamples * nChannels / 8;
		//检查确保最小缓存不小于缓存
		//		if (bufferSize < AudioRecord.getMinBufferSize(FREQUENCY,
		//				CHANNEL, ENCODING)) {
		//			bufferSize = AudioRecord.getMinBufferSize(FREQUENCY,
		//					CHANNEL, ENCODING);
		//		}
		//		mRecord = new AudioRecord(
		//				MediaRecorder.AudioSource.MIC, FREQUENCY,
		//				CHANNEL, ENCODING, bufferSize);
		mRecord = new AudioRecord(
				MediaRecorder.AudioSource.MIC, FREQUENCY,
				CHANNEL, ENCODING, bufferSize);
	}

	public void stopRecording(){
		this.setToStopped  = true;
	}
	
	public void setOnVolumeChangeListener(OnVolumeChangeListener listener){
		this.mListener = listener;
	}

	@Override
	public void run() {
		if(mLinked == null || mRecord.getState() != AudioRecord.STATE_INITIALIZED)
			return;
		try{
			mRecord.startRecording();
			boolean flag = true;
			short[] buffer = new short[bufferSize];
			while (!setToStopped) {

				int len = mRecord.read(buffer, 0, buffer.length);	
				double sum = 0.0;
				int v = 0;
				for(int i = 0; i < len; i++){
					sum += buffer[i];
					v += buffer[i] * buffer[i];  
				}
				// 去掉全0数据
				if(flag){
					if(sum == 0.0){
						continue;
					}else{
						flag = false;
					}
				}
				//				double dB = 10*Math.log10(v/(double)len);
				//				计算了噪音,对音量进行调整：
				// 平方和除以数据总长度，得到音量大小。可以获取白噪声值，然后对实际采样进行标准化。
				//				value 的 值 控制 为 0 到 100 之间 0为最小 》= 100为最大！！
				int volume = (int) (Math.abs((int)(v /(float)len)/10000) >> 1);
//				PublicMethod.log_d("当前音量值："+volume);
				if(mListener != null){
					mListener.onVolumeChange(volume);
				}

				short[] data = new short[len]; 
				System.arraycopy(buffer, 0, data, 0, len);	

				mLinked.add(data);	
			}
			mRecord.stop();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			mRecord.release();
			mRecord = null;
		}
	}
	
	public interface OnVolumeChangeListener{
		/**声音变化事件*/
		public void onVolumeChange(int volume);
	}

}
