package model;

import java.io.Serializable;

/**
 * Created by REA on 6/4/2017.
 * Parent of our user classed used for our user data file.
 */
public class Person implements Serializable {

    private String firstName;
    private String lastName;
    private String gender;

    private String socialSecurityNumber;
    private String dateOfBirth;

    public Person(){
        this.firstName = "First name is wrong";
        this.lastName = "Last name is wrong.";
        this.gender = "Gender was not selected";
        this.socialSecurityNumber = "empty";
        this.dateOfBirth = "empty";
    }

    public Person(String newFirstName, String newLastName, String newGender, String ssn, String dob){
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.gender = newGender;
        this.socialSecurityNumber = ssn;
        this.dateOfBirth = dob;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public String getGender(){
        return gender;
    }

    public String getSocialSecurityNumber(){
        return socialSecurityNumber;
    }

    public String getDateOfBirth(){
        return dateOfBirth;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
