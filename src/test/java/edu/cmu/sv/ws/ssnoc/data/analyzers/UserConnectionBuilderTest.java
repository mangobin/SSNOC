package edu.cmu.sv.ws.ssnoc.data.analyzers;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserConnectionBuilderTest {
	
	UserConnectionBuilder sut;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sut = new UserConnectionBuilder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testThatWeCanAddOneUser(){
		sut.addUser("Bin");
		List<UserConnections> userConnections = sut.getUserConnections();
		assertEquals(userConnections.size(), 1);
	}
	
	@Test
	public void testThatWeCanAddTwoUsers(){
		sut.addUser("Bin");
		sut.addUser("Cef");
		List<UserConnections> userConnections = sut.getUserConnections();
		assertEquals(userConnections.size(), 2);
	}

	@Test
	public void testThatSendingAMessageWillAddAConnection(){
		sut.addUser("Bin");
		sut.addConnection("Bin", "Cef");
		List<UserConnections> userConnections = sut.getUserConnections();
		assertTrue(userConnections.get(0).getConnections().contains("Cef"));
	}
	
	@Test
	public void testThatReceivingAMessageWillAddAConnection(){
		sut.addUser("Bin");
		sut.addConnection("Cef", "Bin");
		List<UserConnections> userConnections = sut.getUserConnections();
		assertTrue(userConnections.get(0).getConnections().contains("Cef"));
	}

}
