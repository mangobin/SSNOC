package edu.cmu.sv.ws.ssnoc.data.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;

public class ChatMessageDAOImplTest {
	
	@Before
	public void setup() throws Exception {
		DBUtils.setTestMode(true);
		DBUtils.initializeDatabase();
		DBUtils.truncateDatabase();
	}

	@Test
	public void testFindingChatHistory() {
		Date date= new Date();
		
		UserPO user1 = new UserPO();
		user1.setUserId(1);
		user1.setCreatedAt(date);
		user1.setAccountStatus("Active");
		user1.setLastStatusID(0);
		user1.setModifiedAt(date);
		user1.setPassword("pass");
		user1.setPrivilegeLevel("Citizen");
		user1.setSalt("");
		user1.setUserName("Nikhil");
		UserPO user2 = new UserPO();
		user2.setUserId(2);
		user2.setCreatedAt(date);
		user2.setAccountStatus("Active");
		user2.setLastStatusID(0);
		user2.setModifiedAt(date);
		user2.setPassword("pass");
		user2.setPrivilegeLevel("Citizen");
		user2.setSalt("");
		user2.setUserName("Cef");
		
		UserDAOImpl user = new UserDAOImpl();
		long userid1 = user.save(user1);
		System.out.println("inserted user id: " + userid1);
		long userid2 = user.save(user2);
		System.out.println("inserted user id: " + userid2);
		
		MessagePO input = new MessagePO();
		input.setAuthor(userid1);
		input.setContent("Message");
		input.setMessageType("CHAT");
		input.setPostedAt(date);
		input.setTarget(userid2);
		
		MessageDAOImpl messageDAO = new MessageDAOImpl();
		
		long ID = messageDAO.save(input);
		assertNotEquals(0, ID);
		
		List<MessagePO> messages = messageDAO.findChatHistoryBetweenTwoUsers(userid1, userid2);
		assertEquals(userid1, messages.get(0).getAuthor());
		assertEquals(userid2, messages.get(0).getTarget());
		assertEquals("Message", messages.get(0).getContent());
		assertEquals("CHAT", messages.get(0).getMessageType());
		assertEquals(date, messages.get(0).getPostedAt());
		
	}
	
	@Test
	public void testFindingChatBuddies() {
		Date date= new Date();
		
		UserPO user1 = new UserPO();
		user1.setUserId(1);
		user1.setCreatedAt(date);
		user1.setAccountStatus("Active");
		user1.setLastStatusID(0);
		user1.setModifiedAt(date);
		user1.setPassword("pass");
		user1.setPrivilegeLevel("Citizen");
		user1.setSalt("");
		user1.setUserName("Nikhil");
		UserPO user2 = new UserPO();
		user2.setUserId(2);
		user2.setCreatedAt(date);
		user2.setAccountStatus("Active");
		user2.setLastStatusID(0);
		user2.setModifiedAt(date);
		user2.setPassword("pass");
		user2.setPrivilegeLevel("Citizen");
		user2.setSalt("");
		user2.setUserName("Cef");
		
		UserDAOImpl user = new UserDAOImpl();
		long userid1 = user.save(user1);
		System.out.println("inserted user id: " + userid1);
		long userid2 = user.save(user2);
		System.out.println("inserted user id: " + userid2);
		
		MessagePO input = new MessagePO();
		input.setAuthor(userid1);
		input.setContent("Message");
		input.setMessageType("CHAT");
		input.setPostedAt(date);
		input.setTarget(userid2);
		
		MessageDAOImpl messageDAO = new MessageDAOImpl();
		
		long ID = messageDAO.save(input);
		assertNotEquals(0, ID);

		List<UserPO> users = messageDAO.findChatBuddies(userid1);
		assertEquals(userid2, users.get(0).getUserId());
		
	}
	
	@Test
	public void testFindingAuthorAndTarget() {
		Date date= new Date();
		
		UserPO user1 = new UserPO();
		user1.setUserId(1);
		user1.setCreatedAt(date);
		user1.setAccountStatus("Active");
		user1.setLastStatusID(0);
		user1.setModifiedAt(date);
		user1.setPassword("pass");
		user1.setPrivilegeLevel("Citizen");
		user1.setSalt("");
		user1.setUserName("Nikhil");
		UserPO user2 = new UserPO();
		user2.setUserId(2);
		user2.setCreatedAt(date);
		user2.setAccountStatus("Active");
		user2.setLastStatusID(0);
		user2.setModifiedAt(date);
		user2.setPassword("pass");
		user2.setPrivilegeLevel("Citizen");
		user2.setSalt("");
		user2.setUserName("Cef");
		
		UserDAOImpl user = new UserDAOImpl();
		long userid1 = user.save(user1);
		System.out.println("inserted user id: " + userid1);
		long userid2 = user.save(user2);
		System.out.println("inserted user id: " + userid2);
		
		MessagePO input = new MessagePO();
		input.setAuthor(userid1);
		input.setContent("Message");
		input.setMessageType("CHAT");
		input.setPostedAt(date);
		input.setTarget(userid2);
		
		MessageDAOImpl messageDAO = new MessageDAOImpl();
		
		long ID = messageDAO.save(input);
		assertNotEquals(0, ID);

		List<UserPO> users = messageDAO.findChatBuddies(userid2);
		assertEquals(userid1, users.get(0).getUserId());
		
	}

	
	@Test
	public void testFindingChatMessagesSinceDate() {
		Date date = new Date();
		
		UserPO user1 = new UserPO();
		user1.setUserId(1);
		user1.setCreatedAt(date);
		user1.setAccountStatus("Active");
		user1.setLastStatusID(0);
		user1.setModifiedAt(date);
		user1.setPassword("pass");
		user1.setPrivilegeLevel("Citizen");
		user1.setSalt("");
		user1.setUserName("Nikhil");
		UserPO user2 = new UserPO();
		user2.setUserId(2);
		user2.setCreatedAt(date);
		user2.setAccountStatus("Active");
		user2.setLastStatusID(0);
		user2.setModifiedAt(date);
		user2.setPassword("pass");
		user2.setPrivilegeLevel("Citizen");
		user2.setSalt("");
		user2.setUserName("Cef");
		
		UserDAOImpl user = new UserDAOImpl();
		long userid1 = user.save(user1);
		System.out.println("inserted user id: " + userid1);
		long userid2 = user.save(user2);
		System.out.println("inserted user id: " + userid2);
		
		MessagePO input = new MessagePO();
		input.setAuthor(userid1);
		input.setContent("Message");
		input.setMessageType("CHAT");
		input.setPostedAt(date);
		input.setTarget(userid2);
		
		MessageDAOImpl messageDAO = new MessageDAOImpl();
		
		long ID = messageDAO.save(input);
		assertNotEquals(0, ID);
		List<MessagePO> messages = messageDAO.findChatMessagesSinceDate(date);
		assertEquals("Message", messages.get(0).getContent());
	}

}
