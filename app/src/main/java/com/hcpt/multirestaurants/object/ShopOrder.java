package com.hcpt.multirestaurants.object;

import java.util.ArrayList;

public class ShopOrder {

	private String orderId;
	private String accountId;
	private int shopId;
	private String shopName, shopImage;
	private String orderAddress;
	private double totalPrice;
	private double grandTotal;
	private double vAT;
	private double shipping;
	private String orderTime;
	private int orderStatus;
	private int paymentStatus;
	private int paymentMethod;
	private ArrayList<Menu> arrFoods;
	private int currentItemsNumber = 0;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public int getShopId() {
		return shopId;
	}

	public void setShopId(int shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getOrderAddress() {
		return orderAddress;
	}

	public void setOrderAddress(String orderAddress) {
		this.orderAddress = orderAddress;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getGrandTotal() {
		// return totalPrice + vAT + shipping;
		return this.grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public double getVAT() {
		return vAT;
	}

	public void setVAT(double vAT) {
		this.vAT = vAT;
	}

	public double getShipping() {
		return shipping;
	}

	public void setShipping(double shipping) {
		this.shipping = shipping;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(int paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public int getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(int paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public ArrayList<Menu> getArrFoods() {
		return arrFoods;
	}

	public void setArrFoods(ArrayList<Menu> arrFoods) {
		this.arrFoods = arrFoods;
		for (Menu food : arrFoods) {
			currentItemsNumber += food.getOrderNumber();
		}
	}

	public String getShopImage() {
		return shopImage;
	}

	public void setShopImage(String shopImage) {
		this.shopImage = shopImage;
	}

	public int getNumberItems() {
		return currentItemsNumber;
	}

}