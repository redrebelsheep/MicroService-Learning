package de.bredex.account.application.api;

public final class AccountRequest {

    private final String firstName;
    private final String lastName;

    public AccountRequest(final String firstName, final String lastName) {
	this.firstName = firstName;
	this.lastName = lastName;
    }
    
    public final String getFirstName() {
	return firstName;
    }
    
    public final String getLastName() {
	return lastName;
    }
}
