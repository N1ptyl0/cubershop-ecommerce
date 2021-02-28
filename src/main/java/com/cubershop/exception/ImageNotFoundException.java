package com.cubershop.exception;

import java.util.UUID;

public final class ImageNotFoundException extends Exception {

	public ImageNotFoundException(UUID uuid) {
		super(String.format("Image with uuid '%s' have not been found!", uuid));
	}
}
