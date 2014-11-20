package edu.cmu.sv.ws.ssnoc.data.dao;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DAOFactoryTest {

	DAOFactory sut;
	
	@Before
	public void setUp() throws Exception {
		sut = DAOFactory.getInstance();
	}

	@After
	public void tearDown() throws Exception {
		DAOFactory.fake = false;
		sut = null;
	}
	
	@Test
	public void testFactorySingleton(){
		assertNotNull(sut);
	}

	@Test
	public void testCreateUserDAO() {
		assertNotNull(sut.getUserDAO());
		assertTrue(sut.getUserDAO() instanceof UserDAOImpl);
	}
	
	@Test
	public void testCreateStatusDAO(){
		assertNotNull(sut.getStatusDAO());
		assertTrue(sut.getStatusDAO() instanceof StatusDAOImpl);
	}
	
	@Test
	public void testCreateMessageDAO(){
		assertNotNull(sut.getMessageDAO());
		assertTrue(sut.getMessageDAO() instanceof MessageDAOImpl);
	}
	
	@Test
	public void testCreateMemoryDAO(){
		assertNotNull(sut.getMemoryDAO());
		assertTrue(sut.getMemoryDAO() instanceof MemoryDAOImpl);
	}
	
	@Test
	public void testCreateFakeMessageDAO(){
		DAOFactory.fake = true;
		assertNotNull(sut.getMessageDAO());
		assertTrue(sut.getMessageDAO() instanceof MessageDAOFakeImpl);
	}

}
