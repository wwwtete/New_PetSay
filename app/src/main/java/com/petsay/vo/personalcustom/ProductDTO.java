package com.petsay.vo.personalcustom;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class ProductDTO implements Serializable {

	private static final long serialVersionUID = 8818970457256977794L;
	/** 商品ID */
	private String id;
	/** 商品状态 */
	private String state;
	/** 商品名称 */
	private String name;
	/** 商品标题(暂时用不到) */
	private String title;
	/** 类别(id,name)  例如明信片,服装等 */
	private CategoryDTO category;
	/** 1是有M卡，2是不可用M卡  */
	private String cardOption;
	/** 普通价格  */
	private float price;
	/** M卡价格  */
	private float cardPrice;
	/** 封面图片  */
	private String cover;
	/** 邮费  */
	private float shippingFee;
	/** 上架时间  */
	private long putOnTime;
	/** 编辑时间  */
	private long updateTime;
	/** 商品描述信息的图片  */
	private List<ProductDescPicDTO> descPics;
	/** 商品的图片  */
	private List<ProductPicDTO> pics;
	/** 商品的规格  */
	private List<SpecDTO> specs;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	public String getCardOption() {
		return cardOption;
	}

	public void setCardOption(String cardOption) {
		this.cardOption = cardOption;
	}

	public float getPrice() {
		BigDecimal b1 = new BigDecimal(price);
		return b1.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getCardPrice() {
		return cardPrice;
	}

	public void setCardPrice(float cardPrice) {
		this.cardPrice = cardPrice;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public float getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(float shippingFee) {
		this.shippingFee = shippingFee;
	}

	public long getPutOnTime() {
		return putOnTime;
	}

	public void setPutOnTime(long putOnTime) {
		this.putOnTime = putOnTime;
	}
	
	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public List<ProductDescPicDTO> getDescPics() {
		return descPics;
	}

	public void setDescPics(List<ProductDescPicDTO> descPics) {
		this.descPics = descPics;
	}

	public List<ProductPicDTO> getPics() {
		return pics;
	}

	public void setPics(List<ProductPicDTO> pics) {
		this.pics = pics;
	}

	public List<SpecDTO> getSpecs() {
		return specs;
	}

	public void setSpecs(List<SpecDTO> specs) {
		this.specs = specs;
	}

}