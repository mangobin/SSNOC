package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.analyzers.SocialNetworkAnalyzer;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;

@Path("/usergroups")
public class UserGroupsService extends BaseService {

	@GET
	@Path("/unconnected")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<List<String>> getUnconnectedUsers(){
		return getUnconnectedUsersWithTimeWindow(Integer.MAX_VALUE);
	}
	
	@GET
	@Path("/unconnected/{timeWindowInHours}")
	@Produces({ MediaType.APPLICATION_JSON })
	public List<List<String>> getUnconnectedUsersWithTimeWindow(@PathParam("timeWindowInHours") int hoursAgo){
		Log.enter("getting unconnected users from last " + hoursAgo + " hours");
		
		Calendar calendar = Calendar.getInstance();
		//calendar.add(Calendar.HOUR_OF_DAY, -hoursAgo);
		calendar.add(Calendar.MINUTE, -hoursAgo); // for testing purposes
		Date date = calendar.getTime();

		List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();
		List<User> users = new ArrayList<User>();
		for(UserPO po : userPOs){
			User dto = ConverterUtils.convert(po);
			users.add(dto);
		}
		
		List<MessagePO> messagePOs = DAOFactory.getInstance().getMessageDAO().findChatMessagesSinceDate(date);
		List<Message> messages = new ArrayList<Message>();
		for(MessagePO po : messagePOs){
			Message dto = ConverterUtils.convert(po);
			messages.add(dto);
		}
		
		SocialNetworkAnalyzer sna = new SocialNetworkAnalyzer();
	    sna.loadUsers(users);
	    sna.loadMessages(messages);
	    
	    List<Set<String>> unconnectedUsers = sna.getUnconnectedUsers();
	    List<List<String>> output = new ArrayList<List<String>>();
	    for(Set<String> set : unconnectedUsers){
	    	List<String> result = new ArrayList<String>(set);
	    	output.add(result);
	    }
	    
	    Log.exit(output);
	    return output;
	}
	
}
