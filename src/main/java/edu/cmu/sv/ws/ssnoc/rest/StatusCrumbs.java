package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElementWrapper;

import edu.cmu.sv.ws.ssnoc.common.exceptions.UnknownUserException;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.nosql.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Status;

@Path("/statuscrumbs")
public class StatusCrumbs extends BaseService {

	@GET
	@Path("/{userName}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@XmlElementWrapper(name = "statuses")
	public List<Status> findStatus(@PathParam("userName") String userName){
		Log.enter(userName);
		
		Log.debug("Searching for user: " + userName);
		// verify user exists, if Not -- return 404?
		IUserDAO dao = DAOFactory.getInstance().getUserDAO();
		UserPO existingUser = dao.findByName(userName);
		if(existingUser == null){
			throw new UnknownUserException(userName);
		}
		
		List<StatusPO> pos = DAOFactory.getInstance().getStatusDAO().findLatestStatusesByUserId(existingUser.getUserIdStr(), 100, 0);
		List<Status> statuses = new ArrayList<Status>();
		
		for(StatusPO po : pos){
			Status status = new Status();
			status.setStatusCode(po.getStatusCode());
			status.setUpdatedAtDate(po.getUpdatedAt());
			statuses.add(status);
		}
		
		Log.exit(statuses);
		return statuses;
	}
	
}
