package edu.cmu.sv.ws.ssnoc.rest;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Request;
import edu.cmu.sv.ws.ssnoc.dto.User;

public class RequestServiceTest {

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
	}

	@After
	public void tearDown() throws Exception {
		DBUtils.truncateDatabase();
	}

	@Test
	public void testCanPostANewRequest() {
		Response r = reqService.postRequest("Nikhil", req);
		assertEquals(201, r.getStatus());
		Request ret = (Request) r.getEntity();
		assertEquals("cmu",ret.getLocation());
		assertEquals(list,ret.getType());
	}
	
	@Test
	public void testCanRetrieveARequestById() {
		Response r = reqService.postRequest("Nikhil", req);
		Request ret = (Request) r.getEntity();
		long reqId = ret.getRequestId();
		
		Response r2 = reqService.getRequestById(reqId);
		Request ret2 = (Request) r2.getEntity();
		assertEquals("cmu",ret2.getLocation());
		assertEquals("urgent",ret2.getDescription());
		assertEquals("Nikhil",ret2.getUsername());
	}
	
	@Test
	public void testCanUpdateARequest() {
		Response r = reqService.postRequest("Nikhil", req);
		Request ret = (Request) r.getEntity();
		long reqId = ret.getRequestId();
		
		Request updatedReq = new Request();
		updatedReq.setUpdated_at("2014-02-01 01:01");
		String newStatus = "resolved";
		updatedReq.setStatus(newStatus);
		Response r2 = reqService.updateRequestById(reqId, updatedReq);
		Request ret2 = (Request)r2.getEntity();
		assertEquals(200, r2.getStatus());
		assertEquals(newStatus,ret2.getStatus());
		
	}
	
	@Test
	public void testCanNotUpdateARequestWithoutTime() {
		Response r = reqService.postRequest("Nikhil", req);
		Request ret = (Request) r.getEntity();
		long reqId = ret.getRequestId();
		
		Request updatedReq = new Request();
		String newStatus = "resolved";
		updatedReq.setStatus(newStatus);
		Response r2 = reqService.updateRequestById(reqId, updatedReq);
		assertEquals(400, r2.getStatus());
		
	}

}
