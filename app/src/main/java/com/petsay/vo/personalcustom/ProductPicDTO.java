package com.petsay.vo.personalcustom;

import java.io.Serializable;

/** 商品的图片  */
public class ProductPicDTO implements Serializable {

	private static final long serialVersionUID = 5588830332195130510L;
	
	private String pic;
	
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

}