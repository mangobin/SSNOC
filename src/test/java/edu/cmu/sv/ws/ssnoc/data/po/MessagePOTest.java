package edu.cmu.sv.ws.ssnoc.data.po;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class MessagePOTest {

	@Test
	public void test() {
		MessagePO message = new MessagePO();
		Date date = new Date();
		message.setAuthor(1);
		message.setContent("Message");
		message.setMessageId(2);
		message.setMessageType("Type");
		message.setPostedAt(date);
		message.setTarget(2);
		
		assertEquals(1, message.getAuthor());
		assertEquals("Message", message.getContent());
		assertEquals(2, message.getMessageId());
		assertEquals("Type", message.getMessageType());
		assertEquals(date, message.getPostedAt());
		assertEquals(2, message.getTarget());
	}

}
