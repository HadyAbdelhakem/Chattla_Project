package com.agri.chattla.model;

import java.io.Serializable;

public class LandTypes implements Serializable {

    String id ;
    String name ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LandTypes() {
    }

    public LandTypes(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
