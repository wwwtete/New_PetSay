package com.petsay.vo.personalcustom;

import java.io.Serializable;

public class CategoryDTO implements Serializable {

	private static final long serialVersionUID = 5588830332195130510L;
	/**明信片id =1<br>日记 id =2<br>服装id =3**/
	private String id;
	private String pid;
	private String name;
	private String level;
	private String productCount;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getProductCount() {
		return productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}

}