package com.petsay.network.net;

import org.json.JSONObject;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.ProtocolManager;
import com.petsay.vo.petalk.PetVo;

/**
 * @author wangw
 *
 */
public class UserNet extends BaseNet {

	
	/**
	 * 登入
	 * @param loginName
	 * @param password
	 */
	public void login(String loginName,String password) {
		try {
			JSONObject object = getDefaultParams();
			object.put(ProtocolManager.COMMAND, "login");
			object.put(ProtocolManager.OPTIONS, "");
			object.put("loginName", loginName);
			object.put("password", password);
			executeByV1(object, RequestCode.REQUEST_LOGIN,false,null,true);
//            executeSecurity(object,RequestCode.REQUEST_LOGIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 登出
	 * @param userId
	 */
	public void logout(String userId) {
		try {
			JSONObject obj = getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "logout");
			obj.put(ProtocolManager.OPTIONS, "");
			obj.put("userId", userId);
			execute(obj, RequestCode.REQUEST_LOGOUT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 检查用户名是否可用
     * @param username
     */
	public void checkUsername(String username) {
		try {
			JSONObject obj = getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "account");
			obj.put(ProtocolManager.OPTIONS, "checkLoginName");
			obj.put("loginName", username);
//			execute(obj, RequestCode.REQUEST_CHECKUSERNAME);
			executeNew(obj, RequestCode.REQUEST_CHECKUSERNAME, false, "", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 注册
	 * @param loginName
	 * @param password
	 * @param phoneNum
	 * @param petInfo
	 * @param platform
	 */
	public void reg(String loginName, String password, String phoneNum,PetVo petVo,String platform){
		try {
			JSONObject obj = getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "register");
			obj.put(ProtocolManager.OPTIONS, "");
			obj.put("loginName", loginName);
			obj.put("password", password);
			obj.put("phoneNum", phoneNum);
			obj.put("headPortrait", petVo.getHeadPortrait());
			obj.put("nickName", petVo.getNickName());
			obj.put("gender", petVo.getGender());
			obj.put("type", petVo.getType());
			obj.put("address", petVo.getAddress());
			obj.put("birthday",petVo.getBirthday());
			obj.put("source", platform);
			executeByV1(obj, RequestCode.REQUEST_REG,false,null,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 获取验证码
	 * @param phoneNum
	 * @param type
	 */
	public void getCaptcha(String phoneNum,String type){
		try {
			JSONObject object = getDefaultParams();
			object.put(ProtocolManager.COMMAND, "account");
			object.put(ProtocolManager.OPTIONS, "generateCaptcha");
			object.put("phoneNum", phoneNum);
			object.put("type", type);
			executeNew(object, RequestCode.REQUEST_GETCAPTCHA, false, "", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证验证码
	 * @param phoneNum
	 * @param type
	 * @param code
	 */
	public void verifyCaptcha(String phoneNum,String type,String code){
		try {
			JSONObject object = getDefaultParams();
			object.put(ProtocolManager.COMMAND, "account");
			object.put(ProtocolManager.OPTIONS, "verifyCaptcha");
			object.put("phoneNum", phoneNum);
			object.put("type", type);
			object.put("code", code);
			executeNew(object, RequestCode.REQUEST_VERIFYCAPTCHA, false, "", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重置密码
	 * @param phoneNum
	 * @param pwd
	 */
	public void restPassword(String phoneNum,String pwd){
		try {
			JSONObject object = getDefaultParams();
			object.put(ProtocolManager.COMMAND, "account");
			object.put(ProtocolManager.OPTIONS, "restPw");
			object.put("loginName", phoneNum);
			object.put("password", pwd);
			execute(object, RequestCode.REQUEST_RESTPASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改密码
	 * @param oldPwd
	 * @param newPwd
	 * @param id
	 */
	public void modifyPassword(String oldPwd,String newPwd,String id){
		try {
			JSONObject object = getDefaultParams();
			object.put(ProtocolManager.COMMAND, "account");
			object.put(ProtocolManager.OPTIONS, "updtPw");
			object.put("id", id);
			object.put("password", oldPwd);
			object.put("newPassword", newPwd);
			execute(object, RequestCode.REQUEST_MODIFYPASSWORD);					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取用户信息
	 * @param userId
	 */
	public void getUserInfo(String userId) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "account");
			obj.put(ProtocolManager.OPTIONS, "userInfo");
			obj.put("id", "id");
			execute(obj, RequestCode.REQUEST_GETUSERINFO);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 添加宠物
	 * @param userId
	 * @param petInfo
	 */
	public void addPet(String userId, PetVo petInfo) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "pet");
			obj.put(ProtocolManager.OPTIONS, "create");
			obj.put("userId", userId);
			obj.put("headPortrait", petInfo.getHeadPortrait());
			obj.put("nickName", petInfo.getNickName());
			obj.put("gender", petInfo.getGender());
			obj.put("type", petInfo.getType());
			obj.put("address", petInfo.getAddress());
			obj.put("birthday", petInfo.getBirthday());
			execute(obj, RequestCode.REQUEST_ADDPET);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改宠物
	 * @param userId 用户id
	 * @param petVo 宠物信息
	 */
	public void editPet(String userId, PetVo petVo) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "pet");
			obj.put(ProtocolManager.OPTIONS, "update");
			obj.put("id", petVo.getId());
			obj.put("userId", userId);
			obj.put("headPortrait", petVo.getHeadPortrait());
			obj.put("nickName", petVo.getNickName());
			obj.put("gender", petVo.getGender());
			obj.put("type", petVo.getType());
			obj.put("address", petVo.getAddress());
			obj.put("birthday", petVo.getBirthday());
			execute(obj, RequestCode.REQUEST_EDITPET);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除宠物
	 * @param id 宠物id
	 */
	public void delPet(String id) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "pet");
			obj.put(ProtocolManager.OPTIONS, "delete");
			obj.put("id", id);
			execute(obj, RequestCode.REQUEST_DELPET);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 激活宠物(切换宠物)
	 * @param id 宠物id
	 */
	public void changePet(String id) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "pet");
			obj.put(ProtocolManager.OPTIONS, "active");
			obj.put("id", id);
			execute(obj, RequestCode.REQUEST_CHANGEPET);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 关注
	 * @param petId 被关注宠物id
	 * @param fansPetId 粉丝宠物id
	 */
	public void focus(String petId, String fansPetId) {
		focus(petId, fansPetId, null);
	}
	
	/**
	 * 列表内关注
	 * @param petId 被关注宠物id
	 * @param fansPetId 粉丝宠物id
	 * @param tag
	 */
	public void focus(String petId, String fansPetId,Object tag) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "petfans");
			obj.put(ProtocolManager.OPTIONS, "focus");
			obj.put("petId", petId);
	        obj.put("fansPetId", fansPetId);
	        execute(obj, RequestCode.REQUEST_FOCUS,tag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消关注
	 * @param id
	 */
	public void cancleFocus(String petId,String fansPetId) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "petfans");
			obj.put(ProtocolManager.OPTIONS, "cancelFocus");
			obj.put("petId", petId);
	        obj.put("fansPetId", fansPetId);
	        execute(obj, RequestCode.REQUEST_CANCLEFOCUS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 我的粉丝
	 * @param petId
	 * @param pageIndex
	 * @param pageSize
	 */
	public void MyFans(String petId, int pageIndex, int pageSize,int arg) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "petfans");
			obj.put(ProtocolManager.OPTIONS, "findFans");
			obj.put("petId", petId);
	        obj.put("pageIndex", pageIndex);
	        obj.put("pageSize", pageSize);
//	        execute(obj, RequestCode.REQUEST_MYFANS);
	        executeNew(obj, RequestCode.REQUEST_MYFANS, false,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 我的关注
	 * @param id
	 * @param petId
	 * @param pageSize
	 */
	public void MyFocus(String petId, int pageIndex, int pageSize,boolean isMore) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "petfans");
			obj.put(ProtocolManager.OPTIONS, "findFocus");
			obj.put("petId", petId);
	        obj.put("pageIndex", pageIndex);
	        obj.put("pageSize", pageSize);
	        execute(obj, RequestCode.REQUEST_MYFOCUS,isMore);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用户消息列表
	 * @param petId
	 * @param type
	 * @param startId
	 * @param pageSize
	 * @param arg
	 */
	public void messageUML(String petId,String type, String startId, int pageSize,boolean isMore) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "message");
			obj.put(ProtocolManager.OPTIONS, "UML");
			obj.put("petId", petId);
	        obj.put("startId", startId);
	        obj.put("pageSize", pageSize);
	        obj.put("type", type);
	        execute(obj, RequestCode.REQUEST_MESSAGEUML,isMore);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	/**
	 * 用户消息数
	 * @param petId
	 */
	public void messageUMC(String petId) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "message");
			obj.put(ProtocolManager.OPTIONS, "UMC");
			obj.put("petId", petId);
			execute(obj, RequestCode.REQUEST_MESSAGEUMC);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}

	/**
	 * 用户留言
	 * @param phoneNum
	 * @param desc
	 */
	public void messageCULM(String phoneNum, String desc) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "message");
			obj.put(ProtocolManager.OPTIONS, "CULM");
			obj.put("phoneNum", phoneNum);
			obj.put("desc", desc);
//			 execute(obj, RequestCode.REQUEST_MESSAGECULM);
			executeNew(obj, RequestCode.REQUEST_MESSAGECULM, false, "", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	 /**
     * 举报
     * @param type
     * ad("1", "广告或垃圾信息"), //
harass("2", "骚扰和人身攻击"), //
eroticism("3", "色情、淫秽内容"), //
radical("4", " 激进时政、敏感内容");
     * @param petId
     * @param petalkId
     * @param reportPetId
     */
	public void messageCUP(int type, String petId, String petalkId,String reportPetId) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "message");
			obj.put(ProtocolManager.OPTIONS, "CUP");
			obj.put("type", type);
			obj.put("petId", petId);
			obj.put("petalkId", petalkId);
			obj.put("reportPetId", reportPetId);
//			 execute(obj, RequestCode.REQUEST_MESSAGECUP);
			 executeNew(obj, RequestCode.REQUEST_MESSAGECUP, false, "", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

    public void petOne(String petId, String currPetId) {
        petOne(petId,currPetId,null);
    }
	
	public void petOne(String petId, String currPetId,Object tag) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "pet");
			obj.put(ProtocolManager.OPTIONS, "one");
			obj.put("petId", petId);
			if (null==currPetId) {
				obj.put("currPetId", petId);
			}else {
				obj.put("currPetId", currPetId);
			}
			executeNew(obj, RequestCode.REQUEST_PETONE, false, tag, false, false);
//			execute(obj, RequestCode.REQUEST_PETONE,false,tag);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	
	 /**
      * 公告数目
      * @param type
      */
	public void announcementCount(String type) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "announcement");
			obj.put(ProtocolManager.OPTIONS, "count");
			obj.put("type", type);
//			 execute(obj, RequestCode.REQUEST_ANNOUNCEMENTCOUNT);
			 executeNew(obj, RequestCode.REQUEST_ANNOUNCEMENTCOUNT, false, "", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	 /**
     * 公告列表
     * @param type
     * @param pageIndex
     * @param pageSize
     */
	public void announcementList(String type, int pageIndex, int pageSize,boolean isMore) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "announcement");
			obj.put(ProtocolManager.OPTIONS, "list");
			obj.put("type", type);
			obj.put("pageIndex", pageIndex);
			obj.put("pageSize", pageSize);
//			execute(obj, RequestCode.REQUEST_ANNOUNCEMENTLIST,isMore);
			executeNew(obj, RequestCode.REQUEST_ANNOUNCEMENTLIST, isMore, "", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public void announcementOne(String id) {
//		
//	}

	 /**
     * 用户消息数目（分组）
     * @param petId
     */
	public void messageUMCG(String petId) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "message");
			obj.put(ProtocolManager.OPTIONS, "UMCG");
			obj.put("petId", petId);
			execute(obj, RequestCode.REQUEST_MESSAGEUMCG);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
     * 消息推送TOKEN保存
     * @param userId
     * @param petId
     * @param token
     */
	public void messageMPTS(String userId, String petId, String token) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "message");
			obj.put(ProtocolManager.OPTIONS, "MPTS");
			obj.put("userId", userId);
			obj.put("petId", petId);
			obj.put("token", token);
			obj.put("type", 2);
			execute(obj, RequestCode.REQUEST_MESSAGEMPTS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
     * 209.3_获取推荐热门宠物
     * @param pageSize
     * @param currPetId
     */
	public void petRecommend(int pageSize, String currPetId) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "pet");
			obj.put(ProtocolManager.OPTIONS, "recommend");
			obj.put("pageSize", pageSize);
			obj.put("currPetId", currPetId);
//			execute(obj, RequestCode.REQUEST_PETRECOMMEND);
			executeNew(obj, RequestCode.REQUEST_PETRECOMMEND, false, "", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
     * 批量关注
     * @param petId
     * @param fansPetId
     */
	public void petfansBatchFocus(String petId, String fansPetId) {
		try {
			JSONObject obj =getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "petfans");
			obj.put(ProtocolManager.OPTIONS, "batchFocus");
			obj.put("petId", petId);
			obj.put("fansPetId", fansPetId);
			execute(obj, RequestCode.REQUEST_PETFANSBATCHFOCUS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 宠物成长经历
     * @param petId
     * @param rankType 1：周排行，0：总排行榜
     */
    public void petGrowup(String petId,int rankType) {
        try {
            JSONObject obj =getDefaultParams();
            obj.put(ProtocolManager.COMMAND, "rank");
            String opt = rankType == 1 ? "petRankWeekMoment" : "petRankEveryMoment";
            obj.put(ProtocolManager.OPTIONS, opt);//"petRankEveryMoment");
            obj.put("petId", petId);
            execute(obj, RequestCode.REQUEST_PETGROWUP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取收货地址
     * @param petId
     */
    public void getShippingAddress(String petId){
        try {
            JSONObject obj =getDefaultParams();
            obj.put(ProtocolManager.COMMAND, "shippingAddress");
            obj.put(ProtocolManager.OPTIONS, "one");
            obj.put("petId", petId);
            execute(obj, RequestCode.REQUEST_GETSHIPPINGADDRESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存收货地址
     * @param petId
     * @param address
     * @param mobile
     * @param name
     * @param zipcode
     */
    public void saveShippingAddress(String petId,String address,String mobile,String name,String zipcode){
        try {
            JSONObject obj =getDefaultParams();
            obj.put(ProtocolManager.COMMAND, "shippingAddress");
            obj.put(ProtocolManager.OPTIONS, "save");
            obj.put("petId", petId);
            obj.put("address", address);
            obj.put("mobile", mobile);
            obj.put("name", name);
            obj.put("zipcode", zipcode);
            execute(obj, RequestCode.REQUEST_SAVESHIPPINGADDRESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   

}