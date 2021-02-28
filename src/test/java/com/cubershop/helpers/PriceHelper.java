package com.cubershop.helpers;

import com.cubershop.entity.Price;

public final class PriceHelper {

	private final Price price = new Price();

	public static PriceHelper builder() {
		return new PriceHelper();
	}

	public PriceHelper value(double value) {
		price.setValue(value);
		return this;
	}

	public Price build() {
		return price;
	}
}
