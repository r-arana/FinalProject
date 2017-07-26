package controller;

import interfaces.BinaryTreeInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Restaurant;
import structure.AntColony;
import structure.BinarySearchTree;
import structure.Graph;
import structure.PriorityQueue;
import structures.BoundedQueue;
import structures.BoundedStack;
import view.IndividualRestaurantJavaFXView;
import view.RestaurantListJavaFXView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by REA on 7/4/2017.  Provides all the logic for displaying our excel data, updating the data, and
 * deciding what categories to search through based on user input.
 */

/*
    The part with lambda expressions is something I need to understand better.
 *  The videos provided were helpful in understanding the logic.
 */

public class RestaurantListController {

    @FXML
    private TableView<Restaurant> restaurantTable;
    @FXML
    private TableColumn<Restaurant, String> nameColumn;
    @FXML
    private TableColumn<Restaurant, String> streetAddressColumn;
    @FXML
    private TableColumn<Restaurant, String> cityColumn;
    @FXML
    private TableColumn<Restaurant, String> stateColumn;
    @FXML
    private TableColumn<Restaurant, String> zipCodeColumn;
    @FXML
    private TableColumn<Restaurant, String> latitudeColumn;
    @FXML
    private TableColumn<Restaurant, String> longitudeColumn;
    @FXML
    private TableColumn<Restaurant, String> phoneNumberColumn;
    @FXML
    private TableColumn<Restaurant, String> photoColumn;
    @FXML
    private TableColumn<Restaurant, String> distanceColumn;
    @FXML
    private TextField searchBar;
    @FXML
    private TextField radiusTextField;
    @FXML
    private TextField startPoint;
    @FXML
    private Label errorLabel;
    @FXML
    private Label noMatchesLabel;
    @FXML
    private Label radiusError;
    @FXML
    private Button resetButton;
    @FXML
    private Button dijkstraButton;
    @FXML
    private Button antButton;


    private enum Search {COORDINATES, PHONE_NUMBER, NAME}

    private RestaurantListJavaFXView restaurantApp;
    private ObservableList<Restaurant> searchResults = FXCollections.observableArrayList();
    private boolean requestedLotsOfAnts = false;
    private boolean requestedDijkstraPath = false;
    private boolean isAskingForDirections = false;
    private boolean found = false;
    private double totalDistanceTraveled;

    public void initialize(){
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        streetAddressColumn.setCellValueFactory(cellData -> cellData.getValue().streetAddressProperty());
        cityColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
        stateColumn.setCellValueFactory(cellData -> cellData.getValue().stateProperty());
        zipCodeColumn.setCellValueFactory(cellData -> cellData.getValue().zipCodeProperty());
        latitudeColumn.setCellValueFactory(cellData -> cellData.getValue().latitudeProperty());
        longitudeColumn.setCellValueFactory(cellData -> cellData.getValue().longitudeProperty());
        phoneNumberColumn.setCellValueFactory(cellData -> cellData.getValue().phoneNumberProperty());
        photoColumn.setCellValueFactory(cellData -> cellData.getValue().photoProperty());
        distanceColumn.setCellValueFactory(cellData -> cellData.getValue().distanceProperty());

        restaurantTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && restaurantTable.getSelectionModel().getSelectedItem() != null){
                try{
                    System.out.println("Attempting to open IndividualRestaurantJavaFXView");
                    new IndividualRestaurantJavaFXView(restaurantTable.getSelectionModel().getSelectedItem());
                }
                catch (IOException e){
                    System.err.println("Error opening IndividualRestaurantJavaFXView");
                }
            }
        });
    }

    /** Sets this RestaurantListJavaFXView object to the passed RestaurantListJavaFXView object.
     *
     * @param restaurantApp
     */
    public void setRestaurantApp(RestaurantListJavaFXView restaurantApp){
        this.restaurantApp = restaurantApp;

        // Set our observable list to the table
        restaurantTable.setItems(restaurantApp.getRestaurantData());
    }

    public void resetButtonPressed(){
        clearDistanceValues();
        restaurantTable.setItems(restaurantApp.getRestaurantData());
    }

    public void handleDijkstraPath(){
        requestedDijkstraPath = true;
        handleEnterPressed();
    }

    public void handleAntsPath(){
        requestedLotsOfAnts = true;
        handleEnterPressed();
    }

    public void handleEnterPressed(){
        String searchBarTop = startPoint.getText().trim();
        String searchBarBot = searchBar.getText().trim();
        BoundedQueue<Restaurant> results;
        isAskingForDirections = false;
        found = false;

        clearDistanceValues();

        if (searchBarTop.equals("") || searchBarBot.equals("")){
            if (searchBarTop.equals("")){
                results = getResultsQueue(searchBarBot);
            }
            else{
                results = getResultsQueue(searchBarTop);
            }

            // While our queue is not null and not empty
            while (results != null && !results.isEmpty()){
                searchResults.add(results.dequeue());
            }

        }
        else{
            isAskingForDirections = true;

            Restaurant startVertex;
            Restaurant endVertex;
            Restaurant container;
            BinarySearchTree<Restaurant> tree = restaurantApp.getBinarySearchTree();

            results = getResultsQueue(searchBarTop);
            // We can allow this to go into our startVertex if found is true
            if (results != null && found){
                startVertex = results.dequeue();

                found = false;
                results = getResultsQueue(searchBarBot);


                if (results != null && found){
                    endVertex = results.dequeue();

                    if (startVertex.equals(endVertex)){
                        searchResults.add(startVertex);
                    }
                    else if (!tree.contains(endVertex)){
                        found = false;
                    }
                    else{

                        if (requestedDijkstraPath){
                            System.out.println("\nCreating Dijkstra path.");
                            results = getDijkstraPath(startVertex, endVertex);
                        }
                        else if (requestedLotsOfAnts){
                            System.out.println("\nCreating ant colony path.");
                            AntColony antColony = new AntColony(getGraph(startVertex), startVertex, endVertex);

                            antColony.sendOutTheAnts(2000);

                            results = antColony.getBestAntSolution();
                        }
                        else{
                            System.out.println("\nCreating standard path.");
                            results = getShortestPath(startVertex, endVertex);
                        }

                        if (results == null){
                            found = false;
                        }
                        else{
                            totalDistanceTraveled = 0;
                            // While our queue is not empty
                            while (!results.isEmpty()){
                                container = results.dequeue();
                                totalDistanceTraveled += container.getDistanceAsDouble();
                                searchResults.add(container);
                            }
                        }


                    }
                }
            }
        }

        if (found){
            System.out.println("Found it.");
            System.out.println("Total distance traveled: " + totalDistanceTraveled);

            // Update the information displayed
            restaurantTable.setItems(searchResults);
        }
        else {
            System.out.println("Didn't find it.");
            noMatchesLabel.setVisible(true);
        }

        requestedDijkstraPath = false;
        requestedLotsOfAnts = false;
    }

    private BoundedQueue<Restaurant> getResultsQueue(String input){
        String latitude;
        String longitude;
        String key;
        String[] container;
        BoundedQueue<Restaurant> results = null;

        errorLabel.setVisible(false);
        noMatchesLabel.setVisible(false);
        //key = searchBar.getText().trim();
        key = input;

        // If the key is not null and not empty
        if (key != null && (!key.isEmpty())){


            if (key.contains(",")) {
                container = key.split(",");
                // Need to make sure that we actually have 2 elements in our container, or we'll be accessing a
                // null pointer.
                if (container.length == 2 && containsCoordinates(container[0].trim(), container[1].trim())) {
                    // If they're actually coordinates, and not some random input with a comma.

                    latitude = container[0].trim();
                    longitude = container[1].trim();
                    System.out.println(searchBar.getText());

                    Restaurant restaurantKey = new Restaurant(latitude, longitude);

                    results = getSearchResults(Search.COORDINATES, restaurantKey);

                } else {
                    System.out.println("Improper coordinates.");
                    errorLabel.setVisible(true);
                }
            }
            // Handle phone numbers
            // This is more restricting than checking for names, so it should be done first.
            else if (containsPhoneNumber(key)){
                Restaurant searchKey = new Restaurant();
                searchKey.setPhoneNumber(key);
                results = getSearchResults(Search.PHONE_NUMBER, searchKey);
            }
            // Handle names
            else if (mightContainName(key)){
                Restaurant searchKey = new Restaurant();
                searchKey.setName(key);
                results = getSearchResults(Search.NAME, searchKey);
            }
            else{
                errorLabel.setVisible(true);
            }
        }
        else{
            errorLabel.setVisible(true);
        }

        return results;
    }

    private BoundedQueue<Restaurant> getSearchResults(Search searchType, Restaurant searchKey){
        found = false;
        searchResults.clear();
        Restaurant newKey;
        BinarySearchTree<Restaurant> tree = restaurantApp.getBinarySearchTree();
        BoundedQueue<Restaurant> queue = new BoundedQueue<>(tree.size());

        if (searchType.equals(Search.COORDINATES)) {

            int radius = getRadius();
            PriorityQueue<Restaurant> minHeap = getMinimumHeap(searchKey);
            Restaurant container;

            System.out.println("Searching for coordinates.");

            if (isAskingForDirections){
                // If we find the corresponding element, we enqueue it
                if (restaurantApp.getBinarySearchTree().contains(searchKey)){

                    queue.enqueue(restaurantApp.getBinarySearchTree().get(searchKey));

                    //searchResults.add(restaurantApp.getBinarySearchTree().get(searchKey));
                    found = true;
                }
                // Otherwise, we simply return the search key
                else{
                    queue.enqueue(searchKey);
                    found = true;
                }

            }
            else if (radius == 0) {
                if (restaurantApp.getBinarySearchTree().contains(searchKey)){

                    queue.enqueue(restaurantApp.getBinarySearchTree().get(searchKey));

                    //searchResults.add(restaurantApp.getBinarySearchTree().get(searchKey));
                }
                else{
                    // While our heap is not empty
                    while (!minHeap.isEmpty()) {
                        // The nature of our priority queue is such that the element closest to our search key
                        // will be returned for every dequeue.
                        // When our radius is 0 it is assumed that the user does not want the list
                        // to be restricted by distance.

                        queue.enqueue(minHeap.dequeue());
                        //searchResults.add(minHeap.dequeue());
                    }
                }
                found = true;

            } else {
                // While our heap is not empty
                while (!minHeap.isEmpty()) {
                    // The nature of our priority queue is such that the element closest to our search key
                    // will be returned for every dequeue.
                    container = minHeap.dequeue();

                    // If the element's distance is within the bounds of our radius then we add it to our list
                    if (Double.valueOf(container.getDistance()) <= radius) {
                        queue.enqueue(container);
                        //searchResults.add(container);
                    }
                }

                if (!queue.isEmpty()){
                    found = true;
                }
            }
        }
        else if (searchType.equals(Search.NAME)){
            System.out.println("Searching for a name.");
            int numberOfElements = tree.reset(BinaryTreeInterface.TraversalOrder.INORDER);

            while (numberOfElements > 0){
                newKey = tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER);

                if (newKey.compareNames(searchKey) == 0){

                    queue.enqueue(restaurantApp.getBinarySearchTree().get(newKey));
                    // We add our search result to our new observable list
                    //searchResults.add(restaurantApp.getBinarySearchTree().get(newKey));

                    found = true;
                }
                numberOfElements--;
            }
        }
        else if (searchType.equals(Search.PHONE_NUMBER)){

            System.out.println("Searching for a phone number.");
            int numberOfElements = tree.reset(BinaryTreeInterface.TraversalOrder.INORDER);

            while (numberOfElements > 0){
                newKey = tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER);

                if (newKey.comparePhoneNumbers(searchKey) == 0){

                    queue.enqueue(restaurantApp.getBinarySearchTree().get(newKey));
                    // We add our search result to our new observable list
                    //searchResults.add(restaurantApp.getBinarySearchTree().get(newKey));

                    found = true;
                }
                numberOfElements--;
            }
        }

        return queue;
    }



    // Only use this after verifying that you've received coordinates.
    private PriorityQueue<Restaurant> getMinimumHeap(Restaurant searchKey){
        // We need the binary search tree to get the elements into our minimum heap
        BinarySearchTree<Restaurant> tree = restaurantApp.getBinarySearchTree();
        int numberOfElements = tree.reset(BinaryTreeInterface.TraversalOrder.INORDER);
        // Instantiate our minHeap to fill it from the binary search tree
        PriorityQueue<Restaurant> minHeap;
        minHeap = new PriorityQueue<Restaurant>(numberOfElements, Restaurant.getDistanceComparator());

        while (numberOfElements > 0){
            Restaurant container = tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER);

            // Set the distance between the chosen location and the restaurant in the container
            container.setDistance(Restaurant.calculateDistance(searchKey, container));

            // Grab the elements inside of our binary tree, and add it to our heap.
            minHeap.enqueue(container);
            numberOfElements--;
        }

        //System.out.println(minHeap);
        return minHeap;
    }

    /* Only use this for our graph
    // Adds adjacent vertices that are not marked to a minimum heap / priority queue
    // Precondition: The edges of the graph are already set
       This may seem a little odd, but the idea is straightforward.  I previously attempted to simply search for the
       shortest distance from the current node to our adjacent nodes.  This usually worked well, but had cases where
       the shortest distance to the next node actually led away from our goal.  By basing the distance of the
       adjacent nodes on how far way they are from our goal we can still use our minimum heap for distance and ensure
       that every step will move closer to our goal.
    */
    private PriorityQueue<Restaurant> getMinimumHeap(Restaurant currentVertex, Restaurant endVertex, Graph<Restaurant> graph){

        BoundedQueue<Restaurant> queue = graph.getToVertices(currentVertex);
        int numberOfVertices = queue.size();

        // Instantiate our minHeap to fill it from the queue
        PriorityQueue<Restaurant> minHeap;
        minHeap = new PriorityQueue<Restaurant>(numberOfVertices, Restaurant.getDistanceComparator());

        // While the queue is not empty
        while (!queue.isEmpty()){
            Restaurant adjacentVertex = queue.dequeue();

            // We only add vertices that are not already marked
            if (!graph.isMarked(adjacentVertex)) {
                // Set the distance between the vertex adjacent to our current vertex and our end point
                adjacentVertex.setDistance(graph.weightIs(adjacentVertex, endVertex));
                //container.setDistance(Restaurant.calculateDistance(searchKey, container));

                // Grab the elements inside of our binary tree, and add it to our heap.
                minHeap.enqueue(adjacentVertex);
            }
        }

        //System.out.println(minHeap);
        return minHeap;
    }

    private int getRadius(){
        String radiusText = radiusTextField.getText().trim().replace(",", "");
        radiusError.setVisible(false);
        try {
            // First we make sure that our text field is not empty
            if (radiusText == null || radiusText.equals("")) {
                return 0;
            }
            // If the textfield is not empty, then we can safely check to make sure it's actually a number.
            else if (Pattern.matches("[0-9]+", radiusText) && (Integer.valueOf(radiusText) < Integer.MAX_VALUE)) {
                return Integer.valueOf(radiusText);
            } else {
                radiusError.setVisible(true);
                return 0;
            }
        }
        catch (NumberFormatException e){
            System.err.println(e.getMessage());
            radiusError.setVisible(true);
            return 0;
        }
    }

    private Graph<Restaurant> getGraph(Restaurant startVertex){

        BinarySearchTree<Restaurant> tree = restaurantApp.getBinarySearchTree();
        Graph<Restaurant> graph;
        Restaurant[] vertices;
        int numberOfVertices;

        // Passes our search key its equivalent element if it exists in our tree
        if (tree.contains(startVertex)){
            startVertex = tree.get(startVertex);
            numberOfVertices = restaurantApp.getBinarySearchTree().size();
            graph = new Graph<Restaurant>(numberOfVertices);
            vertices = new Restaurant[numberOfVertices];
        }
        else{
            // Add an extra element for our startVertex
            numberOfVertices = restaurantApp.getBinarySearchTree().size() + 1;
            graph = new Graph<Restaurant>(numberOfVertices);
            vertices = new Restaurant[numberOfVertices];

            graph.addVertex(startVertex);
            // Place our extra vertex in the last slot
            vertices[numberOfVertices - 1] = startVertex;
        }

        // Add vertices
        int numberOfElements = tree.reset(BinaryTreeInterface.TraversalOrder.INORDER);

        for (int i = 0; i < numberOfElements; i++){
            Restaurant currentRestaurant = tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER);

            // Add our current restaurant to the graph
            graph.addVertex(currentRestaurant);
            vertices[i] = currentRestaurant;
        }

        // Add edges
        for (int row = 0; row < numberOfVertices; row++){

            for (int column = 0; column < numberOfVertices; column++){
                Restaurant fromVertex = vertices[row];
                Restaurant toVertex = vertices[column];

                graph.addEdge(fromVertex, toVertex, Restaurant.calculateDistance(fromVertex, toVertex));
            }
        }
        /*
        // 10 of the graph edges will be randomly destroyed
        for (int i = 0; i < numberOfVertices; i++){
            Restaurant fromVertex = vertices[(int)(Math.random() * (numberOfVertices - 1))];
            Restaurant toVertex = vertices[(int)(Math.random() * (numberOfVertices - 1))];

            graph.addEdge(fromVertex, toVertex, -1);
        }

        System.out.println(graph.drawEdges());
        */
        return graph;
    }

    private BoundedQueue<Restaurant> getShortestPath(Restaurant startVertex, Restaurant endVertex){

        Graph<Restaurant> graph = getGraph(startVertex);
        BoundedQueue<Restaurant> queue = new BoundedQueue<>(graph.getNumberOfVertices());
        PriorityQueue<Restaurant> minHeap;

        // Remove the direct link to our endVertex
        graph.addEdge(startVertex, endVertex, -1);

        graph.markVertex(startVertex);
        // Zero out the distance for our starting point to make sure it displays properly
        startVertex.setDistance("");
        queue.enqueue(startVertex);

        Restaurant currentVertex = startVertex;
        Restaurant previousVertex;
        boolean found = false;

        do{
            if (currentVertex.compareTo(endVertex) == 0){
                found = true;
            }
            else {
                previousVertex = currentVertex;

                minHeap = getMinimumHeap(currentVertex, endVertex, graph);

                currentVertex = minHeap.dequeue();

                //System.out.println(currentVertex + "\n");

                graph.markVertex(currentVertex);
                // Fixing our adjusted distance calculation done in our getMinimumHeap() method.
                currentVertex.setDistance(graph.weightIs(previousVertex, currentVertex));
                queue.enqueue(currentVertex);
            }

        } while (!found);

        return queue;
    }

    private void setDistanceValuesToMax(){
        BinarySearchTree<Restaurant> tree = restaurantApp.getBinarySearchTree();

        int numberOfElements = tree.reset(BinaryTreeInterface.TraversalOrder.INORDER);
        Restaurant currentRestaurant;

        while (numberOfElements > 0){
            currentRestaurant = tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER);
            currentRestaurant.setDistance(Integer.MAX_VALUE);
            numberOfElements--;
        }

    }

    private BoundedQueue<Restaurant> getDijkstraPath(Restaurant startVertex, Restaurant endVertex){

        Graph<Restaurant> graph = getGraph(startVertex);
        BoundedQueue<Restaurant> solution = new BoundedQueue<>(graph.getNumberOfVertices());
        PriorityQueue<Restaurant> minHeap;
        ArrayList<Restaurant> list = new ArrayList<>();
        ArrayList<Restaurant> listPreviousElement = new ArrayList<>();

        // Remove the direct link to our endVertex
        graph.addEdge(startVertex, endVertex, -1);

        // Set all of our distance values to the max integer available for java
        setDistanceValuesToMax();

        // Zero out the distance for our starting point to make sure it displays properly
        startVertex.setDistance(0);
        //queue.enqueue(startVertex);

        Restaurant currentVertex = startVertex;

        boolean found = false;
        double newDistance;

        do{
            if (currentVertex.compareTo(endVertex) == 0){
                found = true;
            }
            else {
                //System.out.println(currentVertex);

                graph.markVertex(currentVertex);
                // This will return a minHeap for the total distance away from our start
                BoundedQueue<Restaurant> queue = graph.getToVertices(currentVertex);
                int numberOfVertices = queue.size();

                // Instantiate our minHeap to fill it from the queue
                minHeap = new PriorityQueue<Restaurant>(numberOfVertices, Restaurant.getDistanceComparator());
                Restaurant adjacentVertex;

                // While the queue is not empty
                while (!queue.isEmpty()){
                    adjacentVertex = queue.dequeue();

                    // We only add vertices that are not already marked
                    if (!graph.isMarked(adjacentVertex)) {

                        newDistance = currentVertex.getDistanceAsDouble() + graph.weightIs(currentVertex, adjacentVertex);

                        // If our new distance is less than the distance currently inside of our vertex
                        if (newDistance < adjacentVertex.getDistanceAsDouble()) {
                            // Every node will contain the total distance traveled from the starting node.
                            adjacentVertex.setDistance(newDistance);
                            int index = list.indexOf(adjacentVertex);
                            if (index == -1){
                                list.add(adjacentVertex);
                                listPreviousElement.add(currentVertex);
                            }
                            else{
                                listPreviousElement.set(index, currentVertex);
                            }
                        }
                        // Grab the elements inside of our queue, and add it to our heap.
                        minHeap.enqueue(adjacentVertex);
                    }
                }

                currentVertex = minHeap.dequeue();
            }

        } while (!found);

        int index = list.indexOf(endVertex);
        currentVertex = endVertex;
        Restaurant nextVertex;
        BoundedStack<Restaurant> stack = new BoundedStack<>(graph.getNumberOfVertices());
        stack.push(currentVertex);

        while (index != -1) {
            nextVertex = listPreviousElement.get(index);
            stack.push(nextVertex);

            currentVertex.setDistance(graph.weightIs(nextVertex, currentVertex));
            currentVertex = nextVertex;
            index = list.indexOf(currentVertex);
            if (index == -1){
                currentVertex.setDistance("");
            }
        }

        while (!stack.isEmpty()) {
            solution.enqueue(stack.top());
            stack.pop();
        }

        return solution;
    }

    //http://www.zparacha.com/phone_number_javascript_regex/
    private boolean containsPhoneNumber(String potentialPhoneNumber){
        return Pattern.matches("^\\(?[0-9]{3}\\)? ?[0-9]{3}-? ?[0-9]{4}$", potentialPhoneNumber);
    }

    private boolean mightContainName(String potentialName){
        return (potentialName.length() > 2);
        //return Pattern.matches("^[a-zA-Z0-9\\W]++$", potentialName);
    }

    // We're just checking if the text they enter matches the typical format for coordinates
    private boolean containsCoordinates(String coordinate1, String coordinate2){
        return Pattern.matches("^-?[0-9]+\\.-?[0-9]+$", coordinate1) &&
               Pattern.matches("^-?[0-9]+\\.-?[0-9]+$", coordinate2);
    }

    /** Getter for the TableView variable used to display our restaurant data.
     *
     * @return TableView variable
     */
    public TableView<Restaurant> getRestaurantTable() {
        return restaurantTable;
    }

    /**
     * Setter for TableView variable used to display our restaurant data.
     * @param restaurantTable
     */
    public void setRestaurantTable(TableView<Restaurant> restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    private void clearDistanceValues(){
        BinarySearchTree<Restaurant> tree = restaurantApp.getBinarySearchTree();

        int numberOfElements = tree.reset(BinaryTreeInterface.TraversalOrder.INORDER);
        Restaurant currentRestaurant;

        while (numberOfElements > 0){
            currentRestaurant = tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER);
            currentRestaurant.setDistance("");
            numberOfElements--;
        }

    }

}
