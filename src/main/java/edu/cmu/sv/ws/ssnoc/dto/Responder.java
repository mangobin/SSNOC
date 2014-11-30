package edu.cmu.sv.ws.ssnoc.dto;

public class Responder {
	private long responderId;
	private long requestId;
	private long userId;
	private String username;
	private String updated_at;
	private String status;
	public static final String DEFAULT_STATUS = "Pending";
	
	public long getResponderId() {
		return responderId;
	}
	public void setResponderId(long responderId) {
		this.responderId = responderId;
	}
	public long getRequestId() {
		return requestId;
	}
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
