package com.petsay.vo.petalk;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/2/9
 * @Description
 */
public class PetTagGroupVo implements Serializable {

    private static final long serialVersionUID = 1825786220724092320L;

    private String id;// 主键
    private String name;// 名称
    private String createTime;// 创建时间

    private List<PetTagVo> tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<PetTagVo> getTags() {
        return tags;
    }

    public void setTags(List<PetTagVo> tags) {
        this.tags = tags;
    }
}
