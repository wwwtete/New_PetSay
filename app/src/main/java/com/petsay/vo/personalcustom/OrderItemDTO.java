package com.petsay.vo.personalcustom;

import java.io.Serializable;
import java.util.List;

public class OrderItemDTO implements Serializable {

	private static final long serialVersionUID = 5588830332195130510L;
	
	//下面两个是冗余，但是需要
	private String productId;
	private long productUpdateTime;
	
	private String id;
	private String amount;
	//1使用2不使用
	private boolean useCard;
	private int count;
	private List<OrderProductSpecDTO> specs;
	
	
	//For show to client
//	private String productName;
//	private String productPirce;
//	private String productCover;
//	private String categoryName;
//	private String cardPrice;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	public long getProductUpdateTime() {
		return productUpdateTime;
	}

	public void setProductUpdateTime(long productUpdateTime) {
		this.productUpdateTime = productUpdateTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<OrderProductSpecDTO> getSpecs() {
		return specs;
	}

	public void setSpecs(List<OrderProductSpecDTO> specs) {
		this.specs = specs;
	}
	
	public boolean getUseCard() {
		return useCard;
	}

	public void setUseCard(boolean useCard) {
		this.useCard = useCard;
	}
	
//	public String getProductName() {
//		return productName;
//	}
//
//	public void setProductName(String productName) {
//		this.productName = productName;
//	}
//
//	public String getProductPirce() {
//		return productPirce;
//	}
//
//	public void setProductPirce(String productPirce) {
//		this.productPirce = productPirce;
//	}
//
//	public String getProductCover() {
//		return productCover;
//	}
//
//	public void setProductCover(String productCover) {
//		this.productCover = productCover;
//	}
//
//	public String getCategoryName() {
//		return categoryName;
//	}
//
//	public void setCategoryName(String categoryName) {
//		this.categoryName = categoryName;
//	}
//
//	public String getCardPrice() {
//		return cardPrice;
//	}
//
//	public void setCardPrice(String cardPrice) {
//		this.cardPrice = cardPrice;
//	}

}