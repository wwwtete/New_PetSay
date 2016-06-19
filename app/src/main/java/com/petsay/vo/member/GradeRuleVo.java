package com.petsay.vo.member;

import java.io.Serializable;

/**
 * Created by wangw on 2014/12/16.
 * 等级规则
 */
public class GradeRuleVo implements Serializable {

    private static final long serialVersionUID = 1240454034596699826L;

    private String code;
    private String level;
    private String score;
    private String memo;
    private int scoreMin;
    private int scoreMax;

    public int getScoreMax() {
        return scoreMax;
    }

    public void setScoreMax(int scoreMax) {
        this.scoreMax = scoreMax;
    }

    public int getScoreMin() {
        return scoreMin;
    }

    public void setScoreMin(int scoreMin) {
        this.scoreMin = scoreMin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
