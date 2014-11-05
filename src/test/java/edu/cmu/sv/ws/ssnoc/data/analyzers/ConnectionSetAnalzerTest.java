package edu.cmu.sv.ws.ssnoc.data.analyzers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectionSetAnalzerTest {

	ConnectionSetAnalzer sut;
	static String cef;
	static String binName;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception { 
		cef = "Cef";
		binName = "Bin";
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sut = new ConnectionSetAnalzer();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testThatWithNoMessagesWeGetTheSetOfAllUsers(){
		Set<String> users = new HashSet<String>();
		users.add(binName);
		users.add(cef);
		users.add("Nikhil");
		sut.loadUsers(users);
		assertEquals(sut.getConnectedSets().size(), 1);
	}
	
	@Test
	public void testThatUserConnectionsGenerateNearbySets(){
		Set<String> users = new HashSet<String>();
		users.add(binName);
		users.add(cef);
		sut.loadUsers(users);
		UserConnections bin = new UserConnections(binName);
		bin.addConnection(cef);
		Set<String> unconnected = sut.processConnection(bin);
		assertEquals(unconnected.size(), 1);
		assertTrue(unconnected.contains(binName));
		assertFalse(unconnected.contains(cef));
	}
	
	@Test
	public void testThatUserConnectionsGeneratesSetIncludingNonConnectedUsers(){
		Set<String> users = new HashSet<String>();
		users.add(binName);
		users.add(cef);
		users.add("Nikhil");
		sut.loadUsers(users);
		UserConnections bin = new UserConnections(binName);
		bin.addConnection(cef);
		Set<String> unconnected = sut.processConnection(bin);
		assertEquals(unconnected.size(), 2);
		assertTrue(unconnected.contains(binName));
		assertFalse(unconnected.contains(cef));
		assertTrue(unconnected.contains("Nikhil"));
	}

}
