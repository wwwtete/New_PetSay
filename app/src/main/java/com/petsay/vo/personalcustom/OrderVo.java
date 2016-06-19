package com.petsay.vo.personalcustom;

import java.io.Serializable;

public class OrderVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7698796120053401237L;
	private String id;
	private String thumbnail;
	private int count;
	private int price;
	private String description;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
