package edu.cmu.sv.ws.ssnoc.test;

import static com.eclipsesource.restfuse.Assert.assertOk;

import java.util.List;

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

import edu.cmu.sv.ws.ssnoc.dto.User;

@RunWith(HttpJUnitRunner.class)
public class UserServiceIT {
	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:1234/ssnoc");
	@Context
	public Response response;
	
	// get all users, should exist
	@HttpTest(order=1, 
			method=Method.GET, 
			path="/users", 
			headers={@Header(name="Accept", value="application/json")})
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
	@HttpTest(order=1, 
			method=Method.GET, 
			path="/user/testUser", 
			headers={@Header(name="Accept", value="application/json")})
	public void testGetNonExistentUser(){
		Assert.assertNotFound(response);
	}
	
	// authenticate = 404 
	@HttpTest(order=1, 
			method=Method.POST, 
			path="/user/testUser/authenticate", 
			headers={@Header(name="Accept", 
			value="application/json")},
			type=MediaType.APPLICATION_JSON, 
			content="{\"password\":\"1234\"}")
	public void testAuthNonExistentUser(){
		Assert.assertNotFound(response);
	}
	
	// update - 404
	@HttpTest(order=1, 
			method=Method.PUT, 
			path="/user/testUser", 
			headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, 
			content="{\"password\":\"4321\"}")
	public void testUpdateUserNotFound(){
		Assert.assertNotFound(response);
	}
	
	// sign up - 201
	@HttpTest(order=2, 
			method=Method.POST, 
			headers={@Header(name="Accept", value="application/json")},
			path="/user/signup", 
			type=MediaType.APPLICATION_JSON, 
			content="{\"userName\":\"testUser\",\"password\":\"1234\",\"createdAt\":\"2014-09-24 09:15\"}")
	public void testSignup(){
		Assert.assertCreated(response);
	}
	
	// sign up - 200
	@HttpTest(order=3, 
			method=Method.POST, 
			headers={@Header(name="Accept", value="application/json")},
			path="/user/signup", 
			type=MediaType.APPLICATION_JSON, 
			content="{\"userName\":\"testUser\",\"password\":\"1234\",\"createdAt\":\"2014-09-24 09:15\"}")
	public void testSignupExisting(){
		Assert.assertOk(response);
	}
	
	// authenticate = 200
	@HttpTest(order=3, 
			method=Method.POST, 
			path="/user/testUser/authenticate", 
			headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, 
			content="{\"password\":\"1234\"}")
	public void testAuthExistingUser(){
		Assert.assertOk(response);
	}
	
	// authenticate = 401
	@HttpTest(order=3, 
			method=Method.POST, 
			path="/user/testUser/authenticate", 
			headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, 
			content="{\"password\":\"4321\"}")
	public void testAuthInvalidPassword(){
		Assert.assertUnauthorized(response);
	}
	
	// get all users, should exist
	@HttpTest(order=3, 
			method=Method.GET, 
			path="/users", 
			headers={@Header(name="Accept", value="application/json")})
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
	
	// update - 200 (change password)
	@HttpTest(order=4, 
			method=Method.PUT, path="/user/testUser", 
			headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, 
			content="{\"password\":\"4321\"}")
	public void testUpdateUserPassword(){
		Assert.assertOk(response);
	}
	
	// update - 201 (change name)
	@HttpTest(order=5, 
			method=Method.PUT, 
			path="/user/testUser", 
			headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, 
			content="{\"userName\":\"testUser1\"}")
	public void testUpdateUserWithNoChanges(){
		Assert.assertCreated(response);
	}
	
	// authenticate with new password
	@HttpTest(order=6, 
			method=Method.POST, 
			path="/user/testUser1/authenticate", 
			headers={@Header(name="Accept", value="application/json")},
			type=MediaType.APPLICATION_JSON, 
			content="{\"password\":\"4321\"}")
	public void testAuthExistingUserAfterUpdate(){
		Assert.assertOk(response);
	}
		
}
