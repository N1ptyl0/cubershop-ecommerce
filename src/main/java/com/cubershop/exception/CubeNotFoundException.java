package com.cubershop.exception;

import java.util.UUID;

public final class CubeNotFoundException extends Exception {

	public CubeNotFoundException() {
		super("Cube can not been found");
	}

	public CubeNotFoundException(String cubeName) {
		super(String.format("Cube with name %s can not been found", cubeName));
	}

	public CubeNotFoundException(UUID cubeID) {
		super(String.format("Cube with id %s can not been found", cubeID));
	}
}
