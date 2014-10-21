package edu.cmu.sv.ws.ssnoc.dto;

import com.google.gson.Gson;

public class Status {
	
	private String updatedAt;
	
	private String statusCode;
	
	private String userName;
	
	private float locLat;
	
	private float locLng;
	
	private long statusId;
	
//	private long userId;
//	
//	public long getUserId() {
//		return userId;
//	}
//
//	public void setUserId(long userId) {
//		this.userId = userId;
//	}

	public long getStatusId() {
		return statusId;
	}

	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public float getLocLat(){
		return locLat;
	}
	
	public void setLocLat(float locLat){
		this.locLat = locLat;
	}
	
	public float getLocLng(){
		return locLng;
	}
	
	public void setLocLng(float locLng){
		this.locLng = locLng;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
