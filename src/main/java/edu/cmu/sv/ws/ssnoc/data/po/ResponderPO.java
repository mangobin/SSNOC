package edu.cmu.sv.ws.ssnoc.data.po;

import java.util.Date;

public class ResponderPO {
	private long responderId;
	private long requestId;
	private long userId;
	private Date updated_at;
	private String status;
	
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
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
