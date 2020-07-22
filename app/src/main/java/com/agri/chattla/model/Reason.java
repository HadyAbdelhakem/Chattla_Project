package com.agri.chattla.model;

import java.io.Serializable;

public class Reason implements Serializable {

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

    public Reason() {
    }

    public Reason(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
