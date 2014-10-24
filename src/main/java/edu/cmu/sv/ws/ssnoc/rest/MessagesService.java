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
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Message;


@Path("/messages")
public class MessagesService extends BaseService {
	
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
			if(po.getAccountStatus().equals("Active")) {
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
	@Path("/{userName1}/{userName2}")
	public List<Message> retrieveAllMessagesBetweenTwoUsers (@PathParam("userName1") String userName1, 
			@PathParam("userName2") String userName2) {
		Log.enter(userName1);
		Log.enter(userName2);
		
		List<MessagePO> list = new ArrayList<MessagePO>();
		
		IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();

		long authorId = DAOFactory.getInstance().getUserDAO().findByName(userName1).getUserId();
		long targetId = DAOFactory.getInstance().getUserDAO().findByName(userName2).getUserId();
		
		list = dao.findChatHistoryBetweenTwoUsers(authorId, targetId);
		
		List<Message> listDto = new ArrayList<Message>();
		
		for(MessagePO m : list) {
			Message msg = ConverterUtils.convert(m);
			listDto.add(msg);
		}
		
		Log.exit(listDto);
		return listDto;
		
	}
}
