package com.petsay.vo.forum;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 创建讨论主题参数
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/4/1
 * @Description
 */
public class CreateTopicParams implements Parcelable {


    public List<PicDTO> pictures = new ArrayList<PicDTO>(3);
    public String topicId;
    public String petId;
    public String content;

    /***************不需要序列化的属性*********************/
    @JSONField(serialize = false)
    public HashMap<String,PicDTO> selectPicMap = new HashMap<String, PicDTO>(3);

    public CreateTopicParams() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.topicId);
        dest.writeString(this.petId);
        dest.writeString(this.content);
        dest.writeList(this.pictures);
        dest.writeMap(this.selectPicMap);
    }

    private CreateTopicParams(Parcel in) {
        this.topicId = in.readString();
        this.petId = in.readString();
        this.content = in.readString();
        this.pictures = new ArrayList<PicDTO>();
        in.readList(this.pictures, PicDTO.class.getClassLoader());
        this.selectPicMap = in.readHashMap(HashMap.class.getClassLoader());
    }

    @JSONField(serialize = false)
    public static final Creator<CreateTopicParams> CREATOR = new Creator<CreateTopicParams>() {
        public CreateTopicParams createFromParcel(Parcel source) {
            return new CreateTopicParams(source);
        }

        public CreateTopicParams[] newArray(int size) {
            return new CreateTopicParams[size];
        }
    };
}
