package com.cubershop.exception;

public final class ListOfCubesNotFoundException extends Exception {

	public ListOfCubesNotFoundException() {
		super("List of cubes have not been found!");
	}

	public ListOfCubesNotFoundException(String category) {
		super(String.format("List of cubes from %s category have not been found!", category));
	}

	public ListOfCubesNotFoundException(String category, String order) {
		super(String.format("List of cubes from %s category with order %s have not been found!", category, order));
	}
}
