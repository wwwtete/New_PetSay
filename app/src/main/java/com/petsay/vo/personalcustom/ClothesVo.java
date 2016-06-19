package com.petsay.vo.personalcustom;

import java.io.Serializable;

/**
 * 服装
 * @author GJ
 *
 */
public class ClothesVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3290544310541891577L;
    private String id;
    private String name;
    private String description;
	private int price;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	
}
