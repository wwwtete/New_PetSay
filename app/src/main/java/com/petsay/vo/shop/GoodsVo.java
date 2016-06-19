package com.petsay.vo.shop;

import java.io.Serializable;
import java.util.List;

public class GoodsVo implements Serializable {

	private static final long serialVersionUID = -2228086340368141318L;

	private String code;
	//商品名称
	private String name;
	//列表显示图片
	private String coverUrl;
	//描述
	private String description;
	private String detail;
	private int price;
	private String inventory;
	private String apply;
	private String sale;
	/**
	 * 商品类型
	 * trial("0", "试用"), //exchange("1", "兑换"), //
	 */
	private int salesMode;
	/**
	 * 试用
	 */
	public static int SalesMode_Free=0;
	/**
	 * 兑换
	 */
	public static int SalesMode_Exchange=1;
	
	/**
	 * 邮递方式（0包邮，1自付）
	 */
	private int postageType;
	//提供商
	private String supplier;
	/**
	 * 商品状态
	 * UNSELL("0", "即将开售"), //SELLING("1", "有货"), //SELLOUT("2", "无货"), //END("3", "活动结束"), //
	 */
	private int state;
	private long beginTime;
	private long endTime;

	/**
	 * 兑换状态
	 * CANCEL("0", "交易取消"), //APPLY("1", "申请中"), //PASS("2", "申请通过"), //REJECT("3", "申请驳回"), //EXCHANGE("4", "兑换成功"), /
	 */
	private String orderState;
	private String[] photos ;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getInventory() {
		return inventory;
	}

	public void setInventory(String inventory) {
		this.inventory = inventory;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

	public int getSalesMode() {
		return salesMode;
	}

	public void setSalesMode(int salesMode) {
		this.salesMode = salesMode;
	}

	public int getPostageType() {
		return postageType;
	}

	public void setPostageType(int postageType) {
		this.postageType = postageType;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public String getApply() {
		return apply;
	}

	public void setApply(String apply) {
		this.apply = apply;
	}

	public String[] getPhotos() {
		return photos;
	}

	public void setPhotos(String[] photos) {
		this.photos = photos;
	}

}
