package edu.cmu.sv.ws.ssnoc.dto;

import static org.junit.Assert.*;

import org.junit.Test;

public class MessageTest {

	@Test
	public void test() {
		Message message = new Message();
		message.setAuthor("Author");
		message.setContent("Message");
		message.setMessageID(1);
		message.setMessageType("Type");
		message.setPostedAt("2014-01-01 01:01");
		message.setTarget("Target");
		
		assertEquals("Author", message.getAuthor());
		assertEquals("Message", message.getContent());
		assertEquals(1, message.getMessageID());
		assertEquals("Type", message.getMessageType());
		assertEquals("2014-01-01 01:01", message.getPostedAt());
		assertEquals("Target", message.getTarget());
	}

}
