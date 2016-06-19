package com.petsay.utile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.petsay.cache.SharePreferenceCache;
import com.petsay.constants.Constants;

/**
 * @author wangw
 * 产生设备唯一标识符
 */
public class DeviceIdentityFactory {

	private static final String INDETITY = "indentity";

	/**
	 * 构建一个设备唯一ID，如果已经存在则直接读取
	 * @param context
	 * @return
	 */
	public static String builder(Context context){
		String id = "";
		//1.先从SharePreference中读取id
		id = SharePreferenceCache.getSingleton(context).getIdentityID();
		if(!TextUtils.isEmpty(id))
			return id;
		//2.如果为空，则查看SD卡中是否存在
		String path = FileUtile.getPath(context, Constants.FilePath);
		File file = new File(path,INDETITY);
		try {
			if(file.exists()){
				id = readInstallationFile(file);
				if(!TextUtils.isEmpty(id))
					return id;
			}
		} catch (Exception e) {
		}
		//3.如果经过上面两步都没有获取到，就重新生成一个身份ID
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if(!TextUtils.isEmpty(telephonyManager.getDeviceId())){
				id = telephonyManager.getDeviceId();
			}else {
				id = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
			}
		} catch (Exception e) {
		}
		//4.如果没有获取到唯一标示，则使用UUID按照当前的时间戳生成一个新的UUID
		if(TextUtils.isEmpty(id) || "9774d56d682e549c".equals(id)){
			id = UUID.randomUUID().toString();
		}
		PublicMethod.log_d("新产生UUID = " + id);
		//5.存储这个新产生的UUID
		SharePreferenceCache.getSingleton(context).saveIdentityID(id);
		writeInstallationFile(file, id);
		return id;
	}

	private static String readInstallationFile(File installation){
		RandomAccessFile f = null;
		try {
			f = new RandomAccessFile(installation, "r");
			byte[] bytes = new byte[(int) f.length()];
			f.readFully(bytes);
			return new String(bytes);
		} catch (Exception e) {
			try {
				f.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	private static void writeInstallationFile(File installation,String id) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(installation);
			out.write(id.getBytes());
		} catch (Exception e) {
			try {
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



}
