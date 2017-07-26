package controller;

import exceptions.DuplicateElementException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.User;
import model.UserDB;
import model.UserIO;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by REA on 6/4/2017.
 * Verifies the user's registration data and creates a user object if the information passes verification.
 */
public class RegistrationController {
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField ssn;
    @FXML
    private TextField dob;

    // These next 3 make up the genders.
    @FXML
    private CheckBox male;
    @FXML
    private CheckBox female;
    @FXML
    private CheckBox nonbinary;

    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPass;
    @FXML
    private TextField email;
    @FXML
    private TextField username;

    @FXML
    private TextField phoneNumber;
    @FXML
    private Label registrationFailed;
    @FXML
    private ImageView profilePhoto;

    @FXML
    private Label errorInfName;
    @FXML
    private Label errorInlName;
    @FXML
    private Label errorInssn;
    @FXML
    private Label errorIndob;
    @FXML
    private Label errorInGender;
    @FXML
    private Label errorInPhoneNumber;
    @FXML
    private Label errorInEmail;
    @FXML
    private Label errorInUsername;
    @FXML
    private Label errorInPassword;
    @FXML
    private Label errorInConfirmPassword;
    @FXML
    private Label errorInPicture;
    @FXML
    private Label accountCreated;
    @FXML
    private Button backButton;

    private String pathForPicture;


    public void createNewUser(){
        if(registrationIsVerified()){
            User user = new User(firstName.getText(), lastName.getText(), genderSelected(), ssn.getText(),
                    dob.getText(), username.getText(), email.getText(), password.getText(), phoneNumber.getText(), pathForPicture);
            try {
                UserDB.getUsers().add(user);
            }
            catch(DuplicateElementException e){
                System.out.println(e.getMessage());
            }

            //System.out.println("All is well: " + UserDB.getUsers());
            accountCreated.setVisible(true);
            try{
                //System.out.println("Got into the try block.");
                UserIO.writeUsers(UserDB.getUsers());
            }
            catch(IOException e){
                System.err.println("Cannot write userDB to binary file.");
                e.printStackTrace();
            }
        }
        System.out.println(UserDB.getUsers());
    }

    /* I'm sure there are better ways, but this allows me to use backButton's methods to
     *  get the stage I'm on, and that should allow me to close it with the stage.close() method.
     */
    public void closeRegistration(){
        Stage stage = (Stage)backButton.getScene().getWindow();
        stage.close();
    }

    private boolean registrationIsVerified(){

        boolean isVerified = true;
        this.makeErrorsInvisible();

        /* This might be a little hard to read, but the idea is simple.
         *  We start with the assumption that our registration is verified.
         *  After that, we check if any part is !(not) ok.
         *  It only takes one error to change isVerified to false, and
         *  we can avoid having a ton of if...else statements.
         */

        //Verify first name
        if(firstName == null || !(this.isOnlyText(firstName.getText()))){
            errorInfName.setVisible(true);
            isVerified = false;
        }

        //Verify last name
        if(lastName == null || !(this.isOnlyText(lastName.getText()))) {
            errorInlName.setVisible(true);
            isVerified = false;
        }

        //Verify ssn
        if(!ssnIsOkay()){
            errorInssn.setVisible(true);
            isVerified = false;
        }

        //Verify dob
        if(!dobIsOkay()){
            errorIndob.setVisible(true);
            isVerified = false;
        }

        //Verify gender
        if(!oneGenderIsSelected()){
            errorInGender.setVisible(true);
            isVerified = false;
        }

        //Verify phone number
        if(!phoneNumberIsOkay()){
            errorInPhoneNumber.setVisible(true);
            isVerified = false;
        }

        //Verify email
        if(!emailIsOkay()){
            errorInEmail.setVisible(true);
            isVerified = false;
        }

        //Verify username
        if(!usernameIsOkay()){
            errorInUsername.setVisible(true);
            isVerified = false;
        }

        //Verify password
        if(!passwordIsStrong()){
            errorInPassword.setVisible(true);
            isVerified = false;
        }

        //Verify confirm password
        if(!passwordIsConfirmed()){
            errorInConfirmPassword.setVisible(true);
            isVerified = false;
        }

        //Verify image (We can just make sure it's not null)
        if(profilePhoto.getImage() == null){
            errorInPicture.setVisible(true);
            isVerified = false;
        }

        //If we had any errors
        if(!isVerified){
            registrationFailed.setVisible(true);
        }

        return isVerified;

    }

    //This checks if the username is empty, and if it's already taken by another user.
    private boolean usernameIsOkay(){
        if(username == null || username.getText().equals("")){
            return false;
        }

        //If the list is not empty
        if ((!(UserDB.getUsers().isEmpty()))){
            UserDB.getUsers().reset();
            for (int i = 0; i < UserDB.getUsers().size(); i++) {

                if ((username.getText().equals(UserDB.getUsers().getNext().getUsername()))){
                    return false;
                }
            }
        }
        return true;
    }


    private void makeErrorsInvisible(){
        registrationFailed.setVisible(false);
        errorInfName.setVisible(false);
        errorInlName.setVisible(false);
        errorInssn.setVisible(false);
        errorIndob.setVisible(false);
        errorInGender.setVisible(false);
        errorInPhoneNumber.setVisible(false);
        errorInEmail.setVisible(false);
        errorInUsername.setVisible(false);
        errorInPassword.setVisible(false);
        errorInConfirmPassword.setVisible(false);
        errorInPicture.setVisible(false);
    }

    //This is only implemented after everything has been verified to be working.
    private String genderSelected(){
        String gender = "empty";

        if(male.isSelected()){
            gender = "male";
        }

        if(female.isSelected()){
            gender = "female";
        }

        if(nonbinary.isSelected()){
            gender = "non-binary";
        }

        return gender;
    }

    private boolean oneGenderIsSelected(){
        int boxesChecked = 0;
        if(male.isSelected()){
            boxesChecked++;
        }

        if(female.isSelected()){
            boxesChecked++;
        }

        if(nonbinary.isSelected()){
            boxesChecked++;
        }

        return (boxesChecked == 1);
    }

    public void getPhotoPathFromUser(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if(selectedFile != null){
            this.pathForPicture = "file:///" + selectedFile.getAbsolutePath();
            Image photo = new Image(this.pathForPicture);
            profilePhoto.setImage(photo);

            //return "file:///" + selectedFile.getAbsolutePath();
        }
    }

    /*  The pattern I used was grabbed from https://stackoverflow.com/questions/3533408/regex-i-want-this-and-that-and-that-in-any-order
     I also used https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html
     to help me understand what it was doing.
     The symbol "^" (a boundary matcher) ties our expressions to the start of the string we're looking through.
     The regex looks at every expression in parentheses separately.  The "?=" allows us to
     look ahead in our string to see if the pattern following it is a match.  By doing this
     we can get around the issue of regex expressions needing to match the string they're
     being compared with exactly (order of expressions included).  The "." is a wildcard, so
     you can use any character to match it. The "*" is a modifier that looks for the preceding
     expression zero or more times.  The combination of these two into ".*" allows us to iterate
     through the string until we either reach the end, or we find a match for our expression.
     The "$" is a boundary matcher for the end of the line.
     */

    private boolean passwordIsStrong(){
        if(password == null){
            return false;
        }

         return Pattern.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*]).*$", password.getText());
    }

    private boolean passwordIsConfirmed(){
        if(password == null || confirmPass == null){
            return false;
        }

        return password.getText().equals(confirmPass.getText());
    }

    private static boolean isOnlyText(String s){

        return Pattern.matches("[a-zA-Z]+", s);
    }

    private static boolean isOnlyNumbers(String n){
        return Pattern.matches("[0-9]+", n);
    }

    private boolean emailIsOkay(){
        if(email == null){
            return false;
        }

        return Pattern.matches("[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]+", this.email.getText());
    }

    private boolean ssnIsOkay(){
        if(ssn == null){
            return false;
        }

        return Pattern.matches("[0-9]{9}", ssn.getText());
    }

    private boolean phoneNumberIsOkay(){
        if(phoneNumber == null){
            return false;
        }

        return Pattern.matches("[0-9]{10,15}", phoneNumber.getText());
    }

    private boolean dobIsOkay(){
        if(dob == null){
            return false;
        }

        return Pattern.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}", dob.getText());
    }


}
