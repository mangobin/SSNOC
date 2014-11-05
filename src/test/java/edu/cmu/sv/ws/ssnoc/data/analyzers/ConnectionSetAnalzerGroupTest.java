package edu.cmu.sv.ws.ssnoc.data.analyzers;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConnectionSetAnalzerGroupTest {

	ConnectionSetAnalzer sut;
	static Set<String> users;
	static UserConnections a, b, c, d, e;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		users = new HashSet<String>();
		users.add("A");
		users.add("B");
		users.add("C");
		users.add("D");
		users.add("E");
				
		a = new UserConnections("A");
		a.addConnection("C");
		
		b = new UserConnections("B");
		b.addConnection("D");
		
		c = new UserConnections("C");
		c.addConnection("A");
		c.addConnection("D");
		c.addConnection("E");
		
		d = new UserConnections("D");
		d.addConnection("B");
		d.addConnection("E");
		d.addConnection("C");
		
		e = new UserConnections("E");
		e.addConnection("C");
		e.addConnection("D");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sut = new ConnectionSetAnalzer();
		sut.loadUsers(users);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConnectionsOfA() {
		Set<String> unconnected = sut.processConnection(a);
		assertTrue(unconnected.contains("A"));
		assertTrue(unconnected.contains("B"));
		assertTrue(unconnected.contains("D"));
		assertTrue(unconnected.contains("E"));
	}
	
	@Test
	public void testConnectionsOfB() {
		Set<String> unconnected = sut.processConnection(b);
		assertTrue(unconnected.contains("A"));
		assertTrue(unconnected.contains("C"));
		assertTrue(unconnected.contains("B"));
		assertTrue(unconnected.contains("E"));
	}
	
	@Test
	public void testConnectionsOfC() {
		Set<String> unconnected = sut.processConnection(c);
		assertTrue(unconnected.contains("B"));
	}
	
	@Test
	public void testConnectionsOfD() {
		Set<String> unconnected = sut.processConnection(d);
		assertTrue(unconnected.contains("A"));
	}
	
	@Test
	public void testConnectionsOfE() {
		Set<String> unconnected = sut.processConnection(e);
		assertTrue(unconnected.contains("A"));
		assertTrue(unconnected.contains("B"));
	}

}
