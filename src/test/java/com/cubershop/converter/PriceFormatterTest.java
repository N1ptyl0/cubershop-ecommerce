package com.cubershop.converter;

import com.cubershop.entity.Price;
import com.cubershop.helpers.PriceHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public final class PriceFormatterTest {

    private static PriceFormatter formatter;

    @BeforeAll
    static void init() {
        formatter = new PriceFormatter();
    }

    @Test
    void mustConvertFromStringToPriceCorrectly() throws ParseException {
        // given
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

        Price[] expectedPrices = {
            PriceHelper.builder().value(50.90d).build(),
            PriceHelper.builder().value(50).build(),
            PriceHelper.builder().value(69.8d).build(),
            PriceHelper.builder().value(114.55d).build(),
            PriceHelper.builder().value(35d).build(),
            PriceHelper.builder().value(70.53d).build(),
            PriceHelper.builder().value(95.7d).build(),
            PriceHelper.builder().value(0).build(),
            PriceHelper.builder().value(0).build()
        };

        // when
        List<Price> actualPrices = Stream.of(valuesToConverter).map(value -> {
             try {
                 return formatter.parse(value, Locale.CANADA);
             } catch(ParseException e) {
                 e.printStackTrace();
             }
             return null;
         })
         .collect(Collectors.toList());

        // then
        assertThat(actualPrices).containsExactly(expectedPrices);
    }

    @Test
    void mustConvertFromPriceToStringCorrectly() {
        // given
        Price[] pricesToConverter = {
            PriceHelper.builder().value(50.90d).build(),
            PriceHelper.builder().value(50).build(),
            PriceHelper.builder().value(69.8d).build(),
            PriceHelper.builder().value(114.55d).build(),
            PriceHelper.builder().value(35d).build(),
            PriceHelper.builder().value(70.53d).build(),
            PriceHelper.builder().value(95.7d).build(),
            PriceHelper.builder().value(0).build(),
        };

        String[] expectedValues = {
            "R$ 50,90",
            "R$ 50,00",
            "R$ 69,80",
            "R$ 114,55",
            "R$ 35,00",
            "R$ 70,53",
            "R$ 95,70",
            "R$ 0,00",
        };

        // when
        List<String> actualValues = List.of(pricesToConverter)
            .stream().map(value -> formatter.print(value, Locale.CANADA))
            .collect(Collectors.toList());

        // then
        assertThat(actualValues).containsExactly(expectedValues);
    }
}
