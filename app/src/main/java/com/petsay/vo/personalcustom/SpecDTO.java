package com.petsay.vo.personalcustom;

import java.io.Serializable;
import java.util.List;

public class SpecDTO implements Serializable {

	private static final long serialVersionUID = -3653256810499874998L;
	
	private String id;
	private String name;
	private String type;
	private List<SpecValueDTO> values;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<SpecValueDTO> getValues() {
		return values;
	}

	public void setValues(List<SpecValueDTO> values) {
		this.values = values;
	}

}