package com.cubershop.exception;

public final class NotAcceptableOrderException extends Exception {

	public NotAcceptableOrderException (String expression) {
		super(String.format("It wasn't possible parse: %s", expression));
	}

	public NotAcceptableOrderException () {
		super("It wasn't possible parse");
	}
}
