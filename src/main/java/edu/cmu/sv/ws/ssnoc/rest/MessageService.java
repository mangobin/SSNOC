package edu.cmu.sv.ws.ssnoc.rest;


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.exceptions.DBException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.ServiceException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.UnknownMessageException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.UnknownUserException;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.dto.Message;


@Path("/message")
public class MessageService extends BaseService {
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{userName}")
	public Response postMessageOnPublicWall(@PathParam ("userName") String userName, Message msg) {
		Log.enter(msg);
		
		Message dtoMsg = new Message();
		try{
			
			if(userName == null) {
				throw new ServiceException();
			}
			IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();
			msg.setAuthor(userName);
			msg.setMessageType(SQL.MESSAGE_TYPE_WALL);
			MessagePO po = ConverterUtils.convert(msg);

			long messageID = dao.save(po);
			po.setMessageId(messageID);
			dtoMsg = ConverterUtils.convert(po);
			
		}  catch(DBException e) {
			throw new DBException(e);	
		} catch (UnknownMessageException e) {
			throw new UnknownMessageException(msg.getMessageID());
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			Log.exit(dtoMsg);
		}
		
		return created(dtoMsg);
	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/announcement")
	public Response postAnnouncement(Message msg) {
		Log.enter(msg);
		
		Message dtoMsg = new Message();
		try{
			if(msg.getAuthor() == null) {
				throw new ServiceException();
			}
			IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();
			msg.setMessageType(SQL.MESSAGE_TYPE_ANNOUNCEMENT);
			MessagePO po = ConverterUtils.convert(msg);

			long messageID = dao.save(po);
			po.setMessageId(messageID);
			dtoMsg = ConverterUtils.convert(po);
			
		} catch(DBException e) {
			throw new DBException(e);	
		} catch (UnknownMessageException e) {
			throw new UnknownMessageException(msg.getMessageID());
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			Log.exit(dtoMsg);
		}
		
		return created(dtoMsg);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{messageID}")
	public Message retrieveMesssageById(@PathParam("messageID") long messageID) {
		Log.enter(messageID);
		MessagePO po = DAOFactory.getInstance().getMessageDAO().findMessageById(messageID);
		if(po == null){
			throw new UnknownMessageException(messageID);
		}
		Message dto = ConverterUtils.convert(po);
		Log.exit(dto);
		return dto;
	}
	
   ///message/sendignUserName/receivingUserName

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{sendingUserName}/{receivingUserName}")
	public Response sendChatMessages(@PathParam("sendingUserName") String sendingUsername,
			@PathParam("receivingUserName") String receivingUserName, Message msg) {
		Log.enter(sendingUsername);
		Log.enter(receivingUserName);
		Log.enter(msg);
		if(sendingUsername == null || receivingUserName == null) {
			throw new ServiceException();
		}
		msg.setAuthor(sendingUsername);
		msg.setTarget(receivingUserName);
		msg.setMessageType(SQL.MESSAGE_TYPE_CHAT);
		
		MessagePO po = ConverterUtils.convert(msg);
		
		IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();
		long messageID = dao.save(po);
		
		po.setMessageId(messageID);
		
		Message dto = ConverterUtils.convert(po);
		
		Log.exit(dto);
		
		return created(dto);
		
		
	}

}
