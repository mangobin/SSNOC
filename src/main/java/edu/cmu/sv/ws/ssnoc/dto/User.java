package edu.cmu.sv.ws.ssnoc.dto;

import com.google.gson.Gson;

/**
 * This object contains user information that is responded as part of the REST
 * API request.
 * 
 */
public class User {
	private String userName;
	private String password;
	private String createdAt;
	private String lastStatusCode;

	public String getLastStatusCode() {
		return lastStatusCode;
	}

	public void setLastStatusCode(String lastStatusCode) {
		this.lastStatusCode = lastStatusCode;
	}

	public String getCreateAt() {
		return createdAt;
	}

	public void setCreateAt(String createAt) {
		this.createdAt = createAt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
