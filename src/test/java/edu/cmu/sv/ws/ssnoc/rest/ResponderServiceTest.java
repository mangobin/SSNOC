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
import edu.cmu.sv.ws.ssnoc.dto.Responder;
import edu.cmu.sv.ws.ssnoc.dto.User;

public class ResponderServiceTest {

	private RequestService reqService;
	private Request req;
	private long requestId;

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

		u = new User();
		u.setUserName("Bin");
		u.setPassword("pass");
		u.setAccountStatus("Active");
		u.setCreatedAt("2014-01-01 01:01");
		u.setPrivilegeLevel("Citizen");
		user.addUser(u);

		reqService = new RequestService();
		req = new Request();
		req.setCreated_at("2014-01-01 01:01");
		List<String> typelist = new ArrayList<String>();
		typelist.add("fire");
		typelist.add("earthquake");
		req.setType(typelist);
		req.setLocation("cmu");
		req.setDescription("urgent");
		Response r = reqService.postRequest("Nikhil", req);
		Request dto = (Request) r.getEntity();
		requestId = dto.getRequestId();

	}

	@After
	public void tearDown() throws Exception {
		DBUtils.truncateDatabase();
	}

	@Test
	public void testSetRespondersForARequest() {
		Responder responder = new Responder();
		responder.setUsername("Bin");
		responder.setUpdated_at("2014-02-01 01:01");
		List<Responder> list = reqService.setRespondersForARequest(requestId,
				responder);

		assertEquals(1, list.size());
		Responder ret = list.get(0);
		assertEquals("Bin", ret.getUsername());
		assertEquals("2014-02-01 01:01", ret.getUpdated_at());
		Response r = reqService.getRequestById(requestId);
		Request request = (Request)r.getEntity();
		assertEquals(1,request.getResponders().size());
		assertEquals("Bin",request.getResponders().get(0).getUsername());

	}

	@Test
	public void testSetNonExistingRespondersForARequest() {
		Responder responder = new Responder();
		responder.setUsername("v.onai");
		responder.setUpdated_at("2014-02-01 01:01");
		List<Responder> list = reqService.setRespondersForARequest(requestId,
				responder);
		assertEquals(0, list.size());

	}

	@Test
	public void testSetRespondersForARequestWithoutDate() {
		Responder responder = new Responder();
		responder.setUsername("Bin");
		List<Responder> list = reqService.setRespondersForARequest(requestId,
				responder);
		assertEquals(0, list.size());

	}

	@Test
	public void testGetResponderByResponderId() {
		Responder responder = new Responder();
		responder.setUsername("Bin");
		responder.setUpdated_at("2014-02-01 01:01");
		List<Responder> list = reqService.setRespondersForARequest(requestId,
				responder);
		long responderId = list.get(0).getResponderId();
		RequestsService requestsService = new RequestsService();
		Response r = requestsService.getAResponderById(responderId);
		Responder ret = (Responder) r.getEntity();
		assertEquals(200, r.getStatus());
		assertEquals("Bin", ret.getUsername());

	}

	@Test
	public void testUpdateResponderById() {
		Responder responder = new Responder();
		responder.setUsername("Bin");
		responder.setUpdated_at("2014-02-01 01:01");
		List<Responder> list = reqService.setRespondersForARequest(requestId,
				responder);
		long responderId = list.get(0).getResponderId();

		Responder update = new Responder();
		update.setUpdated_at("2014-03-01 01:01");
		update.setStatus("Accept");
		RequestsService requestsService = new RequestsService();
		Response r = requestsService.updateAResponderById(responderId, update);
		Responder ret = (Responder) r.getEntity();
		assertEquals(200, r.getStatus());
		assertEquals("Accept", ret.getStatus());

	}

	@Test
	public void testGetAllMissionsByUserName() {
		Responder responder = new Responder();
		responder.setUsername("Bin");
		responder.setUpdated_at("2014-02-01 01:01");
		reqService.setRespondersForARequest(requestId, responder);

		req = new Request();
		req.setCreated_at("2014-03-01 01:01");
		List<String> typelist = new ArrayList<String>();
		typelist.add("fire");
		req.setType(typelist);
		req.setLocation("cornel");
		req.setDescription("urgent");
		Response r = reqService.postRequest("Nikhil", req);
		Request dto = (Request) r.getEntity();
		long request2Id = dto.getRequestId();

		reqService.setRespondersForARequest(request2Id, responder);

		RequestsService requestsService = new RequestsService();
		List<Responder> retList = requestsService.getAllMissionsForAUser("Bin");
		assertEquals(2, retList.size());

	}

}
