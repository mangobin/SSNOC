package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.RequestPO;

public interface IRequestDAO {
	
	long save(RequestPO po);
	RequestPO findRequestById(long id);
	List<RequestPO> findAllRequestsByUserName(String userName);
	List<RequestPO> getAllRequests(int limit, int offset);

}
