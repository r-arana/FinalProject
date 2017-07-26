package view;

import controller.RestaurantListController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.ReadExcelFile;
import model.Restaurant;
import structure.BinarySearchTree;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Rene on 7/3/2017.  Most of the logic is from the videos you provided.
 * Displays the tableview scene, and constructs and passes the observable list with our excel data to the appropriate
 * controller.
 */
public class RestaurantListJavaFXView {

    private String name;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String latitude;
    private String longitude;
    private String phoneNumber;
    private String photo;

    private ObservableList<Restaurant> restaurantData = FXCollections.observableArrayList();
    private BinarySearchTree<Restaurant> restaurantTree = new BinarySearchTree<>();

    private static Stage restaurantListStage = new Stage();
    private BorderPane rootLayout;
    RestaurantListController controller;

    public RestaurantListJavaFXView() throws IOException {
        loadExcelFile();
        initRootLayout();
        showRestaurantView();
        restaurantListStage.setTitle("Restaurant List Page");
    }

    private void initRootLayout(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RestaurantListJavaFXView.class.getResource("RootLayout.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout, 1200,800);
            restaurantListStage.setScene(scene);
            restaurantListStage.show();
        }
        catch (IOException e){
            System.err.println("Error in initRootLayout()");
        }
    }

    private void showRestaurantView(){
        try{
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(RestaurantListJavaFXView.class.getResource("RestaurantList.fxml"));
            SplitPane restaurantOverview = loader.load();

            // Set restaurant overview into the center of root layout
            rootLayout.setCenter(restaurantOverview);

            // Pass the controller access to this application
            controller = loader.getController();
            controller.setRestaurantApp(this);
            controller.initialize();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void loadExcelFile (){
        ArrayList restaurantList = null;
        try {
            restaurantList = ReadExcelFile.readExcelFile("RESTAURANT LIST.xlsx");
        } catch (Exception e) {
            System.err.println("Problem reading our excel file.");
            e.printStackTrace();
        }

        for (int row = 0; row < restaurantList.size(); row++){

            ArrayList record = (ArrayList) restaurantList.get(row);

            name = String.valueOf(record.get(0));
            streetAddress = String.valueOf(record.get(1));
            city = String.valueOf(record.get(2));
            state = String.valueOf(record.get(3));
            zipCode = String.valueOf(record.get(4));
            latitude = String.valueOf(record.get(5));
            longitude = String.valueOf(record.get(6));
            phoneNumber = String.valueOf(record.get(7));
            photo = String.valueOf(record.get(8));

            Restaurant restaurant = new Restaurant(name, streetAddress, city, state, zipCode, latitude, longitude, phoneNumber, photo);
            restaurantTree.add(restaurant);
            restaurantData.add(restaurant);
        }
        // Balance our binary search tree
        restaurantTree.balanceTree();
    }

    // Returns an observable list of restaurants
    public ObservableList<Restaurant> getRestaurantData(){
        return restaurantData;
    }

    public void setRestaurantData(ObservableList<Restaurant> newList){
        this.restaurantData = newList;
    }

    public BinarySearchTree<Restaurant> getBinarySearchTree() {
        return restaurantTree;
    }

}
