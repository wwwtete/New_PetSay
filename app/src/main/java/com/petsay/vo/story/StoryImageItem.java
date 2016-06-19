package com.petsay.vo.story;

import android.text.TextUtils;

import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/14
 * @Description
 */
public class StoryImageItem extends StoryItemVo{

    private String imageUrl;
    private String describe = "";
    private String audioUrl = "";
    private long audioSeconds;
    /**-1:上传失败 | 0：未上传或为空 | 1:上传中 | 2上传成功*/
    private int imageStatus = 0;
    private int audioStatus = 0;
    private float scaleWH;


    public StoryImageItem(String url) {
        this.type = TYPE_IMAGE;
        this.imageUrl = url;
    }

    public boolean setStoryPieceDto(StoryPieceDTO dto){
        if(dto == null || dto.getTypeVals().isEmpty())
            return false;
        List<String> vals= dto.getTypeVals();
        imageUrl = vals.get(0);
        scaleWH = TextUtils.isEmpty(vals.get(1)) ? 0 : Float.parseFloat(vals.get(1));
        describe = vals.get(2);
        audioUrl = vals.get(3);
        audioSeconds = TextUtils.isEmpty(vals.get(4)) ? 0 : Long.parseLong(vals.get(4));
        return true;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public long getAudioSeconds() {
        return audioSeconds;
    }

    public void setAudioSeconds(long audioSeconds) {
        this.audioSeconds = audioSeconds;
    }

    public int getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(int imageStatus) {
        this.imageStatus = imageStatus;
    }

    public int getAudioStatus() {
        return audioStatus;
    }

    public void setAudioStatus(int audioStatus) {
        this.audioStatus = audioStatus;
    }

    public float getScaleWH() {
        return scaleWH;
    }

    public void setScaleWH(float scaleWH) {
        this.scaleWH = scaleWH;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.type);
//        dest.writeString(this.imageUrl);
//    }
//
//    public StoryImageItem(int type) {
//        this.type = type;
//    }
//
//    protected StoryImageItem(Parcel in) {
//        this.type = in.readInt();
//        this.imageUrl = in.readString();
//    }
//
//    public static final Creator<StoryImageItem> CREATOR = new Creator<StoryImageItem>() {
//        public StoryImageItem createFromParcel(Parcel source) {
//            return new StoryImageItem(source);
//        }
//
//        public StoryImageItem[] newArray(int size) {
//            return new StoryImageItem[size];
//        }
//    };
}
