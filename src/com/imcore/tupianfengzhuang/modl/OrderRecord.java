package com.imcore.tupianfengzhuang.modl;

public class OrderRecord {
	public long id;
	public String orderDate;
	public long totalAmount;
	public String orderItems;
	public long userId;
	public long customerId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(String orderItems) {
		this.orderItems = orderItems;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

}
