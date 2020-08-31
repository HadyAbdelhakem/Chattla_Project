package com.agri.chattla.model;

public class Discount {

    String value ;
    String addValue ;
    String expertID ;

    public Discount() {
    }

    public Discount(String value, String addValue, String expertID) {
        this.value = value;
        this.addValue = addValue;
        this.expertID = expertID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAddValue() {
        return addValue;
    }

    public void setAddValue(String addValue) {
        this.addValue = addValue;
    }

    public String getExpertID() {
        return expertID;
    }

    public void setExpertID(String expertID) {
        this.expertID = expertID;
    }
}
