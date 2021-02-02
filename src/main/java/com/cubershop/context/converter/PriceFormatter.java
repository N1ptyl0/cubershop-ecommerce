package com.cubershop.context.converter;

import com.cubershop.context.entity.Price;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

public final class PriceFormatter implements Formatter<Price> {

    @Override
    public Price parse(String text, Locale locale) throws ParseException {
        Price price = new Price();

        text = text.replaceAll("[^\\d,]", "").trim();
        text = text.replaceAll(",", ".");

        if(text.isEmpty()) {
            price.setValue(0);
            return price;
        }

        price.setValue(Double.parseDouble(text));
        return price;
    }

    @Override
    public String print(Price object, Locale locale) {
        return object+"";
    }
}
