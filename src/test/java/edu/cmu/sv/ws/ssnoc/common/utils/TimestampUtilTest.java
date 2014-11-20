package edu.cmu.sv.ws.ssnoc.common.utils;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimestampUtilTest {

	public static String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm";
	public SimpleDateFormat dateFormat;
	public Date date;
	
	@Before
	public void setUp(){
		dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
		date = new Date();
	}
	
	@After
	public void tearDown(){
		date = null;
	}
	
	@Test
	public void testStringToDate() {
		assertEquals(dateFormat.format(date), TimestampUtil.convert(date));
	}
	
	@Test
	public void testDateToString() throws ParseException{
		assertEquals(dateFormat.parse("2012-12-12 12:12"), TimestampUtil.convert("2012-12-12 12:12"));
	}

	@Test
	public void testInvalidString() {
		assertEquals(null, TimestampUtil.convert("2014-12-12"));
	}
}
