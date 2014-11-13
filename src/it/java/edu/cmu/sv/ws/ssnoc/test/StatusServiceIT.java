package edu.cmu.sv.ws.ssnoc.test;


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

import edu.cmu.sv.ws.ssnoc.dto.Status;

@RunWith(HttpJUnitRunner.class)
public class StatusServiceIT {
	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:1234/ssnoc");

	@Context
	public Response response;
	
	// setup user
	@HttpTest(order=1, method=Method.POST, 
			headers={@Header(name="Accept", value="application/json")},
			path="/user/signup", 
			type=MediaType.APPLICATION_JSON, 
			content="{\"userName\":\"testStatusUser\",\"password\":\"1234\",\"createdAt\":\"2014-09-24 09:15\"}")
	public void setupUser1(){
		org.junit.Assert.assertTrue("Response should be 200 or 201, but was " + response.getStatus(), 
				response.getStatus() == 200 || response.getStatus() == 201); 
	}
	
	// get user status crumbs = 0
	@HttpTest(order=2,
			method=Method.GET,
			path="/statuscrumbs/testStatusUser",
			headers={@Header(name="Accept", value="application/json")})
	public void getEmptyStatusCrumbs(){
		Assert.assertOk(response);
		TypeToken<List<Status>> token = new TypeToken<List<Status>>() {};
		List<Status> objects = new Gson().fromJson(response.getBody(), token.getType());
		org.junit.Assert.assertEquals(0, objects.size());
	}
	
	// post status
	@HttpTest(order=3,
			method=Method.POST,
			path="/status/testStatusUser",
			headers={@Header(name="Accept", value="application/json")},
			content="{\"statusCode\":\"RED\", \"updatedAt\":\"2014-09-24 09:15\"}",
			type=MediaType.APPLICATION_JSON
			)
	public void testChangeStatus(){
		Assert.assertCreated(response);
	}
	
	// get status crumbs
	@HttpTest(order=4,
			method=Method.GET,
			path="/statuscrumbs/testStatusUser",
			headers={@Header(name="Accept", value="application/json")})
	public void getStatusCrumbs(){
		Assert.assertOk(response);
		TypeToken<List<Status>> token = new TypeToken<List<Status>>() {};
		List<Status> objects = new Gson().fromJson(response.getBody(), token.getType());
		org.junit.Assert.assertEquals(1, objects.size());
		Status status = objects.get(0);
		org.junit.Assert.assertEquals("RED", status.getStatusCode());
		org.junit.Assert.assertEquals("testStatusUser", status.getUserName());
	}

}