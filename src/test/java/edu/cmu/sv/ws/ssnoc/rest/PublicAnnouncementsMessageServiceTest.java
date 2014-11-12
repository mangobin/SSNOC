package edu.cmu.sv.ws.ssnoc.rest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.MessageDAOImpl;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;

public class PublicAnnouncementsMessageServiceTest {

	static User sUser = null;
	
	MessageService messageService;
	MessagesService messagesService;
	
	@BeforeClass
	public static void setupClass() throws SQLException {
		DBUtils.setTestMode(true);
		DBUtils.initializeDatabase();
		
		User user = new User();
		user.setUserName("TestUser");
		user.setPassword("1234");
		user.setCreatedAt("2014-11-11 11:11");
		
		UserService userService = new UserService();
		userService.addUser(user);
		
		// load back user from DB
		sUser = userService.loadUser(user.getUserName());
		
		// set privilege level to Coordinator
		sUser.setPrivilegeLevel("Coordinator");
		userService.updateUser(sUser.getUserName(), sUser);
	}
	
	@AfterClass
	public static void tearDownClass() throws SQLException {
		DBUtils.truncateDatabase();
		DBUtils.setTestMode(false);
	}
	
	@Before
	public void setup() throws Exception {
		// set account status to active
		sUser.setAccountStatus("Active");
		new UserService().updateUser(sUser.getUserName(), sUser);
		
		new MessageDAOImpl().truncateMessageTable(); 
		messageService = new MessageService();
		messagesService = new MessagesService();
	}

	@Test
	public void testGetEmptyAnnouncements() {
		List<Message> messages = messagesService.retrieveAllAnnouncement();
		assertEquals(0, messages.size());
	}
	
	@Test
	public void testGetEmptyVisibleAnnouncements() {
		List<Message> messages = messagesService.retrieveAllVisibleAnnouncement();
		assertEquals(0, messages.size());
	}
	
	@Test
	public void testPostAnnouncement() {
		Message msg = new Message();
		msg.setAuthor(sUser.getUserName());
		msg.setContent("Test Announcement");
		msg.setPostedAt("2014-11-11 14:00");
		Response response = messageService.postAnnouncement(msg);
		assertEquals(201, response.getStatus());
		Message result = (Message)response.getEntity();
		assertEquals(msg.getAuthor(), result.getAuthor());
		assertEquals(msg.getContent(), result.getContent());
		assertEquals(msg.getPostedAt(), result.getPostedAt());
		assertNotEquals(-1, result.getMessageID());
	}
	
	@Test
	public void testGetAnnouncements(){
		Message msg = new Message();
		msg.setAuthor(sUser.getUserName());
		msg.setContent("Test Announcement");
		msg.setPostedAt("2014-11-11 14:00");
		Response response = messageService.postAnnouncement(msg);
		assertEquals(201, response.getStatus());
		
		List<Message> messages = messagesService.retrieveAllAnnouncement();
		assertEquals(1, messages.size());
		Message result = messages.get(0);
		assertEquals(msg.getAuthor(), result.getAuthor());
		assertEquals(msg.getContent(), result.getContent());
		assertEquals(msg.getPostedAt(), result.getPostedAt());
		assertNotEquals(-1, result.getMessageID());
	}
	
	@Test
	public void testGetVisibleAnnouncements(){
		Message msg = new Message();
		msg.setAuthor(sUser.getUserName());
		msg.setContent("Test Announcement");
		msg.setPostedAt("2014-11-11 14:00");
		Response response = messageService.postAnnouncement(msg);
		assertEquals(201, response.getStatus());
		
		List<Message> messages = messagesService.retrieveAllVisibleAnnouncement();
		assertEquals(1, messages.size());
		Message result = messages.get(0);
		assertEquals(msg.getAuthor(), result.getAuthor());
		assertEquals(msg.getContent(), result.getContent());
		assertEquals(msg.getPostedAt(), result.getPostedAt());
		assertNotEquals(-1, result.getMessageID());
	}
	
	@Test
	public void testGetVisibleAnnouncementsWithInactiveUser(){
		Message msg = new Message();
		msg.setAuthor(sUser.getUserName());
		msg.setContent("Test Announcement");
		msg.setPostedAt("2014-11-11 14:00");
		Response response = messageService.postAnnouncement(msg);
		assertEquals(201, response.getStatus());
		
		// set privilege level to Coordinator
		sUser.setAccountStatus("Inactive");
		new UserService().updateUser(sUser.getUserName(), sUser);
		
		List<Message> messages = messagesService.retrieveAllVisibleAnnouncement();
		assertEquals(0, messages.size());
	}
	
	@Test
	public void testPostAnnouncementFromUnknownUser(){
		Message msg = new Message();
		msg.setAuthor("Unknown User");
		msg.setContent("Test Announcement");
		msg.setPostedAt("2014-11-11 14:00");
		Response response = messageService.postAnnouncement(msg);
		assertEquals(400, response.getStatus());
		
		List<Message> messages = messagesService.retrieveAllAnnouncement();
		assertEquals(0, messages.size());
	}

	@Test
	public void testSQLInjection(){
		Message msg = new Message();
		msg.setAuthor(sUser.getUserName());
		msg.setContent("(SELECT * SSN_USERS)");
		msg.setPostedAt("2014-11-11 14:00");
		Response response = messageService.postAnnouncement(msg);
		assertEquals(201, response.getStatus());
		
		Message result = (Message)response.getEntity();
		assertEquals(msg.getAuthor(), result.getAuthor());
		assertEquals(msg.getContent(), result.getContent());
		assertEquals(msg.getPostedAt(), result.getPostedAt());
		assertNotEquals(-1, result.getMessageID());
	}
	
}
