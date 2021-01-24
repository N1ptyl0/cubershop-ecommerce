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
        String[] slices = (object.getValue()+"").split("\\.");
        String leftPart = slices[0], rightPart = slices[1];

        if(rightPart.length() == 1) rightPart += "0";
        else if(rightPart.length() > 2)
            rightPart = rightPart.charAt(0)+""+rightPart.charAt(1);

        return String.format("R$ %s,%s", leftPart, rightPart);
    }
}
