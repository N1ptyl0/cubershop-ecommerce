package com.cubershop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public abstract class HttpException extends Exception {

	private final HttpStatus statusCode;
	private final String statusName, message;
}
