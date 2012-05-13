package com.kovalenych.tables;

import java.io.Serializable;

public class Cycle implements Serializable{

    int breathe=0;
    int hold=0;

    Cycle(int breathe, int hold) {
        this.breathe=breathe;
        this.hold=hold;
    }

    public String convertToString() {

            return String.format( "%s     %s",timeToString(breathe),timeToString(hold));
    }

    String timeToString(int time) {
        int min = time / 60;

        return String.format("%d:%02d", min, time - min * 60);
    }

}
