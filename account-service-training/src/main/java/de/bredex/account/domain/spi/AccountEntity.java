package de.bredex.account.domain.spi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
public final class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String number;
    private String firstName;
    private String lastName;
    
    public AccountEntity() {}
    
    public AccountEntity(final String number, final String firstName, final String lastName) {
	this.number = number;
	this.firstName = firstName;
	this.lastName = lastName;
    }

    public Integer getId() {
	return id;
    }
    
    public void setId(final Integer id) {
	this.id = id;
    }
    
    public String getNumber() {
	return number;
    }
    
    public void setNumber(final String number) {
	this.number = number;
    }
    
    public String getFirstName() {
	return firstName;
    }
    
    public void setFirstName(final String firstName) {
	this.firstName = firstName;
    }
    
    public String getLastName() {
	return lastName;
    }
    
    public void setLastName(final String lastName) {
	this.lastName = lastName;
    }
}
