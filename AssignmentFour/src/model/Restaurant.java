package model;

import javafx.beans.property.SimpleStringProperty;
import org.junit.Test;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Comparator;

/**
 * Created by REA on 7/2/2017.
 *
 * This was redone using the formatting advice from http://docs.oracle.com/javafx/2/fxml_get_started/fxml_tutorial_intermediate.htm
 * to use the SimpleStringProperty instead of String.
 */

public class Restaurant implements Comparable<Restaurant>, Serializable{

    private SimpleStringProperty name;
    private SimpleStringProperty streetAddress;
    private SimpleStringProperty city;
    private SimpleStringProperty state;
    private SimpleStringProperty zipCode;
    private SimpleStringProperty latitude;
    private SimpleStringProperty longitude;
    private SimpleStringProperty phoneNumber;
    private SimpleStringProperty photo;
    private SimpleStringProperty distance;
    // https://stackoverflow.com/questions/12806278/double-decimal-formatting-in-java
    NumberFormat formatter = new DecimalFormat("#0.00");

    public Restaurant(){
        this.name = new SimpleStringProperty("");
        this.streetAddress = new SimpleStringProperty("");
        this.city = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.zipCode = new SimpleStringProperty("");
        this.latitude = new SimpleStringProperty("");
        this.longitude = new SimpleStringProperty("");
        this.phoneNumber = new SimpleStringProperty("");
        this.photo = new SimpleStringProperty("");
        this.distance = new SimpleStringProperty("");
    }

    public Restaurant(String name, String streetAddress, String city, String state, String zipCode, String latitude, String longitude, String phoneNumber, String photo) {
        this.name = new SimpleStringProperty(name);
        this.streetAddress = new SimpleStringProperty(streetAddress);
        this.city = new SimpleStringProperty(city);
        this.state = new SimpleStringProperty(state);
        this.zipCode = new SimpleStringProperty(zipCode);
        this.latitude = new SimpleStringProperty(latitude);
        this.longitude = new SimpleStringProperty(longitude);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.photo = new SimpleStringProperty(photo);
        this.distance = new SimpleStringProperty("");
    }

    // Used for searching through our BST
    public Restaurant(String latitude, String longitude){
        this.name = new SimpleStringProperty("");
        this.streetAddress = new SimpleStringProperty("");
        this.city = new SimpleStringProperty("");
        this.state = new SimpleStringProperty("");
        this.zipCode = new SimpleStringProperty("");
        this.latitude = new SimpleStringProperty(latitude);
        this.longitude = new SimpleStringProperty(longitude);
        this.phoneNumber = new SimpleStringProperty("");
        this.photo = new SimpleStringProperty("");
        this.distance = new SimpleStringProperty("");
    }

    /** Returns the distance between two locations to the nearest part of a mile.
        Haversine formula http://www.movable-type.co.uk/scripts/latlong.html
    */
    public static double calculateDistance(Restaurant o1, Restaurant o2){
        // Longitude and latitude are already in decimal degrees
        double earthRadius = 3959; //miles

        double latitude1 = Math.toRadians(Double.valueOf(o1.getLatitude()));
        double latitude2 = Math.toRadians(Double.valueOf(o2.getLatitude()));

        double longitude1 = Math.toRadians(Double.valueOf(o1.getLongitude()));
        double longitude2 = Math.toRadians(Double.valueOf(o2.getLongitude()));

        double changeInLatitude = latitude2 - latitude1;
        double changeInLongitude = longitude2 - longitude1;

        double a = Math.sin(changeInLatitude / 2) * Math.sin(changeInLatitude / 2) +
                   Math.cos(latitude1) * Math.cos(latitude2) *
                   Math.sin(changeInLongitude / 2) * Math.sin(changeInLongitude / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c;
    }

    public String getDistance() {
        return distance.get();
    }

    public double getDistanceAsDouble(){
        if (distance.get().equals("")){
            return 0;
        }
        return Double.valueOf(distance.get());
    }

    public SimpleStringProperty distanceProperty(){
        return distance;
    }

    public void setDistance(String distance) {
        this.distance.set(distance);
    }

    public void setDistance(double distance){
        this.distance.set(formatter.format(distance));
    }

    public String getName() {
        return name.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty nameProperty(){
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getStreetAddress() {
        return streetAddress.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty streetAddressProperty(){
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress.set(streetAddress);
    }

    public String getCity() {
        return city.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty cityProperty(){
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getState() {
        return state.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty stateProperty(){
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getZipCode() {
        return zipCode.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty zipCodeProperty(){
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode.set(zipCode);
    }

    public String getLatitude() {
        return latitude.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty latitudeProperty(){
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude.set(latitude);
    }

    public String getLongitude() {
        return longitude.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty longitudeProperty(){
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude.set(longitude);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty phoneNumberProperty(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public String getPhoto() {
        return photo.get();
    }

    // Define a getter for the property of the variable
    public SimpleStringProperty photoProperty(){
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo.set(photo);
    }

    public String toString(){
        return "Restaurant{" +
                "Name: " + getName() + "/ " +
                "Address: " + getStreetAddress() + "/ " + getCity() + ", " + getState() + ", " +  getZipCode() + "/ " +
                "Latitude: " + getLatitude() + "/ " +
                "Longitude: " + getLongitude() + "/ " +
                "Phone Number: " + getPhoneNumber() + "/ " +
                "Distance: " + getDistance() + "/" +
                "Image: " + getPhoto() + "}" +
                "\n";
    }

    public boolean equals(Restaurant o){
        int compare = this.getLatitude().compareTo(o.getLatitude());

        if (compare == 0){
            compare = this.getLongitude().compareTo(o.getLongitude());
            return compare == 0;
        }
        else{
            return compare == 0;
        }
    }

    // Taken from your video https://www.youtube.com/watch?v=IewSoYs68bk at about 7 minutes
    // An explanation of the ?: operator comes from http://www.terminally-incoherent.com/blog/2006/04/21/java-operator/
    // (boolean expression) ? (Evaluate and return if true): (Evaluate and return if false)

    @Override
    public int compareTo(Restaurant o) {
        int compare = this.getLatitude().compareTo(o.getLatitude());
        // If our latitude values are equivalent then we move on to comparing the longitude of
        // our coordinates.  Otherwise, we can just return our initial comparison.
        return (compare == 0) ? this.getLongitude().compareTo(o.getLongitude()) : compare;
    }

    // Compares the name of this restaurant with the given restaurant.
    // This comparison will ignore symbols/punctuation.
    // https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#usc
    public int compareNames(Restaurant o) {
        String restaurant1 = this.getName().replaceAll("[^\\p{IsLatin}0-9]", "");
        //String restaurant1 = this.getName().replaceAll("[\\W[^\\p{L}]]", "");
        String restaurant2 = o.getName().replaceAll("[^\\p{IsLatin}0-9]", "");

        //System.out.println("Restaurant 1: " + restaurant1);
        //System.out.println("Restaurant 2: " + restaurant2);

        return restaurant1.compareToIgnoreCase(restaurant2);
        //return this.getName().compareToIgnoreCase(o.getName());
    }

    // Compares the name of this restaurant with the given restaurant.
    // This comparison will ignore symbols, punctuation, and spacing.
    public int comparePhoneNumbers(Restaurant o){
        String restaurant1 = this.getPhoneNumber().replaceAll("\\W", "");
        String restaurant2 = o.getPhoneNumber().replaceAll("\\W", "");

        return restaurant1.compareTo(restaurant2);
    }

    public static Comparator<Restaurant> getDistanceComparator(){
        Comparator<Restaurant> comparator = new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant o1, Restaurant o2) {
                // Rounding to nearest whole number
                //return (Integer.valueOf(o1.getDistance()) - Integer.valueOf(o2.getDistance()));
                return (int)((o1.getDistanceAsDouble() * 100) -(o2.getDistanceAsDouble() * 100));
            }
        };
        return comparator;
    }
}
