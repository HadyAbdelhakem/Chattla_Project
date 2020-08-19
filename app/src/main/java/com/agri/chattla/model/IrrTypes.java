package com.agri.chattla.model;

import java.io.Serializable;

public class IrrTypes implements Serializable {

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

    public IrrTypes() {
    }

    public IrrTypes(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
