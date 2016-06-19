package com.petsay.network.net;

import com.alibaba.fastjson.JSON;
import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.network.base.PetSayError;
import com.petsay.utile.ProtocolManager;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.petalk.PublishTalkParam;
import com.petsay.vo.story.StoryParams;

import org.json.JSONObject;

/**
 * @author wangw
 * 发布宠物说的Service
 */
public class PublishPetSayNet extends BaseNet {

	public void publishPetTalk(PublishTalkParam dto){
		try {
			JSONObject object = getDefaultParams();
			object.put(ProtocolManager.COMMAND, "petalk");
			object.put(ProtocolManager.OPTIONS, "create");
			String json = JSON.toJSONString(dto);//JSON.toJSONString(dto,true); //
			String temp = object.toString();
            temp = temp.substring(0, temp.length()-1)+",";
//			temp = temp+"\"PublishParam\":"+json+"}";
            temp = temp+"\"petalkDTO\":"+json+"}";
            PublicMethod.log_d("转换json字符串==" + temp);
            execute(temp, RequestCode.REQUEST_PUBLISHPETTALK,dto);
		} catch (Exception e) {
			e.printStackTrace();
			onErrorCallback(new PetSayError(PetSayError.CODE_ERROR), RequestCode.REQUEST_PUBLISHPETTALK);
		}
	}

    public void publishStory(StoryParams params,Object tag){
        try {
            JSONObject object = getDefaultParams();
            object.put(ProtocolManager.COMMAND, "petalk");
            object.put(ProtocolManager.OPTIONS, "create");
            String json = params.toJson();
            String temp = object.toString();
            temp = temp.substring(0, temp.length()-1)+",";
//			temp = temp+"\"PublishParam\":"+json+"}";
            temp = temp+"\"petalkDTO\":"+json+"}";
            PublicMethod.log_d("StoryParam转换json字符串==" + temp);
            execute(temp, RequestCode.REQUEST_PUBLISHSTORY,tag);
        } catch (Exception e) {
            e.printStackTrace();
            onErrorCallback(new PetSayError(PetSayError.CODE_ERROR), RequestCode.REQUEST_PUBLISHSTORY);
        }
    }

    /**
     * 获取所有标签
     */
    public void getSayTagAll() {
        JSONObject obj =  getDefaultParams();
        try {
            obj.put(ProtocolManager.COMMAND, "tag");
            obj.put(ProtocolManager.OPTIONS, "all");
            execute(obj,RequestCode.REQUEST_GETSAYTAGALL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索标签
     * @param petId
     * @param keyword
     * @param pageIndex
     * @param pageSize
     */
    public void searchTag(String petId,String keyword,int pageIndex,int pageSize) {
        JSONObject obj =  getDefaultParams();
        try {
            obj.put(ProtocolManager.COMMAND, "tag");
            obj.put(ProtocolManager.OPTIONS, "search");
            obj.put("currPetId",petId);
            obj.put("keyword",keyword);
            obj.put("pageIndex",pageIndex);
            obj.put("pageSize",pageSize);
            execute(obj,RequestCode.REQUEST_SEARCHTAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建标签
     * @param petId
     * @param keyword
     */
    public void createTag(String petId,String keyword) {
        JSONObject obj =  getDefaultParams();
        try {
            obj.put(ProtocolManager.COMMAND, "tag");
            obj.put(ProtocolManager.OPTIONS, "create");
            obj.put("currPetId",petId);
            obj.put("keyword",keyword);
            execute(obj,RequestCode.REQUEST_CREATETAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取热门标签
     */
    public void getHotTagList() {
        JSONObject obj =  getDefaultParams();
        try {
            obj.put(ProtocolManager.COMMAND, "tag");
            obj.put(ProtocolManager.OPTIONS, "hot");
            execute(obj,RequestCode.REQUEST_GETHOTTAGLIST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
