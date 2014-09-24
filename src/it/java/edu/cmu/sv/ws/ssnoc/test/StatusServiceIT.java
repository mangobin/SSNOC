package edu.cmu.sv.ws.ssnoc.test;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.runner.RunWith;

import com.eclipsesource.restfuse.Destination;
import com.eclipsesource.restfuse.HttpJUnitRunner;
import com.eclipsesource.restfuse.MediaType;
import com.eclipsesource.restfuse.Method;
import com.eclipsesource.restfuse.Response;
import com.eclipsesource.restfuse.annotation.Context;
import com.eclipsesource.restfuse.annotation.HttpTest;

@RunWith(HttpJUnitRunner.class)
public class StatusServiceIT {
	@Rule
	public Destination destination = new Destination(this,
			"http://localhost:4321/ssnoc");

	@Context
	public Response response;
	
	@HttpTest(method = Method.POST, 
			path = "/status/Cef", 
			type = MediaType.APPLICATION_JSON, 
			content = "{\"updatedAt\" : \"2014-09-23 15:45\","
					+ "\"statusCode\" : 1" 
					+ "}")
	public void testCreateStatus(){
		Assert.assertEquals(201, response.getStatus());
	}
	
	@HttpTest(method = Method.POST, 
			path = "/status/Who", 
			type = MediaType.APPLICATION_JSON, 
			content = "{\"updatedAt\" : \"today\","
					+ "\"statusCode\" : 1" 
					+ "}")
	public void testUnknownUser(){
		Assert.assertEquals(404, response.getStatus());
	}
	
	@HttpTest(method = Method.POST, 
			path = "/status/Cef", 
			type = MediaType.APPLICATION_JSON, 
			content = "{\"updatedAt\" : \"today\","
					+ "\"statusCode\" : 1" 
					+ "}")
	public void testInvalidDate(){
		Assert.assertEquals(400, response.getStatus());
	}
	
	@HttpTest(method = Method.POST, 
			path = "/status/Cef", 
			type = MediaType.APPLICATION_JSON, 
			content = "{\"updatedAt\" : \"2014-09-23 15:45\","
					+ "\"statusCode\" : 4" 
					+ "}")
	public void testInvalidStatusCode(){
		Assert.assertEquals(400, response.getStatus());
	}
	
	@HttpTest(method = Method.GET, 
			path = "/statuscrumbs/Cef", 
			type = MediaType.APPLICATION_JSON)
	public void testGetStatuses(){
		Assert.assertEquals(200, response.getStatus());
	}

}