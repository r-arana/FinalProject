package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import model.UserDB;
import model.UserIO;
import structures.SortedList;

import java.io.IOException;

/**
 *  Starts our program, and loads our user database.
 */

public class SignInJavaFXView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        loadUserDB();
        Parent root = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
        primaryStage.setTitle("Sign In");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();
    }

    //If we are unable to load the past user database, we just move on after printing error messages to console.
    public void loadUserDB(){
        try{
            UserDB.setUsers((SortedList<User>) UserIO.readUsers());
        }
        catch(IOException e){
            System.out.println("Unable to open/read users.dat file.");
        }
        // Our method may not properly cast the userDB if it has an issue reading the User class.
        catch(ClassNotFoundException e){
            System.out.println("Unable to read and cast the userDB due to User class issues.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
