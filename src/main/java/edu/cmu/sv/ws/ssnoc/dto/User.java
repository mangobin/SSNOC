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
	private Status lastStatusCode;
	private String privilegeLevel;
	private String accountStatus;
	private String latitude;
	private String longitude;
	private String location_updatedAt;

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLocation_updatedAt() {
		return location_updatedAt;
	}

	public void setLocation_updatedAt(String location_updatedAt) {
		this.location_updatedAt = location_updatedAt;
	}

	public String getPrivilegeLevel() {
		return privilegeLevel;
	}

	public void setPrivilegeLevel(String privilegeLevel) {
		this.privilegeLevel = privilegeLevel;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Status getLastStatusCode() {
		return lastStatusCode;
	}

	public void setLastStatusCode(Status lastStatus) {
		this.lastStatusCode = lastStatus;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createAt) {
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
