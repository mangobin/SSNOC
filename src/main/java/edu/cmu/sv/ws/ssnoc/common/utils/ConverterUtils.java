package edu.cmu.sv.ws.ssnoc.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.RequestPO;
import edu.cmu.sv.ws.ssnoc.data.po.ResponderPO;
import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Memory;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.Request;
import edu.cmu.sv.ws.ssnoc.dto.Responder;
import edu.cmu.sv.ws.ssnoc.dto.Status;
import edu.cmu.sv.ws.ssnoc.dto.User;

/**
 * This is a utility class used to convert PO (Persistent Objects) and View
 * Objects into DTO (Data Transfer Objects) objects, and vice versa. <br/>
 * Rather than having the conversion code in all classes in the rest package,
 * they are maintained here for code re-usability and modularity.
 * 
 */
public class ConverterUtils {
	
	/**
	 * Convert UserPO to User DTO object.
	 * 
	 * @param po
	 *            - User PO object
	 * 
	 * @return - User DTO Object
	 */
	public static final User convert(UserPO po) {
		if (po == null) {
			return null;
		}

		User dto = new User();
		dto.setUserName(po.getUserName());
		dto.setCreatedAt(TimestampUtil.convert(po.getCreatedAt()));
		StatusPO statusPO = DAOFactory.getInstance().getStatusDAO().findStatusById(po.getLastStatusID());
		if(statusPO != null){
			dto.setLastStatusCode(convert(statusPO));
		} else {
			dto.setLastStatusCode(null);
		}
		dto.setAccountStatus(po.getAccountStatus());
		dto.setPrivilegeLevel(po.getPrivilegeLevel());
		dto.setLongitude(po.getLongitude());
		dto.setLatitude(po.getLatitude());
		if(po.getLocation_updatedAt() != null) {
			dto.setLocation_updatedAt(TimestampUtil.convert(po.getLocation_updatedAt()));
		}
		
		return dto;
	}

	/**
	 * Convert User DTO to UserPO object
	 * 
	 * @param dto
	 *            - User DTO object
	 * 
	 * @return - UserPO object
	 */
	public static final UserPO convert(User dto) {
		if (dto == null) {
			return null;
		}

		UserPO po = new UserPO();
		po.setUserName(dto.getUserName());
		po.setPassword(dto.getPassword());
		po.setCreatedAt(TimestampUtil.convert(dto.getCreatedAt()));
		po.setAccountStatus(dto.getAccountStatus());
		po.setPrivilegeLevel(dto.getPrivilegeLevel());
		po.setLatitude(dto.getLatitude());
		po.setLongitude(dto.getLongitude());
		if(dto.getLocation_updatedAt() != null) {
			po.setLocation_updatedAt(TimestampUtil.convert(dto.getLocation_updatedAt()));
		}
		return po;
	}
	
	public static final StatusPO convert(Status dto){
		if(dto == null){
			return null;
		}
		
		StatusPO po = new StatusPO();
		po.setStatusCode(dto.getStatusCode());
		po.setUpdatedAt(TimestampUtil.convert(dto.getUpdatedAt()));
		po.setLocLat(dto.getLocLat());
		po.setLocLng(dto.getLocLng());
		po.setStatusId(dto.getStatusId());
		
		UserPO user = DAOFactory.getInstance().getUserDAO().findByName(dto.getUserName());
		if( user !=null) {
			long userId = user.getUserId();	
			po.setUserId(userId);
		} 
		
		
		return po;
	}
	
	public static final Status convert(StatusPO po){
		if(po == null){
			return null;
		}
		
		Status dto = new Status();
		dto.setStatusCode(po.getStatusCode());
		dto.setUpdatedAt(TimestampUtil.convert(po.getUpdatedAt()));
		dto.setLocLat(po.getLocLat());
		dto.setLocLng(po.getLocLng());
		dto.setStatusId(po.getStatusId());
		
		UserPO user = DAOFactory.getInstance().getUserDAO().findByUserID(po.getUserId());
		if(user != null) {
			String userName = user.getUserName();
			dto.setUserName(userName);
				
		}
		return dto;
	}
	
	public static final Message convert(MessagePO po) {
		if(po == null) {
			return null;
		}
			
		Message dto = new Message();
		
		dto.setContent(po.getContent());
		dto.setMessageID(po.getMessageId());
		dto.setMessageType(po.getMessageType());
		dto.setPostedAt(TimestampUtil.convert(po.getPostedAt()));
		
		IUserDAO dao = DAOFactory.getInstance().getUserDAO();
		UserPO user = dao.findByUserID(po.getAuthor());
		if(user != null){
			dto.setAuthor(user.getUserName());
		} else {
			dto.setAuthor(null);
		}
		
		if(po.getMessageType() != null) {
			if( ! po.getMessageType().equals(SQL.MESSAGE_TYPE_REQUEST)) {
				user = dao.findByUserID(po.getTarget());
				if(user != null){
					dto.setTarget(user.getUserName());
				} else {
					dto.setTarget(null);
				}
			}
		}
		
		
		return dto;
		
	}
	
	public static final MessagePO convert(Message dto) {
		if(dto == null) {
			return null;
		}
		MessagePO  po = new MessagePO();
		
		po.setContent(dto.getContent());
		po.setMessageId(dto.getMessageID());
		po.setMessageType(dto.getMessageType());
						
		po.setPostedAt(TimestampUtil.convert(dto.getPostedAt()));
		
		IUserDAO dao = DAOFactory.getInstance().getUserDAO();
		UserPO user = dao.findByName(dto.getAuthor());
		if(user != null){
			po.setAuthor(user.getUserId());
		} else {
			po.setAuthor(0);
		}
		
		if(dto.getMessageType().equals(SQL.MESSAGE_TYPE_REQUEST)) {
			po.setTarget(Long.parseLong(dto.getTarget()));
		} else {
			user = dao.findByName(dto.getTarget());
			if (user != null) {
				po.setTarget(user.getUserId());
			} else {
				po.setTarget(0);
			}
			
		}
		

		return po;
	}
	
	public static final MemoryPO convert(Memory dto) {
		if(dto == null) {
			return null;
		}
		MemoryPO po = new MemoryPO();
		po.setCreatedAt(TimestampUtil.convert(dto.getCreatedAt()));
		po.setMemoryID(dto.getMemoryID());
		po.setRemainingPersistent(dto.getRemainingPersistent());
		po.setRemainingVolatile(dto.getRemainingVolatile());
		po.setUsedPersistent(dto.getUsedPersistent());
		po.setUsedVolatile(dto.getUsedVolatile());
		
		return po;
	}
	
	public static final Memory convert(MemoryPO po) {
		if(po == null) {
			return null;
		}
		Memory dto = new Memory();
		dto.setCreatedAt(TimestampUtil.convert(po.getCreatedAt()));
		dto.setMemoryID(po.getMemoryID());
		dto.setRemainingPersistent(po.getRemainingPersistent());
		dto.setRemainingVolatile(po.getRemainingVolatile());
		dto.setUsedPersistent(po.getUsedPersistent());
		dto.setUsedVolatile(po.getUsedVolatile());
		
		return dto;
		
	}
	
	public static final Request convert(RequestPO po) {
		if(po == null) {
			return null;
		}
		
		Request dto = new Request();
		dto.setCreated_at(TimestampUtil.convert(po.getCreated_at()));
		if(po.getUpdated_at() != null) {
			dto.setUpdated_at(TimestampUtil.convert(po.getUpdated_at()));
		}
		dto.setDescription(po.getDescription());
		dto.setLocation(po.getLocation());
		dto.setRequesterId(po.getRequesterId());
		dto.setRequestId(po.getRequestId());
		dto.setResolutionDetails(po.getResolutionDetails());
		//waiting for responder dto to completed
		//dto.setResponders(Arrays.asList(po.getResponders()));
		dto.setStatus(po.getStatus());
		dto.setType(Arrays.asList(po.getType()));
		
		UserPO user = DAOFactory.getInstance().getUserDAO().findByUserID(po.getRequesterId());
		if(user != null) {
			String userName = user.getUserName();
			dto.setUsername(userName);
		}
		
		//get List of responder objects for this request
		List<ResponderPO> responderPOList = DAOFactory.getInstance().getResponderDAO()
				.findRespondersByRequestId(po.getRequestId());
		List<Responder> responderList = new ArrayList<Responder>();
		for(ResponderPO responderPO : responderPOList) {
			Responder responderDTO = convert(responderPO);
			responderList.add(responderDTO);
		}
		dto.setResponders(responderList);
		
		return dto;
	}
	
	public static final RequestPO convert(Request dto) {
		if(dto == null) {
			return null;
		}
		
		RequestPO po = new RequestPO();
		po.setCreated_at(TimestampUtil.convert(dto.getCreated_at()));
		if(dto.getUpdated_at() != null) {
			po.setUpdated_at(TimestampUtil.convert(dto.getUpdated_at()));
		}
		po.setDescription(dto.getDescription());
		po.setLocation(dto.getLocation());
		po.setRequesterId(dto.getRequesterId());
		po.setRequestId(dto.getRequestId());
		po.setResolutionDetails(dto.getResolutionDetails());
		po.setStatus(dto.getStatus());
		
		List<String> typeList = dto.getType();
		String[] type = typeList.toArray(new String[typeList.size()]);
		po.setType(type);		
		//waiting for responder dto to completed
//		List<String> responderList = dto.getResponders();
//		String[] responders = responderList.toArray(new String[responderList.size()]);
//		po.setResponders(responders);
		
		return po;
	}
	
	public static final Responder convert(ResponderPO po) {
		if(po == null) {
			return null;
		}
		
		Responder dto = new Responder();
		dto.setRequestId(po.getRequestId());
		dto.setResponderId(po.getResponderId());
		dto.setStatus(po.getStatus());
		dto.setUpdated_at(TimestampUtil.convert(po.getUpdated_at()));
		dto.setUserId(po.getUserId());
		
		UserPO user = DAOFactory.getInstance().getUserDAO().findByUserID(po.getUserId());
		if(user != null) {
			String userName = user.getUserName();
			dto.setUsername(userName);
		}
		return dto;
	}
	
	public static final ResponderPO convert(Responder dto) {
		if(dto == null) {
			return null;
		}
		
		ResponderPO po = new ResponderPO();
		po.setRequestId(dto.getRequestId());
		po.setResponderId(dto.getResponderId());
		po.setStatus(dto.getStatus());
		po.setUserId(dto.getUserId());
		po.setUpdated_at(TimestampUtil.convert(dto.getUpdated_at()));
		
		return po;
	}
	
	
}
