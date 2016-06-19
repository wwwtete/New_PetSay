package com.petsay.vo.user;

import java.io.Serializable;
import java.util.Date;

public class UserShippingAddressDTO implements Serializable {

	private static final long serialVersionUID = -7832744307311749240L;
	
	private String id;
	private String userId;
	private String name;
	private String province;
	private String city;
	private String address;
	private String mobile;
	private String zipcode;
	private boolean isDefault;

    public UserShippingAddressDTO() {
    }

    public UserShippingAddressDTO(String name, String province, String city, String address, String mobile, String zipcode) {
        this.name = name;
        this.province = province;
        this.city = city;
        this.address = address;
        this.mobile = mobile;
        this.zipcode = zipcode;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
}