package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.Restaurant;
import view.IndividualRestaurantJavaFXView;

/**
 * Created by REA on 7/11/2017.
 * Creates the window containing an individual restaurant's information.
 */
public class IndividualRestaurantController {

    @FXML
    private ImageView restaurantImageView;
    @FXML
    private WebView restaurantWebView;
    @FXML
    private ListView<String> restaurantListView = new ListView<>();

    private IndividualRestaurantJavaFXView individualRestaurantApp;

    public void initialize(Restaurant restaurant){
        Restaurant clickedRestaurant = restaurant;
        restaurantWebView.setVisible(false);

        ObservableList<String> temp = FXCollections.observableArrayList();
        temp.add("Name:  " + clickedRestaurant.getName());
        temp.add("Address:  " + clickedRestaurant.getStreetAddress() + ", " + clickedRestaurant.getCity() + ", " + clickedRestaurant.getState() +
                " " + clickedRestaurant.getZipCode());
        temp.add("Coordinates:  " + clickedRestaurant.getLatitude() + ", " + clickedRestaurant.getLongitude());
        temp.add("Phone Number:  " + clickedRestaurant.getPhoneNumber());

        restaurantListView.setItems(temp);

        Image photo = new Image(clickedRestaurant.getPhoto());
        if ((int)photo.getHeight() == 0){

            WebEngine webEngine = restaurantWebView.getEngine();
            webEngine.load(clickedRestaurant.getPhoto());
            restaurantWebView.setVisible(true);
        }
        else {
            restaurantImageView.setImage(photo);
        }

    }

    public void setIndividualRestaurantApp(IndividualRestaurantJavaFXView individualRestaurantApp){
        this.individualRestaurantApp = individualRestaurantApp;
    }

    public ListView<String> getRestaurantListView() {
        return restaurantListView;
    }

    public void setRestaurantListView(ListView<String> restaurantListView) {
        this.restaurantListView = restaurantListView;
    }

}
