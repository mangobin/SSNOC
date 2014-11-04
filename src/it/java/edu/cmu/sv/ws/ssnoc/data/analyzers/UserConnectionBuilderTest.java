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
	static String cef;
	static String bin;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		cef = "Cef";
		bin = "Bin";
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
		sut.addUser(bin);
		List<UserConnections> userConnections = sut.getUserConnections();
		assertEquals(userConnections.size(), 1);
	}
	
	@Test
	public void testThatWeCanAddTwoUsers(){
		sut.addUser(bin);
		sut.addUser(cef);
		List<UserConnections> userConnections = sut.getUserConnections();
		assertEquals(userConnections.size(), 2);
	}

	@Test
	public void testThatSendingAMessageWillAddAConnection(){
		sut.addUser(bin);
		sut.addConnection(bin, cef);
		List<UserConnections> userConnections = sut.getUserConnections();
		assertTrue(userConnections.get(0).getConnections().contains(cef));
	}
	
	@Test
	public void testThatReceivingAMessageWillAddAConnection(){
		sut.addUser(bin);
		sut.addConnection(cef, bin);
		List<UserConnections> userConnections = sut.getUserConnections();
		assertTrue(userConnections.get(0).getConnections().contains(cef));
	}

}
