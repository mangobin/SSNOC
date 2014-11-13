package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElementWrapper;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.User;

@Path("/users")
public class UsersService extends BaseService {
	/**
	 * This method loads all active users in the system.
	 * 
	 * @return - List of all active users.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@XmlElementWrapper(name = "users")
	public List<User> loadUsers() {
		Log.enter("all users");

		List<User> users = null;
		try {
			List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();

			users = new ArrayList<User>();
			for (UserPO po : userPOs) {
				User dto = ConverterUtils.convert(po);
				users.add(dto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}
		
		return users;
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/active")
	public List<User> loadActiveUsers() {
		Log.enter("load active users");

		List<User> users = null;
		try {
			List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();

			users = new ArrayList<User>();
			for (UserPO po : userPOs) {
				if(po != null && po.getAccountStatus().equals("Active")) {
					User dto = ConverterUtils.convert(po);
					users.add(dto);
				}
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}
		
		return users;
	}
	

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{userName}/chatbuddies")
	public Response retrieveChatBuddies(@PathParam("userName") String userName) {
		Log.enter("retrieve chat buddies for: "+ userName);
		long userID = DAOFactory.getInstance().getUserDAO().findByName(userName).getUserId();
		List<UserPO> usersPO = DAOFactory.getInstance().getMessageDAO().findChatBuddies(userID);
		
		List<User> userDto = new ArrayList<User>();
		
		for(UserPO po : usersPO) {
			User user = new User();
			user = ConverterUtils.convert(po);
			userDto.add(user);
		}
		
		Log.exit(userDto);
		
		return ok(userDto);
	}
	
}
