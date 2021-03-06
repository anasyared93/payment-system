package com.progressoft.jip.handlers.validators;

import com.progressoft.jip.handlers.exceptions.ValidationException;

@FunctionalInterface
public interface Validator<T, E extends ValidationException> {

	void validate(T t) throws E;

}
