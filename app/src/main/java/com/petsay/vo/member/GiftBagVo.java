package com.petsay.vo.member;

import java.io.Serializable;

/**
 * Created by wangw on 2014/12/16.
 *  礼包数据模型
 *
 * state对应状态：
 *   disable("0", "停用"), //
     enable("1", "正常"), //
     lock("2", "未解锁"), //
     unlock("3", "已解锁"), //
     draw("4", "已领取"), //
     expired("5", "已过期"), //
 *
 */
public class GiftBagVo implements Serializable {
    private static final long serialVersionUID = 3929203613963934092L;

    private String code;
    private String name;
    private String icon;
    private int state;
    private String description;

    private boolean preview;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     *
     * @return
    disable("0", "停用"), //
    enable("1", "正常"), //
    lock("2", "未解锁"), //
    unlock("3", "已解锁")(已解锁的才能领取礼包), //
    draw("4", "已领取"), //
    expired("5", "已过期"), //
     */
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    /**
     * 获取列表状态
     * @return
    disable("0", "停用"), //
    enable("1", "正常"), //
    lock("2", "未解锁"), //
    unlock("3", "已解锁"), //
    draw("4", "已领取"), //
    expired("5", "已过期"), //
     */
    public String getGiftBagState(){
        if(state == 1 && preview){
            return "可预览";
        }else if(state == 0){
            return "已停用";
        }else if(state == 2){
            return "未解锁";
        }else if(state == 3){
            return "已解锁";
        }else if(state == 4){
            return "已领取";
        }else if(state == 5){
            return "已过期";
        }else{
            return "";
        }
    }

}
