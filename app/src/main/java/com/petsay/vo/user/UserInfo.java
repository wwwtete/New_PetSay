package com.petsay.vo.user;

import java.io.Serializable;
import java.util.List;

import com.petsay.vo.petalk.PetVo;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInfo implements Serializable{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -7478990624826964802L;
	private String id;
	/**
	 * 用户手机号，第三方登陆名
	 */
	private String loginName;
	
	/**
	 * 用户添加的宠物列表
	 */
	private List<PetVo> petList;
	
	private boolean isLogin;
	
	private String channel="";
	private String createTime="";
	private String phoneNum="";

	private String sessionToken;
	private String sessionKey;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public List<PetVo> getPetList() {
		return petList;
	}
	public void setInfos(List<PetVo> petList) {
		this.petList = petList;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
}
