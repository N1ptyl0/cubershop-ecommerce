package com.cubershop.exception;

import org.springframework.http.HttpStatus;

public final class CubeNotFoundException extends HttpException {

	public CubeNotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.name(), message);
	}
}
