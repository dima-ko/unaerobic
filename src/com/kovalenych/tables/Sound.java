package com.kovalenych.tables;

import java.io.Serializable;

public class Sound implements Serializable {

    private int index;
    public String name;
    public String fileName;

    Sound(int toStart2Min, String name, String path) {
        index = toStart2Min;
        this.name = name;
        this.fileName = path;
    }

}
