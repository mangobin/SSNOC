package edu.cmu.sv.ws.ssnoc.common.utils;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Memory;
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
		
		dto.setCreatedAt(TimestampUtil.convert(po.getCreatedAt()));
		StatusPO statusPO = DAOFactory.getInstance().getStatusDAO().findStatusById(po.getLastStatusID());
		if(statusPO != null){
			dto.setLastStatusCode(convert(statusPO));
		} else {
			dto.setLastStatusCode(null);
		}
		dto.setAccountStatus(po.getAccountStatus());
		dto.setPrivilegeLevel(po.getPrivilegeLevel());
		

		String userName = DAOFactory.getInstance().getUserDAO().findByUserID(po.getUserId()).getUserName();
		dto.setUserName(userName);

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
		po.setPassword(dto.getPassword());
		po.setCreatedAt(TimestampUtil.convert(dto.getCreatedAt()));
		po.setAccountStatus(dto.getAccountStatus());
		po.setPrivilegeLevel(dto.getPrivilegeLevel());
		


		long userId = DAOFactory.getInstance().getUserDAO().findByName(dto.getUserName()).getUserId();
		po.setUserId(userId);
		po.setUserName(dto.getUserName());

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
		
		long userId = DAOFactory.getInstance().getUserDAO().findByName(dto.getUserName()).getUserId();
		po.setUserId(userId);
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
		
		String userName = DAOFactory.getInstance().getUserDAO().findByUserID(po.getUserId()).getUserName();
		dto.setUserName(userName);
		return dto;
	}
	
	public static final Message convert(MessagePO po) {
		if(po == null)
			return null;
		Message dto = new Message();
		
		dto.setContent(po.getContent());
		dto.setMessageID(po.getMessageId());
		dto.setMessageType(po.getMessageType());
		dto.setPostedAt(TimestampUtil.convert(po.getPostedAt()));
		
		
		String author = DAOFactory.getInstance().getUserDAO().findByUserID(po.getAuthor()).getUserName();
		String target = DAOFactory.getInstance().getUserDAO().findByUserID(po.getTarget()).getUserName();
		dto.setAuthor(author);
		dto.setTarget(target);
		return dto;
		
	}
	
	public static final MessagePO convert(Message dto) {
		if(dto == null)
			return null;
		MessagePO  po = new MessagePO();
		
		po.setContent(dto.getContent());
		po.setMessageId(dto.getMessageID());
		po.setMessageType(dto.getMessageType());
						
		po.setPostedAt(TimestampUtil.convert(dto.getPostedAt()));

		long authorId = DAOFactory.getInstance().getUserDAO().findByName(dto.getAuthor()).getUserId();

		long targetId = DAOFactory.getInstance().getUserDAO().findByName(dto.getAuthor()).getUserId();
		
		po.setAuthor(authorId);
		po.setTarget(targetId);
		return po;
	}
	
	public static final MemoryPO convert(Memory dto) {
		if(dto == null)
			return null;
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
		if(po == null)
			return null;
		Memory dto = new Memory();
		dto.setCreatedAt(TimestampUtil.convert(po.getCreatedAt()));
		dto.setMemoryID(po.getMemoryID());
		dto.setRemainingPersistent(po.getRemainingPersistent());
		dto.setRemainingVolatile(po.getRemainingVolatile());
		dto.setUsedPersistent(po.getUsedPersistent());
		dto.setUsedVolatile(po.getUsedVolatile());
		
		return dto;
		
	}
	
	
}
