package com.kovalenych;


public class Record {

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCountry() {
        return country;
    }

    public String getResult() {
        return result;
    }

    public String getDate() {
        return date;
    }

    private final String name;
    private final String surname;
    private final String country;
    private final String result;
    private final String date;


    public Record(String name, String surname, String result, String country, String date) {

        this.name = name;
        this.surname = surname;
        this.result = result;
        this.country = country;
        this.date = date;

    }


}
