package com.petsay.network.net;

import com.alibaba.fastjson.JSON;
import com.petsay.component.view.UploadTopicReplyView;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.network.base.PetSayError;
import com.petsay.utile.ProtocolManager;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.forum.CreateTopicParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 话题接口
 */
public class TopicNet extends BaseNet {

	
	
	/**
	 * 话题列表
	 */
	public void topicList(String startId,int pageSize,boolean isMore){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "topic");
			object.put(ProtocolManager.OPTIONS, "list");
            object.put("startId", startId);
            object.put("pageSize  ", pageSize);
//			execute(object, RequestCode.REQUEST_TOPICLIST,isMore);
			executeNew(object, RequestCode.REQUEST_TOPICLIST, isMore,"", false, false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 互动列表
	 * @param topicId
	 */
	public void topicTalkList(String petId,String topicId,String startId ,int pageSize ,boolean isMore){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "topic");
			object.put(ProtocolManager.OPTIONS, "talkList");
			object.put("topicId", topicId);
			object.put("startId", startId);
			object.put("pageSize", pageSize);
			object.put("petId", petId);
//			execute(object, RequestCode.REQUEST_TOPICTALKLIST,isMore);
			executeNew(object, RequestCode.REQUEST_TOPICTALKLIST, isMore,"", false, false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 评论列表
	 * @param talkId
	 * @param startId
	 * @param pageSize
	 * @param isMore
	 */
	public void topicCommentList(String talkId,String startId ,int pageSize ,boolean isMore){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "topic");
			object.put(ProtocolManager.OPTIONS, "commentList");
			object.put("talkId", talkId);
			object.put("startId", startId);
			object.put("pageSize", pageSize);
//			execute(object, RequestCode.REQUEST_TOPICCOMMENTLIST,isMore);
			executeNew(object, RequestCode.REQUEST_TOPICCOMMENTLIST, isMore,"", false, false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取单个讨论详情
	 * @param id
	 * @param petId
	 */
	public void topicTalkOne(String id,String petId){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "topic");
			object.put(ProtocolManager.OPTIONS, "talkOne");
			object.put("id", id);
			object.put("petId", petId);
//			execute(object, RequestCode.REQUEST_TOPICTALKONE);
			executeNew(object, RequestCode.REQUEST_TOPICTALKONE, false,"", false, false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 创建一个讨论
	 * @param topicId
	 * @param petId
	 * @param content
	 * @param pictures(json的字符串，格式[{"pic":"xxxxx","sacleWH":"5:5"}])
	 */
	public void topicCreateTalk(String topicId,String petId,String content,String pictures,UploadTopicReplyView view){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "topic");
			object.put(ProtocolManager.OPTIONS, "createTalk");
			object.put("topicId", topicId);
			object.put("petId", petId);
			object.put("content", content);
			object.put("pictures", pictures);
			execute(object, RequestCode.REQUEST_TOPICCREATETALK,false,view);
		    
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    public void topicCreateTalk(CreateTopicParams params,UploadTopicReplyView view){
        try {
            JSONObject object = getDefaultParams();
            object.put(ProtocolManager.COMMAND, "topic");
            object.put(ProtocolManager.OPTIONS, "createTalk");
            String json = JSON.toJSONString(params);
            json = json.substring(1,json.length()-1);
            String temp = object.toString();
            temp = temp.substring(0, temp.length()-1)+",";
            temp += json +"}";
            PublicMethod.log_d("转换json字符串==" + temp);
            execute(temp, RequestCode.REQUEST_TOPICCREATETALK,view);
        } catch (Exception e) {
            e.printStackTrace();
            onErrorCallback(new PetSayError(PetSayError.CODE_ERROR), RequestCode.REQUEST_PUBLISHPETTALK);
        }
    }
	
	/**
	 * 创建一个评论

	 * @param talkId
	 * @param petId
	 * @param comment
	 * @param atPetId（@的宠物ID）
	 */
	public void topicCreateComment (String talkId,String petId,String comment,String atPetId){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "topic");
			object.put(ProtocolManager.OPTIONS, "createComment");
			object.put("talkId", talkId);
			object.put("petId", petId);
			object.put("comment", comment);
			object.put("atPetId", atPetId);
			execute(object, RequestCode.REQUEST_TOPICCREATECOMMENT);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 赞
	 * @param talkId
	 * @param petId
	 */
	public void topicCreateLike   (String talkId,String petId,Object tag){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "topic");
			object.put(ProtocolManager.OPTIONS, "createLike");
			object.put("talkId", talkId);
			object.put("petId", petId);
			execute(object, RequestCode.REQUEST_TOPICCREATELIKE,tag);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  取消赞
	 * @param talkId
	 * @param petId
	 */
	public void topicCancelLike  (String talkId,String petId,Object tag){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "topic");
			object.put(ProtocolManager.OPTIONS, "cancelLike");
			object.put("talkId", talkId);
			object.put("petId", petId);
			execute(object, RequestCode.REQUEST_TOPICCANCELLIKE,tag);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取话题详情
	 */
	public void topicTopOne (){
		JSONObject object = getDefaultParams();
		try {
			object.put(ProtocolManager.COMMAND, "topic");
			object.put(ProtocolManager.OPTIONS, "topOne");
//			execute(object, RequestCode.REQUEST_TOPICTOPONE);
			executeNew(object, RequestCode.REQUEST_TOPICTOPONE, false, "", false, false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
