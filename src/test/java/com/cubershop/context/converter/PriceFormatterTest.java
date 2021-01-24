package com.cubershop.context.converter;

import static org.junit.jupiter.api.Assertions.*;

import com.cubershop.context.entity.Price;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Locale;

public final class PriceFormatterTest {

    private static PriceFormatter formatter;

    @BeforeAll
    public static void init() {
        formatter = new PriceFormatter();
    }

    @Test
    public void deve_converter_de_string_para_price_corretamente() throws ParseException {
        String[] valuesToConverter = {
            "R$ 50,90",
            "R$ 50",
            "R$ 69,8",
            "R$ 114,55",
            "35",
            "70,53",
            "95,7",
            "java",
            "R$0",
        };

        double[] expectedValues = {
            50.90d,
            50d,
            69.8d,
            114.55d,
            35d,
            70.53d,
            95.7d,
            0d,
            0d
        };

        for(int i = 0; i < valuesToConverter.length; i++) {
            Price expectedPrice = new Price();
            expectedPrice.setValue(expectedValues[i]);
            assertEquals(expectedPrice, formatter.parse(valuesToConverter[i], Locale.CANADA));
        }
    }

    @Test
    public void deve_converter_price_para_string_corretamente() {
        Price price = new Price();
        price.setValue(53.40d);

        assertEquals("R$ 53,40", formatter.print(price, Locale.CANADA));
    }
}
