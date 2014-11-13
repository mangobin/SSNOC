package edu.cmu.sv.ws.ssnoc.data.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;

public class PublicAnnouncementMessageDAOImplTest {

	MessageDAOImpl sut;
	
	@Before
	public void setup() throws Exception {
		DBUtils.setTestMode(true);
		DBUtils.initializeDatabase();
		DBUtils.truncateDatabase();
		
		sut = new MessageDAOImpl();
	}

	@Test
	public void testGetEmptyAnnouncements() {
		List<MessagePO> announcements = sut.findAllAnnouncement(100, 0);
		assertEquals(0, announcements.size());
	}
	
	@Test
	public void testSaveANewAnnouncement(){
		Date date = new Date();
		MessagePO announcement = new MessagePO();
		announcement.setMessageType(SQL.MESSAGE_TYPE_ANNOUNCEMENT);
		announcement.setAuthor(1);
		announcement.setContent("Test Announcement");
		announcement.setPostedAt(date);
		long messageID = sut.save(announcement);
		assertNotEquals(-1, messageID);
		
		// query announcement back from database
		// this method is tested in another class
		MessagePO result = sut.findMessageById(messageID);
		assertNotNull(result);
		assertEquals(messageID, result.getMessageId());
		assertEquals(announcement.getAuthor(), result.getAuthor());
		assertEquals(announcement.getPostedAt(), result.getPostedAt());
		assertEquals(announcement.getContent(), result.getContent());
		assertEquals(announcement.getMessageType(), result.getMessageType());
	}
	
	@Test
	public void testGetAllAnnouncements(){
		Date date = new Date();
		MessagePO announcement = new MessagePO();
		announcement.setMessageType(SQL.MESSAGE_TYPE_ANNOUNCEMENT);
		announcement.setAuthor(1);
		announcement.setContent("Test Announcement");
		announcement.setPostedAt(date);
		long messageID = sut.save(announcement);
		assertNotEquals(-1, messageID);
		
		List<MessagePO> announcements = sut.findAllAnnouncement(100, 0);
		assertNotNull(announcements);
		assertEquals(1, announcements.size());
		assertEquals(messageID, announcements.get(0).getMessageId());
		assertEquals(announcement.getAuthor(), announcements.get(0).getAuthor());
		assertEquals(announcement.getPostedAt(), announcements.get(0).getPostedAt());
		assertEquals(announcement.getContent(), announcements.get(0).getContent());
		assertEquals(announcement.getMessageType(), announcements.get(0).getMessageType());
	}

}
