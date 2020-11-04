package com.agri.chattla.model;

public class Discount {

    String value ;
    String addValue ;
    String expertID ;
    String numOfUses ;

    public Discount() {
    }

    public Discount(String value, String addValue, String expertID, String numOfUses) {
        this.value = value;
        this.addValue = addValue;
        this.expertID = expertID;
        this.numOfUses = numOfUses;
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

    public String getNumOfUses() {
        return numOfUses;
    }

    public void setNumOfUses(String numOfUses) {
        this.numOfUses = numOfUses;
    }
}
