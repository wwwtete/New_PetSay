package com.petsay.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.inject.Singleton;
import com.petsay.constants.Constants;
import com.petsay.database.greendao.DaoMaster;
import com.petsay.database.greendao.DaoSession;
import com.petsay.utile.PublicMethod;

import de.greenrobot.dao.query.QueryBuilder;


/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/9
 * @Description
 */
@Singleton
public class DBManager {
    private static final String CHATDB_NAME = "-chat-db";
    public static DBManager instance;
    private Context mContext;
    private String mCurrPetID;
    private SQLiteDatabase mDB;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public static DBManager getInstance(){
        if(instance == null){
            instance = new DBManager();
        }
        return instance;
    }

    private DBManager(){
    }

    public void init(Context application,String petID){
        release();
        if(TextUtils.isEmpty(mCurrPetID) || !petID.equals(mCurrPetID)) {
            mCurrPetID = petID;
            mContext = application;
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(application, petID + CHATDB_NAME, null);
            mDB = helper.getWritableDatabase();
            mDaoMaster = new DaoMaster(mDB);
            mDaoSession = mDaoMaster.newSession();
            QueryBuilder.LOG_SQL = Constants.isDebug;
            QueryBuilder.LOG_VALUES = Constants.isDebug;
        }
    }

    public boolean isOpen(){
        return mDB != null && mDB.isOpen();
    }

    public SQLiteDatabase getDB() {
        return mDB;
    }

    public DaoMaster getDaoMaster() {
        return mDaoMaster;
    }

    public String getCurrPetID() {
        return mCurrPetID;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void close(){
        PublicMethod.log_e("database", "数据库被关闭");
        if(mDB != null) {
            mDB.close();
        }
    }

    public void release(){
        PublicMethod.log_e("database", "释放数据库");
        if(mDB != null) {
            close();
            mDB.releaseReference();
        }
        mCurrPetID = "";
    }



}
