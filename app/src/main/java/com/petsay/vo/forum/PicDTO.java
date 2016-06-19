package com.petsay.vo.forum;

import java.io.Serializable;

/**
 * 图片
 * 
 * @author xiaoyi
 *
 */
public class PicDTO implements Serializable  {
	
	private static final long serialVersionUID = -1542832297284418361L;
	/**
	 * 宽高比
	 */
	private float scaleWH;
	private String pic;
	
	public float getScaleWH() {
		return scaleWH;
	}
	public void setScaleWH(float scaleWH) {
		this.scaleWH = scaleWH;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}


}
