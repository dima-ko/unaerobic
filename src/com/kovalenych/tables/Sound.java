package com.kovalenych.tables;

import java.io.Serializable;

public class Sound implements Serializable {

    public int index;
    public String label;
    public String fileName;

    Sound(int toStart2Min, String name, String path) {
        index = toStart2Min;
        this.label = name;
        this.fileName = path;
    }

}
