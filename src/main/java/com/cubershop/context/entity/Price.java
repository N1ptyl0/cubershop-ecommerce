package com.cubershop.context.entity;

public class Price implements Comparable<Price> {

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

    @Override
    public int compareTo(Price price) {
        if(this.equals(price)) return 0;
        return (int)(this.value - price.getValue());
    }

    @Override
    public String toString() {
        String[] slices = (this.value+"").split("\\.");
        String leftPart = slices[0], rightPart = slices[1];

        if(rightPart.length() == 1) rightPart += "0";
        else if(rightPart.length() > 2)
            rightPart = rightPart.charAt(0)+""+rightPart.charAt(1);

        return String.format("R$ %s,%s", leftPart, rightPart);
    }
}
