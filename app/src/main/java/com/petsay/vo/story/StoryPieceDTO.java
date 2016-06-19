package com.petsay.vo.story;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/8/3
 * @Description
 */
public class StoryPieceDTO implements Serializable, Parcelable {

    private static final long serialVersionUID = 7441941479353544697L;

    private String id;// 主键
    private int type;//类型
    private List<String> typeVals;// 值数组

    public StoryPieceDTO() {
    }

    public StoryPieceDTO(int type, List<String> typeVals) {
        this.type = type;
        this.typeVals = typeVals;
    }

    public StoryPieceDTO(int type) {
        this.type = type;
        this.typeVals = new ArrayList<String>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getTypeVals() {
        return typeVals;
    }

    public void addTypeVals(String vals){
        this.typeVals.add(vals);
    }

    public void addTypeVals(int location,String vals){
        this.typeVals.add(location,vals);
    }


    public void setTypeVals(List<String> typeVals) {
        this.typeVals = typeVals;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.type);
        dest.writeStringList(this.typeVals);
    }

    protected StoryPieceDTO(Parcel in) {
        this.id = in.readString();
        this.type = in.readInt();
        this.typeVals = in.createStringArrayList();
    }

    public static final Creator<StoryPieceDTO> CREATOR = new Creator<StoryPieceDTO>() {
        public StoryPieceDTO createFromParcel(Parcel source) {
            return new StoryPieceDTO(source);
        }

        public StoryPieceDTO[] newArray(int size) {
            return new StoryPieceDTO[size];
        }
    };
}
