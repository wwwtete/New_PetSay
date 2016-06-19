package com.petsay.vo.personalcustom;

import java.io.Serializable;

public class OrderProductDTO implements Serializable {

	private static final long serialVersionUID = 5588830332195130510L;
	
	private String productId;
	private String categoryName;
	private String name;
	private float price;
	private String cover;
	private String cardPrice;
	private String cardOption;
	
//	private String id;
	//下面的快照，暂时客户端不需要
//	private String descPics;
//	private String pics;
//	private String updateTime;
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getCardOption() {
		return cardOption;
	}

	public void setCardOption(String cardOption) {
		this.cardOption = cardOption;
	}

}