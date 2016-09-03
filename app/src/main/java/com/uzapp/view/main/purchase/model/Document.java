package com.uzapp.view.main.purchase.model;

/**
 * Created by Vladimir on 01.09.2016.
 */
public class Document {

    private int number;
    private int countPlace;
    private String firstName;
    private String lastName;
    private String middleName;
    private int timestamp;
    private String passport;

    public Document() {}

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCountPlace() {
        return countPlace;
    }

    public void setCountPlace(int countPlace) {
        this.countPlace = countPlace;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    @Override
    public String toString() {
        return "[{" +
                '\"' + "number" + '\"' + ':' + number +
                ',' + '\"' + "count_place" + '\"' + ':' + countPlace +
                ',' + '\"' + "firstname" + '\"' + ':' + '\"' + firstName + '\"' +
                ',' + '\"' + "lastname" + '\"' + ':' + '\"' + lastName + '\"' +
                ',' + '\"' + "middlename" + '\"' + ':' + '\"' + middleName + '\"' +
                ',' + '\"' + "timestamp" + '\"' + ':' + timestamp +
                ',' + '\"' + "passport" + '\"' + ':' + '\"' + passport + '\"' +
                '}' + ']';
    }
}
