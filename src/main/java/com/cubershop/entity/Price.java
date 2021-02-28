package com.cubershop.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@EqualsAndHashCode
public class Price implements Comparable<Price> {

    private double value;

    public Price() {
        this.value = 69;
    }

    @Override
    public int compareTo(@NonNull Price price) {
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
