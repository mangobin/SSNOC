package edu.cmu.sv.ws.ssnoc.data.po;

import java.util.Date;

import com.google.gson.Gson;

public class StatusPO {

	private String statusId;
	
	private String userId;
	
	private Date updatedAt;
	
	private int statusCode;

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	public static class Builder {
		private String statusId;
		private String userId;
		private Date updatedAt;
		private int statusCode;
		
		public Builder setStatusId(String statusId){
			this.statusId = statusId;
			return this;
		}
		
		public Builder setUserId(String userId){
			this.userId = userId;
			return this;
		}
		
		public Builder setUpdatedAt(Date updatedAt){
			this.updatedAt = updatedAt;
			return this;
		}
		
		public Builder setStatusCode(int statusCode){
			this.statusCode = statusCode;
			return this;
		}
		
		public StatusPO build(){
			StatusPO po = new StatusPO();
			
			po.setStatusId(statusId);
			po.setUserId(userId);
			po.setUpdatedAt(updatedAt);
			po.setStatusCode(statusCode);
			
			return po;
		}	
	}
	
}
