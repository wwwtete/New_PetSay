package com.petsay.vo.petalk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TagPetalkDTO implements Serializable {
	private static final long serialVersionUID = 3209246374139388463L;

	private String id;// 主键
	private String type;// 类型
	private String name;// 名称
	private String iconUrl;// 图标
	private String bgUrl;// 背景图
	private String detailUrl;// 详情介绍
	private String ctrl;// tab控制器
	private String deleted;// 删除
	private String createTime;// 创建时间
	
	private List<PetalkVo> petalks = new ArrayList<PetalkVo>();

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

	public String getBgUrl() {
		return bgUrl;
	}

	public void setBgUrl(String bgUrl) {
		this.bgUrl = bgUrl;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getCtrl() {
		return ctrl;
	}

	public void setCtrl(String ctrl) {
		this.ctrl = ctrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
	public List<PetalkVo> getPetalks() {
		return petalks;
	}

	public void setPetalks(List<PetalkVo> petalks) {
		this.petalks = petalks;
	}

}
