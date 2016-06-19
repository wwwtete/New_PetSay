package com.petsay.network.net;

import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.ProtocolManager;

import org.json.JSONObject;

/**
 * @author wangw
 * 获取UploadTokenService
 */
public class UploadTokenNet extends BaseNet {

	public void getUploadToken(){
		try {
			JSONObject obj = getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "fs");
			obj.put(ProtocolManager.OPTIONS, "uptoken");
			obj.put("domain", Constants.UPLOADDOMAIN);
//			execute(obj, RequestCode.REQUEST_GETUPLOADTOKEN);
			executeNew(obj, RequestCode.REQUEST_GETUPLOADTOKEN, false,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
