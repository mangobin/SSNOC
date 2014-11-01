package edu.cmu.sv.ws.ssnoc.dto;

public class Memory {

	private String createdAt;
	private long memoryID;
	private long usedVolatile;
	private long remainingVolatile;
	private long usedPersistent;
	private long remainingPersistent;
	
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public long getMemoryID() {
		return memoryID;
	}
	public void setMemoryID(long memoryID) {
		this.memoryID = memoryID;
	}
	public long getUsedVolatile() {
		return usedVolatile;
	}
	public void setUsedVolatile(long usedVolatile) {
		this.usedVolatile = usedVolatile;
	}
	public long getRemainingVolatile() {
		return remainingVolatile;
	}
	public void setRemainingVolatile(long remainingVolatile) {
		this.remainingVolatile = remainingVolatile;
	}
	public long getUsedPersistent() {
		return usedPersistent;
	}
	public void setUsedPersistent(long usedPersistent) {
		this.usedPersistent = usedPersistent;
	}
	public long getRemainingPersistent() {
		return remainingPersistent;
	}
	public void setRemainingPersistent(long remainingPersistent) {
		this.remainingPersistent = remainingPersistent;
	}

}
