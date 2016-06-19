package com.petsay.vo.story;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/14
 * @Description
 */
public class StoryTextItem extends StoryItemVo{

    private String content;

    public StoryTextItem(String content) {
        this.type = TYPE_TEXT;
        this.content = content;
    }

    @Override
    public boolean setStoryPieceDto(StoryPieceDTO dto) {
        if(dto == null || dto.getTypeVals().isEmpty())
            return false;
        content = dto.getTypeVals().get(0);
        return true;
    }

    public StoryTextItem() {
        this.type = TYPE_TEXT;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.type);
//        dest.writeString(this.content);
//    }
//
//    protected StoryTextItem(Parcel in) {
//        this.type =in.readInt();
//        this.content = in.readString();
//    }
//
//    public static final Creator<StoryTextItem> CREATOR = new Creator<StoryTextItem>() {
//        public StoryTextItem createFromParcel(Parcel source) {
//            return new StoryTextItem(source);
//        }
//
//        public StoryTextItem[] newArray(int size) {
//            return new StoryTextItem[size];
//        }
//    };
}
