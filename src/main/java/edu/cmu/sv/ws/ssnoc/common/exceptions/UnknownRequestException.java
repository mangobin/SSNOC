package edu.cmu.sv.ws.ssnoc.common.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class UnknownRequestException extends CheckedException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownRequestException(long requestId) {
		super("Unknown Request: " + requestId, null, Response
				.status(Status.NOT_FOUND)
				.entity("Unknown Request ID: " + requestId)
				.type(MediaType.TEXT_PLAIN_TYPE).build());
	}

}
