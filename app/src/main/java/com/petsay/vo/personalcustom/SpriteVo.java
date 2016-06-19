package com.petsay.vo.personalcustom;

import java.io.Serializable;

public class SpriteVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1228162374580689688L;
	private int id;
	/**
	 * 元素类型：1头像，2说说图片，3说说文字内容，4，日期(年月日),5昵称,6时间
	 */
	private int type;
	private int startX;
	
	private int startY;
	//区域宽度
	private int spriteWidth;
	//区域高度
	private int spriteHeight;
	//字体
	private String font;
	//字体大小
	private int fontSize;
	//特殊区域标识
	private String special;
	
	//是否镂空
	private int piercedType;
	//对齐方式
	private String alignType;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStartX() {
		return startX;
	}
	public void setStartX(int startX) {
		this.startX = startX;
	}
	public int getStartY() {
		return startY;
	}
	public void setStartY(int startY) {
		this.startY = startY;
	}
	public int getSpriteWidth() {
		return spriteWidth;
	}
	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}
	public int getSpriteHeight() {
		return spriteHeight;
	}
	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}
	public String getFont() {
		return font;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public String getSpecial() {
		return special;
	}
	public void setSpecial(String special) {
		this.special = special;
	}
	public int getPiercedType() {
		return piercedType;
	}
	public void setPiercedType(int piercedType) {
		this.piercedType = piercedType;
	}
	public String getAlignType() {
		return alignType;
	}
	public void setAlignType(String alignType) {
		this.alignType = alignType;
	}
}
