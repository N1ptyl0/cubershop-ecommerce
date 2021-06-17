package com.cubershop.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public final class Installment {

    private double totalPrice;
    private int quantity;
    private double quote;

    public Installment(final double totalPrice, final int quantity) {
        this.totalPrice = totalPrice;
        this.quantity = quantity;
        this.quote = totalPrice / quantity;
    }

    @Override
    public String toString() {
        return String.format("De R$ %s por %dx de %s", this.totalPrice+"", this.quantity, this.quote);
    }
}
