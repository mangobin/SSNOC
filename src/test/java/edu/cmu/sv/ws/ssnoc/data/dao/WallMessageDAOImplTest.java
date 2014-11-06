package edu.cmu.sv.ws.ssnoc.data.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;

public class WallMessageDAOImplTest {
	
	@Before
	public void setup() throws Exception {
		DBUtils.setTestMode(true);
		DBUtils.initializeDatabase();
		DBUtils.truncateDatabase();
	}

	
	
	@Test
	public void testFindingMsgByID() {
		Date date= new Date();
		MessagePO input = new MessagePO();
		input.setAuthor(1);
		input.setContent("New Message");
		input.setMessageType("WALL");
		input.setPostedAt(date);
		
		MessageDAOImpl messageDAO = new MessageDAOImpl();
		long ID =  messageDAO.save(input);
		assertNotEquals(0, ID);
		
		MessagePO m = messageDAO.findMessageById(ID);
		assertEquals(1, m.getAuthor());
		assertEquals("New Message", m.getContent());
		assertEquals("WALL", m.getMessageType());
		assertEquals(date, m.getPostedAt());	
	}
	
	@Test
	public void testFindingLatestWallMessages() {
		Date date= new Date();
		MessagePO input = new MessagePO();
		input.setAuthor(1);
		input.setContent("New Message");
		input.setMessageType("WALL");
		input.setPostedAt(date);
		
		MessageDAOImpl messageDAO = new MessageDAOImpl();
		long ID =  messageDAO.save(input);
		assertNotEquals(0, ID);
		List<MessagePO> messages = messageDAO.findLatestWallMessages(1, 0);
		assertEquals(1, messages.get(0).getAuthor());
		assertEquals("New Message", messages.get(0).getContent());
		assertEquals("WALL", messages.get(0).getMessageType());
		assertEquals(date, messages.get(0).getPostedAt());
	}

}
