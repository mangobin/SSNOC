package edu.cmu.sv.ws.ssnoc.dto;

import com.google.gson.Gson;

public class UserPassword {


	String password;
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
