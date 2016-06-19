package com.petsay.utile;

import com.google.inject.Singleton;
import com.petsay.application.MobileInfoManager;
import com.petsay.application.PetSayApplication;
import com.petsay.constants.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wangw 协议管理类<br>
 *         作用是处理通讯协议发送，接收的组装<br>
 *         通讯协议使用json格式<br>
 *         数据在联网层做了加密<br>
 *         singleton 模式
 */
@Singleton
public class ProtocolManager {
	public static String IMEI = "imei";
	public static String IMSI = "imsi";
	public static String MACADDR = "macAddr";
	public static String VERSION = "version";
	public static String SIM = "sim";
	public static String MACHINE_ID = "machineid";
	public static String PLATFORM = "platform";
	public static String INFORMATION_TYPE = "type";
	public static String CHANNEL = "channel";
	public static String MODEL = "model";
	public static String IPADDR = "ipAddr";

	public static String COMMAND = "command";
	public static String OPTIONS = "options";
	public static String ID = "id";
	
	public static int SUCCESS_CODE = 200;
	
	
	private static ProtocolManager protocolManager = null;

	public synchronized static ProtocolManager getInstance() {
		if (protocolManager == null) {
			protocolManager = new ProtocolManager();
		}
		return protocolManager;
	}

	/**
	 * 获取除userno,sessionid,phonenum公共参数(imsi,imei,softwareversion,machineid,
	 * coopid,smscenter,platform)
	 * 
	 * @return 含有公共参数的jsonobject
	 */
	public static JSONObject getDefaultJsonProtocol() {
		JSONObject defaultJsonObject = new JSONObject();
		try {
			if (Constants.IMEI == null||Constants.IMEI.trim().equals("")) {
//				defaultJsonObject.put(IMEI, "");
				MobileInfoManager.getSingleton().getMachineInfo(PetSayApplication.getInstance());
				
			} else {
				defaultJsonObject.put(IMEI, Constants.IMEI);
			}
			
			if (Constants.IMSI == null) {
				defaultJsonObject.put(IMSI, "");
			} else {
				defaultJsonObject.put(IMSI, Constants.IMSI);
			}
			if (Constants.MAC == null) {
				defaultJsonObject.put(MACADDR, "");
			} else {
				defaultJsonObject.put(MACADDR, Constants.MAC);
			}
			defaultJsonObject.put(IPADDR, Constants.IPADDR);
			defaultJsonObject.put(MODEL, Constants.MODEL);
			defaultJsonObject.put(CHANNEL, Constants.CHANNEL);
			defaultJsonObject.put(VERSION, Constants.VERSION);
			defaultJsonObject.put(MACHINE_ID, Constants.MODEL);
			defaultJsonObject.put(SIM, Constants.PHONE_SIM);
			defaultJsonObject.put(PLATFORM, Constants.PLATFORM);
			return defaultJsonObject;
		} catch (JSONException e) {
			PublicMethod.log_d("公共参数出错！");
		}
		return defaultJsonObject;
	}
}
