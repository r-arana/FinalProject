package tests;

import model.ReadExcelFile;
import model.Restaurant;
import org.junit.Test;
import structure.BinarySearchTree;

import java.util.ArrayList;

/**
 * Created by REA on 7/4/2017.
 */
public class ReadExcelFileTest {
    private String name;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String latitude;
    private String longitude;
    private String phoneNumber;
    private String photo;

    @Test
    public void readExcelFile() throws Exception {
        BinarySearchTree<Restaurant> restaurantTree = new BinarySearchTree<>();
        ArrayList restaurantList = ReadExcelFile.readExcelFile("RESTAURANT LIST.xlsx");

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
        }

        System.out.print(restaurantTree);


    }

}