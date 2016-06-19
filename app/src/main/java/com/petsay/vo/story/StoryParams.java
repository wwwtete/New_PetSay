package com.petsay.vo.story;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.petsay.utile.PetsayLog;
import com.petsay.vo.petalk.PublishPublickParams;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/7/13
 * @Description
 */
public class StoryParams extends PublishPublickParams {


    public List<StoryPieceDTO> storyPieces = new ArrayList<StoryPieceDTO>();

    /**不需要序列化的属性*/
    @JSONField(serialize = false)
    public List<StoryItemVo> items = new ArrayList<StoryItemVo>(15);
//    @JSONField(serialize = false)
//    public String title;
    @JSONField(serialize = false)
    public String createTime;
//    @JSONField(serialize = false)
//    public String coverUrl;





    public String toJson(){
        storyPieces.clear();
        //封面
        StoryPieceDTO cover = new StoryPieceDTO(1);
        cover.addTypeVals(this.photoUrl);
        cover.addTypeVals(description);
        cover.addTypeVals(createTime);
        storyPieces.add(cover);

        //内容
        for (int i=0;i<items.size();i++){
            StoryItemVo itemVo = items.get(i);
            StoryPieceDTO dto = null;
            switch (itemVo.getType()){
                case StoryItemVo.TYPE_IMAGE:
                    dto = getStoryImageItem((StoryImageItem) itemVo);
                    break;
                case StoryItemVo.TYPE_ADDRESS_TIME:
                    dto = getStoryAddressItem((StoryAddressItem) itemVo);
                    break;
                case StoryItemVo.TYPE_TEXT:
                    dto = getStoryTextItem((StoryTextItem) itemVo);
                    break;
            }
            if(dto != null)
                storyPieces.add(dto);
        }

        //封底
        storyPieces.add(new StoryPieceDTO(5));

        String json = JSON.toJSONString(this);
        PetsayLog.e("[toJson]json => "+json);

        return json;
    }

    private StoryPieceDTO getStoryTextItem(StoryTextItem itemVo) {
        StoryPieceDTO dto = new StoryPieceDTO(3);
        dto.addTypeVals(itemVo.getContent());
        return dto;
    }

    private StoryPieceDTO getStoryAddressItem(StoryAddressItem itemVo) {
        StoryPieceDTO dto = new StoryPieceDTO(4);
        dto.addTypeVals(itemVo.getAddress());
        dto.addTypeVals(itemVo.getTime());
        return dto;
    }

    private StoryPieceDTO getStoryImageItem(StoryImageItem itemVo) {
        StoryPieceDTO dto = new StoryPieceDTO(2);
        dto.addTypeVals(itemVo.getImageUrl());
        dto.addTypeVals(itemVo.getScaleWH()+"");
        dto.addTypeVals(itemVo.getDescribe());
        dto.addTypeVals(itemVo.getAudioUrl());
        dto.addTypeVals(itemVo.getAudioSeconds()+"");
        return dto;
    }


//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeList(this.items);
//        dest.writeString(this.title);
//    }
//
//    protected StoryParams(Parcel in) {
//        this.items = new ArrayList<StoryItemVo>();
//        in.readList(this.items, List.class.getClassLoader());
//        this.title = in.readString();
//    }
//
//    public static final Creator<StoryParams> CREATOR = new Creator<StoryParams>() {
//        public StoryParams createFromParcel(Parcel source) {
//            return new StoryParams(source);
//        }
//
//        public StoryParams[] newArray(int size) {
//            return new StoryParams[size];
//        }
//    };
}
