package com.petsay.vo.chat;

import com.petsay.database.greendao.DaoSession;
import com.petsay.database.greendao.chat.ChatContactsDao;
import com.petsay.database.greendao.chat.ChatMsgEntityDao;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table CHAT_MSG_ENTITY.
 */
public class ChatMsgEntity {

    private Long id;
    private java.util.Date date;
    private String text;
    private Integer mediaTime;
    private String type;
    private Boolean isComMeg;
    private String accFromId;
    private String accToId;
    private Integer states;
    /** Not-null value. */
    private String petId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ChatMsgEntityDao myDao;

    private ChatContacts chatContacts;
    private String chatContacts__resolvedKey;


    public ChatMsgEntity() {
    }

    public ChatMsgEntity(Long id) {
        this.id = id;
    }

    public ChatMsgEntity(Long id, java.util.Date date, String text, Integer mediaTime, String type, Boolean isComMeg, String accFromId, String accToId, Integer states, String petId) {
        this.id = id;
        this.date = date;
        this.text = text;
        this.mediaTime = mediaTime;
        this.type = type;
        this.isComMeg = isComMeg;
        this.accFromId = accFromId;
        this.accToId = accToId;
        this.states = states;
        this.petId = petId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChatMsgEntityDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getMediaTime() {
        return mediaTime;
    }

    public void setMediaTime(Integer mediaTime) {
        this.mediaTime = mediaTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsComMeg() {
        return isComMeg;
    }

    public void setIsComMeg(Boolean isComMeg) {
        this.isComMeg = isComMeg;
    }

    public String getAccFromId() {
        return accFromId;
    }

    public void setAccFromId(String accFromId) {
        this.accFromId = accFromId;
    }

    public String getAccToId() {
        return accToId;
    }

    public void setAccToId(String accToId) {
        this.accToId = accToId;
    }

    public Integer getStates() {
        return states;
    }

    public void setStates(Integer states) {
        this.states = states;
    }

    /** Not-null value. */
    public String getPetId() {
        return petId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPetId(String petId) {
        this.petId = petId;
    }

    /** To-one relationship, resolved on first access. */
    public ChatContacts getChatContacts() {
        String __key = this.petId;
        if (chatContacts__resolvedKey == null || chatContacts__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChatContactsDao targetDao = daoSession.getChatContactsDao();
            ChatContacts chatContactsNew = targetDao.load(__key);
            synchronized (this) {
                chatContacts = chatContactsNew;
            	chatContacts__resolvedKey = __key;
            }
        }
        return chatContacts;
    }

    public void setChatContacts(ChatContacts chatContacts) {
        if (chatContacts == null) {
            throw new DaoException("To-one property 'petId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.chatContacts = chatContacts;
            petId = chatContacts.getPetId();
            chatContacts__resolvedKey = petId;
        }
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
