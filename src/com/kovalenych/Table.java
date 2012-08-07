package com.kovalenych;

import com.kovalenych.tables.Cycle;

import java.io.Serializable;
import java.util.ArrayList;

public class Table implements Serializable {    //DO NOT CHANGE PACKAGE!!!  MUST BE com.kovalenych

    ArrayList<Cycle> cycles;
    ArrayList<Integer> voices;

    public Table() {
        cycles = new ArrayList<Cycle>();
        voices = new ArrayList<Integer>();
    }

    public Table(ArrayList<Cycle> curTable) {
        this.cycles = curTable;
        voices = new ArrayList<Integer>();
    }

    public ArrayList<Cycle> getCycles() {
        return cycles;
    }

    public void setCycles(ArrayList<Cycle> cycles) {
        this.cycles = cycles;
    }

    public ArrayList<Integer> getVoices() {
        return voices;
    }

    public void setVoices(ArrayList<Integer> voices) {
        this.voices = voices;
    }
}
