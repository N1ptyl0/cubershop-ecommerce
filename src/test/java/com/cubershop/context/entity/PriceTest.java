package com.cubershop.context.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PriceTest {

    private Price price1, price2;

    @BeforeEach
    public void init() {
        price1 = new Price();
        price2 = new Price();
    }

    @Test
    public void two_Prices_should_be_the_same() {
        price1.setValue(132.3D);
        price2.setValue(132.3D);

        assertEquals(price1, price2);
    }

    @Test
    public void two_Prices_should_not_be_the_same() {
        price1.setValue(132.3D);
        price2.setValue(122.3D);

        assertNotEquals(price1, price2);
    }

    @Test
    public void price1_should_be_less_than_price2() {
        price1.setValue(57.9D);
        price2.setValue(68.9D);

        Assertions.assertThat(price1).isLessThan(price2);
    }

    @Test
    public void price1_should_be_greater_than_price2() {
        price1.setValue(79.54D);
        price2.setValue(68.9D);

        Assertions.assertThat(price1).isGreaterThan(price2);
    }
}
