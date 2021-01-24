package com.cubershop.context.entity;

public class Price {

    private double value;

    public Price() {
        this.value = 69;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Price)) return false;
        if(this == obj) return false;

        return ((Price) obj).getValue() == this.value;
    }
}
