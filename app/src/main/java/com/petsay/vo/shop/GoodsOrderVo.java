package com.petsay.vo.shop;

import java.io.Serializable;

public class GoodsOrderVo implements Serializable {

	private static final long serialVersionUID = 2282110211968841642L;

	private String no;
	private String petId;
	private String receiveKey;
	private int state;
	private long updateTime;
	private long createTime;

	private String goodsCode;
	private String goodsName;
	private String goodsCoverUrl;
	private String goodsDescription;
	private int goodsPrice;
	private int goodsSalesMode;
	private int goodsPostageType;
	private String goodsSupplierId;
	private String goodsSupplier;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getPetId() {
		return petId;
	}

	public void setPetId(String petId) {
		this.petId = petId;
	}

	public String getReceiveKey() {
		return receiveKey;
	}

	public void setReceiveKey(String receiveKey) {
		this.receiveKey = receiveKey;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsCoverUrl() {
		return goodsCoverUrl;
	}

	public void setGoodsCoverUrl(String goodsCoverUrl) {
		this.goodsCoverUrl = goodsCoverUrl;
	}

	public String getGoodsDescription() {
		return goodsDescription;
	}

	public void setGoodsDescription(String goodsDescription) {
		this.goodsDescription = goodsDescription;
	}

	public int getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(int goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getGoodsSalesMode() {
		return goodsSalesMode;
	}

	public void setGoodsSalesMode(int goodsSalesMode) {
		this.goodsSalesMode = goodsSalesMode;
	}

	public int getGoodsPostageType() {
		return goodsPostageType;
	}

	public void setGoodsPostageType(int goodsPostageType) {
		this.goodsPostageType = goodsPostageType;
	}

	public String getGoodsSupplierId() {
		return goodsSupplierId;
	}

	public void setGoodsSupplierId(String goodsSupplierId) {
		this.goodsSupplierId = goodsSupplierId;
	}

	public String getGoodsSupplier() {
		return goodsSupplier;
	}

	public void setGoodsSupplier(String goodsSupplier) {
		this.goodsSupplier = goodsSupplier;
	}

//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}

}
