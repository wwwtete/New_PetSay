package com.petsay.vo.user;

public class AddressInfo {
	private String Province;
	private String[] city;
	public String getProvince() {
		return Province;
	}
	public void setProvince(String province) {
		Province = province;
	}
	public String[] getCity() {
		return city;
	}
	public void setCity(String[] city) {
		this.city = city;
	}
}
