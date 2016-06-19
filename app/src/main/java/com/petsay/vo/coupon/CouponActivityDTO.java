package com.petsay.vo.coupon;

import java.io.Serializable;
import java.util.List;

public class CouponActivityDTO implements Serializable {

	private static final long serialVersionUID = -3831260480476864539L;
	
	private String id;
	private String name;
	private String desc;
	private String pic;
	private List<CouponActivitySettingsDTO> settings;
	
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
	public List<CouponActivitySettingsDTO> getSettings() {
		return settings;
	}

	public void setSettings(List<CouponActivitySettingsDTO> settings) {
		this.settings = settings;
	}

	
}
