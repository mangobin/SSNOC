package edu.cmu.sv.ws.ssnoc.data.po;

import java.util.Date;

import com.google.gson.Gson;

public class StatusPO {

	private long statusId;
	
	private String userName;
	
	private Date updatedAt;
	
	private String statusCode;

	private float locLat;
	
	private float locLng;

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

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	public static class Builder {
		private long statusId;
		private String userName;
		private Date updatedAt;
		private String statusCode;
		private float locLat;
		private float locLng;

		public Builder setLocLat(float locLat){
			this.locLat = locLat;
			return this;
		}
		
		public Builder setLocLng(float locLng){
			this.locLng = locLng;
			return this;
		}
		
		public Builder setStatusId(long statusId){
			this.statusId = statusId;
			return this;
		}
		
		public Builder setUserName(String userName){
			this.userName = userName;
			return this;
		}
		
		public Builder setUpdatedAt(Date updatedAt){
			this.updatedAt = updatedAt;
			return this;
		}
		
		public Builder setStatusCode(String statusCode){
			this.statusCode = statusCode;
			return this;
		}
		
		public StatusPO build(){
			StatusPO po = new StatusPO();
			
			po.setStatusId(statusId);
			po.setUserName(userName);
			po.setUpdatedAt(updatedAt);
			po.setStatusCode(statusCode);
			po.setLocLat(locLat);
			po.setLocLng(locLng);
			
			return po;
		}	
	}
	
}
