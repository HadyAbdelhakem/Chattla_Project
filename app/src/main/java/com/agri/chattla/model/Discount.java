package com.agri.chattla.model;

public class Discount {

    String value ;

    public Discount() {
    }

    public Discount(String discount) {
        this.value = discount;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
