package com.kovalenych.stats;

/**
 * this class was made
 * by insomniac and angryded
 * for their purposes
 */
public class Session {
    public Session(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long id;
    public long start;
    public long end;
    public String comment;
}
