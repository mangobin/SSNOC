package edu.cmu.sv.ws.ssnoc.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.eclipsesource.restfuse.Assert;
import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.MediaType;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.Header;
import com.eclipsesource.restfuse.annotation.HttpTest;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.TimestampUtil;

@RunWith(HttpJUnitRunner.class)
public class ExchangeInformationTest {
	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:8080/ssnoc/");
	@Context
	public Response response;
	String temp;

	@Before
	public void setUp() throws Exception {
		
	    Date date = new Date();
	    temp = TimestampUtil.convert(date);
	}

	@After
	public void tearDown() throws Exception {
	}

	
   //two users in local databse. BIN, justForTest
	
	@HttpTest(method = Method.GET, headers = {@Header(name = "Accept", value = "application/json")},
			path = "/messages/wall",  content = "") 
	public void testCanGetPublicWallMessage() {
		Assert.assertOk(response);
		String messg = response.getBody();
		Log.debug(messg);

	}
	
	@HttpTest(method = Method.POST, headers = {@Header(name = "Accept", value = "application/json")},
			path = "/message/justForTest",  type = MediaType.APPLICATION_JSON,
			content = "{\"content\":\"I post a message on public wall second!\",\"postedAt\":\"2014-10-14 12:15\"}") 
	public void testCanPostPublicWallMessage() {
		Assert.assertCreated(response);
		String messg = response.getBody();
		Log.debug(messg);

	}
	
	@HttpTest(method = Method.POST, headers = {@Header(name = "Accept", value = "application/json")},
			path = "/message/justForTest/BIN",  type = MediaType.APPLICATION_JSON,
			content = "{\"content\":\"justForTest send a message to BIN!\",\"postedAt\":\"temp\"}") 
	public void testCanSendPrivateChatMessageToAnotherUser() {
		Assert.assertCreated(response);
		String messg = response.getBody();
		Log.debug(messg);

	}
	
	@HttpTest(method = Method.GET, headers = {@Header(name = "Accept", value = "application/json")},
			path = "/message/33",  content = "" ) 
	public void testCanGetMessageById() {
		Assert.assertOk(response);
		String messg = response.getBody();
		Log.debug(messg);

	}
	
	@HttpTest(method = Method.GET, headers = {@Header(name = "Accept", value = "application/json")},
			path = "/messages/BIN/justForTest",  content = "") 
	public void testCanGetAllMessagesBetweenTwoUsers() {
		Assert.assertOk(response);
		String messg = response.getBody();
		Log.debug(messg);

	}

	@HttpTest(method = Method.GET, headers = {@Header(name = "Accept", value = "application/json")},
			path = "/users/BIN/chatbuddies",  content = "") 
	public void testCanGetAllUsersWithWhomTheUserChattedWith() {
		Assert.assertOk(response);
		String messg = response.getBody();
		Log.debug(messg);

	}

}
