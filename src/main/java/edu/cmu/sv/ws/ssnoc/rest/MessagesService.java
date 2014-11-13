package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDAO;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Message;


@Path("/messages")
public class MessagesService extends BaseService {
	private static final String ACTIVE = "Active";
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/wall")
	public List<Message> retrieveAllMsgOnPublicWall () {
		List<MessagePO> list = new ArrayList<MessagePO>();
		
		IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();
		list = dao.findLatestWallMessages(50, 0);
		
		List<Message> listDto = new ArrayList<Message>();
		
		for(MessagePO m : list) {
			Message msg = ConverterUtils.convert(m);
			listDto.add(msg);
		}
		
		Log.exit(listDto);
		return listDto;
		
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/wall/visible")
	public List<Message> retrieveAllVisibleMsgOnPublicWall () {
		Log.enter("enter retrieve all visible message on public wall");
		List<MessagePO> list = new ArrayList<MessagePO>();
		
		IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();
		list = dao.findLatestWallMessages(50, 0);
		
		List<Message> listDto = new ArrayList<Message>();
		
		for(MessagePO m : list) {
			UserPO po = DAOFactory.getInstance().getUserDAO().findByUserID(m.getAuthor());
			if(po.getAccountStatus().equals(ACTIVE)) {
				Message msg = ConverterUtils.convert(m);
				listDto.add(msg);
			}
		}
		
		Log.exit(listDto);
		return listDto;
		
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/announcement")
	public List<Message> retrieveAllAnnouncement () {
		Log.enter("enter retrieve All announcement");
		List<MessagePO> list = new ArrayList<MessagePO>();
		
		IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();
		list = dao.findAllAnnouncement(50, 0);
		
		List<Message> listDto = new ArrayList<Message>();
		
		for(MessagePO m : list) {
			Message msg = ConverterUtils.convert(m);
			listDto.add(msg);
		}
		
		Log.exit(listDto);
		return listDto;
		
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/announcement/visible")
	public List<Message> retrieveAllVisibleAnnouncement () {
		Log.enter("enter retrieve All visible announcements");
		List<MessagePO> list = new ArrayList<MessagePO>();
		
		IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();
		list = dao.findAllAnnouncement(50, 0);
		
		List<Message> listDto = new ArrayList<Message>();
		
		for(MessagePO m : list) {
			UserPO po = DAOFactory.getInstance().getUserDAO().findByUserID(m.getAuthor());
			if(po.getAccountStatus().equals(ACTIVE)) {
				Message msg = ConverterUtils.convert(m);
				listDto.add(msg);
			}
		}
		
		Log.exit(listDto);
		return listDto;
		
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{userName1}/{userName2}")
	public List<Message> retrieveAllMessagesBetweenTwoUsers (@PathParam("userName1") String userName1, 
			@PathParam("userName2") String userName2) {
		Log.enter(userName1);
		Log.enter(userName2);
		
		List<MessagePO> list = new ArrayList<MessagePO>();
		
		IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();
		IUserDAO userDao = DAOFactory.getInstance().getUserDAO();
		
		UserPO author = userDao.findByName(userName1);
		UserPO target = userDao.findByName(userName2);
				
		if(author != null && target != null){
			list = dao.findChatHistoryBetweenTwoUsers(author.getUserId(), target.getUserId());
		}
		
		List<Message> listDto = new ArrayList<Message>();
		
		for(MessagePO m : list) {
			Message msg = ConverterUtils.convert(m);
			listDto.add(msg);
		}
		
		Log.exit(listDto);
		
		return listDto;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{userName1}/{userName2}/visible")
	public List<Message> retrieveAllVisibleMessagesBetweenTwoUsers (@PathParam("userName1") String userName1, 
			@PathParam("userName2") String userName2) {
		Log.enter(userName1);
		Log.enter(userName2);
		
		List<MessagePO> list = new ArrayList<MessagePO>();
		
		IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();
		UserPO authorPO = DAOFactory.getInstance().getUserDAO().findByName(userName1);
		UserPO targetPO = DAOFactory.getInstance().getUserDAO().findByName(userName2);
		long authorId = authorPO.getUserId();
		long targetId = targetPO.getUserId();
		
		List<Message> listDto = new ArrayList<Message>();
		if(authorPO.getAccountStatus().equals(ACTIVE) && targetPO.getAccountStatus().equals(ACTIVE)) {
			list = dao.findChatHistoryBetweenTwoUsers(authorId, targetId);
			for(MessagePO m : list) {
				Message msg = ConverterUtils.convert(m);
				listDto.add(msg);
			}
		}
		
		Log.exit(listDto);
		return listDto;
		
	}
}
