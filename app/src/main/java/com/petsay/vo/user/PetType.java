package com.petsay.vo.user;

public class PetType {
	//类型id
	private int typeId;
	//类型名称（如狗 猫等）
	private String typeName;
	//宠物类名（藏獒，吉娃娃等）
	private String[] name;
	//宠物id
	private int[] id;
	
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String[] getName() {
		return name;
	}
	public void setName(String[] name) {
		this.name = name;
	}
	public int[] getId() {
		return id;
	}
	public void setId(int[] id) {
		this.id = id;
	}

}
