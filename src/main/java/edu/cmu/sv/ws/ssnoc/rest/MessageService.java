package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.common.utils.SSNCipher;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;


@Path("/message")
public class MessageService {
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{userName}")
	public Response postMessageOnPublicWall(@PathParam ("userName") String userName, Message msg) {
		Log.enter(msg);

		try{
			msg.setAuthor(userName);
			MessagePO po = ConverterUtils.convert(msg);

			dao.save(po);
			resp = ConverterUtils.convert(po);
			
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}

		return created(resp);
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("wall")
	public List<Message> retrieveAllMsgOnPublicWall () {
		List<Message> list = new ArrayList<Message>();
		
		return list;
		
	}

}
