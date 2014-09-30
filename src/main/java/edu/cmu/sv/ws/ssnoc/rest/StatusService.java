package edu.cmu.sv.ws.ssnoc.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.exceptions.UnknownStatusException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.UnknownUserException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.ValidationException;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Status;
import edu.cmu.sv.ws.ssnoc.dto.validators.StatusValidator;

@Path("/status")
public class StatusService extends BaseService {

	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{userName}")
	public Response createStatus(@PathParam("userName") String userName, Status status){
		Log.enter(status);
		Log.debug("Searching for user: " + userName);
		// verify user exists, if Not -- return 404?
		IUserDAO dao = DAOFactory.getInstance().getUserDAO();
		UserPO existingUser = dao.findByName(userName);
		if(existingUser == null){
			throw new UnknownUserException(userName);
		}
		
		// verify status is correct, if Not -- return 403?
		StatusValidator validator = new StatusValidator();
		if(!validator.validate(status)){
			throw new ValidationException("Invalid Status");
		}
		
		//set the status code as the lastStatusCode for the user
//		existingUser.setLastStatusCode(status.getStatusCode());
//		dao.save(existingUser);
		
		// save status to database
		status.setUserName(userName);;
		StatusPO po = ConverterUtils.convert(status);
		
		long id = DAOFactory.getInstance().getStatusDAO().save(po);

		//set the DB ID Key as the lastStatusID for the user
		existingUser.setLastStatusID(id);
		dao.save(existingUser);
		
		// return 201 -- created
		Log.exit();
		return created(id);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{statusId}")
	public Status findStatus(@PathParam("statusId") long statusId){
		Log.enter(statusId);
		
		StatusPO po = DAOFactory.getInstance().getStatusDAO().findStatusById(statusId);
		if(po == null){
			throw new UnknownStatusException(statusId);
		}

		Status status = ConverterUtils.convert(po);
	
		Log.exit(status);
		return status;
	}
	
}
