package com.petsay.vo.petalk;

import android.content.Context;

import com.petsay.constants.Constants;
import com.petsay.database.greendao.DaoSession;
import com.petsay.database.greendao.petsay.DecorationPositionDao;
import com.petsay.database.greendao.petsay.DraftboxVoDao;
import com.petsay.utile.FileUtile;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table DRAFTBOX_VO.
 */
public class DraftboxVo {

    private Long id;
    private Integer model;
    private String petId;
    private String photoPath;
    private String audioPath;
    private String thumbPath;
    private String description;
    private Integer audioSecond;
    private java.util.Date createTime;
    private String tag;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient DraftboxVoDao myDao;

    private List<DecorationPosition> decorations;

    public DraftboxVo() {
    }

    public DraftboxVo(Long id) {
        this.id = id;
    }

    public DraftboxVo(Long id, Integer model, String petId, String photoPath, String audioPath, String thumbPath, String description, Integer audioSecond, java.util.Date createTime, String tag) {
        this.id = id;
        this.model = model;
        this.petId = petId;
        this.photoPath = photoPath;
        this.audioPath = audioPath;
        this.thumbPath = thumbPath;
        this.description = description;
        this.audioSecond = audioSecond;
        this.createTime = createTime;
        this.tag = tag;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDraftboxVoDao() : null;
    }

    public static DraftboxVo parsePublishTalkParam(Context context,PublishTalkParam param){
        if(param == null)
            return null;
        DraftboxVo vo = new DraftboxVo();
        String path = FileUtile.getPath(context,Constants.SDCARD_DRAFTBOX);
        String photo =  path+ UUID.randomUUID().toString()+".png";
        boolean flag = FileUtile.saveImage(photo, param.editImg, 100);
        if(flag) {
            vo.setPhotoPath(photo);
        }else {
            return null;
        }
        String thumb = path + UUID.randomUUID().toString()+".png";
        flag = FileUtile.saveImage(thumb,param.thumbImg,100);
        if(flag) {
            vo.setThumbPath(thumb);
        }else {
            return null;
        }
        vo.setModel(param.model);
        if(param.model == 0) {
            if(param.audioFile != null) {
                vo.setAudioPath(param.audioFile.getPath());
                vo.setAudioSecond(param.audioSecond);
            }
        }
        vo.setDescription(param.description);
        vo.setPetId(param.petId);
        vo.setCreateTime(new Date());

        if(param.tags != null && !param.tags.isEmpty()) {
            vo.setTag(param.tags.get(0).getId());
        }
        return vo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAudioSecond() {
        return audioSecond;
    }

    public void setAudioSecond(Integer audioSecond) {
        this.audioSecond = audioSecond;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<DecorationPosition> getDecorations() {
        if (decorations == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DecorationPositionDao targetDao = daoSession.getDecorationPositionDao();
            List<DecorationPosition> decorationsNew = targetDao._queryDraftboxVo_Decorations(id);
            synchronized (this) {
                if(decorations == null) {
                    decorations = decorationsNew;
                }
            }
        }
        return decorations;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetDecorations() {
        decorations = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}