package edu.cmu.sv.ws.ssnoc.common.utils;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Message;
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
		po.setUserName(dto.getUserName());
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
		dto.setUserName(po.getUserName());
		
		return dto;
	}
	
	public static final Message convert(MessagePO po) {
		if(po == null)
			return null;
		Message dto = new Message();
		dto.setAuthor(po.getAuthor());
		dto.setContent(po.getContent());
		dto.setMessageID(po.getMessageId());
		dto.setMessageType(po.getMessageType());
		dto.setPostAt(TimestampUtil.convert(po.getPostedAt()));
		dto.setTarget(po.getTarget());
		
		return dto;
		
	}
	
	public static final MessagePO convert(Message dto) {
		if(dto == null)
			return null;
		MessagePO  po = new MessagePO();
		po.setAuthor(dto.getAuthor());
		po.setContent(dto.getContent());
		po.setMessageId(dto.getMessageID());
		po.setMessageType(dto.getMessageType());
		po.setTarget(dto.getTarget());
		po.setPostedAt(TimestampUtil.convert(dto.getPostedAt()));
		
		
		return po;
	}
	
	
}
