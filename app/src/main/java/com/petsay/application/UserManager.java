package com.petsay.application;

import android.content.Context;
import android.text.TextUtils;

import com.petsay.cache.DataFileCache;
import com.petsay.chat.ChatMsgManager;
import com.petsay.constants.Constants;
import com.petsay.vo.decoration.DecorationDataManager;
import com.petsay.vo.user.UserInfo;
import com.petsay.vo.petalk.PetVo;
import com.petsay.vo.petalk.PetalkVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserManager {
	// 登陆账号的用户信息
	private UserInfo _userInfo;
	// 是否为登陆状态
	private boolean loginStatus;
	public static final String CACHE_USER_KEY = "user";
	// 登录状态是否发生改变,在设置登录状态时发生改变
	public static boolean isUserChanged = false;

	//关注
	public HashMap<String, String> focusMap = new HashMap<String, String>();
	//赞
	public HashMap<String, String> stepMap=new HashMap<String, String>();

	private static UserManager _instance;

	public static UserManager getSingleton() {
		if (null == _instance) {
			_instance = new UserManager();
		}
		return _instance;
	}

	public UserManager(){
		initUserInfo();
	}

	private void initUserInfo() {
		try {
			Object object= DataFileCache.getSingleton().loadObject(Constants.UserFile);
			if (null!=object) {
				UserInfo info=(UserInfo) object;
				if (info.isLogin()) {
					setUserInfo(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取登陆账号的用户信息
	 * 
	 * @return
	 */
	public UserInfo getUserInfo() {
		return _userInfo;
	}

	/**
	 * 获取解密的Key
	 * @return
	 */
	public String getDecryptKey(){
		if(_userInfo != null)
			return _userInfo.getSessionKey();
		return null;
	}

	/**
	 * 获取SeesionToken
	 * @return
	 */
	public String getSeesionToken(){
		if(_userInfo != null)
			return _userInfo.getSessionToken();
		return "";
	}

	/**
	 * 设置登陆账号的用户信息
	 * 
	 * @return
	 */
	public void setUserInfo(UserInfo userInfo) {
		focusMap.clear();
		isUserChanged = true;
		if (null == userInfo) {
			// MainActivity.getInstance().setUnreadMsgCount(0);
			logout();
		} else {
			// 同时设置用户登陆状态为true

			setLoginStatus(true);
			_userInfo = userInfo;
			// MainActivity.getInstance().setUnreadMsgCount(SharePreferenceCache.getSingleton(context).getReadMsgCount());
		}
	}

	/**
	 * 注销
	 */
	public void logout(){
		setLoginStatus(false);
		try {
			UserInfo temp = (UserInfo) DataFileCache.getSingleton()
					.loadObject(Constants.UserFile);
			if(temp != null) {
				temp.setLogin(false);
				temp.setSessionToken(null);
				DataFileCache.getSingleton().saveObj(Constants.UserFile, temp);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 移除已删除的宠物
	 * 
	 * @param petInfo
	 */
	public void removePet(PetVo petInfo) {
		for (int i = 0; i < _userInfo.getPetList().size(); i++) {
			if (_userInfo.getPetList().get(i).getId().equals(petInfo.getId())) {
				_userInfo.getPetList().remove(i);
			}
		}
	}

	/**
	 * 设置当前激活宠物信息
	 * 
	 * @return
	 */
	public void setActivePetInfo(PetVo petInfo) {
		if (_userInfo != null) {
			isUserChanged = true;
			focusMap.clear();
			for (int i = 0; i < _userInfo.getPetList().size(); i++) {
				if (_userInfo.getPetList().get(i).getId().equals(petInfo.getId())) {
					petInfo.setActive(true);
					_userInfo.getPetList().set(i, petInfo);
				} else {
					_userInfo.getPetList().get(i).setActive(false);
				}
			}
		}
	}

	/**
	 * 获取当前激活宠物信息
	 * 
	 * @return
	 */
	public PetVo getActivePetInfo() {
		if (null==_userInfo) {
			initUserInfo();
		}
		if (null!=_userInfo) {
			List<PetVo> petInfos = _userInfo.getPetList();
			for (int i = 0; i < petInfos.size(); i++) {
				if (petInfos.get(i).isActive()) {
					return petInfos.get(i);
				}
			}
		}
		
		return null;
	}

	public void editActivePetInfo(PetVo info) {
		if (_userInfo != null) {
			List<PetVo> petInfos = _userInfo.getPetList();
			for (int i = 0; i < petInfos.size(); i++) {
				if (petInfos.get(i).isActive()) {
					petInfos.set(i, info);
					break;
				}
			}
		}
	}

	public String getActivePetId() {
		PetVo petInfo = getActivePetInfo();
		if (null == petInfo) {
			return "";
		}
		return getActivePetInfo().getId();
	}

	/**
	 * 获取当前登录用户非激活的宠物列表
	 * 
	 * @return
	 */
	public List<PetVo> getOtherPets() {
		List<PetVo> petInfos = new ArrayList<PetVo>();
		if (_userInfo != null) {
			List<PetVo> tempPetInfos = _userInfo.getPetList();
			for (int i = 0; i < tempPetInfos.size(); i++) {
				if (!tempPetInfos.get(i).isActive()) {
					petInfos.add(tempPetInfos.get(i));
				}
			}
			// return petInfos;
		}
		return petInfos;
	}

	/**
	 * 获取登陆状态
	 * 
	 * @return false 未登录 true 已登录
	 */
	public boolean isLoginStatus() {
		return loginStatus && !TextUtils.isEmpty(getSeesionToken());
	}

	/**
	 * 设置登陆状态 false 未登录 true 登陆
	 * 
	 * @param loginStatus
	 */
	public void setLoginStatus(boolean loginStatus) {
		// isLoginStatusChanged=true;
		this.loginStatus = loginStatus;
	}

	public void addFocusAndStepByList(List<PetalkVo> petalkVos) {
		if (loginStatus) {
			for (int i = 0; i < petalkVos.size(); i++) {
				String petInfoId = petalkVos.get(i).getPetId();
				if (petalkVos.get(i).getRs() == 1
						|| petalkVos.get(i).getRs() == 2) {
					UserManager.getSingleton().focusMap.put(petInfoId,petInfoId);
				}
				String petalkId = petalkVos.get(i).getPetalkId();
				if (petalkVos.get(i).getZ() == 1) {
					stepMap.put(petalkId,petalkId);
				}
			}
		} else
			focusMap.clear();
	}

	/**
	 * 通过说说判断是否已关注该用户，如果已关注添加入focusMap
	 * 
	 * @param petalkVo
	 */
	public void addFocusBySayVo(PetalkVo petalkVo) {
		String focusPetId = petalkVo.getPet().getId();
		addFocusByPetId(focusPetId);
	}
	
	public void addFocusByPetId(String focusPetId) {
		if (loginStatus) {
			focusMap.put(focusPetId, focusPetId);
		} else
			focusMap.clear();
	}
	
	public void removeFocusByPetId(String focusPetId) {
		if (loginStatus&&focusMap.containsKey(focusPetId)) {
			focusMap.remove(focusPetId);
		} else if(!loginStatus)
			focusMap.clear();
	}

	/**
	 * 通过说说判断是否已关注该用户，如果未关注从focusMap移除
	 * 
	 * @param petalkVo
	 */
	public void removeFocusBySayVo(PetalkVo petalkVo) {
		if (loginStatus) {
			String petInfoId = petalkVo.getPet().getId();
			if (focusMap.containsKey(petInfoId)) {
				focusMap.remove(petInfoId);
			}
		} else
			focusMap.clear();
	}

	/**
	 * 判断已登录用户是否已关注此用户
	 * 
	 * @param petInfoId
	 * @return
	 */
	public boolean isAlreadyFocus(String petInfoId) {
		if (loginStatus && !petInfoId.equals(getActivePetId())
				&& !UserManager.getSingleton().focusMap.containsKey(petInfoId)) {
			return false;
		} else if (!loginStatus) {
			return false;
		}
		return true;

	}
	
	
	public void addStepByPetalkVos(List<PetalkVo> petalkVos) {
		if (loginStatus) {
			for (int i = 0; i < petalkVos.size(); i++) {
				String petalkId = petalkVos.get(i).getPetalkId();
				if (petalkVos.get(i).getZ() == 1) {
					stepMap.put(petalkId,petalkId);
				}
			}
		} else
			stepMap.clear();
	}
	
	/**
	 * 通过说说判断是否已踩，如果已踩添加入stepMap
	 * 
	 * @param petalkVo
	 */
	public void addStepByPetalkVo(PetalkVo petalkVo) {
		String focusPetId = petalkVo.getPetalkId();
		addStepByPetalkId(focusPetId);
	}
	
	public void addStepByPetalkId(String petalkId){
		if (loginStatus) {
			stepMap.put(petalkId, petalkId);
		} else
			stepMap.clear();
	}
	
	
	/**
	 * 判断已登录用户是否已踩某条说说
	 * 
	 * @param petalkId
	 * @return
	 */
	public boolean isAlreadyStep(String petalkId) {
		if (loginStatus &&!UserManager.getSingleton().stepMap.containsKey(petalkId)) {
			return false;
		} else if (!loginStatus) {
			return false;
		}
		return true;

	}

}
