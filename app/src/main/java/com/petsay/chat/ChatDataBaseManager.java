package com.petsay.chat;

import android.content.Context;
import android.text.TextUtils;

import com.google.inject.Singleton;
import com.petsay.cache.SharePreferenceCache;
import com.petsay.constants.Constants;
import com.petsay.database.DBManager;
import com.petsay.database.greendao.DaoSession;
import com.petsay.database.greendao.chat.ChatBlackDao;
import com.petsay.database.greendao.chat.ChatContactsDao;
import com.petsay.database.greendao.chat.ChatMsgEntityDao;
import com.petsay.database.greendao.chat.NewestMsgDao;
import com.petsay.utile.PublicMethod;
import com.petsay.vo.chat.ChatBlack;
import com.petsay.vo.chat.ChatContacts;
import com.petsay.vo.chat.ChatMsgEntity;
import com.petsay.vo.chat.NewestMsg;

import java.util.Date;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/1/12
 * @Description
 */
@Singleton
public class ChatDataBaseManager {

    private static final String CHATDB_NAME = "-chat-db";

    private static ChatDataBaseManager instance = null;
    public static ChatDataBaseManager getInstance(){
        if(instance == null)
            instance = new ChatDataBaseManager();
        return instance;
    }

    private Context mContext;
    private DBManager mDBManager;
    //    private String mCurrPetID;
//    private SQLiteDatabase mDB;
//    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private ChatContactsDao mContactsDao;
    private ChatMsgEntityDao mMsgEntityDao;
    private NewestMsgDao mNewestMsgDao;
    private ChatBlackDao mBlackDao;
    //    private QueryBuilder<ChatContacts> mContactsQB;
//    private QueryBuilder<ChatMsgEntity> mMsgEntityQB;
    private ChatDataBaseManager(){
        mDBManager = mDBManager.getInstance();
    }

    public void init(Context context,String petID) {
        if(TextUtils.isEmpty(mDBManager.getCurrPetID()) || !petID.equals(mDBManager.getCurrPetID())) {
            release();
//            mCurrPetID = petID;
//            mContext = context;
//            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, petID + CHATDB_NAME, null);
//            mDB = helper.getWritableDatabase();
//            mDaoMaster = new DaoMaster(mDB);
//            mDaoSession = mDaoMaster.newSession();
            mDBManager.init(context,petID);
            mDaoSession = mDBManager.getDaoSession();
            mContactsDao = mDaoSession.getChatContactsDao();
            mMsgEntityDao = mDaoSession.getChatMsgEntityDao();
            mNewestMsgDao = mDaoSession.getNewestMsgDao();
            mBlackDao = mDaoSession.getChatBlackDao();
            QueryBuilder.LOG_SQL = Constants.isDebug;
            QueryBuilder.LOG_VALUES = Constants.isDebug;
        }
    }

    /**
     * 保存聊天消息内容
     * @param entity
     * @param operate 执行什么操作 -1:什么都不操作 0：消息数+1 1：归零
     * @return
     */
    public long saveMsgEntity(ChatMsgEntity entity,int operate){
        if(checkEnabled()) {
            synchronized (mMsgEntityDao) {
                long id = mMsgEntityDao.insert(entity);
                updateNewestMsg(entity.getPetId(),id,entity.getDate(),operate);
                return id;
            }
        }else{
            return -1;
        }
    }

    /**
     * 保存聊天消息内容
     * @param entity
     * @return
     */
    public long saveMsgEntity(ChatMsgEntity entity){
        if(checkEnabled()) {
            synchronized (mMsgEntityDao) {
                long id = mMsgEntityDao.insert(entity);
                updateNewestMsg(entity.getPetId(),id,entity.getDate(),-1);
                return id;
            }
        }else{
            return -1;
        }
    }

    /**
     * 更新聊天内容
     * @param entity
     */
    public void updateMsgEntity(ChatMsgEntity entity){
        if(checkEnabled()){
            mMsgEntityDao.update(entity);
        }
    }

    /**
     * 保存聊天联系人
     * @param contacts
     * @return
     */
    public long saveChatContacts(ChatContacts contacts){
        if(checkEnabled()) {
            synchronized (mContactsDao) {
                if (!hasContacts(contacts.getPetId())) {
                    return mContactsDao.insert(contacts);
                }
                return -1;
            }
        }else {
            return -1;
        }
    }

    /**
     * 添加黑名单
     * @param petId
     * @return
     */
    public long addChatBlack(String petId){
        if(checkEnabled()){
            ChatBlack black = new ChatBlack();
            black.setPetId(petId);
            return mBlackDao.insert(black);
        }
        return  -1;
    }

    /**
     * 查询黑名单是否存在指定的PetId
     * @param petId
     * @return
     */
    public boolean hasBlackPetId(String petId){
        if(checkEnabled()){
            return mBlackDao.queryBuilder().where(ChatBlackDao.Properties.PetId.eq(petId)).buildCount().count() > 0;
        }
        return false;
    }

    /**
     * 查询指定联系人是否存在
     * @param petId
     * @return
     */
    public boolean hasContacts(String petId){
        if(!TextUtils.isEmpty(petId) && checkEnabled()) {
            return mContactsDao.queryBuilder().where(ChatContactsDao.Properties.PetId.eq(petId)).buildCount().count() > 0;
        }
        return false;
    }

    /**
     * 查询指定最新消息是否存在
     * @param petId
     * @return
     */
    public boolean hasNewestMsg(String petId){
        if(!TextUtils.isEmpty(petId) && checkEnabled()){
            return mNewestMsgDao.queryBuilder().where(NewestMsgDao.Properties.PetId.eq(petId)).buildCount().count() > 0;
        }
        return false;
    }

    /**
     * 获取指定联系人列表
     * @param limit
     * @return
     */
    public List<ChatContacts> getChatContactsList(int limit){
        if(checkEnabled())
            return mContactsDao.queryBuilder().limit(limit).list();
        else
            return null;
    }

    /**
     * 查询指定联系人聊天内容总条数
     * @param petId
     * @return
     */
    public long getMsgEntityCountByid(String petId){
        if(checkEnabled()){
            return mMsgEntityDao.queryBuilder()
                    .whereOr(ChatMsgEntityDao.Properties.AccFromId.eq(petId), ChatMsgEntityDao.Properties.AccToId.eq(petId))
                    .buildCount().count();
        }
        return  0;
    }

    /**
     * 查询指定联系人聊天内容列表
     * @param petId
     * @param limit
     * @return
     */
    public List<ChatMsgEntity> getMsgEntityById(String petId,long start,int limit,boolean orderByDesc){
        if(checkEnabled() ) {
            QueryBuilder<ChatMsgEntity> qb = mMsgEntityDao.queryBuilder();
            qb = qb.whereOr(ChatMsgEntityDao.Properties.AccFromId.eq(petId), ChatMsgEntityDao.Properties.AccToId.eq(petId));
            if(start > -1){
                qb = qb.where(ChatMsgEntityDao.Properties.Id.lt(start));
            }
            if(orderByDesc)
                qb = qb.orderDesc(ChatMsgEntityDao.Properties.Date);
            return qb.limit(limit)
                    .list();
        }else{
            return null;
        }
    }

    public List<ChatMsgEntity> getAllChatMsgEntity(){
        if(checkEnabled())
            return mMsgEntityDao.loadAll();
        else return null;
    }

    public ChatContacts getChatContacts(String petID){
//        mContactsDao.queryBuilder().where(ChatContactsDao.Properties.PetId.eq(id)).list();
        return mContactsDao.load(petID);
    }

    public ChatMsgEntity getChatMsgEntitiy(long id){
        return  mMsgEntityDao.load(id);
    }

    /**
     * 获取未读消息总数
      * @return
     */
    public int getNewestMsgTotalCount(){
        int count = 0;
        if(checkEnabled()){
            List<NewestMsg> list = mNewestMsgDao.loadAll();
            if(list != null && !list.isEmpty()){
                for (int i=0;i<list.size();i++){
                    count += list.get(i).getMsgCount();
                }
            }
        }
        return count;
    }

    /**
     * 删除黑名单
     * @param petId
     */
    public void deleteChatBlackByPetId(String petId){
        if(checkEnabled() && !TextUtils.isEmpty(petId)){
            mBlackDao.queryBuilder()
                    .where(ChatBlackDao.Properties.PetId.eq(petId))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
        }
    }

    /**
     * 删除联系人信息
     * @param msg
     */
    public void deleteChatContacts(NewestMsg msg){
        if(checkEnabled() && msg != null){
            mMsgEntityDao.queryBuilder()
                    .whereOr(ChatMsgEntityDao.Properties.AccFromId.eq(msg.getPetId()), ChatMsgEntityDao.Properties.AccToId.eq(msg.getPetId()))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
            mContactsDao.deleteByKey(msg.getPetId());
            msg.delete();
        }
    }

    /**
     * 清空聊天消息
     * @param petId
     */
    public void clearChatMsgByPetId(String petId){
        if(checkEnabled() && !TextUtils.isEmpty(petId)){
            mMsgEntityDao.queryBuilder()
                    .whereOr(ChatMsgEntityDao.Properties.AccFromId.eq(petId), ChatMsgEntityDao.Properties.AccToId.eq(petId))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
            deleteNewestMsg(petId);
        }
    }

    /**
     * 删除单条聊天相信
     * @param msgEntityId
     */
    public void deleteChatMsgEntity(long msgEntityId,String petId){
        if(checkEnabled() && msgEntityId != -1){
            try{
                mMsgEntityDao.queryBuilder().where(ChatMsgEntityDao.Properties.Id.eq(msgEntityId))
                        .buildDelete()
                        .executeDeleteWithoutDetachingEntities();

                if(hasNewestMsg(petId)){
                    deleteNewestMsg(petId);
                }

            }catch (Exception e){
                PublicMethod.log_e("deleteChatMsgEntity","删除聊天信息异常");
            }
        }
    }

    /**
     * 删除最新消息
     * @param petId
     */
    public void deleteNewestMsg(String petId){
        if(!TextUtils.isEmpty(petId) && checkEnabled()){
            mNewestMsgDao.queryBuilder().where(NewestMsgDao.Properties.PetId.eq(petId))
                    .buildDelete()
                    .executeDeleteWithoutDetachingEntities();
        }
    }

    /**
     * 获取最新消息列表
     * @param limit
     * @return
     */
    public List<NewestMsg> getNewestMsgList(int limit){
        if(checkEnabled()){
            return mNewestMsgDao.queryBuilder()
                    .limit(limit)
                    .orderDesc(NewestMsgDao.Properties.Date)
                    .list();
        }
        return null;
    }

    /**
     * 更新最新聊天信息表
     * @param petId
     * @param msgId
     * @param operate 执行什么操作 -1:什么都不操作 0：消息数+1 1：归零
     */
    public void updateNewestMsg(String petId,long msgId,Date date,int operate){
        if(checkEnabled()){
            NewestMsg nMsg = mNewestMsgDao.load(petId);
            if(nMsg == null){
                int count = 0;
                if(operate == 0)
                    count = 1;
                nMsg = new NewestMsg(petId,msgId,count,date);
                mNewestMsgDao.insert(nMsg);
            }else {
                nMsg.setChatMsgEntityId(msgId);
                nMsg.setDate(date);
                if(operate == - 1) {
                }if(operate == 0){
                    nMsg.setMsgCount(nMsg.getMsgCount() + 1);
                }else {
                    nMsg.setMsgCount(0);
                }
                mNewestMsgDao.update(nMsg);
            }
        }
    }

    /**
     * 清空未读消息数
     * @param petId
     */
    public void clearMsgCount(String petId){
        if(checkEnabled()){
            NewestMsg nMsg =mNewestMsgDao.load(petId);
            if(nMsg != null){
                int count = nMsg.getMsgCount();
                if(count > 0){
                    int readCount = SharePreferenceCache.getSingleton(mContext).getReadMsgCount();
                    if(readCount > 0){
                        int temp = readCount - count;
                        SharePreferenceCache.getSingleton(mContext).setReadMsgCount(temp < 0 ? 0 : temp);
                    }
                }
                nMsg.setMsgCount(0);
                mNewestMsgDao.update(nMsg);
            }
        }
    }

    public boolean checkEnabled(){
        return mDBManager.isOpen();
//        return mDB != null && mDB.isOpen();
    }

    public void release(){
//        if(mDB != null) {
//            mDB.close();
//            mDB.releaseReference();
//        }
//        mCurrPetID = "";
        mDBManager.release();
    }


}
