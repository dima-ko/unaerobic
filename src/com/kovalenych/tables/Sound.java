package com.kovalenych.tables;

import java.io.Serializable;

public class Sound implements Serializable {

    public String name;
    public String path;

    Sound(String name, String path) {
        this.name = name;
        this.path = path;
    }

}
