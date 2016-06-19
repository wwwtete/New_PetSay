package com.petsay.database.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.petsay.database.greendao.chat.ChatBlackDao;
import com.petsay.database.greendao.chat.ChatContactsDao;
import com.petsay.database.greendao.chat.ChatMsgEntityDao;
import com.petsay.database.greendao.chat.NewestMsgDao;
import com.petsay.database.greendao.petsay.DecorationPositionDao;
import com.petsay.database.greendao.petsay.DraftboxVoDao;
import com.petsay.database.greendao.petsay.PetTagVoDao;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 3): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 3;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        ChatContactsDao.createTable(db, ifNotExists);
        ChatMsgEntityDao.createTable(db, ifNotExists);
        NewestMsgDao.createTable(db, ifNotExists);
        ChatBlackDao.createTable(db, ifNotExists);
        PetTagVoDao.createTable(db, ifNotExists);
        DecorationPositionDao.createTable(db, ifNotExists);
        DraftboxVoDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        ChatContactsDao.dropTable(db, ifExists);
        ChatMsgEntityDao.dropTable(db, ifExists);
        NewestMsgDao.dropTable(db, ifExists);
        ChatBlackDao.dropTable(db, ifExists);
        PetTagVoDao.dropTable(db, ifExists);
        DecorationPositionDao.dropTable(db, ifExists);
        DraftboxVoDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(ChatContactsDao.class);
        registerDaoClass(ChatMsgEntityDao.class);
        registerDaoClass(NewestMsgDao.class);
        registerDaoClass(ChatBlackDao.class);
        registerDaoClass(PetTagVoDao.class);
        registerDaoClass(DecorationPositionDao.class);
        registerDaoClass(DraftboxVoDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
