package edu.cmu.sv.ws.ssnoc.dto;

import com.google.gson.Gson;

public class Message {
	private String content;
	private String author;
	private String messageType;
	private String target;
	private String postAt;
	private long messageID;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getPostAt() {
		return postAt;
	}
	public void setPostAt(String postAt) {
		this.postAt = postAt;
	}
	public long getMessageID() {
		return messageID;
	}
	public void setMessageID(long messageID) {
		this.messageID = messageID;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	

}
