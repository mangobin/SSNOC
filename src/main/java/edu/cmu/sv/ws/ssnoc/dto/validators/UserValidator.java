package edu.cmu.sv.ws.ssnoc.dto.validators;

import edu.cmu.sv.ws.ssnoc.common.utils.ReservedUserNamesUtils;
import edu.cmu.sv.ws.ssnoc.dto.User;

public class UserValidator extends AbstractValidator<User> {

	@Override
	public boolean validate(User entity) {
		// check if provided user name is part of reserved names
		if(ReservedUserNamesUtils.INVALID_NAMES.contains(entity.getUserName())){
			return false;
		}
		return true;
	}

}
