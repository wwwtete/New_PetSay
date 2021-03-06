package com.petsay.vo.chat;

import com.petsay.database.greendao.DaoSession;
import com.petsay.database.greendao.chat.ChatContactsDao;
import com.petsay.database.greendao.chat.ChatMsgEntityDao;

import java.util.List;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table CHAT_CONTACTS.
 */
public class ChatContacts {

    private String petId;
    private String headPortrait;
    private String nickName;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ChatContactsDao myDao;

    private List<ChatMsgEntity> chatMsgEntityList;

    public ChatContacts() {
    }

    public ChatContacts(String petId) {
        this.petId = petId;
    }

    public ChatContacts(String petId, String headPortrait, String nickName) {
        this.petId = petId;
        this.headPortrait = headPortrait;
        this.nickName = nickName;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChatContactsDao() : null;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<ChatMsgEntity> getChatMsgEntityList() {
        if (chatMsgEntityList == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChatMsgEntityDao targetDao = daoSession.getChatMsgEntityDao();
            List<ChatMsgEntity> chatMsgEntityListNew = targetDao._queryChatContacts_ChatMsgEntityList(petId);
            synchronized (this) {
                if(chatMsgEntityList == null) {
                    chatMsgEntityList = chatMsgEntityListNew;
                }
            }
        }
        return chatMsgEntityList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetChatMsgEntityList() {
        chatMsgEntityList = null;
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
