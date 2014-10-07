package edu.cmu.sv.ws.ssnoc.common.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class UnknownMessageException extends CheckedException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8471775569312722253L;

	public UnknownMessageException(long messageId) {
		super("Unknown Message: " + messageId, null, Response
				.status(Status.NOT_FOUND)
				.entity("Unknown Message ID: " + messageId)
				.type(MediaType.TEXT_PLAIN_TYPE).build());
	}
}
