package com.cubershop.context.entity;

public final class Installment implements Comparable<Installment> {

    private Price price;
    private int quote;

    public Installment(final Price price, final int quote) {
        price.setValue(price.getValue() / quote);
        
        this.price = price;
        this.quote = quote;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public int getQuote() {
        return quote;
    }

    public void setQuote(int quote) {
        this.quote = quote;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Installment))
            return false;
        if(this == obj) return true;

        Installment installment = (Installment) obj;
        return this.price.equals(installment.getPrice()) && this.quote == installment.getQuote();
    }

    @Override
    public int compareTo(Installment installment) {
        return (int) ((this.price.getValue() + this.quote)
            - (installment.getPrice().getValue() + installment.getQuote()));
    }

    @Override
    public String toString() {
        return String.format("Até %dx de %s", this.quote, this.price+"");
    }
}
