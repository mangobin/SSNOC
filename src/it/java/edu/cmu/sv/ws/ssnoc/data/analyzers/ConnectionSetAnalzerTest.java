package edu.cmu.sv.ws.ssnoc.data.analyzers;

import static org.junit.Assert.*;

import java.util.HashSet;
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
		sut.loadUser(users);
		assertEquals(sut.getConnectedSets().size(), 1);
	}
	
	@Test
	public void testThatUserConnectionsCreatesSeparateSets(){
		Set<String> users = new HashSet<String>();
		users.add("Bin");
		users.add("Cef");
		sut.loadUser(users);
	}

}
