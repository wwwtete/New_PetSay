package com.petsay.vo.petalk;

import android.text.TextUtils;

import com.petsay.constants.Constants;
import com.petsay.utile.PublicMethod;

import java.io.Serializable;

public class PetVo implements Serializable {
	private static final long serialVersionUID = -6469871493012768675L;

    private String id;// ID
	private String userId;// 主人ID
	private String headPortrait;// 头像
	private String backgroundImg;// 背景
	private String nickName;// 呢称
	private int gender;// 性别
	private int type;// 品种
	private long birthday;// 生日
	private String age;
	private String address;// 地址
	private boolean active;// 最后一次登陆
	private long createTime;// 创建时间

	private int rs;// 关注关系
	private PetCounterVo counter;// 计数器
	private String star;// 达宠
    private int score;// 积分余额
    private String grade;// 当前等级
    private int coin;// 金币余额

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    /**
     * 获取用户等级，
     * @return 返回带有前缀的用户等级，格式：DJ01，如果想要int类型的值，请调用getIntGrade方法
     */
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    /**
     * 返回格式化后的用户等级
     * @return
     */
    public int getIntGrade(){
        int num = 0;
        if(TextUtils.isEmpty(grade)){
            return num;
        }else if(grade.contains("DJ") || grade.contains("dj")){
            try{
                String temp = grade.substring(2,grade.length());
                num = Integer.valueOf(temp);
            }catch (Exception e){
                PublicMethod.log_e("getIntGrade","转换等级字符串异常");
            }

        }
         return num;
    }

    /**
     * 获取宠物等级图标ResId
     * @return 资源ID
     */
    public int getLevenIconResId(){
        if(getIntGrade() == 0) {
            return -1;
        }else if(getIntGrade() > 0 && getIntGrade() <= Constants.LEVEL_ICONS.length){
            return Constants.LEVEL_ICONS[getIntGrade() - 1];
        }else{
            return Constants.LEVEL_ICONS[0];
        }

    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	public String getBackgroundImg() {
		return backgroundImg;
	}

	public void setBackgroundImg(String backgroundImg) {
		this.backgroundImg = backgroundImg;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getRs() {
		return rs;
	}

	public void setRs(int rs) {
		this.rs = rs;
	}

	public PetCounterVo getCounter() {
		return counter;
	}

	public void setCounter(PetCounterVo counter) {
		this.counter = counter;
	}

	public String getAge() {
		return PublicMethod.getAge(getBirthday(), true);
	}
	public String getAge(boolean isShowMonth) {
		return PublicMethod.getAge(getBirthday(), isShowMonth);
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

}
