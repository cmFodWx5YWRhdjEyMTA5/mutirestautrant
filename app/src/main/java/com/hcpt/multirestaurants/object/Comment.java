package com.hcpt.multirestaurants.object;

public class Comment {
	private String id;
	private String content = "";
	private String createdDate;
	private float rateValue;
	private String userID;
	private String userName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public float getRateValue() {
		return rateValue;
	}

	public void setRateValue(float rateValue) {
		this.rateValue = rateValue;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
