package com.petsay.database.greendao.petsay;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.petsay.database.greendao.DaoSession;

import com.petsay.vo.petalk.PetTagVo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PET_TAG_VO.
*/
public class PetTagVoDao extends AbstractDao<PetTagVo, Void> {

    public static final String TABLENAME = "PET_TAG_VO";

    /**
     * Properties of entity PetTagVo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", false, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property IconUrl = new Property(2, String.class, "iconUrl", false, "ICON_URL");
        public final static Property BgUrl = new Property(3, String.class, "bgUrl", false, "BG_URL");
        public final static Property DetailUrl = new Property(4, String.class, "detailUrl", false, "DETAIL_URL");
        public final static Property Ctrl = new Property(5, String.class, "ctrl", false, "CTRL");
        public final static Property Deleted = new Property(6, Boolean.class, "deleted", false, "DELETED");
        public final static Property CreateTime = new Property(7, String.class, "createTime", false, "CREATE_TIME");
    };


    public PetTagVoDao(DaoConfig config) {
        super(config);
    }
    
    public PetTagVoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PET_TAG_VO' (" + //
                "'ID' TEXT," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'ICON_URL' TEXT," + // 2: iconUrl
                "'BG_URL' TEXT," + // 3: bgUrl
                "'DETAIL_URL' TEXT," + // 4: detailUrl
                "'CTRL' TEXT," + // 5: ctrl
                "'DELETED' INTEGER," + // 6: deleted
                "'CREATE_TIME' TEXT);"); // 7: createTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PET_TAG_VO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PetTagVo entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String iconUrl = entity.getIconUrl();
        if (iconUrl != null) {
            stmt.bindString(3, iconUrl);
        }
 
        String bgUrl = entity.getBgUrl();
        if (bgUrl != null) {
            stmt.bindString(4, bgUrl);
        }
 
        String detailUrl = entity.getDetailUrl();
        if (detailUrl != null) {
            stmt.bindString(5, detailUrl);
        }
 
        String ctrl = entity.getCtrl();
        if (ctrl != null) {
            stmt.bindString(6, ctrl);
        }
 
        Boolean deleted = entity.getDeleted();
        if (deleted != null) {
            stmt.bindLong(7, deleted ? 1l: 0l);
        }
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(8, createTime);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public PetTagVo readEntity(Cursor cursor, int offset) {
        PetTagVo entity = new PetTagVo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // iconUrl
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // bgUrl
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // detailUrl
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ctrl
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0, // deleted
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // createTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PetTagVo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIconUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBgUrl(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDetailUrl(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCtrl(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setDeleted(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
        entity.setCreateTime(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(PetTagVo entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(PetTagVo entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}