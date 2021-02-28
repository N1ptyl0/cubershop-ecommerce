package com.cubershop.exception;

public final class EmptyParameterValueException extends Exception {

	public EmptyParameterValueException (String parameterName) {
		super(String.format("The value of '%s' parameter is empty!", parameterName));
	}
}
