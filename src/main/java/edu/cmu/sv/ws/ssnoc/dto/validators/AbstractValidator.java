package edu.cmu.sv.ws.ssnoc.dto.validators;

public abstract class AbstractValidator<T> {

	public abstract boolean validate(T entity);
	
}
