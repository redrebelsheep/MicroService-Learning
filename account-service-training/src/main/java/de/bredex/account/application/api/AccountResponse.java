package de.bredex.account.application.api;

public final class AccountResponse {

    private final String number;
    private final String firstName;
    private final String lastName;

    public AccountResponse(final String number, final String firstName, final String lastName) {
	this.number = number;
	this.firstName = firstName;
	this.lastName = lastName;
    }
    
    public final String getNumber() {
	return number;
    }
    
    public final String getFirstName() {
	return firstName;
    }
    
    public final String getLastName() {
	return lastName;
    }
}
