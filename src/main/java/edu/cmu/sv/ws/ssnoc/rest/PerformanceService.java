package edu.cmu.sv.ws.ssnoc.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.MessageDAOImpl;

@Path("/performance")
public class PerformanceService extends BaseService{

	@POST
	@Path("/setup")
	public void setUpPerformance() {
		MessageDAOImpl.FAKE = true;
	}
	
	@POST
	@Path("/teardown")
	public void tearDownPerformance() {
		MessageDAOImpl.FAKE = false;
		DAOFactory.getInstance().getMessageDAO().deleteFakeMessageTable();
	}
}
