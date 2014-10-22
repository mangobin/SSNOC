package edu.cmu.sv.ws.ssnoc.data.dao;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.common.utils.TimestampUtil;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.Message;

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
		Message dto = new Message();
		dto.setAuthor("testAuthor");
		dto.setContent("testContent");
		dto.setMessageType("WALL");
		dto.setPostedAt(TimestampUtil.convert(new Date()));
		dto.setTarget("randomTarget");
		
		MessagePO po = ConverterUtils.convert(dto);
		long messageId = sut.save(po);
		
		MessagePO insertedPO = sut.findMessageById(messageId);
		
		assertEquals(po, insertedPO);
	}
	
	@Test
	public void testShouldUpdateMessageInDatabase(){
		Message dto = new Message();
		dto.setAuthor("testAuthor");
		dto.setContent("testContent");
		dto.setMessageType("WALL");
		dto.setPostedAt(TimestampUtil.convert(new Date()));
		dto.setTarget("randomTarget");
		
		MessagePO po = ConverterUtils.convert(dto);
		long messageId = sut.save(po);
		
		po.setMessageId(messageId);
		po.setAuthor(231312);
		po.setContent("someOtherContent");
		
		sut.save(po);
		
		MessagePO updatedPO = sut.findMessageById(messageId);
		
		assertEquals(po, updatedPO);
	}

}
