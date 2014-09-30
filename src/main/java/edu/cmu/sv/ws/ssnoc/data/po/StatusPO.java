package edu.cmu.sv.ws.ssnoc.data.po;

import java.util.Date;

import com.google.gson.Gson;

import edu.cmu.sv.ws.ssnoc.dto.Location;

public class StatusPO {

	private String statusId;
	
	private String userName;
	
	private Date updatedAt;
	
	private String statusCode;

	private Location location;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}


	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
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
		private String statusId;
		private String userName;
		private Date updatedAt;
		private String statusCode;
		
		public Builder setStatusId(String statusId){
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
			
			return po;
		}	
	}
	
}
