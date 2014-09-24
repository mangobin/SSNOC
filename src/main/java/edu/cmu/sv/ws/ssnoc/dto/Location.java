package edu.cmu.sv.ws.ssnoc.dto;

import com.google.gson.Gson;

public class Location {

	private float longitude;
	
	private float latitude;
	
	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
}
