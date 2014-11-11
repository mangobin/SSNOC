package edu.cmu.sv.ws.ssnoc.test;


import static com.eclipsesource.restfuse.Assert.assertOk;

import java.util.List;

import org.junit.Rule;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.cmu.sv.ws.ssnoc.dto.Message;

@RunWith(HttpJUnitRunner.class)
public class ExchangeInformationPublicWallIT {
	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:1234/ssnoc");
	@Context
	public Response response;

	// setup tests
	// create two users
	@HttpTest(order=1, method=Method.POST, headers={@Header(name="Accept", value="application/json")},
			path="/user/signup", type=MediaType.APPLICATION_JSON, content="{\"userName\":\"user1\",\"password\":\"1234\",\"createdAt\":\"2014-09-24 09:15\"}")
	public void setupUser1(){
		org.junit.Assert.assertTrue("Response should be 200 or 201, but was " + response.getStatus(), response.getStatus() == 200 || response.getStatus() == 201); 
	}
	
	@HttpTest(order=1, method=Method.POST, headers={@Header(name="Accept", value="application/json")},
			path="/user/signup", type=MediaType.APPLICATION_JSON, content="{\"userName\":\"user2\",\"password\":\"1234\",\"createdAt\":\"2014-09-24 09:15\"}")
	public void setupUser2(){
		org.junit.Assert.assertTrue("Response should be 200 or 201, but was " + response.getStatus(), response.getStatus() == 200 || response.getStatus() == 201); 
	}
	
	@HttpTest(order=2, method=Method.GET, headers={@Header(name="Accept", value="application/json")},
			path="/messages/wall")
	public void testGetEmptyWall(){
		assertOk(response);
		TypeToken<List<Message>> token = new TypeToken<List<Message>>() {};
		List<Message> objects = new Gson().fromJson(response.getBody(), token.getType());
		org.junit.Assert.assertEquals(0, objects.size());
	} 
	
	/*
	 * Test posting a public wall message
	 */
	@HttpTest(order=3, method=Method.POST, headers={@Header(name="Accept", value="application/json")},
			path="/message/user1",  type=MediaType.APPLICATION_JSON, content="{\"content\":\"test wall message\", \"postedAt\":\"2014-09-24 09:15\"}")
	public void testCanPostPublicWallMessage(){
		Assert.assertCreated(response);
		Message msg = new Gson().fromJson(response.getBody(), Message.class);
		org.junit.Assert.assertEquals("test wall message", msg.getContent());
		org.junit.Assert.assertEquals("user1", msg.getAuthor());
	}
	
	/*
	 * Test get public wall
	 */
	@HttpTest(order=4, method=Method.GET, headers={@Header(name="Accept", value="application/json")},
			path="/messages/wall")
	public void testCanGetPublicWallMessages(){
		assertOk(response);
		TypeToken<List<Message>> token = new TypeToken<List<Message>>() {};
		List<Message> objects = new Gson().fromJson(response.getBody(), token.getType());
		org.junit.Assert.assertEquals(1, objects.size());
		Message msg = objects.get(0);
		org.junit.Assert.assertEquals("test wall message", msg.getContent());
		org.junit.Assert.assertEquals("user1", msg.getAuthor());
	}
}
