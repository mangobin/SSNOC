package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.ResponderPO;

public interface IResponderDAO {
	
	long save(ResponderPO po);
	ResponderPO findResponderById(long id);
	List<ResponderPO> findRespondersByUserName(String username);
	List<ResponderPO> findRespondersByRequestId(long requestId);
	
	

}
