package com.petsay.vo.petalk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.petsay.database.DBManager;
import com.petsay.database.greendao.petsay.PetTagVoDao;
import com.petsay.vo.decoration.DecorationBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw
 * 创建说说参数实体类
 */
@JSONType(ignores = {"mouth","audioFile","editImg","thumbImg"})
public class PublishTalkParam extends PublishPublickParams {

	public String petId = "";
    public String audioUrl = "";
	public int audioSecond = 0;
//	public String positionName= "";
//	public double positionLon = 0;
//	public double positionLat = 0;


//    public PetalkDecorationVo position;
    /***************不需要序列化的属性*********************/
    @JSONField(serialize = false)
    public long id= -1;
    @JSONField(serialize = false)
    public DecorationBean mouth;
    @JSONField(serialize = false)
    public File audioFile;
    @JSONField(serialize = false)
    public Bitmap editImg;
    @JSONField(serialize = false)
    public Bitmap thumbImg;
    @JSONField(serialize = false)
    public File editFile;
    @JSONField(serialize = false)
    public Bitmap cameraImg;

    public static PublishTalkParam parseDraftBoxVo(DraftboxVo vo){
        if(vo == null)
            return null;
        PublishTalkParam param = new PublishTalkParam();
        param.id = vo.getId();
        param.model = vo.getModel();
        param.description = vo.getDescription();
        param.petId = vo.getPetId();
        DBManager dbManager = DBManager.getInstance();
        if(!TextUtils.isEmpty(vo.getTag()) && dbManager.isOpen()) {
            List<PetTagVo> tags = dbManager.getDaoSession().getPetTagVoDao().queryBuilder()
                    .where(PetTagVoDao.Properties.Id.eq(vo.getTag()))
                    .build()
                    .list();
            if(tags != null && !tags.isEmpty()){
                param.tags.addAll(tags);
            }
        }
        if(param.model == 0){
             if(!TextUtils.isEmpty(vo.getAudioPath())){
                 param.audioFile = new File(vo.getAudioPath());
             }
            param.audioSecond = vo.getAudioSecond();
            if(!vo.getDecorations().isEmpty()){
                param.decorations.clear();
                param.decorations.add(param.parseDecorationPosition(vo.getDecorations().get(0)));
            }
        }
        if(!TextUtils.isEmpty(vo.getPhotoPath())){
            param.editFile = new File(vo.getPhotoPath());
        }
        if(!TextUtils.isEmpty(vo.getThumbPath())){
//            InputStream stream = FileUtile.readImageBySdCard(vo.getThumbPath());
//            if(stream != null){
//                param.thumbImg = BitmapFactory.decodeStream(stream);
//            }
            param.thumbImg = BitmapFactory.decodeFile(vo.getThumbPath());
        }
        return  param;
    }

    public Position parseDecorationPosition(DecorationPosition dp){
        if(dp == null)
            return  null;
        Position pp = new Position();
        pp.id = dp.getId();
        pp.decorationId = dp.getDecorationId();
        pp.width = dp.getWidth();
        pp.height = dp.getHeight();
        pp.centerX = dp.getCenterX();
        pp.centerY = dp.getCenterY();
        pp.rotationX = dp.getRotationX();
        pp.rotationY = dp.getRotationY();
        pp.rotationZ = dp.getRotationY();
        return pp;
    }

    /**********************************/
	
    public void recycle(){
        if(editImg != null)
            editImg.recycle();
        editImg = null;
        if(thumbImg != null)
            thumbImg.recycle();
    }

    public void release(){
        recycle();
        thumbImg = null;
        mouth = null;
        audioFile = null;
    }

}
