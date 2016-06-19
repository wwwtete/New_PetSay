package com.petsay.network.net;

import com.google.inject.Singleton;
import com.petsay.constants.Constants;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.network.base.PetSayError;
import com.petsay.utile.ProtocolManager;

import org.json.JSONObject;

/**
 * @author wangw
 * 饰品信息接口
 */
@Singleton
public class DecorationNet extends BaseNet {

//    private static DecorationNet instance;
//
//    public static DecorationNet getInstance(){
//        if(instance == null)
//            instance = new DecorationNet();
//        return instance;
//    }

	public void getDecorationData(String petID){
		try{
			JSONObject jsonObject = getDefaultParams();
			jsonObject.put(ProtocolManager.COMMAND, "decoration");
			jsonObject.put(ProtocolManager.OPTIONS, "tree");
			jsonObject.put("version", Constants.DECORATION_VERSION);
            jsonObject.put("petId",petID);
			execute(jsonObject, RequestCode.REQUEST_GETDECORATIONDATA);
		}catch(Exception e){
			onErrorCallback(new PetSayError(e, PetSayError.CODE_ERROR), RequestCode.REQUEST_GETDECORATIONDATA);
		}
	}

}
