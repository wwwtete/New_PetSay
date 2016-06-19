package com.petsay.vo.petalk;

import com.petsay.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw
 * 变声音效
 */
public class VoiceEffect {
	public String title;
	public int imgSource;
	public String id;
	public int pitch;
	public float rate;
	public float tempo;
	
	public VoiceEffect(String title,int imgSource,String id,int pitch,float rate,float tempo){
		this.title = title;
		this.imgSource = imgSource;
		this.id = id;
		this.pitch = pitch;
		this.rate = rate;
		this.tempo = tempo;
	}
	
	
	public static final List<VoiceEffect> mDatas = new ArrayList<VoiceEffect>();
	public static List<VoiceEffect> getAudioDatas(){
		if(mDatas.size() > 0)
			return mDatas;
		initAudioData("原声", R.drawable.effects_original, "original", 0, 0, 0);
		initAudioData("萝莉", R.drawable.effects_lolita, "lolita", 0, 2, 1);
		initAudioData("少侠", R.drawable.effects_hero, "hero", 0, -2, 0);
		initAudioData("猫咪", R.drawable.effects_cat, "cat", 0, 4, -1);
		initAudioData("型男", R.drawable.effects_man, "man", 0, -4, 0);
		initAudioData("萌音", R.drawable.effects_bud, "bud", 0, 7, -3);
		initAudioData("狗狗", R.drawable.effects_dog, "dog", 0, -6, 0);
		initAudioData("高音", R.drawable.effects_soprano, "soprano", 0,10,-4);
		initAudioData("魔鬼", R.drawable.effects_devil, "devil", 0,-8,0);
		initAudioData("欢快", R.drawable.effects_happy, "happy", 0,-5,30);
		initAudioData("蜗牛", R.drawable.effects_snail, "snail", 0,5,-20);
		return mDatas;
	}
	
	/**
	 * 
	 * @param title
	 * @param imgSource
	 * @param id
	 * @param tempo 速度
	 * @param pitch 音调
	 * @param rate 频率
	 * @return
	 */
	private static VoiceEffect initAudioData(String title,int imgSource,String id,float tempo,int pitch,float rate){
		VoiceEffect data = new VoiceEffect(title, imgSource, id, pitch, rate, tempo);
		mDatas.add(data);
		return data;
	}
	
}
