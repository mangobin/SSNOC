package edu.cmu.sv.ws.ssnoc.dto.validators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.cmu.sv.ws.ssnoc.dto.Status;

public class StatusValidator extends AbstractValidator<Status> {
	
	private static String[] statusCodes = {"GREEN","YELLOW","RED"};
	private static Set<String> acceptableStatusCodes = new HashSet<String>(Arrays.asList(statusCodes));
	
	@Override
	public boolean validate(Status entity) {
		return entity.getUpdatedAtDate() != null && 
				acceptableStatusCodes.contains(entity.getStatusCode());
	}

}
