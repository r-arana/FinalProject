package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *  Creates the window for the registration page.
 */

public class RegistrationJavaFXView {

    public void registrationJavaFXView() throws Exception {
        Stage createRegistrationStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Registration.fxml"));
        createRegistrationStage.setTitle("Registration");
        createRegistrationStage.setScene(new Scene(root, 500, 900));
        createRegistrationStage.show();
    }



}
