package com.petsay.vo.personalcustom;

import java.io.Serializable;
import java.util.List;

import com.petsay.vo.coupon.CouponDTO;


public class OrderDTO implements Serializable {

	private static final long serialVersionUID = 8477155305394952550L;
	
	private String id;
	private String petId;
	private String userId;
	private float amount;
	private float productAmount;
	private String discountAmount;
	private String productCount;
	private String cardCount;
	private String pChargeId;
	private String payChannel;
	private float shippingFee;
	private String shippingName;
	private String shippingMobile;
	private String shippingAddress;
	private String shippingProvince;
	private String shippingCity;
	private String shippingZipcode;
	private String note;
	private String state;
	private String stateDesc;
	private long createTime;
	private long payTime;
	private long confirmTime;
	private long updateTime;
	private long deliverTime;
	private long cancelTime;
	private boolean useCoupon;
	
	private List<OrderProductDTO> orderProducts;
	private List<OrderItemDTO> orderItems;
	private CouponDTO coupon;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getPetId() {
		return petId;
	}

	public void setPetId(String petId) {
		this.petId = petId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getProductCount() {
		return productCount;
	}

	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}

	public String getCardCount() {
		return cardCount;
	}

	public void setCardCount(String cardCount) {
		this.cardCount = cardCount;
	}

	public String getpChargeId() {
		return pChargeId;
	}

	public void setpChargeId(String pChargeId) {
		this.pChargeId = pChargeId;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public float getShippingFee() {
		return shippingFee;
	}

	public void setShippingFee(float shippingFee) {
		this.shippingFee = shippingFee;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getShippingMobile() {
		return shippingMobile;
	}

	public void setShippingMobile(String shippingMobile) {
		this.shippingMobile = shippingMobile;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	public String getShippingProvince() {
		return shippingProvince;
	}

	public void setShippingProvince(String shippingProvince) {
		this.shippingProvince = shippingProvince;
	}

	public String getShippingCity() {
		return shippingCity;
	}

	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}

	public String getShippingZipcode() {
		return shippingZipcode;
	}

	public void setShippingZipcode(String shippingZipcode) {
		this.shippingZipcode = shippingZipcode;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public long getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(long deliverTime) {
		this.deliverTime = deliverTime;
	}
	
	public long getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(long cancelTime) {
		this.cancelTime = cancelTime;
	}

	public List<OrderItemDTO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemDTO> orderItems) {
		this.orderItems = orderItems;
	}
	
	public List<OrderProductDTO> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProducts(List<OrderProductDTO> orderProducts) {
		this.orderProducts = orderProducts;
	}
	
	public CouponDTO getCoupon() {
		return coupon;
	}

	public void setCoupon(CouponDTO coupon) {
		this.coupon = coupon;
	}
	
	public String getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}

	public boolean getUseCoupon() {
		return useCoupon;
	}

	public void setUseCoupon(boolean useCoupon) {
		this.useCoupon = useCoupon;
	}
	
	public float getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(float productAmount) {
		this.productAmount = productAmount;
	}

}