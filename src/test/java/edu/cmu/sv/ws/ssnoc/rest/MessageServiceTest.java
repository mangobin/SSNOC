package edu.cmu.sv.ws.ssnoc.rest;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;

public class MessageServiceTest {
	
	@Before
	public void setup() throws Exception {
		DBUtils.setTestMode(true);
		DBUtils.initializeDatabase();
		DBUtils.truncateDatabase();
	}

	@Test
	public void testPostingOnWall(){
		User u = new User();
		u.setUserName("Nikhil");
		u.setPassword("pass");
		u.setAccountStatus("Active");
		u.setCreatedAt("2014-01-01 01:01");
		u.setPrivilegeLevel("Citizen");
		
		UserService user = new UserService();
		user.addUser(u);
		MessageService input = new MessageService();
		Message message = new Message();
		message.setAuthor("Nikhil");
		message.setContent("Message");
		message.setMessageID(1);
		message.setMessageType("WALL");
		message.setPostedAt("2014-01-01 01:01");
		Response r = input.postMessageOnPublicWall("Nikhil", message);
		assertEquals(201, r.getStatus());
		Message m = (Message)r.getEntity();
		assertEquals("Nikhil", m.getAuthor());
	}	
	
	@Test
	public void testRetrievingMessageById(){		
		User u = new User();
		u.setUserName("Nikhil");
		u.setPassword("pass");
		u.setAccountStatus("Active");
		u.setCreatedAt("2014-01-01 01:01");
		u.setPrivilegeLevel("Citizen");
		
		UserService user = new UserService();
		user.addUser(u);
		MessageService input = new MessageService();
		Message message = new Message();
		message.setAuthor("Nikhil");
		message.setContent("Message");
		message.setMessageID(1);
		message.setMessageType("WALL");
		message.setPostedAt("2014-01-01 01:01");
		Response r = input.postMessageOnPublicWall("Nikhil", message);
		assertEquals(201, r.getStatus());
		MessagesService output = new MessagesService();
		List<Message> messages = output.retrieveAllMsgOnPublicWall();
		long ID = messages.get(0).getMessageID();
	
		MessageService output1 = new MessageService();
		Message message1 = output1.retrieveMesssageById(ID);
		assertEquals("Nikhil", message1.getAuthor());
		
	}
	
	@Test
	public void testRetrievingAllMessages(){
		User u = new User();
		u.setUserName("Nikhil");
		u.setPassword("pass");
		u.setAccountStatus("Active");
		u.setCreatedAt("2014-01-01 01:01");
		u.setPrivilegeLevel("Citizen");
		
		UserService user = new UserService();
		user.addUser(u);
		MessageService input = new MessageService();
		Message message = new Message();
		message.setAuthor("Nikhil");
		message.setContent("Message");
		message.setMessageID(1);
		message.setMessageType("WALL");
		message.setPostedAt("2014-01-01 01:01");
		Response r = input.postMessageOnPublicWall("Nikhil", message);
		assertEquals(201, r.getStatus());
		MessagesService output = new MessagesService();
		List<Message> messages = output.retrieveAllMsgOnPublicWall();
		assertEquals("Message", messages.get(0).getContent());
	}
	
	@Test
	public void testRetrievingInvisibleMessagesReturnsNothing() {
		User u = new User();
		u.setUserName("Nikhil");
		u.setPassword("pass");
		u.setAccountStatus("Inactive");
		u.setCreatedAt("2014-01-01 01:01");
		u.setPrivilegeLevel("Citizen");
		
		UserService user = new UserService();
		user.addUser(u);
		MessageService input = new MessageService();
		Message message = new Message();
		message.setAuthor("Nikhil");
		message.setContent("Message");
		message.setMessageID(1);
		message.setMessageType("WALL");
		message.setPostedAt("2014-01-01 01:01");
		Response r = input.postMessageOnPublicWall("Nikhil", message);
		assertEquals(201, r.getStatus());
		
		UserService user1 = new UserService();
		Response r1 = user1.updateUser("Nikhil", u);
		assertEquals(200, r1.getStatus());
		
		MessagesService m = new MessagesService();
		List<Message> messages = m.retrieveAllVisibleMsgOnPublicWall();
		assertTrue(messages.isEmpty());
	}
	
	@Test
	public void testSendingChatMessage() {
		
		User u1 = new User();
		u1.setUserName("Nikhil");
		u1.setPassword("pass");
		u1.setAccountStatus("Active");
		u1.setCreatedAt("2014-01-01 01:01");
		u1.setPrivilegeLevel("Citizen");
		
		User u2 = new User();
		u2.setUserName("Cef");
		u2.setPassword("pass");
		u2.setAccountStatus("Active");
		u2.setCreatedAt("2014-01-01 01:01");
		u2.setPrivilegeLevel("Citizen");
		
		UserService user = new UserService();
		user.addUser(u1);
		user.addUser(u2);
		
		Message msg = new Message();
		msg.setAuthor("Nikhil");
		msg.setContent("Message");
		msg.setMessageID(1);
		msg.setMessageType("CHAT");
		msg.setPostedAt("2014-01-01 01:01");
		msg.setTarget("Cef");
		
		MessageService input = new MessageService();
		Response r = input.sendChatMessages("Nikhil", "Cef", msg);
		assertEquals(201, r.getStatus());
		Message m = (Message)r.getEntity();
		assertEquals("Nikhil", m.getAuthor());
	}
	
	@Test
	public void testRetrievingMessagesBetweenTwoUsers() {
		User u1 = new User();
		u1.setUserName("Nikhil");
		u1.setPassword("pass");
		u1.setAccountStatus("Active");
		u1.setCreatedAt("2014-01-01 01:01");
		u1.setPrivilegeLevel("Citizen");
		
		User u2 = new User();
		u2.setUserName("Cef");
		u2.setPassword("pass");
		u2.setAccountStatus("Active");
		u2.setCreatedAt("2014-01-01 01:01");
		u2.setPrivilegeLevel("Citizen");
		
		UserService user = new UserService();
		user.addUser(u1);
		user.addUser(u2);
		
		Message msg = new Message();
		msg.setAuthor("Nikhil");
		msg.setContent("Message");
		msg.setMessageID(1);
		msg.setMessageType("CHAT");
		msg.setPostedAt("2014-01-01 01:01");
		msg.setTarget("Cef");
		
		MessageService input = new MessageService();
		Response r = input.sendChatMessages("Nikhil", "Cef", msg);
		assertEquals(201, r.getStatus());
		
		MessagesService m = new MessagesService();
		List<Message> messages = m.retrieveAllMessagesBetweenTwoUsers("Nikhil", "Cef");
		assertEquals("Message", messages.get(0).getContent());
	}
	
//	@Test
//	public void testRetrievingInvisibleChatMessages() {
//		User u1 = new User();
//		u1.setUserName("Nikhil");
//		u1.setPassword("pass");
//		u1.setCreatedAt("2014-01-01 01:01");
//		u1.setPrivilegeLevel("Citizen");
//		
//		User u2 = new User();
//		u2.setUserName("Cef");
//		u2.setPassword("pass");
//		u2.setAccountStatus("Inactive");
//		u2.setCreatedAt("2014-01-01 01:01");
//		u2.setPrivilegeLevel("Citizen");
//		
//		UserService user = new UserService();
//		user.addUser(u1);
//		user.addUser(u2);
//		
//		Message msg = new Message();
//		msg.setAuthor("Nikhil");
//		msg.setContent("Message");
//		msg.setMessageID(1);
//		msg.setMessageType("CHAT");
//		msg.setPostedAt("2014-01-01 01:01");
//		msg.setTarget("Cef");
//		
//		MessageService input = new MessageService();
//		Response r = input.sendChatMessages("Nikhil", "Cef", msg);
//		assertEquals(201, r.getStatus());
//		
//		UserService user1 = new UserService();
//		User u3 = new User();
//		u3.setAccountStatus("Inactive");
//		Response r1 = user1.updateUser("Nikhil", u3);
//		assertEquals(200, r1.getStatus());
//		
//		UserService user2 = new UserService();
//		Response r2 = user2.updateUser("Cef", u2);
//		assertEquals(200, r2.getStatus());
//		
//		MessagesService m = new MessagesService();
//		List<Message> messages = m.retrieveAllMessagesBetweenTwoUsers("Nikhil", "Cef");
//		assertTrue(messages.isEmpty());
//		
//	}

}
