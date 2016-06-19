package com.petsay.vo.personalcustom;

import java.io.Serializable;

public class SpecValueDTO implements Serializable {

	private static final long serialVersionUID = 7706454171990542935L;
	
	private String id;
	private String value;
	private float price;
	private String cardPrice;
	//OrderProductSpecDTO.Class id 非网络获取项，在选择商品规格时用于判断
	private String specId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getCardPrice() {
		return cardPrice;
	}

	public void setCardPrice(String cardPrice) {
		this.cardPrice = cardPrice;
	}

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

}