package edu.cmu.sv.ws.ssnoc.rest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Request;
import edu.cmu.sv.ws.ssnoc.dto.User;

public class RequestsServiceTest {

	private RequestService reqService;
	private Request req;
	private static List<String> list = new ArrayList<String>();

	@Before
	public void setUp() throws Exception {
		DBUtils.setTestMode(true);
		DBUtils.initializeDatabase();
		User u = new User();
		u.setUserName("Nikhil");
		u.setPassword("pass");
		u.setAccountStatus("Active");
		u.setCreatedAt("2014-01-01 01:01");
		u.setPrivilegeLevel("Citizen");
		UserService user = new UserService();
		user.addUser(u);
		
		reqService = new RequestService();
		req = new Request();
		req.setCreated_at("2014-01-01 01:01");
		list.add("fire");
		list.add("earthquake");
		req.setType(list);
		req.setLocation("cmu");
		req.setDescription("urgent");
		reqService.postRequest("Nikhil", req);
	}

	@After
	public void tearDown() throws Exception {
		DBUtils.truncateDatabase();
	}

	@Test
	public void testGetAllRequests() {
		RequestsService requestsService = new RequestsService();
		List<Request> list = requestsService.getAllRequests();
		Request req = list.get(0);
		assertEquals("cmu",req.getLocation());
	}

	@Test
	public void testGetAllRequestsSentByAUserHasOneRequest() {
		RequestsService requestsService = new RequestsService();
		List<Request> list = requestsService.getAllRequestsSentByAUser("Nikhil");
		Request req = list.get(0);
		assertEquals("urgent",req.getDescription());
	}
	
	@Test
	public void testGetAllRequestsSentByAUserHasMultipleRequest() {
		req = new Request();
		req.setCreated_at("2014-02-01 01:01");
		req.setType(list);
		req.setLocation("London");
		req.setDescription("normal");
		reqService.postRequest("Nikhil", req);
		
		RequestsService requestsService = new RequestsService();
		List<Request> list = requestsService.getAllRequestsSentByAUser("Nikhil");
		assertEquals(2,list.size());
		Request req = list.get(0);
		assertEquals("urgent",req.getDescription());
		Request req2 = list.get(1);
		assertEquals("normal",req2.getDescription());
	}
	
	@Test
	public void testGetAllRequestsSentByANonExistingUser() {
		RequestsService requestsService = new RequestsService();
		List<Request> list = requestsService.getAllRequestsSentByAUser("Bin");
		assertEquals(0,list.size());
	}
	

}
