package com.petsay.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.petsay.R;
import com.petsay.application.UserManager;
import com.petsay.vo.user.UserInfo;

public class SharePreferenceCache {
	private SharedPreferences mSharedPreferences;

    private static SharePreferenceCache _instance;
	private Context mContext;
	private static final String mPreference="config_data";
	public static SharePreferenceCache getSingleton(Context context){
		if (null==_instance) {
			_instance=new SharePreferenceCache(context);
		}
		return _instance;
	}
	
	private SharePreferenceCache(Context context){
		mContext=context;
		mSharedPreferences=mContext.getSharedPreferences(mPreference, Context.MODE_PRIVATE);
	}
	
	public void setUserStatusPreference(UserInfo userInfo){
		
	}

    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }
	/**
	 * 是否第一次进入主页 true要显示新手引导
	 * @return
	 */
	public boolean isShowMainpageGuide(){
		return mSharedPreferences.getBoolean("isShowMainpageGuide2", true);
	}
	/**
	 * 设置首页新手引导状态
	 * @param value 
	 */
	public void setShowMainpageGuide(boolean value){
		SharedPreferences.Editor editor=mSharedPreferences.edit();
		editor.putBoolean("isShowMainpageGuide2", value);
		editor.commit();
	}
	
	/**
	 * 检查是否首为新功能
	 * @param key 
	 * @return
	 */
	public boolean checkNewFunction(String key){
		return mSharedPreferences.getBoolean(key, true);
	}
	
	/**
	 * 设置新功能状态
	 * @param key
	 */
	public void setFunctionUsedState(String key){
		mSharedPreferences.edit()
		.putBoolean(key, false)
		.commit();
	}
	
	/**
	 * 获取设备唯一ID
	 * @return
	 */
	public String getIdentityID(){
		return mSharedPreferences.getString("identityid", "");
	}
	
	/**
	 * 存储设备唯一ID
	 * @param id
	 */
	public void saveIdentityID(String id){
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString("identityid", id);
		editor.commit();
	}
	
	/**
	 * 获取已读消息数
	 */
	public int getReadMsgCount(){
		return mSharedPreferences.getInt(UserManager.getSingleton().getActivePetId()+"readMsgCount", 0);
	}
	
	/**
	 * 存储已读消息数
	 * @param count
	 */
	public void setReadMsgCount(int count){
		SharedPreferences.Editor editor=mSharedPreferences.edit();
		editor.putInt(UserManager.getSingleton().getActivePetId()+"readMsgCount", count);
		editor.commit();
	}
	
	/**
	 * 存储已读评论转发消息数
	 * @param count
	 */
	public void setLocal_C_RMsgCount(int count){
		SharedPreferences.Editor editor=mSharedPreferences.edit();
		editor.putInt(UserManager.getSingleton().getActivePetId()+"local_C_RMsgCount", count);
		editor.commit();
	}
	/**
	 * 获取已读评论转发消息数
	 * @return
	 */
	public int getLocal_C_RMsgCount(){
		return mSharedPreferences.getInt(UserManager.getSingleton().getActivePetId()+"local_C_RMsgCount", 0);
	}
	
	/**
	 * 存储已读踩消息数
	 * @param count
	 */
	public void setLocal_FMsgCount(int count){
		SharedPreferences.Editor editor=mSharedPreferences.edit();
		editor.putInt(UserManager.getSingleton().getActivePetId()+"local_FMsgCount", count);
		editor.commit();
	}
	/**
	 * 获取已读踩消息数
	 * @return
	 */
	public int getLocal_FMsgCount(){
		return mSharedPreferences.getInt(UserManager.getSingleton().getActivePetId()+"local_FMsgCount", 0);
	}
	
	/**
	 * 存储已读关注消息数
	 * @param count
	 */
	public void setLocal_FansMsgCount(int count){
		SharedPreferences.Editor editor=mSharedPreferences.edit();
		editor.putInt(UserManager.getSingleton().getActivePetId()+"local_fansMsgCount", count);
		editor.commit();
	}
	/**
	 * 获取已读关注消息数
	 * @return
	 */
	public int getLocal_FansMsgCount(){
		return mSharedPreferences.getInt(UserManager.getSingleton().getActivePetId()+"local_fansMsgCount", 0);
	}
	
	/**
	 * 存储已读公告消息数
	 * @param count
	 */
	public void setLocal_announcement_MsgCount(int count){
		SharedPreferences.Editor editor=mSharedPreferences.edit();
		editor.putInt("local_announcementMsgCount", count);
		editor.commit();
	}
	/**
	 * 获取已读公告消息数
	 * @return
	 */
	public int getLocal_announcementMsgCount(){
		return mSharedPreferences.getInt("local_announcementMsgCount", 0);
	}
	
	/**
	 * 存储是否正在运行app
	 * @param count
	 */
	public void setIsRunningApp(boolean is){
		SharedPreferences.Editor editor=mSharedPreferences.edit();
		editor.putBoolean("IsRunningApp", is);
		editor.commit();
	}
	/**
	 * 获取是否正在运行app
	 * @return
	 */
	public boolean isRunningApp(){
		boolean result=mSharedPreferences.getBoolean("IsRunningApp", false);
		return result;
	}
	
	/**
	 * @param skinName
	 */
	public void setSkin(String skinName){
		mSharedPreferences.edit()
		.putString("skin", skinName)
		.commit();
	}

    @Deprecated
	public String getSkin(){
		return mSharedPreferences.getString("skin", mContext.getString(R.string.skin_violet));
//		if(TextUtils.isEmpty(locaSkin) && Constants.CHANNEL.equals(SkinManager.CHANNEL_360)){
//			return mContext.getString(R.string.skin_360);
//		}else if(TextUtils.isEmpty(locaSkin))  {
//			return mContext.getString(R.string.skin_blue);
//		}
//		return locaSkin;
	}
	
	/**
	 * 缓存广场布局更新时间
	 * @param time
	 */
	public void setSquareLayoutUpdateTime(long time){
		mSharedPreferences.edit().putLong("square_updatetime", time).commit();
	}
	
	/**
	 * 获取广场布局更新时间
	 * @return
	 */
	public long getSquareLayoutUpdateTime(){
		return mSharedPreferences.getLong("square_updatetime", 0);
	}
	
	/*
	 * 获取播放模式
	 * @return 0：禁止自动播放 1：wifi自动播放，2：自动播放
	 */
	public int getPlayMode(){
		return mSharedPreferences.getInt("playmode", -1);
	}
	
	/**
	 * 设置播放模式
	 * @param mode 0：禁止自动播放 1：wifi自动播放，2：自动播放
	 */
	public void setPlaymode(int mode){
		mSharedPreferences.edit()
		.putInt("playmode", mode)
		.commit();
	}

    public void saveValue(String key,int value){
        mSharedPreferences.edit()
                .putInt(key, value)
                .commit();
    }

    public int getValue(String key){
        return mSharedPreferences.getInt(key,0);
    }

    public void saveBooleanValue(String key,boolean value){
        mSharedPreferences.edit()
                .putBoolean(key,value)
                .commit();
    }

    public boolean getBooleanValue(String key,boolean defaultValue){
        return mSharedPreferences.getBoolean(key,defaultValue);
    }


    public void setBooleanValue(String key,boolean value){
        mSharedPreferences.edit().putBoolean(key,value).commit();
    }
    
    /**
     * 图片自动保存到本地
     * @param mode
     */
    public void setIsAutoSavePicMode(boolean mode){
		mSharedPreferences.edit()
		.putBoolean("isSavePic", mode)
		.commit();
	}
    
    public boolean getIsAutoSavePicMode(){
    	boolean result=mSharedPreferences.getBoolean("isSavePic", true);
		return result;
    }
    /**
     * 拍照自动保存原图
     * @param mode
     */
    public void setIsAutoSaveCameraMode(boolean mode){
		mSharedPreferences.edit()
		.putBoolean("isSaveCamera", mode)
		.commit();
	}
    public boolean getIsAutoSaveCameraMode(){
    	boolean result=mSharedPreferences.getBoolean("isSaveCamera", false);
		return result;
    }
    
    /**
     * 是否第一次访问奖品列表
     */
    public void setFirstVisitAward(){
    	mSharedPreferences.edit()
		.putBoolean("firstVisitAward", false)
		.commit();
    }
    public boolean getFirstVisitAward(){
    	boolean result=mSharedPreferences.getBoolean("firstVisitAward", true);
		return result;
    }
	
}
