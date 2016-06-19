package com.example.soundtouchdemo;

/**
 * SoundTouch 本地库方法，
 * 注意：不要改变这个类的路径，否则会引发错误找不到soundtouch本地方法异常
 * @author wangw
 *
 */
public class JNISoundTouch {

	public native void setSampleRate(int sampleRate);
	public native void setChannels(int channel);
	public native void setTempoChange(float newTempo);
	public native void setPitchSemiTones(int newPitch);
	public native void setRateChange(float newRate);
	
	public native void putSamples(short[] samples, int len);
	public native short[] receiveSamples();
	
	static{
		System.loadLibrary("soundtouch");
	}
}
