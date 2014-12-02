package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.exceptions.DBException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.ServiceException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.UnknownRequestException;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.common.utils.TimestampUtil;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IRequestDAO;
import edu.cmu.sv.ws.ssnoc.data.dao.IResponderDAO;
import edu.cmu.sv.ws.ssnoc.data.po.RequestPO;
import edu.cmu.sv.ws.ssnoc.data.po.ResponderPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Request;
import edu.cmu.sv.ws.ssnoc.dto.Responder;

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
			dto.setStatus(Request.DEFAULT_STATUS);
			
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
	

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{id}")
	public Response getRequestById(@PathParam("id") long requestId) {
		Log.enter(requestId);
		RequestPO po = DAOFactory.getInstance().getRequestDAO().findRequestById(requestId);
		if(po == null) {
			throw new UnknownRequestException(requestId);
		}
		
		Request dto = ConverterUtils.convert(po);
		Log.exit(dto);
		return ok(dto);
	}
	
	@PUT
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{id}")
	public Response updateRequestById(@PathParam("id") long requestId, Request request) {
		Log.enter(requestId);
		RequestPO po = DAOFactory.getInstance().getRequestDAO().findRequestById(requestId);
		if(po == null) {
			throw new UnknownRequestException(requestId);
		}
		
		String newStatus = request.getStatus();
		String resolutionDetails = request.getResolutionDetails();
		Date updatedAt = TimestampUtil.convert(request.getUpdated_at());
		if(updatedAt == null ) {
			return badRequest();
		} else if ( newStatus == null && resolutionDetails == null) {
			return badRequest();
		}else {
			po.setUpdated_at(updatedAt);
			if(newStatus != null) {
				po.setStatus(newStatus);
			}
			if(resolutionDetails != null){ 
				po.setResolutionDetails(resolutionDetails);
			}
			DAOFactory.getInstance().getRequestDAO().save(po);
			Request dto = ConverterUtils.convert(po);
			
			Log.exit(dto);
			return ok(dto);
		}
		
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/responder/{requestid}")
	public List<Responder> setRespondersForARequest(@PathParam("requestid") long requestid, Responder dto) {
		Log.enter("setRespondersForARequest"+ dto.getUsername());
		
		List<Responder> responderList = new ArrayList<Responder>();
		try {
			UserPO userPO = DAOFactory.getInstance().getUserDAO().findByName(dto.getUsername());
			
			if(userPO == null || TimestampUtil.convert(dto.getUpdated_at()) == null) {
				return responderList;
			}
			
			dto.setRequestId(requestid);
			dto.setUserId(userPO.getUserId());
			dto.setStatus(Responder.DEFAULT_STATUS);
			
			ResponderPO po = ConverterUtils.convert(dto);
			IResponderDAO responderDAO = DAOFactory.getInstance().getResponderDAO();
			responderDAO.save(po);
			List<ResponderPO> poList = responderDAO.findRespondersByRequestId(requestid);
			
			for(ResponderPO responderPO : poList) {
				Responder responderdto = ConverterUtils.convert(responderPO);
				responderList.add(responderdto);
			}
			
		}  catch(DBException e) {
			throw new DBException(e);	
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			Log.exit(responderList);
		}
		
		return responderList;
		
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/responderlist/{requestid}")
	public List<Responder> setResponderListForARequest(@PathParam("requestid") long requestid, List<Responder> dtoList) {
		Log.enter("setResponderListForARequest"+ dtoList.size());
		
		IResponderDAO responderDAO = DAOFactory.getInstance().getResponderDAO();
		List<ResponderPO> originalResponderList = responderDAO.findRespondersByRequestId(requestid);
		//record userid
		Map<Long,Boolean> map = new HashMap<Long,Boolean>();
		for(ResponderPO po : originalResponderList ) {
			map.put(po.getUserId(),false);
		}
		
		List<Responder> responderList = new ArrayList<Responder>();
		try {
			
			for(Responder dto : dtoList) {
				UserPO userPO = DAOFactory.getInstance().getUserDAO().findByName(dto.getUsername());
				
				if(userPO == null || TimestampUtil.convert(dto.getUpdated_at()) == null) {
					return responderList;
				}
				if(map.containsKey(userPO.getUserId())) {
					map.put(userPO.getUserId(), true);
					continue;
				}
				
				dto.setRequestId(requestid);
				dto.setUserId(userPO.getUserId());
				dto.setStatus(Responder.DEFAULT_STATUS);
				
				ResponderPO po = ConverterUtils.convert(dto);

				responderDAO.save(po);
			}
			
			//delete unselected user
			for(long userid : map.keySet()) {
				if(!map.get(userid)) {
					responderDAO.deleteResponderByUserId(userid,requestid);
				}
			}
			
			List<ResponderPO> poList = responderDAO.findRespondersByRequestId(requestid);
			
			for(ResponderPO responderPO : poList) {
				if(!map.containsKey(responderPO.getUserId())) {
					Responder responderdto = ConverterUtils.convert(responderPO);
					responderList.add(responderdto);	
				}
			}
			
		}  catch(DBException e) {
			throw new DBException(e);	
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			Log.exit(responderList);
		}
		
		return responderList;
		
	}
	
}
