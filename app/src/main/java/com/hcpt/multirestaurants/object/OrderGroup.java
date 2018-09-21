package com.hcpt.multirestaurants.object;

public class OrderGroup {

	private String id = "";
	private double price = 0;
	private String datetime = ""; // yyyy-mm-dd HH:MM:SS
	private String status = "1";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
