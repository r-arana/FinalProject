package model;

import java.io.Serializable;
import java.nio.file.Path;

/**
 * Created by REA on 6/4/2017.
 * Provides us with an object containing all relevant user data.
 */

public class User extends Person implements Comparable<User>, Serializable{

    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String profilePicturePath;


    public User(String newFirstName, String newLastName, String newGender, String ssn, String dob, String username, String email, String password, String phoneNumber, String profilePicturePath) {
        super(newFirstName, newLastName, newGender, ssn, dob);
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profilePicturePath = profilePicturePath;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicture(Path profilePicture) {
        this.profilePicturePath = profilePicturePath;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    @Override
    public String toString(){
        return ("\n" + "Name: " + super.getFirstName() + " " + super.getLastName() + "\n" +
                "SSN: " + super.getSocialSecurityNumber() + "\n" +
                "DOB: " + super.getDateOfBirth() + "\n" +
                "Gender: " + super.getGender() + "\n" +
                "Phone Number: " + this.getPhoneNumber() + "\n" +
                "Email: " + this.getEmail() + "\n" +
                "Username: " + this.getUsername() + "\n" +
                "Password: " + this.getPassword() + "\n" +
                "Picture path: " + this.getProfilePicturePath()+ "\n\n");

    }

    public boolean equals(User newUser){
        if (!(this.getUsername().equals(newUser.getUsername()))){
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(User newUser){
        return this.getUsername().toUpperCase().compareTo(newUser.getUsername().toUpperCase());
    }
}
