package com.cubershop.context.converter;

import com.cubershop.context.entity.Price;
import org.springframework.core.convert.converter.Converter;

@SuppressWarnings("all")
public final class DoubleToPriceConverter implements Converter<Double, Price> {

    @Override
    public Price convert(Double source) {
        Price price = new Price();
    	price.setValue(source.doubleValue());
    	return price;
    }
}
