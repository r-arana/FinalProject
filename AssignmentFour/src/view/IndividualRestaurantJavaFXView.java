package view;

import controller.IndividualRestaurantController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Restaurant;

import java.io.IOException;

/**
 * Created by REA on 7/11/2017.
 * Creates the window for individual restaurant information, and passes restaurant data to the relevant controller.
 *
 */
public class IndividualRestaurantJavaFXView {

    IndividualRestaurantController controller;

    public IndividualRestaurantJavaFXView(Restaurant restaurant) throws IOException{

        try {
            Stage individualRestaurantStage = new Stage();
            Parent root = null;
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(IndividualRestaurantJavaFXView.class.getResource("IndividualRestaurant.fxml"));
            root = loader.load();
            individualRestaurantStage.setTitle("Summary");
            individualRestaurantStage.setScene(new Scene(root, 1200, 400));
            individualRestaurantStage.show();

            controller = loader.getController();
            controller.setIndividualRestaurantApp(this);
            controller.initialize(restaurant);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
