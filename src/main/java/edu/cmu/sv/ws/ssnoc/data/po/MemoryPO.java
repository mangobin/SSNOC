package edu.cmu.sv.ws.ssnoc.data.po;

import java.util.Date;

public class MemoryPO {
	
	private Date createdAt;
	private long memoryID;
	private int usedVolatile;
	private int remainingVolatile;
	private int usedPersistent;
	private int remainingPersistent;
	
	public MemoryPO() {
		
	}
	
	public MemoryPO(Date createdAt, int usedVolatile, int remainingVolatile,
			int usedPersistent, int remainingPersistent) {
		super();
		this.createdAt = createdAt;
		this.usedVolatile = usedVolatile;
		this.remainingVolatile = remainingVolatile;
		this.usedPersistent = usedPersistent;
		this.remainingPersistent = remainingPersistent;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public long getMemoryID() {
		return memoryID;
	}
	public void setMemoryID(long memoryID) {
		this.memoryID = memoryID;
	}
	public int getUsedVolatile() {
		return usedVolatile;
	}
	public void setUsedVolatile(int usedVolatile) {
		this.usedVolatile = usedVolatile;
	}
	public int getRemainingVolatile() {
		return remainingVolatile;
	}
	public void setRemainingVolatile(int remainingVolatile) {
		this.remainingVolatile = remainingVolatile;
	}
	public int getUsedPersistent() {
		return usedPersistent;
	}
	public void setUsedPersistent(int usedPersistent) {
		this.usedPersistent = usedPersistent;
	}
	public int getRemainingPersistent() {
		return remainingPersistent;
	}
	public void setRemainingPersistent(int remainingPersistent) {
		this.remainingPersistent = remainingPersistent;
	}

	
}
