package edu.cmu.sv.ws.ssnoc.rest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;

@Path("/performance")
public class PerformanceService extends BaseService{

	@POST
	@Path("/setup")
	public void setUpPerformance() {
		if(!DAOFactory.fake ) {
			DAOFactory.fake = true;
			DAOFactory.getInstance().getMessageDAO().truncateMessageTable();	
		}
		
	}
	
	@POST
	@Path("/teardown")
	public void tearDownPerformance() {
		if(DAOFactory.fake ) {
			DAOFactory.getInstance().getMessageDAO().truncateMessageTable();
			DAOFactory.fake = false;
	
		}
	}
}
