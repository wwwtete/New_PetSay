package com.petsay.network.net;

import org.json.JSONObject;

import android.R.integer;
import android.os.Message;

import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.InternetUtils;
import com.petsay.utile.ProtocolManager;
import com.petsay.utile.json.JsonUtils;
import com.petsay.vo.ResponseBean;

/**
 * @author wangw
 * app系统操作，如更新版本等
 */
public class SystemNet extends BaseNet {

	/**
	 * 版本更新
	 * @param petId
	 * @param requestCode
	 * @param pagesize
	 */
	public void systemVersion(){
		JSONObject obj =  getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "system");
			obj.put(ProtocolManager.OPTIONS, "version");
//			execute(obj, RequestCode.REQUEST_SYSTEMVERSION);
			executeNew(obj, RequestCode.REQUEST_SYSTEMVERSION, false,"", false, false);
		}catch(Exception e){
			
		}
	}
	
	 public void systemStartUp() {
			JSONObject obj = getDefaultParams();
			try {
				obj.put(ProtocolManager.COMMAND, "system");
				obj.put(ProtocolManager.OPTIONS, "startUp");
				obj.put("domain", Constants.UPLOADDOMAIN);
//				execute(obj, RequestCode.REQUEST_SYSTEMSTARTUP);
				executeNew(obj, RequestCode.REQUEST_SYSTEMSTARTUP, false,"", false, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
}
