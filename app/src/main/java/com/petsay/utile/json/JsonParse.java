package com.petsay.utile.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.petsay.cache.DataFileCache;
import com.petsay.constants.Constants;
import com.petsay.application.UserManager;
import com.petsay.utile.ToolsAesCrypt;
import com.petsay.vo.user.UserInfo;
import com.petsay.vo.petalk.PetCounterVo;
import com.petsay.vo.petalk.PetVo;

public class JsonParse {
	
	private static JsonParse _instance;
	public static JsonParse getSingleton(){
		if (null==_instance) {
			_instance=new JsonParse();
		}
		return _instance;
	}
	
	private JsonParse(){}
	
	public  void parseLogin(Context context,String jsonString){
		UserManager userManager=UserManager.getSingleton();
		UserInfo userInfo=new UserInfo();
		try {
			JSONObject userJson=new JSONObject(jsonString);
			userInfo.setLoginName(userJson.optString("loginName"));
			userInfo.setId(userJson.optString("id"));
			userInfo.setPhoneNum(userJson.optString("phoneNum"));
			userInfo.setSessionToken(userJson.optString("sessionToken"));
			userInfo.setSessionKey(userJson.optString("sessionKey"));
			JSONArray petJsonArray=userJson.optJSONArray("petList");
			List<PetVo> petInfos=new ArrayList<PetVo>();
			for (int i = 0; i < petJsonArray.length(); i++) {
				PetVo petInfo=new PetVo();
				JSONObject petJson =petJsonArray.optJSONObject(i);
				petInfo.setId(petJson.optString("id"));
				petInfo.setNickName(petJson.optString("nickName"));
				petInfo.setGender(petJson.optInt("gender"));
				petInfo.setHeadPortrait(petJson.optString("headPortrait"));
				petInfo.setAddress(petJson.optString("address"));
				petInfo.setBirthday(petJson.optLong("birthday"));
				petInfo.setType(petJson.optInt("type"));
				petInfo.setActive(petJson.optBoolean("active"));
				JSONObject countJson=petJson.optJSONObject("counter");
				PetCounterVo counterVo=new PetCounterVo();
				counterVo.setComment(countJson.optInt("commentCount"));
				petInfo.setCreateTime(countJson.optLong("createTime"));
				counterVo.setFans(countJson.optInt("fansCount"));
				counterVo.setFavour(countJson.optInt("favourCount"));
				counterVo.setFocus(countJson.optInt("focusCount"));
				counterVo.setIssue(countJson.optInt("issueCount"));
				counterVo.setRelay(countJson.optInt("relayCount"));
				petInfo.setCounter(counterVo);
				petInfos.add(petInfo);
			}
			userInfo.setInfos(petInfos);
			userInfo.setLogin(true);
//			Db4oUtil.delAll(UserInfo.class);
//			try {
//				DataFileCache.getSingleton().save("listaaa", jsonString);
//				PetsayLog.e("tagaaa:","aaa:"+ DataFileCache.getSingleton().read("listaaa"));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			try {
				DataFileCache.getSingleton().saveObj(Constants.UserFile, userInfo);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				
				e1.printStackTrace();
			}
		
			userManager.setUserInfo(userInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
//	public  List<PetVo> parsePetInfoList(String jsonString){
////		PetsayLog.e("tagaaa===", jsonString);
////		UserManager userManager=UserManager.getSingleton();
//		List<PetVo> petInfos=new ArrayList<PetVo>();
//		try {
//			JSONObject userJson=new JSONObject(jsonString);
//			JSONArray petJsonArray=userJson.optJSONArray("list");
//			for (int i = 0; i < petJsonArray.length(); i++) {
//				PetVo petInfo=new PetVo();
//				JSONObject petJson =petJsonArray.optJSONObject(i);
//				petInfo.setId(petJson.optString("id"));
//				petInfo.setNickname(petJson.optString("nickName"));
//				petInfo.setGender(petJson.optInt("gender"));
//				petInfo.setHeadPortrait(petJson.optString("headPortrait"));
//				petInfo.setAddress(petJson.optString("address"));
//				petInfo.setBirthday(petJson.optLong("birthday"));
//				petInfo.setType(petJson.optInt("type"));
//				petInfo.setActive(petJson.optBoolean("active"));
//				petInfo.setRs(petJson.optInt("rs"));
//				JSONObject countJson=petJson.optJSONObject("counter");
//				petInfo.setCommentCount(countJson.optInt("commentCount"));
//				petInfo.setCreateTime(countJson.optLong("createTime"));
//				petInfo.setFansCount(countJson.optInt("fansCount"));
//				petInfo.setFavourCount(countJson.optInt("favourCount"));
//				petInfo.setFocusCount(countJson.optInt("focusCount"));
//				petInfo.setIssueCount(countJson.optInt("issueCount"));
//				petInfo.setRelayCount(countJson.optInt("relayCount"));
//				petInfos.add(petInfo);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return petInfos;
//	}
	
//public synchronized ArrayList<SayVo> parsePublishPetalkList(String jsonString){
//		
//		ArrayList<SayVo> sayVos=new ArrayList<SayVo>();
//		try {
//			JSONArray jsonArray=new JSONArray(jsonString);
//			for (int i = 0; i < jsonArray.length(); i++) {
//				JSONObject sayJson=jsonArray.optJSONObject(i);
//				SayVo sayVo=new SayVo();
//				sayVo.setId(sayJson.optString("id"));
//				sayVo.setPetalkId(sayJson.optString("petalkId"));
//				sayVo.setDescription(sayJson.optString("description"));
//				sayVo.setAudioUrl(sayJson.optString("audioUrl"));
//				sayVo.setAudioDuration(sayJson.optLong("audioSecond"));
//				sayVo.setPublishTime(sayJson.optLong("createTime"));
//				sayVo.setPhotoUrl(sayJson.optString("photoUrl"));
//				sayVo.setThumbUrl(sayJson.optString("thumbUrl"));
//				sayVo.setRs(sayJson.optInt("rs"));
//				sayVo.setZ(sayJson.optInt("z"));
//				sayVo.setType(sayJson.optString("type"));
//				JSONArray animationJsonArray=sayJson.optJSONArray("decorations");
//				PetalkDecorationVo[] animationArray =new PetalkDecorationVo[animationJsonArray.length()];
//				for (int j = 0; j < animationJsonArray.length(); j++) {
//					JSONObject animImgJson=animationJsonArray.optJSONObject(j);
//					PetalkDecorationVo animationImg=new PetalkDecorationVo();
//					animationImg.setId(animImgJson.optString("decorationId"));
//					animationImg.setCenterX(Float.parseFloat(animImgJson.optString("centerX")));
//					animationImg.setCenterY(Float.parseFloat(animImgJson.optString("centerY")));
//					animationImg.setHeight(Float.parseFloat(animImgJson.optString("height")));
//					animationImg.setWidth(Float.parseFloat(animImgJson.optString("width")));
//					animationImg.setRotationX(Float.parseFloat(animImgJson.optString("rotationX")));
//					animationImg.setRotationY(Float.parseFloat(animImgJson.optString("rotationY")));
//					animationImg.setRotationZ(Float.parseFloat(animImgJson.optString("rotationZ")));
//					animationImg.setOrigin(JsonUtils.resultData(animImgJson.optString("origin"), DecorationBean.class));
//					animationArray[j]=animationImg;
//				}
//				sayVo.setAnimationArray(animationArray);
//				
//				LocationInfo locationInfo=new LocationInfo();
//				locationInfo.setLat(sayJson.optDouble("positionLat"));
//				locationInfo.setLon(sayJson.optDouble("positionLon"));
//				locationInfo.setLocationName(sayJson.optString("positionName"));
//				sayVo.setLocation(locationInfo);
//				
//				JSONArray tagJosnArray=sayJson.optJSONArray("tags");
//				TagInfo[] tagArray=new TagInfo[tagJosnArray.length()];
//				for (int j = 0; j < tagJosnArray.length(); j++) {
//					JSONObject tagJson=tagJosnArray.optJSONObject(j);
//					TagInfo tagInfo=new TagInfo();
//					tagInfo.setId(tagJson.optString("id"));
//					tagInfo.setName(tagJson.optString("name"));
//					tagInfo.setCreateTime(tagJson.optLong("createTime"));
//					tagInfo.setDelete(tagJson.optBoolean("deleted"));
//					tagArray[j]=tagInfo;		
//				}
//				JSONObject countJson=sayJson.optJSONObject("counter");
//				sayVo.setForwardNum(countJson.optInt("relay"));
//				sayVo.setShareNum(countJson.optInt("share"));
//				sayVo.setFavorNum(countJson.optInt("favour"));
//				sayVo.setCommentNum(countJson.optInt("comment"));
//				sayVo.setTagArray(tagArray);
//				PetInfo petInfo=new PetInfo();
//				petInfo.setId(sayJson.optString("petId"));
//				petInfo.setNickname(sayJson.optString("petNickName"));
//				petInfo.setHeadPortrait(sayJson.optString("petHeadPortrait"));
//				sayVo.setPetInfo(petInfo);
//				sayVos.add(sayVo);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		
//		return sayVos;
//	}
	
//	public synchronized ArrayList<SayVo> parsePetalkList(String jsonString){
//		
//		ArrayList<SayVo> sayVos=new ArrayList<SayVo>();
//		try {
//			JSONArray jsonArray=new JSONArray(jsonString);
//			for (int i = 0; i < jsonArray.length(); i++) {
//				JSONObject sayJson=jsonArray.optJSONObject(i);
//				SayVo sayVo=new SayVo();
//				sayVo.setId(sayJson.optString("id"));
//				sayVo.setPetalkId(sayJson.optString("petalkId"));
//				sayVo.setDescription(sayJson.optString("description"));
//				sayVo.setAudioUrl(sayJson.optString("audioUrl"));
//				sayVo.setAudioDuration(sayJson.optLong("audioSecond"));
//				sayVo.setPublishTime(sayJson.optLong("createTime"));
//				sayVo.setPhotoUrl(sayJson.optString("photoUrl"));
//				sayVo.setThumbUrl(sayJson.optString("thumbUrl"));
//				sayVo.setRs(sayJson.optInt("rs"));
//				sayVo.setZ(sayJson.optInt("z"));
//				sayVo.setType(sayJson.optString("type"));
//				JSONArray animationJsonArray=sayJson.optJSONArray("decorations");
//				PetalkDecorationVo[] animationArray =new PetalkDecorationVo[animationJsonArray.length()];
//				for (int j = 0; j < animationJsonArray.length(); j++) {
//					JSONObject animImgJson=animationJsonArray.optJSONObject(j);
//					PetalkDecorationVo animationImg=new PetalkDecorationVo();
//					animationImg.setId(animImgJson.optString("decorationId"));
//					animationImg.setCenterX(Float.parseFloat(animImgJson.optString("centerX")));
//					animationImg.setCenterY(Float.parseFloat(animImgJson.optString("centerY")));
//					animationImg.setHeight(Float.parseFloat(animImgJson.optString("height")));
//					animationImg.setWidth(Float.parseFloat(animImgJson.optString("width")));
//					animationImg.setRotationX(Float.parseFloat(animImgJson.optString("rotationX")));
//					animationImg.setRotationY(Float.parseFloat(animImgJson.optString("rotationY")));
//					animationImg.setRotationZ(Float.parseFloat(animImgJson.optString("rotationZ")));
//					animationImg.setOrigin(JsonUtils.resultData(animImgJson.optString("origin"), DecorationBean.class));
//					animationArray[j]=animationImg;
//				}
//				sayVo.setAnimationArray(animationArray);
//				
//				LocationInfo locationInfo=new LocationInfo();
//				locationInfo.setLat(sayJson.optDouble("positionLat"));
//				locationInfo.setLon(sayJson.optDouble("positionLon"));
//				locationInfo.setLocationName(sayJson.optString("positionName"));
//				sayVo.setLocation(locationInfo);
//				
//				JSONArray tagJosnArray=sayJson.optJSONArray("tags");
//				TagInfo[] tagArray=new TagInfo[tagJosnArray.length()];
//				for (int j = 0; j < tagJosnArray.length(); j++) {
//					JSONObject tagJson=tagJosnArray.optJSONObject(j);
//					TagInfo tagInfo=new TagInfo();
//					tagInfo.setId(tagJson.optString("id"));
//					tagInfo.setName(tagJson.optString("name"));
//					tagInfo.setCreateTime(tagJson.optLong("createTime"));
//					tagInfo.setDelete(tagJson.optBoolean("deleted"));
//					tagArray[j]=tagInfo;		
//				}
//				JSONObject countJson=sayJson.optJSONObject("counter");
//				sayVo.setForwardNum(countJson.optInt("relay"));
//				sayVo.setShareNum(countJson.optInt("share"));
//				sayVo.setFavorNum(countJson.optInt("favour"));
//				sayVo.setCommentNum(countJson.optInt("comment"));
//				sayVo.setTagArray(tagArray);
//				PetInfo petInfo=new PetInfo();
//				try {
//					JSONObject petJson=sayJson.optJSONObject("pet");
//					petInfo.setId(petJson.optString("id"));
//					petInfo.setNickname(petJson.optString("nickName"));
//					petInfo.setGender(petJson.optInt("gender"));
//					petInfo.setHeadPortrait(petJson.optString("headPortrait"));
//					petInfo.setBirthday(petJson.optLong("birthday"));
//					petInfo.setType(petJson.optInt("type"));
//					
//				} catch (Exception e) {
//					petInfo=new PetInfo();
//					petInfo.setId(sayJson.optString("petId"));
//					petInfo.setNickname(sayJson.optString("petNickName"));
//					petInfo.setHeadPortrait(sayJson.optString("petHeadPortrait"));
//				}
//				sayVo.setPetInfo(petInfo);
//				sayVos.add(sayVo);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		
//		return sayVos;
//	}
	
//public synchronized ArrayList<SayVo> parseTagPetalkList(String jsonString){
//		ArrayList<SayVo> sayVos=new ArrayList<SayVo>();
//		try {
//			JSONArray jsonArray=new JSONArray(jsonString);
//			for (int i = 0; i < jsonArray.length(); i++) {
//				JSONObject sayJson=jsonArray.optJSONObject(i);
//				SayVo sayVo=new SayVo();
//				sayVo.setId(sayJson.optString("id"));
//				sayVo.setPetalkId(sayJson.optString("petalkId"));
//				sayVo.setDescription(sayJson.optString("description"));
//				sayVo.setAudioUrl(sayJson.optString("audioUrl"));
//				sayVo.setAudioDuration(sayJson.optLong("audioSecond"));
//				sayVo.setPublishTime(sayJson.optLong("createTime"));
//				sayVo.setPhotoUrl(sayJson.optString("photoUrl"));
//				sayVo.setThumbUrl(sayJson.optString("thumbUrl"));
//				sayVo.setRs(sayJson.optInt("rs"));
//				sayVo.setZ(sayJson.optInt("z"));
//				sayVo.setType(sayJson.optString("type"));
//				JSONArray animationJsonArray=sayJson.optJSONArray("decorations");
//				PetalkDecorationVo[] animationArray =new PetalkDecorationVo[animationJsonArray.length()];
//				for (int j = 0; j < animationJsonArray.length(); j++) {
//					JSONObject animImgJson=animationJsonArray.optJSONObject(j);
//					PetalkDecorationVo animationImg=new PetalkDecorationVo();
//					animationImg.setId(animImgJson.optString("decorationId"));
//					animationImg.setCenterX(Float.parseFloat(animImgJson.optString("centerX")));
//					animationImg.setCenterY(Float.parseFloat(animImgJson.optString("centerY")));
//					animationImg.setHeight(Float.parseFloat(animImgJson.optString("height")));
//					animationImg.setWidth(Float.parseFloat(animImgJson.optString("width")));
//					animationImg.setRotationX(Float.parseFloat(animImgJson.optString("rotationX")));
//					animationImg.setRotationY(Float.parseFloat(animImgJson.optString("rotationY")));
//					animationImg.setRotationZ(Float.parseFloat(animImgJson.optString("rotationZ")));
//					animationImg.setOrigin(JsonUtils.resultData(animImgJson.optString("origin"), DecorationBean.class));
//					animationArray[j]=animationImg;
//				}
//				sayVo.setAnimationArray(animationArray);
//				
//				LocationInfo locationInfo=new LocationInfo();
//				locationInfo.setLat(sayJson.optDouble("positionLat"));
//				locationInfo.setLon(sayJson.optDouble("positionLon"));
//				locationInfo.setLocationName(sayJson.optString("positionName"));
//				sayVo.setLocation(locationInfo);
//				
//				JSONArray tagJosnArray=sayJson.optJSONArray("tags");
//				TagInfo[] tagArray=new TagInfo[tagJosnArray.length()];
//				for (int j = 0; j < tagJosnArray.length(); j++) {
//					JSONObject tagJson=tagJosnArray.optJSONObject(j);
//					TagInfo tagInfo=new TagInfo();
//					tagInfo.setId(tagJson.optString("id"));
//					tagInfo.setName(tagJson.optString("name"));
//					tagInfo.setCreateTime(tagJson.optLong("createTime"));
//					tagInfo.setDelete(tagJson.optBoolean("deleted"));
//					tagArray[j]=tagInfo;		
//				}
//				JSONObject countJson=sayJson.optJSONObject("counter");
//				sayVo.setForwardNum(countJson.optInt("relay"));
//				sayVo.setShareNum(countJson.optInt("share"));
//				sayVo.setFavorNum(countJson.optInt("favour"));
//				sayVo.setCommentNum(countJson.optInt("comment"));
//				sayVo.setTagArray(tagArray);
//				PetInfo petInfo = new PetInfo();
//				petInfo.setId(sayJson.optString("petId"));
//				petInfo.setNickname(sayJson.optString("petNickName"));
//				petInfo.setHeadPortrait(sayJson.optString("petHeadPortrait"));
//				sayVo.setPetInfo(petInfo);
//				sayVos.add(sayVo);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		
//		return sayVos;
//	}
	
//	public synchronized ArrayList<SayVo> parseAttentionPetalkList(String jsonString){
//	
//	ArrayList<SayVo> sayVos=new ArrayList<SayVo>();
//	try {
//		JSONArray jsonArray=new JSONArray(jsonString);
//		for (int i = 0; i < jsonArray.length(); i++) {
//			JSONObject sayJson=jsonArray.optJSONObject(i);
//			SayVo sayVo=new SayVo();
//			sayVo.setId(sayJson.optString("id"));
//			sayVo.setPetalkId(sayJson.optString("petalkId"));
//			sayVo.setDescription(sayJson.optString("description"));
//			sayVo.setAudioUrl(sayJson.optString("audioUrl"));
//			sayVo.setAudioDuration(sayJson.optLong("audioSecond"));
//			sayVo.setPublishTime(sayJson.optLong("createTime"));
//			sayVo.setPhotoUrl(sayJson.optString("photoUrl"));
//			sayVo.setThumbUrl(sayJson.optString("thumbUrl"));
//			sayVo.setRs(sayJson.optInt("rs"));
//			sayVo.setZ(sayJson.optInt("z"));
//			sayVo.setType(sayJson.optString("type"));
//			JSONArray animationJsonArray=sayJson.optJSONArray("decorations");
//			PetalkDecorationVo[] animationArray =new PetalkDecorationVo[animationJsonArray.length()];
//			for (int j = 0; j < animationJsonArray.length(); j++) {
//				JSONObject animImgJson=animationJsonArray.optJSONObject(j);
//				PetalkDecorationVo animationImg=new PetalkDecorationVo();
//				animationImg.setId(animImgJson.optString("decorationId"));
//				animationImg.setCenterX(Float.parseFloat(animImgJson.optString("centerX")));
//				animationImg.setCenterY(Float.parseFloat(animImgJson.optString("centerY")));
//				animationImg.setHeight(Float.parseFloat(animImgJson.optString("height")));
//				animationImg.setWidth(Float.parseFloat(animImgJson.optString("width")));
//				animationImg.setRotationX(Float.parseFloat(animImgJson.optString("rotationX")));
//				animationImg.setRotationY(Float.parseFloat(animImgJson.optString("rotationY")));
//				animationImg.setRotationZ(Float.parseFloat(animImgJson.optString("rotationZ")));
//				animationImg.setOrigin(JsonUtils.resultData(animImgJson.optString("origin"), DecorationBean.class));
//				animationArray[j]=animationImg;
//			}
//			sayVo.setAnimationArray(animationArray);
//			
//			LocationInfo locationInfo=new LocationInfo();
//			locationInfo.setLat(sayJson.optDouble("positionLat"));
//			locationInfo.setLon(sayJson.optDouble("positionLon"));
//			locationInfo.setLocationName(sayJson.optString("positionName"));
//			sayVo.setLocation(locationInfo);
//			
//			JSONArray tagJosnArray=sayJson.optJSONArray("tags");
//			TagInfo[] tagArray=new TagInfo[tagJosnArray.length()];
//			for (int j = 0; j < tagJosnArray.length(); j++) {
//				JSONObject tagJson=tagJosnArray.optJSONObject(j);
//				TagInfo tagInfo=new TagInfo();
//				tagInfo.setId(tagJson.optString("id"));
//				tagInfo.setName(tagJson.optString("name"));
//				tagInfo.setCreateTime(tagJson.optLong("createTime"));
//				tagInfo.setDelete(tagJson.optBoolean("deleted"));
//				tagArray[j]=tagInfo;		
//			}
//			JSONObject countJson=sayJson.optJSONObject("counter");
//			sayVo.setForwardNum(countJson.optInt("relay"));
//			sayVo.setShareNum(countJson.optInt("share"));
//			sayVo.setFavorNum(countJson.optInt("favour"));
//			sayVo.setCommentNum(countJson.optInt("comment"));
//			sayVo.setTagArray(tagArray);
//			PetInfo petInfo=new PetInfo();
////			petInfo.setId(sayJson.optString("petId"));
////			petInfo.setNickname(sayJson.optString("petNickName"));
////			petInfo.setHeadPortrait(sayJson.optString("petHeadPortrait"));
////			sayVo.setPetInfo(petInfo);
//			
//			JSONObject petJson=sayJson.optJSONObject("pet");
//			petInfo.setId(petJson.optString("id"));
//			petInfo.setNickname(petJson.optString("nickName"));
//			petInfo.setGender(petJson.optInt("gender"));
//			petInfo.setHeadPortrait(petJson.optString("headPortrait"));
//			petInfo.setBirthday(petJson.optLong("birthday"));
//			petInfo.setType(petJson.optInt("type"));
//			sayVo.setPetInfo(petInfo);
//			
//
//			CommentVo commentVo=new CommentVo();
//			commentVo.setComment(sayJson.optString("comment"));
//			commentVo.setCreateTime(sayJson.optLong("relayTime"));
//			commentVo.setCommentAudioSecond(sayJson.optString("commentAudioSecond"));
//			commentVo.setCommentAudioUrl(sayJson.optString("commentAudioUrl"));
//			commentVo.setAimPetNickName(sayJson.optString("aimPetNickName"));
//			commentVo.setAimPetId(sayJson.optString("aimPetId"));
//			commentVo.setAimPetHeadPortrait(sayJson.optString("aimPetHeadPortrait"));
//			sayVo.setCommentVo(commentVo);
//			sayVos.add(sayVo);
//		}
//	} catch (JSONException e) {
//		e.printStackTrace();
//	}
//	
//	
//	return sayVos;
//}
	
//public synchronized ArrayList<SayVo> parseCommentPetalkList(String jsonString){
//		
//		ArrayList<SayVo> sayVos=new ArrayList<SayVo>();
//		try {
//			JSONArray jsonArray=new JSONArray(jsonString);
//			for (int i = 0; i < jsonArray.length(); i++) {
//				JSONObject sayJson=jsonArray.optJSONObject(i);
//				SayVo sayVo=new SayVo();
//				sayVo.setId(sayJson.optString("id"));
//				sayVo.setPetalkId(sayJson.optString("petalkId"));
//				sayVo.setDescription(sayJson.optString("description"));
//				sayVo.setAudioUrl(sayJson.optString("audioUrl"));
//				sayVo.setAudioDuration(sayJson.optLong("audioSecond"));
//				sayVo.setPublishTime(sayJson.optLong("createTime"));
//				sayVo.setPhotoUrl(sayJson.optString("photoUrl"));
//				sayVo.setThumbUrl(sayJson.optString("thumbUrl"));
//				sayVo.setRs(sayJson.optInt("rs"));
//				sayVo.setZ(sayJson.optInt("z"));
//				sayVo.setType(sayJson.optString("type"));
//				JSONArray animationJsonArray=sayJson.optJSONArray("decorations");
//				PetalkDecorationVo[] animationArray =new PetalkDecorationVo[animationJsonArray.length()];
//				for (int j = 0; j < animationJsonArray.length(); j++) {
//					JSONObject animImgJson=animationJsonArray.optJSONObject(j);
//					PetalkDecorationVo animationImg=new PetalkDecorationVo();
//					animationImg.setId(animImgJson.optString("decorationId"));
//					animationImg.setCenterX(Float.parseFloat(animImgJson.optString("centerX")));
//					animationImg.setCenterY(Float.parseFloat(animImgJson.optString("centerY")));
//					animationImg.setHeight(Float.parseFloat(animImgJson.optString("height")));
//					animationImg.setWidth(Float.parseFloat(animImgJson.optString("width")));
//					animationImg.setRotationX(Float.parseFloat(animImgJson.optString("rotationX")));
//					animationImg.setRotationY(Float.parseFloat(animImgJson.optString("rotationY")));
//					animationImg.setRotationZ(Float.parseFloat(animImgJson.optString("rotationZ")));
//					animationImg.setOrigin(JsonUtils.resultData(animImgJson.optString("origin"), DecorationBean.class));
//					animationArray[j]=animationImg;
//				}
//				sayVo.setAnimationArray(animationArray);
//				
//				LocationInfo locationInfo=new LocationInfo();
//				locationInfo.setLat(sayJson.optDouble("positionLat"));
//				locationInfo.setLon(sayJson.optDouble("positionLon"));
//				locationInfo.setLocationName(sayJson.optString("positionName"));
//				sayVo.setLocation(locationInfo);
//				
//				JSONArray tagJosnArray=sayJson.optJSONArray("tags");
//				TagInfo[] tagArray=new TagInfo[tagJosnArray.length()];
//				for (int j = 0; j < tagJosnArray.length(); j++) {
//					JSONObject tagJson=tagJosnArray.optJSONObject(j);
//					TagInfo tagInfo=new TagInfo();
//					tagInfo.setId(tagJson.optString("id"));
//					tagInfo.setName(tagJson.optString("name"));
//					tagInfo.setCreateTime(tagJson.optLong("createTime"));
//					tagInfo.setDelete(tagJson.optBoolean("deleted"));
//					tagArray[j]=tagInfo;		
//				}
//				JSONObject countJson=sayJson.optJSONObject("counter");
//				sayVo.setForwardNum(countJson.optInt("relay"));
//				sayVo.setShareNum(countJson.optInt("share"));
//				sayVo.setFavorNum(countJson.optInt("favour"));
//				sayVo.setCommentNum(countJson.optInt("comment"));
//				sayVo.setTagArray(tagArray);
//				PetInfo petInfo=new PetInfo();
//				try {
//					JSONObject petJson=sayJson.optJSONObject("pet");
//					petInfo.setId(petJson.optString("id"));
//					petInfo.setNickname(petJson.optString("nickName"));
//					petInfo.setGender(petJson.optInt("gender"));
//					petInfo.setHeadPortrait(petJson.optString("headPortrait"));
//					petInfo.setBirthday(petJson.optLong("birthday"));
//					petInfo.setType(petJson.optInt("type"));
//				} catch (Exception e) {
//					petInfo.setId(sayJson.optString("petId"));
//					petInfo.setNickname(sayJson.optString("petNickName"));
//					petInfo.setHeadPortrait(sayJson.optString("petHeadPortrait"));
//				}
//				sayVo.setPetInfo(petInfo);
//				
//
//				CommentVo commentVo=new CommentVo();
//				commentVo.setComment(sayJson.optString("comment"));
//				commentVo.setCreateTime(sayJson.optLong("relayTime"));
//				commentVo.setCommentAudioSecond(sayJson.optString("commentAudioSecond"));
//				commentVo.setCommentAudioUrl(sayJson.optString("commentAudioUrl"));
//				commentVo.setAimPetNickName(sayJson.optString("aimPetNickName"));
//				commentVo.setAimPetId(sayJson.optString("aimPetId"));
//				commentVo.setAimPetHeadPortrait(sayJson.optString("aimPetHeadPortrait"));
//				sayVo.setCommentVo(commentVo);
//				sayVos.add(sayVo);
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		
//		
//		return sayVos;
//	}
	
	
//	public List<SearchTagVo>  parseSearchTagList(String jsonString){
//		List<SearchTagVo> searchTagVos=new ArrayList<SearchTagVo>();
//		try {
//			JSONArray jsonArray=new JSONArray(jsonString);
//			for (int i = 0; i < jsonArray.length(); i++) {
//				SearchTagVo searchTagVo=new SearchTagVo();
//				JSONObject tagJson=jsonArray.optJSONObject(i);
//				TagInfo tagInfo=new TagInfo();
//				tagInfo.setId(tagJson.optString("id"));
//				tagInfo.setName(tagJson.optString("name"));
//				tagInfo.setIconUrl(tagJson.optString("iconUrl"));
//				tagInfo.setBgUrl(tagJson.optString("bgUrl"));
//				tagInfo.setCreateTime(tagJson.optLong("createTime"));
//				
//				List<PetalkVo> sayVos=new ArrayList<PetalkVo>();
//				sayVos=parseTagPetalkList(tagJson.optString("petalks"));
//				searchTagVo.setSayVos(sayVos);
//				searchTagVo.setTagInfo(tagInfo);
//				searchTagVos.add(searchTagVo);
//			}
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		Db4oUtil.save(searchTagVos);
//		return searchTagVos;
//		
//	}
	
//	public List<PetInfo>  parseSearchUserList(String jsonString){
//		List<PetInfo> petInfos=new ArrayList<PetInfo>();
//		try {
//			JSONArray jsonArray=new JSONArray(jsonString);
//			for (int i = 0; i < jsonArray.length(); i++) {
//				PetInfo petInfo=new PetInfo();
//				JSONObject petJson=jsonArray.optJSONObject(i);
//				
//				petInfo.setId(petJson.optString("id"));
//				petInfo.setHeadPortrait(petJson.optString("headPortrait"));
//				petInfo.setBackgroundImg(petJson.optString("backgroundImg"));
//				petInfo.setNickname(petJson.optString("nickName"));
//				petInfo.setGender(petJson.optInt("gender"));
//				petInfo.setType(petJson.optInt("type"));
//				petInfo.setBirthday(petJson.optLong("birthday"));
//				petInfo.setAddress(petJson.optString("address"));
//				petInfo.setActive(petJson.optBoolean("active"));
//				petInfo.setCreateTime(petJson.optLong("createTime"));
//				petInfo.setRs(petJson.optInt("active"));
//				JSONObject jsonCount=petJson.getJSONObject("counter");
//				petInfo.setCommentCount(jsonCount.optInt("comment"));
//				petInfo.setFavourCount(jsonCount.optInt("favour"));
//				petInfo.setRelayCount(jsonCount.optInt("relay"));
//				petInfo.setIssueCount(jsonCount.optInt("issue"));
//				petInfo.setFansCount(jsonCount.optInt("fans"));
//				petInfo.setFocusCount(jsonCount.optInt("focus"));
//				petInfo.setShareCount(jsonCount.optInt("share"));
//				petInfos.add(petInfo);
//			}
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return petInfos;
//		
//	}
	
	public  String parseListString(String jsonString){
		JSONObject listJson;
		try {
			listJson = new JSONObject(jsonString);
//			System.out.println("JsonParse.parsePetalkList()   jsonStr"+jsonString);
			JSONArray jsonArray=listJson.optJSONArray("list");
			return jsonArray.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	
	
}
