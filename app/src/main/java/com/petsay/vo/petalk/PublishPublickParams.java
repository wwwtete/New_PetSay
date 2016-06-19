package com.petsay.vo.petalk;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布说说或故事公用的参数
 * @author wangw (wwwtete@163.com)
 * @CreateDate 2015/8/1
 * @Description
 */
public class PublishPublickParams implements Serializable {

    private static final long serialVersionUID = 6742651415625816890L;

    /**0-图音；1-纯图；2:故事*/
    public int model = 1;
    public String description= "";
    public List<Position> decorations = new ArrayList<Position>();
    public List<PetTagVo> tags = new ArrayList<PetTagVo>();
    public String photoUrl = "";
    public String thumbUrl = "";


    public class Position {
        @JSONField(serialize = false)
        public long id = -1;
        public String decorationId = "";
        public double width = 0;
        public double height = 0;
        public double centerX = 0;
        public double centerY = 0;
        public double rotationX = 0;
        public double rotationY = 0;
        public double rotationZ = 0;
    }

    public void setTag(PetTagVo tag){
        if(tag == null)
            return;
        tags.clear();
        tags.add(tag);
    }

}
