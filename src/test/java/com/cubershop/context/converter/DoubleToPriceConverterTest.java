package com.cubershop.context.converter;

import com.cubershop.context.entity.Price;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("All")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DoubleToPriceConverterTest {

    private DoubleToPriceConverter doubleToPriceConverter;
    private Double value;

    @BeforeAll
    public void init() {
        this.doubleToPriceConverter = new DoubleToPriceConverter();
        this.value = new Double(96d);
    }

    @Test
    public void price_convertido_deve_ser_igual_ao_price_esperado() {
        Price price = doubleToPriceConverter.convert(value);

        assertEquals(new Price() {
            {
                setValue(96d);
            }
        }, price);
    }

    @Test
    public void price_convertido_nao_deve_ser_igual_ao_price_esperado() {
        Price price = doubleToPriceConverter.convert(value);

        assertNotEquals(new Price() {
            {
                setValue(155d);
            }
        }, price);
    }
}
