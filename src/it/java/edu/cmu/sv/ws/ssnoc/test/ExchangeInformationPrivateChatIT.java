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
import edu.cmu.sv.ws.ssnoc.dto.User;

@RunWith(HttpJUnitRunner.class)
public class ExchangeInformationPrivateChatIT {

	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:1234/ssnoc");
	@Context
	public Response response;

	// setup tests
	// create two users
	@HttpTest(order=1, 
			method=Method.POST, 
			headers={@Header(name="Accept", value="application/json")},
			path="/user/signup", 
			type=MediaType.APPLICATION_JSON, 
			content="{\"userName\":\"user1\",\"password\":\"1234\",\"createdAt\":\"2014-09-24 09:15\"}")
	public void setupUser1(){
		org.junit.Assert.assertTrue("Response should be 200 or 201, but was " + response.getStatus(), response.getStatus() == 200 || response.getStatus() == 201); 
	}
	
	@HttpTest(order=1, 
			method=Method.POST, 
			headers={@Header(name="Accept", value="application/json")},
			path="/user/signup", type=MediaType.APPLICATION_JSON, content="{\"userName\":\"user2\",\"password\":\"1234\",\"createdAt\":\"2014-09-24 09:15\"}")
	public void setupUser2(){
		org.junit.Assert.assertTrue("Response should be 200 or 201, but was " + response.getStatus(), response.getStatus() == 200 || response.getStatus() == 201); 
	}
	
	/*
	 * Test get empty chat messages between two users
	 */
	@HttpTest(order=2, method=Method.GET, 
			headers={@Header(name="Accept", value="application/json")},
			path="/messages/user1/user2")
	public void testGetEmptyChatMessages(){
		assertOk(response);
		TypeToken<List<Message>> token = new TypeToken<List<Message>>() {};
		List<Message> objects = new Gson().fromJson(response.getBody(), token.getType());
		org.junit.Assert.assertEquals(0, objects.size());
	}
	
	/*
	 * Test get empty chat buddies of user1 
	 */
	@HttpTest(order=2, method=Method.GET, 
			headers={@Header(name="Accept", value="application/json")},
			path="/users/user1/chatbuddies")
	public void testEmptyUser1ChatBuddies(){
		assertOk(response);
		TypeToken<List<User>> token = new TypeToken<List<User>>() {};
		List<User> objects = new Gson().fromJson(response.getBody(), token.getType());
		org.junit.Assert.assertEquals(0, objects.size());
	}
	
	/*
	 * Test get empty chat buddies of user2 
	 */
	@HttpTest(order=2, method=Method.GET, 
			headers={@Header(name="Accept", value="application/json")},
			path="/users/user2/chatbuddies")
	public void testEmptyUser2ChatBuddies(){
		assertOk(response);
		TypeToken<List<User>> token = new TypeToken<List<User>>() {};
		List<User> objects = new Gson().fromJson(response.getBody(), token.getType());
		org.junit.Assert.assertEquals(0, objects.size());
	}
	
	/*
	 * Send a message from user1 to user2
	 */
	@HttpTest(order=3, method=Method.POST, 
			headers={@Header(name="Accept", value="application/json")},
			path="/message/user1/user2", 
			type=MediaType.APPLICATION_JSON, 
			content="{\"content\":\"test message from user1 to user2\", \"postedAt\":\"2014-09-24 09:15\"}")
	public void testSendMessageFromUser1ToUser2(){
		Assert.assertCreated(response);
		Message msg = new Gson().fromJson(response.getBody(), Message.class);
		org.junit.Assert.assertEquals("user1", msg.getAuthor());
		org.junit.Assert.assertEquals("user2", msg.getTarget());
		org.junit.Assert.assertEquals("test message from user1 to user2", msg.getContent());
		org.junit.Assert.assertEquals("CHAT", msg.getMessageType());
	}
	
	/*
	 * Test get chat messages between two users
	 */
	@HttpTest(order=4, method=Method.GET, 
			headers={@Header(name="Accept", value="application/json")},
			path="/messages/user1/user2")
	public void testGetChatMessagesBetweenUser1AndUser2(){
		assertOk(response);
		TypeToken<List<Message>> token = new TypeToken<List<Message>>() {};
		List<Message> objects = new Gson().fromJson(response.getBody(), token.getType());
		org.junit.Assert.assertEquals(1, objects.size());
		Message msg = objects.get(0);
		org.junit.Assert.assertEquals("user1", msg.getAuthor());
		org.junit.Assert.assertEquals("user2", msg.getTarget());
		org.junit.Assert.assertEquals("test message from user1 to user2", msg.getContent());
		org.junit.Assert.assertEquals("CHAT", msg.getMessageType());
	}
	
	/*
	 * Test get chat buddies of user1 
	 */
	@HttpTest(order=4, method=Method.GET, 
			headers={@Header(name="Accept", value="application/json")},
			path="/users/user1/chatbuddies")
	public void testUser1ChatBuddies(){
		assertOk(response);
		TypeToken<List<User>> token = new TypeToken<List<User>>() {};
		List<User> objects = new Gson().fromJson(response.getBody(), token.getType());
		org.junit.Assert.assertEquals(1, objects.size());
		User user = objects.get(0);
		org.junit.Assert.assertEquals("user2", user.getUserName());
	}
	
	/*
	 * Test get chat buddies of user2 
	 */
	@HttpTest(order=4, method=Method.GET, 
			headers={@Header(name="Accept", value="application/json")},
			path="/users/user2/chatbuddies")
	public void testUser2ChatBuddies(){
		assertOk(response);
		TypeToken<List<User>> token = new TypeToken<List<User>>() {};
		List<User> objects = new Gson().fromJson(response.getBody(), token.getType());
		org.junit.Assert.assertEquals(1, objects.size());
		User user = objects.get(0);
		org.junit.Assert.assertEquals("user1", user.getUserName());
	}
	
	// Sad cases
	
	/*
	 * Test get chat buddies of Unknown User
	 */
	@HttpTest(order=5, method=Method.GET, 
			headers={@Header(name="Accept", value="application/json")},
			path="/users/unknownUser/chatbuddies")
	public void testUnkownUserChatBuddies(){
		//FIXME
		//Assert.assertNotFound(response);
	}
	
	/*
	 * Test get chat message between known and Unknown User
	 */
	@HttpTest(order=5, method=Method.GET, 
			headers={@Header(name="Accept", value="application/json")},
			path="/messages/user1/unknownUser")
	public void testGetChatMessagesBetweenUser1AndUnknownUser(){
		//FIXME
		//Assert.assertNotFound(response);
	}
	
	/*
	 * Test get chat message between Unknown and Known User
	 */
	@HttpTest(order=5, method=Method.GET, 
			headers={@Header(name="Accept", value="application/json")},
			path="/messages/unknownUser/user2")
	public void testGetChatMessagesBetweenUnknownUserAndUser2(){
		//FIXME
		//Assert.assertNotFound(response);
	}
	
	/*
	 * Test get chat message between two unknown users
	 */
	@HttpTest(order=5, method=Method.GET, 
			headers={@Header(name="Accept", value="application/json")},
			path="/messages/unknownUser1/unknownUser2")
	public void testGetChatMessagesBetweenUnknownUser1AndUnknownUser2(){
		//FIXME
		//Assert.assertNotFound(response);
	}
	
	/*
	 * Test sending a message to an unknown user
	 */
	@HttpTest(order=5, method=Method.POST, 
			headers={@Header(name="Accept", value="application/json")},
			path="/message/user1/unknownUser", 
			type=MediaType.APPLICATION_JSON, 
			content="{\"content\":\"test message from user1 to unknown user\", \"postedAt\":\"2014-09-24 09:15\"}")
	public void testSendMessageFromUser1ToUnknownUser(){
		//FIXME
		//Assert.assertNotFound(response);
	}
	
	/*
	 * Test sending a message from an unknown user
	 */
	@HttpTest(order=5, method=Method.POST, 
			headers={@Header(name="Accept", value="application/json")},
			path="/message/unknownUser/user2", 
			type=MediaType.APPLICATION_JSON, 
			content="{\"content\":\"test message from unknown user to user2\", \"postedAt\":\"2014-09-24 09:15\"}")
	public void testSendMessageFromUnknownUserToUser2(){
		//FIXME
		//Assert.assertNotFound(response);
	}
	
	/*
	 * Test sending a message from an unknown user to an unknown user
	 */
	@HttpTest(order=5, method=Method.POST, 
			headers={@Header(name="Accept", value="application/json")},
			path="/message/unknownUser1/unknownUser2", 
			type=MediaType.APPLICATION_JSON, 
			content="{\"content\":\"test message from unknown user 1 to unknown user 2\", \"postedAt\":\"2014-09-24 09:15\"}")
	public void testSendMessageFromUnknownUser1ToUnknownUser2(){
		//FIXME
		//Assert.assertNotFound(response);
	}
}
