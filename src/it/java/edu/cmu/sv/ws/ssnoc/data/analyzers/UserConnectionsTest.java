package edu.cmu.sv.ws.ssnoc.data.analyzers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserConnectionsTest {

	UserConnections user;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		user = new UserConnections("Bin");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testThatWeAddUserConnection(){
		user.addConnection("Cef");
		assertTrue(user.getConnections().contains("Cef"));
	}
	
	@Test
	public void testAddingTwoUsers(){
		user.addConnection("Cef");
		user.addConnection("Nikhil");
		assertTrue(user.getConnections().contains("Nikhil"));
		assertTrue(user.getConnections().contains("Cef"));
	}

}
