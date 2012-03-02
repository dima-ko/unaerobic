package com.kovalenych;


public class Record {

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getResult() {
        return result;
    }


    private final String name;
    private final String country;
    private final String result;


    public Record(String name, String result, String country) {

        this.name = name;
        this.result = result;
        this.country = country;

    }


}
