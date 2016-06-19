package com.petsay.vo.petalk;

import java.io.Serializable;

public class LocationVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7629606891017257451L;
	private String positionName;// 位置名称
	private String positionLon;// 位置经度
	private String positionLat;// 位置纬度
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getPositionLon() {
		return positionLon;
	}
	public void setPositionLon(String positionLon) {
		this.positionLon = positionLon;
	}
	public String getPositionLat() {
		return positionLat;
	}
	public void setPositionLat(String positionLat) {
		this.positionLat = positionLat;
	}
	

}
