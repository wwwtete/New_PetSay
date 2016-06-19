package com.petsay.vo.petalk;

import com.petsay.vo.story.StoryPieceDTO;

import java.io.Serializable;
import java.util.List;



/**
 * @author wangw
 * 宠物说说基础类，所有关于说说的都继承自这个类
 */
public class PetalkVo implements Serializable {

	private static final long serialVersionUID = -6262311410661762569L;
	
	private String id;// Interaction主键
    private int model;// 0：图音；1：纯图 2:故事
    private String type;// 操作类型
	private String aimPetId;// 宠物ID
	private String aimPetNickName;// 宠物昵称
	private String aimPetHeadPortrait;// 宠物头像
	private String comment;// 评论内容
	private String commentAudioUrl;// 音频资源标识
	private String commentAudioSecond;// 音频时长
	private long relayTime;// 转发时间

	private String petalkId;// Petalk主键
	private String petId;// 宠物ID
	private String petNickName;// 宠物昵称
	private String petHeadPortrait;// 宠物头像
	private String thumbUrl;// 作品缩略图
	private String photoUrl;// 照片资源标识
	private String audioUrl;// 音频资源标识	
	
	private String audioOriginUrl;// 原始音频资源标识
	private String audioSecond;// 音频时长
	private String positionName;// 位置名称
	private String positionLon;// 位置经度
	private String positionLat;// 位置纬度
	private String description;// 描述
	private long createTime;// 创建时间

	private int rs;// 关注关系0未关注,1已关注,2相互关注,9自己
	private int z;// 赞

	public PetVo getPet() {
		return pet;
	}
	public void setPet(PetVo pet) {
		this.pet = pet;
	}
	private PetTagVo[] tags;// 标签
	private PetalkCounterVo counter;// 计数器
	private PetalkDecorationVo[] decorations;// 饰品
    private StoryPieceDTO[] storyPieces;    //故事模式
	/**上次播放时间*/
	private int mprePlayTime = 0;
	
	private List<CommentVo> c;
	private List<CommentVo> f;
	
	
	
	
	private PetVo pet;


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }
	public void setType(String type) {
		this.type = type;
	}
	public String getAimPetId() {
		return aimPetId;
	}
	public void setAimPetId(String aimPetId) {
		this.aimPetId = aimPetId;
	}
	public String getAimPetNickName() {
		return aimPetNickName;
	}
	public void setAimPetNickName(String aimPetNickName) {
		this.aimPetNickName = aimPetNickName;
	}
	public String getAimPetHeadPortrait() {
		return aimPetHeadPortrait;
	}
	public void setAimPetHeadPortrait(String aimPetHeadPortrait) {
		this.aimPetHeadPortrait = aimPetHeadPortrait;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentAudioUrl() {
		return commentAudioUrl;
	}
	public void setCommentAudioUrl(String commentAudioUrl) {
		this.commentAudioUrl = commentAudioUrl;
	}
	public String getCommentAudioSecond() {
		return commentAudioSecond;
	}
	public void setCommentAudioSecond(String commentAudioSecond) {
		this.commentAudioSecond = commentAudioSecond;
	}
	public long getRelayTime() {
		return relayTime;
	}
	public void setRelayTime(long relayTime) {
		this.relayTime = relayTime;
	}
	public String getPetalkId() {
		return petalkId;
	}
	public void setPetalkId(String petalkId) {
		this.petalkId = petalkId;
	}
	public String getPetId() {
		return petId;
	}
	public void setPetId(String petId) {
		this.petId = petId;
	}
	public String getPetNickName() {
		return petNickName;
	}
	public void setPetNickName(String petNickName) {
		this.petNickName = petNickName;
	}
	public String getPetHeadPortrait() {
		return petHeadPortrait;
	}
	public void setPetHeadPortrait(String petHeadPortrait) {
		this.petHeadPortrait = petHeadPortrait;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	public String getPhotoUrl() {
		return photoUrl;
	}
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
	public String getAudioUrl() {
		return audioUrl;
	}
	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}
	public String getAudioOriginUrl() {
		return audioOriginUrl;
	}
	public void setAudioOriginUrl(String audioOriginUrl) {
		this.audioOriginUrl = audioOriginUrl;
	}
	public String getAudioSecond() {
		return audioSecond;
	}
	public void setAudioSecond(String audioSecond) {
		this.audioSecond = audioSecond;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getPositionLon() {
		return positionLon;
	}
	public void setPositionLon(String positionLon) {
		this.positionLon = positionLon;
	}
	public String getPositionLat() {
		return positionLat;
	}
	public void setPositionLat(String positionLat) {
		this.positionLat = positionLat;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getRs() {
		return rs;
	}
	public void setRs(int rs) {
		this.rs = rs;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public PetTagVo[] getTags() {
		return tags;
	}
	public void setTags(PetTagVo[] tags) {
		this.tags = tags;
	}
	public PetalkCounterVo getCounter() {
		return counter;
	}
	public void setCounter(PetalkCounterVo counter) {
		this.counter = counter;
	}
	public PetalkDecorationVo[] getDecorations() {
		return decorations;
	}
	
	public int getPrePlayTime() {
		return mprePlayTime;
	}
	public void setPrePlayTime(int mprePlayTime) {
		this.mprePlayTime = mprePlayTime;
	}
	public void setDecorations(PetalkDecorationVo[] decorations) {
		this.decorations = decorations;
	}
	
	public List<CommentVo> getC() {
		return c;
	}
	public void setC(List<CommentVo> c) {
		this.c = c;
	}
	public List<CommentVo> getF() {
		return f;
	}
	public void setF(List<CommentVo> f) {
		this.f = f;
	}

    public StoryPieceDTO[] getStoryPieces() {
        return storyPieces;
    }

    public void setStoryPieces(StoryPieceDTO[] storyPieces) {
        this.storyPieces = storyPieces;
    }

    /**
     * 是否语音模式
     * @return
     */
    public boolean isAudioModel(){
        return model == 0 && getDecorations() != null && getDecorations().length > 0;
    }

    /**
     * 是否为故事模式
     * @return
     */
    public boolean isStoryModel(){
        return model == 2 && storyPieces != null && storyPieces.length > 0;
    }

    public String getStoryTitle(){
        if(storyPieces == null || storyPieces.length < 3)
            return "";
        return storyPieces[0].getTypeVals().get(1);
    }

    public String getStoryTime(){
        if(storyPieces == null || storyPieces.length < 3)
            return "";
        return storyPieces[0].getTypeVals().get(2);
    }

}
