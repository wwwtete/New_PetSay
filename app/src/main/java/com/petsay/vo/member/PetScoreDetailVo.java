package com.petsay.vo.member;

import java.io.Serializable;

/**
 * @author wangw (404441027@qq.com)
 * Date: 2014/12/18.
 * 积分详情
 */
public class PetScoreDetailVo implements Serializable {

    private static final long serialVersionUID = -2683740505658953334L;

    private String id;
    private String petId;
    private int blsign;     //本次交易表示，-1:表示减分，1:表示加分
    private String amount;  //交易积分
    private String balance;
    private String ruleCode;
    private String memo;
    private long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public int getBlsign() {
        return blsign;
    }

    public void setBlsign(int blsign) {
        this.blsign = blsign;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


}
