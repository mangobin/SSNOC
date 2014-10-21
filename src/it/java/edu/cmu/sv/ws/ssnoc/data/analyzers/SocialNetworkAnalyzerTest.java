package edu.cmu.sv.ws.ssnoc.data.analyzers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.dto.Message;
import edu.cmu.sv.ws.ssnoc.dto.User;

public class SocialNetworkAnalyzerTest {

	SocialNetworkAnalyzer sut;
	List<User> users;
	List<Message> messages;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sut = new SocialNetworkAnalyzer();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testThatWithNoMessageWeHaveOneSetOfAllUsers(){
		users = new ArrayList<User>();
		User user1 = new User();
		user1.setUserName("Cef");
		User user2 = new User();
		user2.setUserName("Bin");
		User user3 = new User();
		user3.setUserName("Nikhil");
		User user4 = new User();
		user4.setUserName("Jian");
		
		users.add(user1);
		users.add(user2);
		users.add(user3);
		users.add(user4);
		
		sut.loadUsers(users);
		
		List<Set<String>> unconnected = sut.getUnconnectedUsers();
		assertEquals(1, unconnected.size());
		assertEquals(4, unconnected.get(0).size());
	}
	
	@Test
	public void testThatAMessageBetweenTwoUsersWillSeparateThem(){
		users = new ArrayList<User>();
		messages = new ArrayList<Message>();
		User user1 = new User();
		user1.setUserName("Cef");
		User user2 = new User();
		user2.setUserName("Bin");

		users.add(user1);
		users.add(user2);
		
		sut.loadUsers(users);

		Message msg = new Message();
		msg.setAuthor("Cef");
		msg.setTarget("Bin");
		messages.add(msg);
		
		sut.loadMessages(messages);
				
		List<Set<String>> unconnected = sut.getUnconnectedUsers();
		assertEquals(2, unconnected.size());
		assertEquals(1, unconnected.get(0).size());
		assertEquals(1, unconnected.get(1).size());
	}
	
	@Test
	public void testThatAMessageBetweenTwoUsersWillSeparateFromAThirdUser(){
		users = new ArrayList<User>();
		messages = new ArrayList<Message>();
		User user1 = new User();
		user1.setUserName("Cef");
		User user2 = new User();
		user2.setUserName("Bin");
		User user3 = new User();
		user3.setUserName("Nikhil");

		users.add(user1);
		users.add(user2);
		users.add(user3);
		
		sut.loadUsers(users);

		Message msg = new Message();
		msg.setAuthor("Cef");
		msg.setTarget("Bin");
		messages.add(msg);
		
		sut.loadMessages(messages);
				
		List<Set<String>> unconnected = sut.getUnconnectedUsers();
		assertEquals(2, unconnected.size());
		assertEquals(2, unconnected.get(0).size());
		assertEquals(2, unconnected.get(1).size());
	}
	
	@Test
	public void testThatAMessageBetweenThreeUsersWillSeparateThem(){
		users = new ArrayList<User>();
		messages = new ArrayList<Message>();
		User user1 = new User();
		user1.setUserName("Cef");
		User user2 = new User();
		user2.setUserName("Bin");
		User user3 = new User();
		user3.setUserName("Nikhil");

		users.add(user1);
		users.add(user2);
		users.add(user3);
		
		sut.loadUsers(users);

		Message msg = new Message();
		msg.setAuthor("Cef");
		msg.setTarget("Bin");
		messages.add(msg);
		
		Message msg2 = new Message();
		msg2.setAuthor("Bin");
		msg2.setTarget("Nikhil");
		messages.add(msg2);
		
		sut.loadMessages(messages);
						
		List<Set<String>> unconnected = sut.getUnconnectedUsers();
		assertEquals(2, unconnected.size());
		if(unconnected.get(0).size() == 1){
			assertTrue(unconnected.get(0).contains("Bin"));
			assertTrue(unconnected.get(1).contains("Cef"));
			assertTrue(unconnected.get(1).contains("Nikhil"));
		} else {
			assertTrue(unconnected.get(0).contains("Cef"));
			assertTrue(unconnected.get(0).contains("Nikhil"));
			assertTrue(unconnected.get(1).contains("Bin"));
		}
	}
	
}
