package de.bredex.lending.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AccountResponse {

    private  String number;
    private  String firstName;
    private  String lastName;

    public AccountResponse(final String number, final String firstName, final String lastName) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public AccountResponse() {
    }

    public final String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
}
