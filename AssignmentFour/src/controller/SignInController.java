package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import model.UserDB;
import view.RestaurantListJavaFXView;
import view.RegistrationJavaFXView;

import java.io.IOException;

/**
 * This class is responsible for user authentication.
 * Users are allowed to sign in if they already have an account.
 * Users may create an account in the event that they don't have an account.
 */
public class SignInController {

    @FXML
    private TextField uName;
    @FXML
    private PasswordField pass;
    @FXML
    private Label signInFailed;
    @FXML
    private Label pleaseRegister;

    public void authenticate() {
        signInFailed.setVisible(false);
        pleaseRegister.setVisible(false);
        boolean didNotFindMatch = true;
        User userInDB;

        //If the list isn't empty, check username and password.
        if (!(UserDB.getUsers().isEmpty())){
            UserDB.getUsers().reset(); // initializing our list

            for (int i = 0; i < UserDB.getUsers().size(); i++) {
                userInDB = UserDB.getUsers().getNext();

                if ((uName.getText().equals(userInDB.getUsername()))
                        && (pass.getText().equals(userInDB.getPassword()))){
                    try {
                        didNotFindMatch = false;
                        System.out.println("Attempting to open RestaurantListJavaFXView()");
                        new view.RestaurantListJavaFXView();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Error in opening RestaurantListJavaFXView()");
                        e.printStackTrace();
                    }
                }
            }
            if(didNotFindMatch){
                signInFailed.setVisible(true);
            }
        }
        //If the list is empty, ask them to register.
        else{
            pleaseRegister.setVisible(true);
        }
    }

    public void openRegistrationJavaFXView() {
        System.out.println("Open Registration View");
        try{
            RegistrationJavaFXView register = new RegistrationJavaFXView();
            register.registrationJavaFXView();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

}
