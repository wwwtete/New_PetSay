package com.petsay.vo.story;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/14
 * @Description
 */
public abstract class StoryItemVo implements Serializable{

    public static final int TYPE_ADDRESS_TIME = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_TEXT = 2;

    /**0:时间地点 | 1：图片 | 2：文字*/
    protected int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public abstract boolean setStoryPieceDto(StoryPieceDTO dto);


//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.type);
//    }

    public StoryItemVo() {
    }

//    protected StoryItemVo(Parcel in) {
//        this.type = in.readInt();
//    }
//
//    public static final Creator<StoryItemVo> CREATOR = new Creator<StoryItemVo>() {
//        public StoryItemVo createFromParcel(Parcel source) {
//            return new StoryItemVo(source);
//        }
//
//        public StoryItemVo[] newArray(int size) {
//            return new StoryItemVo[size];
//        }
//    };
}
