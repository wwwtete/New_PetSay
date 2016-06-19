package com.petsay.vo.personalcustom;

import java.io.Serializable;

public class PostCardVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5967020277128071318L;
	private int id;
	private int width;
	private int height;
	private SpriteVo[] sprites;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public SpriteVo[] getSprites() {
		return sprites;
	}
	public void setSprites(SpriteVo[] sprites) {
		this.sprites = sprites;
	}
	

}
