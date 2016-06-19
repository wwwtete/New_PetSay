package com.petsay.vo.personalcustom;

import java.io.Serializable;

public class OrderProductSpecDTO implements Serializable {

	private static final long serialVersionUID = 404282586472502498L;
	
	private String id;
	private String name;
	private String value;
//	private String count;
	
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}