package com.petsay.application;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.petsay.R;
import com.petsay.cache.DataFileCache;
import com.petsay.chat.ChatMsgManager;
import com.petsay.constants.Constants;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.vo.decoration.DecorationDataManager;
import com.petsay.vo.user.UserInfo;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;

/**
 * @author wangw
 *
 */
public class PetSayApplication extends Application  {

	private static PetSayApplication instance;
	public List<String> platformNames=new ArrayList<String>();
	
	
	
	public static PetSayApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		ImageLoaderHelp.initImageLoader(this);
		instance=PetSayApplication.this;
		//获取配置文件中的渠道号并设置
		Constants.CHANNEL = getApplicationContext().getString(R.string.channel);
		MobclickAgent.setDebugMode(Constants.isDebug);

        ShareSDK.initSDK(this);
		initTalkManager();
		initPush();

//		StrictModeUtile.initMode();
		super.onCreate();
	}

    private void initTalkManager() {
        ChatMsgManager.getInstance().init(this);
    }


    private void initPush(){
        // 从AndroidManifest.xml的meta-data中读取SDK配置信息
        String packageName = getApplicationContext().getPackageName();
        ApplicationInfo appInfo;
//        try {
//            appInfo = getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
//            if (appInfo.metaData != null) {
//                appid = appInfo.metaData.getString("PUSH_APPID");
//                appsecret = appInfo.metaData.getString("PUSH_APPSECRET");
//                appkey = (appInfo.metaData.get("PUSH_APPKEY") != null) ? appInfo.metaData.get("PUSH_APPKEY").toString() : null;
//            }
//
//        } catch (PackageManager.NameNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        // SDK初始化，第三方程序启动时，都要进行SDK初始化工作
        Log.d("GetuiSdkDemo", "initializing sdk...");
        PushManager.getInstance().initialize(this);
        PushManager.getInstance().turnOnPush(this);
    }


	/**
     * 程序退出时
     */
	@Override
	public void onTerminate() {
		super.onTerminate();
	}
	
	
	
	
	
}
