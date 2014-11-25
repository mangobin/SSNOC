package edu.cmu.sv.ws.ssnoc.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.exceptions.DBException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.ServiceException;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IRequestDAO;
import edu.cmu.sv.ws.ssnoc.data.po.RequestPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Request;

@Path("/request")
public class RequestService extends BaseService {
	

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{username}")
	public Response postRequest(@PathParam("username") String username, Request dto) {

		Log.enter(dto);
		
		Request dtoReq = new Request();
		try{
			UserPO userPO = DAOFactory.getInstance().getUserDAO().findByName(username);
			if(userPO == null || dto == null || dto.getCreated_at() == null) {
				return badRequest();
			}
			
			IRequestDAO dao = DAOFactory.getInstance().getRequestDAO();

			dto.setRequesterId(userPO.getUserId());
			
			RequestPO requestpo = ConverterUtils.convert(dto);
			if(requestpo.getCreated_at() == null){
				return badRequest();
			}

			long requestID = dao.save(requestpo);
			requestpo.setRequestId(requestID);
			dtoReq = ConverterUtils.convert(requestpo);
			
		}  catch(DBException e) {
			throw new DBException(e);	
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			Log.exit(dtoReq);
		}
		
		return created(dtoReq);
		
	}

}
