package com.agri.chattla.model;

import java.io.Serializable;

public class Price implements Serializable {

    String newPrice;
    String lastPrice;

    public Price(String newPrice, String lastPrice) {
        this.newPrice = newPrice;
        this.lastPrice = lastPrice;
    }

    public Price() {
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }
}
