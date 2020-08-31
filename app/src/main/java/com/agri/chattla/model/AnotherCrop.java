package com.agri.chattla.model;

import java.io.Serializable;

public class AnotherCrop implements Serializable {

    private String anothercropName ;
    private String anothercropItemName ;
    private String numOfAcre ;

    public AnotherCrop() {
    }

    public String getAnothercropName() {
        return anothercropName;
    }

    public void setAnothercropName(String anothercropName) {
        this.anothercropName = anothercropName;
    }

    public String getAnothercropItemName() {
        return anothercropItemName;
    }

    public void setAnothercropItemName(String anothercropItemName) {
        this.anothercropItemName = anothercropItemName;
    }

    public String getNumOfAcre() {
        return numOfAcre;
    }

    public void setNumOfAcre(String numOfAcre) {
        this.numOfAcre = numOfAcre;
    }
}
