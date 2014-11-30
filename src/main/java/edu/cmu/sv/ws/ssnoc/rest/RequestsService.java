package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.common.utils.TimestampUtil;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IResponderDAO;
import edu.cmu.sv.ws.ssnoc.data.po.RequestPO;
import edu.cmu.sv.ws.ssnoc.data.po.ResponderPO;
import edu.cmu.sv.ws.ssnoc.dto.Request;
import edu.cmu.sv.ws.ssnoc.dto.Responder;

@Path("/requests")
public class RequestsService extends BaseService{
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/all")
	public List<Request> getAllRequests() {
		Log.enter("retrieve all requests");
		List<RequestPO> poList = DAOFactory.getInstance().getRequestDAO().getAllRequests(50, 0);
		
		List<Request> dtoList = new ArrayList<Request>();
		for(RequestPO po : poList) {
			Request dto = ConverterUtils.convert(po);
			dtoList.add(dto);
		}

		Log.exit(dtoList);
		return dtoList;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{username}")
	public List<Request> getAllRequestsSentByAUser(@PathParam("username") String username) {
		Log.enter("retrieve all requests sent by a user: "+ username);
		List<RequestPO> poList = DAOFactory.getInstance().getRequestDAO().findAllRequestsByUserName(username);

		List<Request> dtoList = new ArrayList<Request>();
		for(RequestPO po : poList) {
			Request dto = ConverterUtils.convert(po);
			dtoList.add(dto);
		}

		Log.exit(dtoList);
		return dtoList;
	}
	

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/missions/{username}")
	public List<Responder> getAllMissionsForAUser(@PathParam("username") String username) {
		Log.enter("retrieve all Missions sent by a user: "+ username);
		
		List<ResponderPO> poList = DAOFactory.getInstance().getResponderDAO().findRespondersByUserName(username);
		
		List<Responder> dtoList = new ArrayList<Responder>();
		for(ResponderPO po : poList) {
			Responder dto = ConverterUtils.convert(po);
			dtoList.add(dto);
		}
		Log.exit(dtoList);
		return dtoList;
		
	}
	

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/missions/{responderid}")
	public Response getAResponderById(@PathParam("responderid") long responderid) {
		Log.enter("getAResponseById: "+ responderid);
		ResponderPO po = DAOFactory.getInstance().getResponderDAO().findResponderById(responderid);
		if(po == null) {
			return badRequest();
		} else {
			Responder dto = ConverterUtils.convert(po);
			Log.exit("getAResponseById: "+ responderid);
			return ok(dto);
		}
		
	}
	
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/missions/{responderid}")
	public Response updateAResponderById(@PathParam("responderid") long responderid, Responder dto) {
		Log.enter("updateAResponseById: "+ responderid);
		
		String newStatus = dto.getStatus();
		Date newDate = TimestampUtil.convert(dto.getUpdated_at());
		IResponderDAO responderDAO = DAOFactory.getInstance().getResponderDAO();
		ResponderPO po = responderDAO.findResponderById(responderid);
		
		if(po == null || newStatus == null || newDate == null) {
			return badRequest();
		} else {
			po.setStatus(newStatus);
			po.setUpdated_at(newDate);
			responderDAO.save(po);
			Responder newdto = ConverterUtils.convert(po);
			Log.exit("updateAResponseById: "+ responderid);
			return ok(newdto);
		}
		
	}
	
	
	

}
