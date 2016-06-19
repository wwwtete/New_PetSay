package com.petsay.application;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.petsay.constants.Constants;
import com.petsay.utile.DeviceIdentityFactory;
import com.petsay.utile.PublicMethod;
import com.umeng.analytics.MobclickAgent;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 设备信息管理
 * @author G
 *
 */
public class MobileInfoManager {

	private static MobileInfoManager _instance;
	public static MobileInfoManager getSingleton() {
		if (null == _instance) {
			_instance = new MobileInfoManager();
		}
		return _instance;
	}
	
	/**
	 * 获取手机设备信息
	 */
	public void getMachineInfo(Context context) {
		try {  
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			Constants.MAC = info.getMacAddress();
			Constants.IMSI = telephonyManager.getSubscriberId();
			Constants.MODEL = Build.MODEL;
			String moblie = telephonyManager.getLine1Number();
			if (moblie != null && moblie.equals("null")) {
				Constants.PHONE_SIM = moblie;
			}
			//获取所有本地可用网络接口信息，然后返回枚举中的元素  
			for (Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces(); enumeration.hasMoreElements();) {  
				//返回枚举本地网络接口的下一个元素  
				NetworkInterface networkInterface = enumeration.nextElement();  
				//获取IP地址信息，然后返回枚举中的元素  
				for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
					//返回枚举集合中的下一个IP地址信息  
					InetAddress inetAddress = enumIpAddr.nextElement();  
					//如果该地址不为回环地址  
					if (!inetAddress.isLoopbackAddress()) {  
						//显示出主机IP地址  
						Constants.IPADDR = inetAddress.getHostAddress().toString();
					}  
				}  
			}  
		} catch (SocketException ex) {  
			MobclickAgent.onEvent(context, "getMachineInfo");
			PublicMethod.log_d("[getMachineInfo]异常:" + ex);
		}
		Constants.IMEI = DeviceIdentityFactory.builder(context);
        getAppInfo(context);
		
	}
	
	/**
	 * 获取app信息
	 * @param context
	 */
	public void getAppInfo(Context context){
		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			Constants.VERSION=info.versionName;  
			Constants.VERSION_CODE=info.versionCode;
			Constants.PackageName=info.packageName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}  
	}
}
