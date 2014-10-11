package edu.cmu.sv.ws.ssnoc.data.dao;

import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;

public interface IMemoryDAO {

	long save(MemoryPO po);
	
	MemoryPO findMemoryByID(long memoryId);
	void deleteAllMemoryCrumbs();
}
