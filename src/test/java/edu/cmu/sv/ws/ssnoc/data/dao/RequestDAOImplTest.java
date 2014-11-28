package edu.cmu.sv.ws.ssnoc.data.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.po.RequestPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Request;
import edu.cmu.sv.ws.ssnoc.dto.User;
import edu.cmu.sv.ws.ssnoc.rest.UserService;

public class RequestDAOImplTest {
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
	public void testSaveNotNullARequest() {
		RequestDAOImpl requestDAO = new RequestDAOImpl();
		RequestPO po = ConverterUtils.convert(req);
		long id = requestDAO.save(po);
		assertNotEquals(-1,id);
	}
	
	@Test
	public void testSaveNullARequest() {
		RequestDAOImpl requestDAO = new RequestDAOImpl();
		RequestPO po =  null;
		long id = requestDAO.save(po);
		assertEquals(-1,id);
	}

	@Test
	public void testFindRequestById() {
		RequestDAOImpl requestDAO = new RequestDAOImpl();
		long id = requestDAO.save(ConverterUtils.convert(req));
		RequestPO po = requestDAO.findRequestById(id);
		assertEquals("cmu",po.getLocation());
	}

	@Test
	public void testFindAllRequestsByUserName() {
		RequestDAOImpl requestDAO = new RequestDAOImpl();
		UserPO userPO = DAOFactory.getInstance().getUserDAO().findByName("Nikhil");
		req.setRequesterId(userPO.getUserId());
		RequestPO po = ConverterUtils.convert(req);
		requestDAO.save(po);
		
		List<RequestPO> poList = requestDAO.findAllRequestsByUserName("Nikhil");
		assertEquals(1,poList.size());
		assertEquals("urgent",poList.get(0).getDescription());
		
	}

	@Test
	public void testGetAllRequests() {
		RequestDAOImpl requestDAO = new RequestDAOImpl();
		UserPO userPO = DAOFactory.getInstance().getUserDAO().findByName("Nikhil");
		req.setRequesterId(userPO.getUserId());
		RequestPO po = ConverterUtils.convert(req);
		requestDAO.save(po);
		List<RequestPO> poList = requestDAO.getAllRequests(50, 0);
		Request dto = ConverterUtils.convert(poList.get(0));
		assertEquals(1,poList.size());
		assertEquals("Nikhil",dto.getUsername());
		assertEquals("urgent",dto.getDescription());
		
		
	}

}
