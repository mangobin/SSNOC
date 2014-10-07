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
			IMessageDAO dao = DAOFactory.getInstance().getMessageDAO();
			
			msg.setAuthor(userName);
			msg.setMessageType("WALL");
			MessagePO po = ConverterUtils.convert(msg);

			dao.save(po);
			dtoMsg = ConverterUtils.convert(po);
			
		} catch (Exception e) {
		} finally {
			Log.exit(dtoMsg);
		}
		
		return created(dtoMsg);
	}
	
	

}
