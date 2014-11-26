package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.RequestPO;
import edu.cmu.sv.ws.ssnoc.dto.Request;

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
		Log.enter("retrieve all requests sent by a user");
		List<RequestPO> poList = DAOFactory.getInstance().getRequestDAO().findAllRequestsByUserName(username);
		
		List<Request> dtoList = new ArrayList<Request>();
		for(RequestPO po : poList) {
			Request dto = ConverterUtils.convert(po);
			dtoList.add(dto);
		}

		Log.exit(dtoList);
		return dtoList;
	}

}
