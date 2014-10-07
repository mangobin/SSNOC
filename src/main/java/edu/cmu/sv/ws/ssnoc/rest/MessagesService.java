package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
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
}
