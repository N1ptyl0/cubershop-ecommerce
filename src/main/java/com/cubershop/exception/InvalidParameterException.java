package com.cubershop.exception;

public final class InvalidParameterException extends Exception {

	public InvalidParameterException (String parameterName) {
		super(String.format("The value of '%s' parameter have invalid format!", parameterName));
	}
}
