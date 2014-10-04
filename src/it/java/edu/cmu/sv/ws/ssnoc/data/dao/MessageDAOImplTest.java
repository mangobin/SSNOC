package edu.cmu.sv.ws.ssnoc.data.dao;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;

public class MessageDAOImplTest {
	
	MessageDAOImpl sut = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DBUtils.initializeDatabase();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sut = new MessageDAOImpl();
	}

	@After
	public void tearDown() throws Exception {
		sut = null;
	}

	@Test
	public void testNonExistentMessageId() {
		// when I search for a message by a random ID,
		MessagePO po = sut.findMessageById(145241);
		// should return null
		assertNull(po);
	}
	
	@Test
	public void testShouldSaveMessageInDatabase(){
		MessagePO po = new MessagePO();
		po.setAuthor("testAuthor");
		po.setContent("testContent");
		po.setMessageType("WALL");
		po.setPostedAt(new Date());
		po.setTarget("randomTarget");
		
		long messageId = sut.save(po);
		
		MessagePO insertedPO = sut.findMessageById(messageId);
		
		assertEquals(po, insertedPO);
	}
	
	@Test
	public void testShouldUpdateMessageInDatabase(){
		MessagePO po = new MessagePO();
		po.setAuthor("testAuthor");
		po.setContent("testContent");
		po.setMessageType("WALL");
		po.setPostedAt(new Date());
		po.setTarget("randomTarget");
		
		long messageId = sut.save(po);
		
		po.setMessageId(messageId);
		po.setAuthor("anotherAuthor");
		po.setContent("someOtherContent");
		
		sut.save(po);
		
		MessagePO updatedPO = sut.findMessageById(messageId);
		
		assertEquals(po, updatedPO);
	}

}
