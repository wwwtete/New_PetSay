package com.petsay.network.net;

import com.petsay.constants.RequestCode;
import com.petsay.network.base.BaseNet;
import com.petsay.utile.ProtocolManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author wangw
 * 获取宠物说数据
 */
public class SayDataNet extends BaseNet {

	/**
	 * 获取审核热门说说列表
	 * @param petId
	 * @param pagesize
	 */
	public void getReviewHotSayList(String petId,int pagesize){
		JSONObject obj =  getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "fun");
			obj.put(ProtocolManager.OPTIONS, "schedule");
			obj.put("petId", petId);
			obj.put("pageSize", pagesize);
			execute(obj, RequestCode.REQUEST_GETREVIEWHOTSAYLIST);
		}catch(Exception e){
		}
	}
	
	/**
	 * 选择热门说说
	 * @param code //"0"：过；"1"：有趣；"2"：糟糕；
	 * @param petId
	 * @param petalkId
	 */
	public void choiceHotSay(int code,String petId,String petalkId){
		try {
			JSONObject jsonObject = getDefaultParams();
			jsonObject.put(ProtocolManager.COMMAND, "fun");
			jsonObject.put(ProtocolManager.OPTIONS, "choice");
			jsonObject.put("code", code);
			jsonObject.put("petId", petId);
			jsonObject.put("petalkId", petalkId);
			execute(jsonObject, RequestCode.REQUEST_CHOICEHOTSAY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 获取某用户的说说列表
	 * @param petId
	 * @param type
	 * @param id
	 * @param pageSize
	 * @param arg
	 */
	public void petalkUserList(String petId,String currPetId,String type,String id,int pageSize,boolean isMore){
		try {
			JSONObject obj = getDefaultParams();
			obj.put(ProtocolManager.COMMAND, "petalk");
			obj.put(ProtocolManager.OPTIONS, "userList");
			obj.put("petId", petId);
			obj.put("currPetId", currPetId);
			obj.put("type", type);
			obj.put("id", id);
			obj.put("pageSize", pageSize);
			
			executeNew(obj, RequestCode.REQUEST_PETALKUSERLIST, isMore,type, false, true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void petalkFocusList(String petId,  String id, int pageSize,boolean isMore) {
		JSONObject obj = getDefaultParams();
			try {
				obj.put(ProtocolManager.COMMAND, "petalk");
				obj.put(ProtocolManager.OPTIONS, "focusList");
				obj.put("petId", petId);
				
				obj.put("id", id);
				obj.put("pageSize", pageSize);
				execute(obj, RequestCode.REQUEST_PETALKFOCUSLIST,isMore);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void petalkList(String petId,int pageIndex,int pageSize,boolean isMore) {
		JSONObject obj = getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "petalk");
			obj.put(ProtocolManager.OPTIONS, "hotList");
			obj.put("petId", petId);
			obj.put("pageIndex", pageIndex);
			obj.put("pageSize", pageSize);
//			execute(obj, RequestCode.REQUEST_PETALKLIST,isMore);
			executeNew(obj, RequestCode.REQUEST_PETALKLIST, isMore,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void petalkAll(String currPetId, String id, int pageSize,boolean isMore) {
		JSONObject obj = getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "petalk");
			obj.put(ProtocolManager.OPTIONS, "all");
			obj.put("currPetId", currPetId);
			obj.put("id", id);
			obj.put("pageSize", pageSize);
//			execute(obj, RequestCode.REQUEST_PETALKALL,isMore);
			executeNew(obj, RequestCode.REQUEST_PETALKALL, isMore,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void petalkChannel(String code, String petId, int pageIndex,int pageSize,boolean isMore) {
		JSONObject obj =  getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "petalk");
			obj.put(ProtocolManager.OPTIONS, "channel");
			obj.put("code", code);
			obj.put("petId", petId);
			obj.put("pageIndex", pageIndex);
			obj.put("pageSize", pageSize);
//			execute(obj, RequestCode.REQUEST_PETALKCHANNEL,isMore);
			executeNew(obj, RequestCode.REQUEST_PETALKALL, isMore,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void petalkPetBreed(String code, String currPetId, int pageIndex,int pageSize,boolean isMore) {
		JSONObject obj = getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "petalk");
			obj.put(ProtocolManager.OPTIONS, "petBreed");
			obj.put("code", code);
			obj.put("currPetId", currPetId);
			obj.put("pageIndex", pageIndex);
			obj.put("pageSize", pageSize);
//			execute(obj, RequestCode.REQUEST_PETALKPETBREED,isMore);
			executeNew(obj, RequestCode.REQUEST_PETALKPETBREED, isMore,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void petalkOne(String petalkId,String currPetId) {
		JSONObject obj =   getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "petalk");
			obj.put(ProtocolManager.OPTIONS, "one");
			obj.put("petalkId", petalkId);
			obj.put("currPetId", currPetId);
//			execute(obj, RequestCode.REQUEST_PETALKONE);
			executeNew(obj, RequestCode.REQUEST_PETALKONE, false,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取说说详情互动列表
	 * @param petalkId 说说id
	 * @param type 
	 * comment("C", "评论"), // 评论
	 * favour("F", "赞"), // 赞
	 * relay("R", "转发"), // 转发
	 * share("S", "分享"), // 分享
	 * play("P", "浏览");// 浏览
	 * @param id 上一次获取列表的最后一条id
	 * @param pageSize
	 */
	public void interactionList(String petalkId, String type, String id,int pageSize,boolean isMore) {
		JSONObject obj =  getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "interaction");
			obj.put(ProtocolManager.OPTIONS, "list");
			obj.put("petalkId", petalkId);
			obj.put("type", type);
			obj.put("id", id);
			obj.put("pageSize", pageSize);
//			execute(obj, RequestCode.REQUEST_INTERACTIONLIST,isMore,type);
			executeNew(obj,RequestCode.REQUEST_INTERACTIONLIST,isMore,type, false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void interactionCreate(String petalkId, String type, String petId,String aimPetId) {
		interactionCreate(petalkId, type, petId, aimPetId, "", "", 0);
	}
	
	public void interactionCreate(String petalkId,String type,String petId,String aimPetId,String comment,String audioUrl,float  audioSecond) {
		JSONObject obj = getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "interaction");
			obj.put(ProtocolManager.OPTIONS, "create");
			obj.put("petalkId", petalkId);
			obj.put("type", type);
			obj.put("petId", petId);
			obj.put("aimPetId", aimPetId);
			obj.put("comment", comment);
			obj.put("audioUrl", audioUrl);
			obj.put("audioSecond", audioSecond);
			execute(obj, RequestCode.REQUEST_INTERACTIONCREATE,type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void petalkDelete(String petalkId) {
		JSONObject obj = getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "petalk");
			obj.put(ProtocolManager.OPTIONS, "delete");
			obj.put("petalkId", petalkId);
			execute(obj, RequestCode.REQUEST_PETALKDELETE);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 319.1_布局简介
	 * @param code
	 */
	public void layoutIntro(int code) {
		try {
			JSONObject jsonObject = getDefaultParams();
			jsonObject.put(ProtocolManager.COMMAND, "layout");
			jsonObject.put(ProtocolManager.OPTIONS, "intro");
			jsonObject.put("code", code);
//			execute(jsonObject, RequestCode.REQUEST_LAYOUTINTRO);
			executeNew(jsonObject,RequestCode.REQUEST_LAYOUTINTRO,false,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 319.2_布局数据
	 * @param code
	 */
	public void layoutDatum(int code) {
		try {
			JSONObject jsonObject = getDefaultParams();
			jsonObject.put(ProtocolManager.COMMAND, "layout");
			jsonObject.put(ProtocolManager.OPTIONS, "datum");
			jsonObject.put("code", code);
//			execute(jsonObject, RequestCode.REQUEST_LAYOUTDATUM);
			executeNew(jsonObject,RequestCode.REQUEST_LAYOUTDATUM,false,code, false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void layoutTop3Hot(){
		try {
			JSONObject jsonObject = getDefaultParams();
			jsonObject.put(ProtocolManager.COMMAND, "layout");
			jsonObject.put(ProtocolManager.OPTIONS, "top3Hot");
//			execute(jsonObject, RequestCode.REQUEST_LAYOUTTOP3HOT);
			executeNew(jsonObject,RequestCode.REQUEST_LAYOUTTOP3HOT,false,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void tagOne(String tagId) {
		try {
			JSONObject jsonObject = getDefaultParams();
			jsonObject.put(ProtocolManager.COMMAND, "tag");
			jsonObject.put(ProtocolManager.OPTIONS, "one");
			jsonObject.put("tagId", tagId);
//			execute(jsonObject, RequestCode.REQUEST_TAGONE);
			executeNew(jsonObject,RequestCode.REQUEST_TAGONE,false,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void channelOne(String tagId) {
		try {
			JSONObject jsonObject = getDefaultParams();
			jsonObject.put(ProtocolManager.COMMAND, "channel");
			jsonObject.put(ProtocolManager.OPTIONS, "one");
			jsonObject.put("code", tagId);
//			execute(jsonObject, RequestCode.REQUEST_CHANNELONE);
			executeNew(jsonObject,RequestCode.REQUEST_CHANNELONE,false,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void petalkTagList(String tagId, String petId, int pageIndex,int pageSize,boolean isMore) {
		
		try {
			JSONObject jsonObject = getDefaultParams();
			jsonObject.put(ProtocolManager.COMMAND, "petalk");
			jsonObject.put(ProtocolManager.OPTIONS, "tagList");
			jsonObject.put("tagId", tagId);
			jsonObject.put("petId", petId);
			jsonObject.put("pageIndex", pageIndex);
			jsonObject.put("pageSize", pageSize);
//			execute(jsonObject, RequestCode.REQUEST_PETALKTAGLIST,isMore);
			executeNew(jsonObject,RequestCode.REQUEST_PETALKTAGLIST,isMore,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		try {
//			obj.put(ProtocolManager.COMMAND, "petalk");
//			obj.put(ProtocolManager.OPTIONS, "tagList");
//			
//			String response = InternetUtils.GetMethodOpenHttpConnectSecurity(obj);
//			ResponseBean bean = JsonUtils.resultData(response, ResponseBean.class);
//			int errorCode=bean.getError();
//			switch (errorCode) {
//			case 200:
//				Message message=new Message();
//				message.what=GET_TAG_LIST_SUCCESS;
//				message.obj=bean;
//				message.arg1 = what;
//				handler.sendMessage(message);
//				break;
//			case 500:
//				message=new Message();
//				message.what=GET_TAG_LIST_FAILED;
//				message.obj=bean;
//				message.what = what;
//				handler.sendMessage(message);
//				break;
//			case -1:
//				message=new Message();
//				message.what=GET_TAG_LIST_FAILED;
//				message.arg1=-1;
//				handler.sendMessage(message);
//				break;
//			}
//		
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public void petalkTagListTimeline(String tagId, String petId, String id,int pageSize, boolean isMore) {
		try {
			JSONObject jsonObject = getDefaultParams();
			jsonObject.put(ProtocolManager.COMMAND, "petalk");
			jsonObject.put(ProtocolManager.OPTIONS, "tagListTimeline");
			jsonObject.put("tagId", tagId);
			jsonObject.put("petId", petId);
			jsonObject.put("id", id);
			jsonObject.put("pageSize", pageSize);
//			execute(jsonObject, RequestCode.REQUEST_PETALKTAGLISTTIMELINE,isMore);
			executeNew(jsonObject,RequestCode.REQUEST_PETALKTAGLISTTIMELINE,isMore,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 说说互动删除
	 * @param id
	 */
	public void interactionDelete(String id) {
		try {
			JSONObject jsonObject = getDefaultParams();
			jsonObject.put(ProtocolManager.COMMAND, "interaction");
			jsonObject.put(ProtocolManager.OPTIONS, "delete");
			jsonObject.put("id", id);
			execute(jsonObject, RequestCode.REQUEST_INTERACTIONDELETE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 说说热度排行榜
     * @param rankType 1：周排行，0：总排行榜
     * @param pageIndex
     * @param pageSize
     * @param isMore
     */
    public void petalkPopRankWeekList(int rankType,int pageIndex,int pageSize, boolean isMore) {
        try {
            JSONObject jsonObject = getDefaultParams();
            jsonObject.put(ProtocolManager.COMMAND, "rank");
            String opt = rankType == 0 ? "petalkTotalPopRankList" : "petalkWeekPopRankList";
            jsonObject.put(ProtocolManager.OPTIONS, opt);//"petalkPopRankWeekList");
            jsonObject.put("pageIndex",pageIndex);
            jsonObject.put("pageSize",pageSize);
//            execute(jsonObject, RequestCode.REQUEST_PETALKPOPRANKWEEKLIST,isMore);
            executeNew(jsonObject,RequestCode.REQUEST_PETALKPOPRANKWEEKLIST,isMore,"", false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 宠物积分排行榜
     * @param rankType 1：周排行，0：总排行榜
     * @param pageIndex
     * @param pageSize
     * @param isMore
     */
    public void petScoreTotalRankDayList(int rankType,int pageIndex,int pageSize, boolean isMore) {
        try {
            JSONObject jsonObject = getDefaultParams();
            jsonObject.put(ProtocolManager.COMMAND, "rank");
            String opt = rankType == 0 ? "petScoreTotalRankList" : "petScoreWeekRankList";
            jsonObject.put(ProtocolManager.OPTIONS, opt);//"petScoreTotalRankDayList");
            jsonObject.put("pageIndex",pageIndex);
            jsonObject.put("pageSize",pageSize);
//            execute(jsonObject, RequestCode.REQUEST_PETSCORETOTALRANKDAYLIST,isMore);
            executeNew(jsonObject,RequestCode.REQUEST_PETSCORETOTALRANKDAYLIST,isMore,"", false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 广场宠物排行榜
//     * @param pageIndex
//     * @param pageSize
//     * @param isMore
//     */
//    public void simplePetScoreTotalRankDayList(int pageIndex,int pageSize, boolean isMore) {
//        try {
//            JSONObject jsonObject = getDefaultParams();
//            jsonObject.put(ProtocolManager.COMMAND, "rank");
//            jsonObject.put(ProtocolManager.OPTIONS, "simplePetScoreTotalRankDayList");
//            jsonObject.put("pageIndex",pageIndex);
//            jsonObject.put("pageSize",pageSize);
//            execute(jsonObject, RequestCode.REQUEST_SIMPLEPETSCORETOTALRANKDAYLIST,isMore);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    
    
	public void counterPet(String petId) {
		JSONObject obj = getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "counter");
			obj.put(ProtocolManager.OPTIONS, "pet");
			obj.put("petId", petId);
//			execute(obj, RequestCode.REQUEST_COUNTERPET);
			 executeNew(obj,RequestCode.REQUEST_COUNTERPET,false,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void searchPetalk(String petId, String keyword, int pageIndex,int pageSize, boolean isMore) {
		JSONObject obj = getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "search");
			obj.put(ProtocolManager.OPTIONS, "petalk");
			obj.put("petId", petId);
			obj.put("keyword", keyword);
			obj.put("pageIndex", pageIndex);
			obj.put("pageSize", pageSize);
//			execute(obj, RequestCode.REQUEST_SEARCHPETALK, isMore);
			executeNew(obj,RequestCode.REQUEST_SEARCHPETALK,isMore,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void searchTag(String petId, String keyword, int pageIndex,int pageSize,boolean isMore) {
		JSONObject obj =  getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "search");
			obj.put(ProtocolManager.OPTIONS, "tag");
			obj.put("petId", petId);
			obj.put("keyword", keyword);
			obj.put("pageIndex", pageIndex);
			obj.put("pageSize", pageSize);
//			execute(obj, RequestCode.REQUEST_SEARCHTAG, isMore);
			executeNew(obj,RequestCode.REQUEST_SEARCHTAG,isMore,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//
	public void searchUser(String petId, String keyword, int pageIndex,int pageSize,boolean isMore) {
		JSONObject obj =  getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "search");
			obj.put(ProtocolManager.OPTIONS, "user");
			obj.put("petId", petId);
			obj.put("keyword", keyword);
			obj.put("pageIndex", pageIndex);
			obj.put("pageSize", pageSize);
//			execute(obj, RequestCode.REQUEST_SEARCHUSER, isMore);
			executeNew(obj,RequestCode.REQUEST_SEARCHUSER,isMore,"", false, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 明信片说说获取
	 * @param startId
	 * @param pageSize
	 * @param isMore
	 */
	public void petalkList4Postcard(String id,int pageSize,boolean isMore) {
		JSONObject obj =  getDefaultParams();
		try {
			obj.put(ProtocolManager.COMMAND, "petalk");
			obj.put(ProtocolManager.OPTIONS, "list4Postcard");
			obj.put("id", id);
			obj.put("pageSize", pageSize);
			execute(obj, RequestCode.REQUEST_PETALKLIST4POSTCARD, isMore);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * 成长日记
     * @param pageIndex
     * @param pageSize
     */
    public void petalkListBook(int pageIndex,int pageSize) {
        JSONObject obj =  getDefaultParams();
        try {
            obj.put(ProtocolManager.COMMAND, "petalk");
            obj.put(ProtocolManager.OPTIONS, "list4Book");
            obj.put("pageIndex", pageIndex);
            obj.put("pageSize", pageSize);
            execute(obj, RequestCode.REQUEST_PETALKLIST4POSTCARD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取广场热门标签说说列表
     */
    public void getTopTagHotList() {
        JSONObject obj =  getDefaultParams();
        try {
            obj.put(ProtocolManager.COMMAND, "petalk");
            obj.put(ProtocolManager.OPTIONS, "topTagHotList");
//            execute(obj, RequestCode.REQUEST_GETTOPTAGHOTLIST);
            executeNew(obj,RequestCode.REQUEST_GETTOPTAGHOTLIST,false,"", false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
