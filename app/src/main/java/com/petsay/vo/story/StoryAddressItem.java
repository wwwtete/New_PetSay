package com.petsay.vo.story;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/14
 * @Description
 */
public class StoryAddressItem extends StoryItemVo {

    private String address;
    private String time;

    @Override
    public boolean setStoryPieceDto(StoryPieceDTO dto) {
        if(dto == null || dto.getTypeVals().isEmpty())
            return false;
        List<String> vals= dto.getTypeVals();
        address = vals.get(0);
        time = vals.get(1);
        return true;
    }

    public StoryAddressItem() {
        this.type = TYPE_ADDRESS_TIME;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(this.type);
//        dest.writeString(this.address);
//        dest.writeString(this.createTime);
//    }
//
//    public StoryAddressItem(int type) {
//        this.type = type;
//    }
//
//    protected StoryAddressItem(Parcel in) {
//        this.type = in.readInt();
//        this.address = in.readString();
//        this.createTime = in.readString();
//    }
//
//    public static final Creator<StoryAddressItem> CREATOR = new Creator<StoryAddressItem>() {
//        public StoryAddressItem createFromParcel(Parcel source) {
//            return new StoryAddressItem(source);
//        }
//
//        public StoryAddressItem[] newArray(int size) {
//            return new StoryAddressItem[size];
//        }
//    };
}
