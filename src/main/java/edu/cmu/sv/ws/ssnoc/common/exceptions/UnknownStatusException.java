package edu.cmu.sv.ws.ssnoc.common.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class UnknownStatusException extends CheckedException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2039132458669629395L;

	/**
	 * Default constructor to raise an unknown user validation exception.
	 */
	public UnknownStatusException(String statusId) {
		super("Unknown Status: " + statusId, null, Response
				.status(Status.NOT_FOUND)
				.entity("Unknown Status: " + statusId)
				.type(MediaType.TEXT_PLAIN_TYPE).build());
	}
}
