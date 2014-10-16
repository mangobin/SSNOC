package edu.cmu.sv.ws.ssnoc.data.analyzers;

import static org.junit.Assert.*;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectionSetAnalzerTest {

	ConnectionSetAnalzer sut;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
		users.add("Bin");
		users.add("Cef");
		users.add("Nikhil");
		sut.loadUsers(users);
		assertEquals(sut.getConnectedSets().size(), 1);
	}
	
	@Test
	public void testThatUserConnectionsGenerateNearbySets(){
		Set<String> users = new HashSet<String>();
		users.add("Bin");
		users.add("Cef");
		sut.loadUsers(users);
		UserConnections bin = new UserConnections("Bin");
		bin.addConnection("Cef");
		Set<String> unconnected = sut.processConnection(bin);
		assertEquals(unconnected.size(), 1);
		assertTrue(unconnected.contains("Bin"));
		assertFalse(unconnected.contains("Cef"));
	}
	
	@Test
	public void testThatUserConnectionsGeneratesSetIncludingNonConnectedUsers(){
		Set<String> users = new HashSet<String>();
		users.add("Bin");
		users.add("Cef");
		users.add("Nikhil");
		sut.loadUsers(users);
		UserConnections bin = new UserConnections("Bin");
		bin.addConnection("Cef");
		Set<String> unconnected = sut.processConnection(bin);
		assertEquals(unconnected.size(), 2);
		assertTrue(unconnected.contains("Bin"));
		assertFalse(unconnected.contains("Cef"));
		assertTrue(unconnected.contains("Nikhil"));
	}

}
