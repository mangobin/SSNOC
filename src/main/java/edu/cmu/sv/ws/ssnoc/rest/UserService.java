package edu.cmu.sv.ws.ssnoc.rest;

import javax.crypto.SecretKey;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.h2.util.StringUtils;

import edu.cmu.sv.ws.ssnoc.common.exceptions.ServiceException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.UnauthorizedUserException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.UnknownUserException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.ValidationException;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.common.utils.SSNCipher;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.User;
import edu.cmu.sv.ws.ssnoc.dto.UserPassword;
import edu.cmu.sv.ws.ssnoc.dto.validators.UserValidator;

/**
 * This class contains the implementation of the RESTful API calls made with
 * respect to users.
 * 
 */

@Path("/user")
public class UserService extends BaseService {
	/**
	 * This method checks the validity of the user name and if it is valid, adds
	 * it to the database, return code 201 created.
	 * 
	 * If a user exists: return 200 OK
	 * 
	 * If a user exists, but the password is wrong, return 400 Bad request
	 * 
	 * Note: Last modified by Bin at 6:17 pm on OCT. 23
	 * @param user
	 *            - An object of type User
	 * @return - An object of type Response with the status of the request
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/signup")
	public Response addUser(User user) {
		Log.enter(user);
		User resp = new User();

		try {
			IUserDAO dao = DAOFactory.getInstance().getUserDAO();
			UserPO existingUser = dao.findByName(user.getUserName());

			// Validation to check that user name should be unique
			// in the system. If a new users tries to register with
			// an existing userName, notify that to the user.
			if (existingUser != null) {
				Log.trace("User name provided already exists. Validating if it is same password ...");
				if (!validateUserPassword(user.getPassword(), existingUser)) {
					Log.warn("Password is different for the existing user name.");
					throw new ValidationException("User name already taken");
				} else {
					Log.debug("Yay!! Password is same for the existing user name.");
					resp.setUserName(existingUser.getUserName());
					return ok(resp);
				}
			}
			
			UserValidator validator = new UserValidator();
			
			validUser(user, validator);

			UserPO po = ConverterUtils.convert(user);
			po = SSNCipher.encryptPassword(po);

			//set default privilege level and account status
			po.setAccountStatus("Active");
			po.setPrivilegeLevel("Citizen");
			dao.save(po);
			resp = ConverterUtils.convert(po);
			
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}

		return created(resp);
	}


	/**
	 * This method is used to login a user. 
	 * 
	 * Note:Last modified by Bin at 8:41 am on Sept.24. 
	 * 
	 * @param user's password
	 * 
	 * @return - Status 200 when successful login. 
	 * 			 if password is wrong: 401 Unauthorized
	 * 			 if user does not exist: 404 Not Found
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{userName}/authenticate")
	public Response loginUser(@PathParam("userName") String userName,
			 UserPassword pass) {
		User user = new User();
		user.setUserName(userName);
		user.setPassword(pass.getPassword());
		Log.enter(userName, user);

		try {
			UserPO po = loadExistingUser(userName);
			if(po.getAccountStatus() != null && !po.getAccountStatus().equals("Active")) {
				throw new UnauthorizedUserException(userName, "Account has been deactivated");
			}
			if (!validateUserPassword(pass.getPassword(), po)) {
				throw new UnauthorizedUserException(userName, "Invalid password");
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}

		return ok();
	}
	
	/**
	 * This method will validate the user's password based on what information
	 * is sent from the UI, versus the information retrieved for that user from
	 * the database.
	 * 
	 * @param password
	 *            - Encrypted Password
	 * @param po
	 *            - User info from DB
	 * 
	 * @return - Flag specifying YES or NO
	 */
	private boolean validateUserPassword(String password, UserPO po) {
		try {
			SecretKey key = SSNCipher.getKey(StringUtils.convertHexToBytes(po
					.getSalt()));
			if (password.equals(SSNCipher.decrypt(
					StringUtils.convertHexToBytes(po.getPassword()), key))) {
				return true;
			}
		} catch (Exception e) {
			Log.error("An Error occured when trying to decrypt the password", e);
			throw new ServiceException("Error when trying to decrypt password",
					e);
		}

		return false;
	}

	/**
	 * All information related to a particular userName.
	 * 
	 * @param userName
	 *            - User Name
	 * 
	 * @return - Details of the User
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Path("/{userName}")
	public User loadUser(@PathParam("userName") String userName) {
		Log.enter(userName);

		User user = null;
		try {
			UserPO po = loadExistingUser(userName);
			user = ConverterUtils.convert(po);
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(user);
		}

		return user;
	}

	/**
	 *	Update a user's record
	 * @param userName
	 *            - User Name
	 * 
	 * @return - If user name is updated: 201 Created
	 * 			 If user name is not updated: 200 OK
	 */
	@PUT
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/{userName}")
	public Response updateUser(@PathParam("userName") String userName, User user) {
		Log.enter(user);

		String newUserName = user.getUserName();
		String newPassWord = user.getPassword();
		String newAccountStatus = user.getAccountStatus();
		String newPrivilegeLevel  = user.getPrivilegeLevel();
		
		User temp = new User();
		
		try {
			IUserDAO dao = DAOFactory.getInstance().getUserDAO();
			UserPO existingUser = dao.findByName(userName);
			
			if(existingUser == null) {
				throw new UnknownUserException(userName);
			}
			checkNewUserName(userName, newUserName, dao, existingUser);
			
			existingUser = setNewPasswordisNotNull(newPassWord, existingUser); 
			
			setAccountStatusIfNotNull(newAccountStatus, existingUser);
			
			setPrivilegeLevelIfNotNull(newPrivilegeLevel, existingUser);
			
			UserValidator validator = new UserValidator();
			
			validUser(user, validator);

			dao.save(existingUser);
			temp =  ConverterUtils.convert(existingUser);
		}catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit();
		}
		
		if(newUserName != null && !newUserName.equals(userName)) {
			return created(temp);
		}
		else {
			return ok(temp);
		}

	}


	/**
	 * @param userName
	 * @param newUserName
	 * @param dao
	 * @param existingUser
	 */
	private void checkNewUserName(String userName, String newUserName,
			IUserDAO dao, UserPO existingUser) {
		if(newUserName != null ) {
			UserPO existingSameNameUser = dao.findByName(newUserName);
			if(existingSameNameUser == null) {
				existingUser.setUserName(newUserName);
			}
			else if(userName.equals(newUserName)) {
				existingUser.setUserName(newUserName);
			} 
			else {
				throw new ValidationException("User name already taken");
			}
			
		}
	}


	/**
	 * @param user
	 * @param validator
	 */
	private void validUser(User user, UserValidator validator) {
		if(!validator.validate(user)){
			throw new ValidationException("User name is not allowed");
		}
	}


	/**
	 * @param newPrivilegeLevel
	 * @param existingUser
	 */
	private void setPrivilegeLevelIfNotNull(String newPrivilegeLevel,
			UserPO existingUser) {
		if (newPrivilegeLevel != null) {
			existingUser.setPrivilegeLevel(newPrivilegeLevel);
		}
	}


	/**
	 * @param newAccountStatus
	 * @param existingUser
	 */
	private void setAccountStatusIfNotNull(String newAccountStatus,
			UserPO existingUser) {
		if(newAccountStatus != null) {
			existingUser.setAccountStatus(newAccountStatus);
		}
	}


	/**
	 * @param newPassWord
	 * @param existingUser
	 * @return
	 */
	private UserPO setNewPasswordisNotNull(String newPassWord,
			UserPO existingUser) {
		if(newPassWord != null) {
			existingUser.setPassword(newPassWord);
			existingUser = SSNCipher.encryptPassword(existingUser);
		}
		return existingUser;
	}
}
