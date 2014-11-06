package edu.cmu.sv.ws.ssnoc.common.utils;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import edu.cmu.sv.ws.ssnoc.data.po.MessagePO;
import edu.cmu.sv.ws.ssnoc.dto.Message;

public class ConverterUtilsTest {

	@Test
	public void testConvertMessagePoToDto() {
	
		Date date = new Date();
		// What is given?
		MessagePO input = new MessagePO();
		input.setContent("some value");
		input.setPostedAt(date);
		
		// When I do ...
		Message output = ConverterUtils.convert(input);
		
		// Then I expect
		assertEquals("some value", output.getContent());
		
	}
	
	@Test
	public void testConvertMessageDtoToPo() {
		Message input = new Message();
		input.setContent("Message");
		input.setMessageID(1);
		input.setMessageType("ABC");
		input.setPostedAt("2014-01-01 01:01");
		
		
		MessagePO output = ConverterUtils.convert(input);
		
		assertEquals("Message", output.getContent());
	}

}
