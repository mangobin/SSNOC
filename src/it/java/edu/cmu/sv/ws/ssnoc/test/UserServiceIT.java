package edu.cmu.sv.ws.ssnoc.test;

import static com.eclipsesource.restfuse.Assert.assertOk;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.jetty.util.log.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;

import com.eclipsesource.restfuse.Assert;
import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.MediaType;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;

@RunWith(HttpJUnitRunner.class)
public class UserServiceIT {
	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:1234/ssnoc");
	@Context
	public Response response;
	
	// get all users, should exist
	@HttpTest(order=1, method=Method.GET, path="/users", headers={@Header(name="Accept", value="application/json")})
	public void testGetUnknownUsers(){
		assertOk(response);
		TypeToken<List<User>> token = new TypeToken<List<User>>() {};
		List<User> objects = new Gson().fromJson(response.getBody(), token.getType());
		for(User user : objects){
			if(user.getUserName().equalsIgnoreCase("testUser")){ // testUser should not be known at this point
				org.junit.Assert.fail("User found in list of users when it should not exist");
			}
		}
	}

	// get user = 404
	@HttpTest(order=1, method=Method.GET, path="/user/testUser", headers={@Header(name="Accept", value="application/json")})
	public void testGetNonExistentUser(){
		Assert.assertNotFound(response);
	}
	
	// authenticate = 404 
	@HttpTest(order=1, method=Method.POST, path="/user/testUser/authenticate", 
			headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, content="{\"password\":\"1234\"}")
	public void testAuthNonExistentUser(){
		Assert.assertNotFound(response);
	}
	
	// update - 404
	@HttpTest(order=1, method=Method.PUT, path="/user/testUser", headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, 
			content="{\"password\":\"4321\"}")
	public void testUpdateUserNotFound(){
		Assert.assertNotFound(response);
	}
	
	// sign up - 201
	@HttpTest(order=2, method=Method.POST, headers={@Header(name="Accept", value="application/json")},
			path="/user/signup", type=MediaType.APPLICATION_JSON, content="{\"userName\":\"testUser\",\"password\":\"1234\",\"createdAt\":\"2014-09-24 09:15\"}")
	public void testSignup(){
		Assert.assertCreated(response);
	}
	
	// sign up - 200
	@HttpTest(order=3, method=Method.POST, headers={@Header(name="Accept", value="application/json")},
			path="/user/signup", type=MediaType.APPLICATION_JSON, content="{\"userName\":\"testUser\",\"password\":\"1234\",\"createdAt\":\"2014-09-24 09:15\"}")
	public void testSignupExisting(){
		Assert.assertOk(response);
	}
	
	// authenticate = 200
	@HttpTest(order=3, method=Method.POST, path="/user/testUser/authenticate", 
			headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, content="{\"password\":\"1234\"}")
	public void testAuthExistingUser(){
		Assert.assertOk(response);
	}
	
	// authenticate = 401
	@HttpTest(order=3, method=Method.POST, path="/user/testUser/authenticate", 
			headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, content="{\"password\":\"4321\"}")
	public void testAuthInvalidPassword(){
		Assert.assertUnauthorized(response);
	}
	
	// get all users, should exist
	@HttpTest(order=3, method=Method.GET, path="/users", headers={@Header(name="Accept", value="application/json")})
	public void testGetExistingUsers(){
		assertOk(response);
		TypeToken<List<User>> token = new TypeToken<List<User>>() {};
		List<User> objects = new Gson().fromJson(response.getBody(), token.getType());
		boolean userFound = false;
		for(User user : objects){
			if(user.getUserName().equalsIgnoreCase("testUser")){
				userFound = true;
				break;
			}
		}
		org.junit.Assert.assertTrue("User not found in list of users", userFound);
	}
	
	// update - 201 (change password)
	@HttpTest(order=4, method=Method.PUT, path="/user/testUser", headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, 
			content="{\"password\":\"4321\"}")
	public void testUpdateUserPassword(){
		Assert.assertCreated(response);
	}
	
	// update - 200
	@HttpTest(order=4, method=Method.PUT, path="/user/testUser", headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, 
			content="{\"userName\":\"testUser\"}")
	public void testUpdateUserWithNoChanges(){
		Assert.assertOk(response);
	}
	
	// authenticate with new password
	@HttpTest(order=5, method=Method.POST, path="/user/testUser/authenticate", 
			headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, content="{\"password\":\"4321\"}")
	public void testAuthExistingUserAfterUpdate(){
		Assert.assertOk(response);
	}
		
	//****************************************************
	//	Start of authentication test
	//	Three different cases
	/*
	// if the user name exists, and the password is correct
	@HttpTest(method = Method.POST, path = "user/justForTest/authenticate", type = MediaType.APPLICATION_JSON, 
			content = "{\"password\":\"1234\"}") 
	public void testAuthenticateOne() {

		assertOk(response);
	}
	
	// if the user name exists, but the password is wrong
	@HttpTest(method = Method.POST, path = "user/justForTest/authenticate", type = MediaType.APPLICATION_JSON, 
			content = "{\"password\":\"1233\"}") 
	public void testAuthenticateTwo() {
		Assert.assertUnauthorized(response);
	}
	
	//	if the user name does not exist
	@HttpTest(method = Method.POST, path = "user/non-existed-users/authenticate", type = MediaType.APPLICATION_JSON, 
			content = "{\"password\":\"1234\"}") 
	public void testAuthenticateThree() {
		Assert.assertNotFound(response);
	}
	
	//	End of authentication tests
	//*****************************************************
	
	
	
	//*****************************************************
	//	Start of sign up tests
	//	Three different cases for sign up tests
	
	//if a user already exists, and the password is correct.
	@HttpTest(method = Method.POST, path = "user/signup", type = MediaType.APPLICATION_JSON, 
			content = "{\"userName\":\"justForTest\",\"password\":\"1234\",\"createdAt\":\"2014-09-24 09:15\"}" ) 
	public void testSignupOne() {
		assertOk(response);
		String messg = response.getBody();
		System.out.println(messg);
	}
	
	// if a user already exists, but the password is wrong.
	@HttpTest(method = Method.POST, path = "user/signup", type = MediaType.APPLICATION_JSON, 
			content = "{\"userName\":\"justForTest\",\"password\":\"1233\",\"createAt\":\"2014-09-24 09:15\"}" ) 
	public void testSignupTwo() {
		Assert.assertBadRequest(response);
		String messg = response.getBody();
		System.out.println(messg);
	}
	
	//sign up as a new user. 
	//note: every time you run this, you should replace the user name with a new one
//	@HttpTest(method = Method.POST, path = "user/signup", type = MediaType.APPLICATION_JSON, 
//			content = "{\"userName\":\"Cef\",\"password\":\"pass\",\"createAt\":\"2014-09-24 09:15\"}" ) 
//	public void testSignupThree() {
//		Assert.assertCreated(response);
//		String messg = response.getBody();
//		System.out.println(messg);
//	}
	
	// sign up with invalid username
	@HttpTest(method = Method.POST, path = "user/signup", type = MediaType.APPLICATION_JSON,
			content = "{\"userName\":\"www\", \"password\":\"pass\",\"createdAt\":\"2014-09-29 09:15\"}")
	public void testSignupInvalidUserName(){
		Assert.assertBadRequest(response);
		String messg = response.getBody();
		
		System.out.println(messg);
	}
	
	//	End of sign up tests
	//*****************************************************
	
	
	//*****************************************************
	//	Start of retrieve all users test
	
	@SuppressWarnings("deprecation")
	@HttpTest(method = Method.GET, headers = {@Header(name = "Accept", value = "application/json")},
			path = "users", type = MediaType.APPLICATION_XML, content = "") 
	public void testRetrieveAllUsers() {
		Assert.assertOk(response);
		String messg = response.getBody();
		Log.debug("3333333333"+messg);
	}
	
	//	End of retrieve all users  test
	//*****************************************************
	
	
	//*****************************************************
	//	Start of retrieve a user's record test
	
	@HttpTest(method = Method.GET, headers = {@Header(name = "Accept", value = "application/json")},
			path = "user/justForTest",  content = "") 
	public void testRetrieveOneUserRec() {
		Assert.assertOk(response);
		String messg = response.getBody();
		System.out.println(messg);

	}
	
	//	End of retrieve a user's record test
	//*****************************************************
	
	//*****************************************************
	//	Start of update a user's record test
	
	@HttpTest(method = Method.PUT, headers = {@Header(name = "Accept", value = "application/json")},
			type = MediaType.APPLICATION_JSON, path = "user/justForTest",  content = "{\"password\":\"1234\"}") 
	public void testUpdateOneUserRec() {
		Assert.assertOk(response);
		String messg = response.getBody();
		System.out.println(messg);

	}
	
	//	End of update a user's record test
	//*****************************************************
	
	*/
}
