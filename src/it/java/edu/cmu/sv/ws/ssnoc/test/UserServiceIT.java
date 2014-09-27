package edu.cmu.sv.ws.ssnoc.test;

import static com.eclipsesource.restfuse.Assert.assertOk;

import org.junit.Rule;
import org.junit.runner.RunWith;

import com.eclipsesource.restfuse.Assert;
import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.MediaType;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;
import com.eclipsesource.restfuse.annotation.*;

@RunWith(HttpJUnitRunner.class)
public class UserServiceIT {
	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:4321/ssnoc/");
	@Context
	public Response response;


//	@HttpTest(method = Method.POST, path = "/user/Cef/authenticate", type = MediaType.APPLICATION_JSON, 
//			content = "{\"userName\":\"Cefe\",\"password\":\"pass\"}")
//	public void testInvalidLogin() {
//		Assert.assertBadRequest(response);
//		String messg = response.getBody();
//		org.junit.Assert.assertEquals("Invalid username: Cef", messg);
//	}
//	
//	@HttpTest(method = Method.GET, path = "/")
//	public void testFail(){
//		org.junit.Assert.assertFalse(true);
//		
//	}
	
	//****************************************************
	//	Start of authentication test
	//	Three different cases
	
	// if the user name exists, and the password is correct
	@HttpTest(method = Method.POST, path = "user/test/authenticate", type = MediaType.APPLICATION_JSON, 
			content = "{\"password\":\"11\"}") 
	public void testAuthenticateOne() {

		assertOk(response);
		String messg = response.getBody();
	}
	
	// if the user name exists, but the password is wrong
	@HttpTest(method = Method.POST, path = "user/test/authenticate", type = MediaType.APPLICATION_JSON, 
			content = "{\"password\":\"123\"}") 
	public void testAuthenticateTwo() {
		Assert.assertUnauthorized(response);
		String messg = response.getBody();
	}
	
	//	if the user name does not exist
	@HttpTest(method = Method.POST, path = "user/non-existed-users/authenticate", type = MediaType.APPLICATION_JSON, 
			content = "{\"password\":\"12\"}") 
	public void testAuthenticateThree() {
		Assert.assertNotFound(response);
		String messg = response.getBody();
	}
	
	//	End of authentication tests
	//*****************************************************
	
	
	
	//*****************************************************
	//	Start of sign up tests
	//	Three different cases for sign up tests
	
	//if a user already exists, and the password is correct.
	@HttpTest(method = Method.POST, path = "user/signup", type = MediaType.APPLICATION_JSON, 
			content = "{\"userName\":\"test\",\"password\":\"12\",\"createAt\":\"2014-09-24 09:15\"}" ) 
	public void testSignupOne() {
		assertOk(response);
		String messg = response.getBody();
		System.out.println(messg);
	}
	
	// if a user already exists, but the password is wrong.
	@HttpTest(method = Method.POST, path = "user/signup", type = MediaType.APPLICATION_JSON, 
			content = "{\"userName\":\"test\",\"password\":\"123\",\"createAt\":\"2014-09-24 09:15\"}" ) 
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
	
	
	//	End of sign up tests
	//*****************************************************
	
	
	//*****************************************************
	//	Start of retrieve all users test
	
	@HttpTest(method = Method.GET, headers = {@Header(name = "Accept", value = "application/json")},
			path = "users", type = MediaType.APPLICATION_XML, content = "") 
	public void testRetrieveAllUsers() {
		Assert.assertOk(response);
		String messg = response.getBody();
		System.out.println(messg);
	}
	
	//	End of retrieve all users  test
	//*****************************************************
	
	
	//*****************************************************
	//	Start of retrieve a user's record test
	
	@HttpTest(method = Method.GET, headers = {@Header(name = "Accept", value = "application/json")},
			path = "user/test",  content = "") 
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
			type = MediaType.APPLICATION_JSON, path = "user/test",  content = "{\"password\":\"12\"}") 
	public void testUpdateOneUserRec() {
		Assert.assertOk(response);
		String messg = response.getBody();
		System.out.println(messg);

	}
	
	//	End of update a user's record test
	//*****************************************************
	
	
}
