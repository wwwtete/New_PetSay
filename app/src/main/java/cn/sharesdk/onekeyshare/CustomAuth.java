package cn.sharesdk.onekeyshare;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

public class CustomAuth {

//	public static boolean getAuthStatus(Context context,String platform){
//		Platform qzone = ShareSDK.getPlatform(context, platform);
//		String accessToken = qzone.getDb().getToken();
//		return false;
//		
//	}
	
	/**
	 * 手动授权
	 * @param platformActionListener
	 */
	public static synchronized void  addAuth(PlatformActionListener platformActionListener,String name){
		Platform platform = ShareSDK.getPlatform(name);
//        facebook.SSOSetting(true); // true表示不使用SSO方式授权
		platform.setPlatformActionListener(platformActionListener);
		platform.authorize();
	}
}
