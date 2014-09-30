package edu.cmu.sv.ws.ssnoc.data.po;

import com.google.gson.Gson;

/**
 * This is the persistence class to save all user information in the system.
 * This contains information like the user's name, his role, his account status
 * and the password information entered by the user when signing up. <br/>
 * Information is saved in SSN_USERS table.
 * 
 */
public class UserPO {
	private long userId;
	private String userIdStr;
	private String userName;
	private String password;
	private String salt;
	private String createdAt;
	private String modifiedAt;
	private String lastStatusID;
	
	
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getLastStatusID() {
		return lastStatusID;
	}

	public void setLastStatusID(String lastStatusID) {
		this.lastStatusID = lastStatusID;
	}

	
	public String getModifiedAt() {
		
		return modifiedAt;
	}

	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	

	public String getCreateAt() {
		return createdAt;
	}

	public void setCreateAt(String createAt) {
		this.createdAt = createAt;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public String getUserIdStr(){
		return userIdStr;
	}
	
	public void setUserIdStr(String userIdStr){
		this.userIdStr = userIdStr;
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

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
