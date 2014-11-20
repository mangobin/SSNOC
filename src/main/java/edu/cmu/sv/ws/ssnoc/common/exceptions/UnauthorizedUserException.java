package edu.cmu.sv.ws.ssnoc.common.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class UnauthorizedUserException extends CheckedException {
	private static final long serialVersionUID = -6940340725011302810L;
	private static final String unAuthorizedUser = "Unauthorized User: ";

	/**
	 * Default constructor to raise an unauthorized user validation exception.
	 */
	public UnauthorizedUserException(String userName) {
		super(unAuthorizedUser + userName, null, Response
				.status(Status.UNAUTHORIZED)
				.entity(unAuthorizedUser + userName)
				.type(MediaType.TEXT_PLAIN_TYPE).build());
	}
	
	public UnauthorizedUserException(String userName, String reason){
		super(unAuthorizedUser + userName + " (Reason:" + reason + ")", null, Response
				.status(Status.UNAUTHORIZED)
				.entity(unAuthorizedUser + userName + " (Reason:" + reason + ")")
				.type(MediaType.TEXT_PLAIN_TYPE).build());
	}
}
