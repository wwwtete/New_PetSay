package com.petsay.utile;

import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;

import com.petsay.constants.Constants;

/**
 * @author wangw
 * 严苛模式工具类
 */ 
public class StrictModeUtile {

	
	public static void initMode(){
		if(Constants.isDebug){
			StrictMode.setThreadPolicy(new ThreadPolicy.Builder()
			.detectAll()
			.detectDiskReads()
			.detectDiskWrites()
			.detectNetwork()
			.detectCustomSlowCalls()
//			.penaltyDialog()
			.penaltyLog()
			.build());
			
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
			.detectAll()
			.penaltyLog()
			.build());
		}
	}
	
}
