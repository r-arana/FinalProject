package tests;

import model.Restaurant;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by REA on 7/12/2017.
 */
public class RestaurantTest {
    Restaurant res1 = new Restaurant("Restaurant 7", "Super Fake Address 1", "Woodbridge", "VA", "22150", "34.0000", "-71.0000", "(703) 111-4909", "fakePhoto1" );
    Restaurant res2 = new Restaurant("Restaurant 6", "Super Fake Address 2", "Woodbridge", "VA", "22150", "34.0000", "-72.0000", "(571)123-1290", "fakePhoto1" );
    Restaurant res3 = new Restaurant("Restaurant 5", "Super Fake Address 3", "Woodbridge", "VA", "22150", "34.0003", "-73.0000", "(202) 231-4579", "fakePhoto1" );
    Restaurant res4 = new Restaurant("Restaurant 4", "Super Fake Address 4", "Woodbridge", "VA", "22150", "34.0004", "-74.0000", "(703)111-4909", "fakePhoto1" );
    Restaurant res5 = new Restaurant("Restaurant 3", "Super Fake Address 5", "Woodbridge", "VA", "22150", "34.0005", "-75.0000", "(583) 890-4444", "fakePhoto1" );
    Restaurant res6 = new Restaurant("Restaurant 2", "Super Fake Address 6", "Woodbridge", "VA", "22150", "34.0005", "-76.0000", "(757) 160-4444", "fakePhoto1" );
    Restaurant res7 = new Restaurant("Restaurant 1", "Super Fake Address 7", "Woodbridge", "VA", "22150", "34.0005", "-77.0000", "(571)234-4444", "fakePhoto1" );

    Restaurant res8 = new Restaurant("Restaurant 1", "Super Fake Address 6", "Woodbridge", "VA", "22150", "38.837951", "-77.210698", "(757) 160-4444", "fakePhoto1" );
    Restaurant res9 = new Restaurant("Restaurant 2", "Super Fake Address 7", "Woodbridge", "VA", "22150", "47.283258", "-122.480733", "(571)234-4444", "fakePhoto1" );

    // Test equals method
    Restaurant res10 = new Restaurant("Restaurant 7", "Super Fake Address 1", "Woodbridge", "VA", "22150", "34.0000", "-71.0000", "(703) 111-4909", "fakePhoto1" );


    @Test
    public void compareNames() throws Exception {
        Restaurant res1 = new Restaurant("Restaurant 1", "Super Fake Address 1", "Woodbridge", "VA", "22150", "34.0000", "-71.0000", "(703) 111-4909", "fakePhoto1" );
        Restaurant res2 = new Restaurant("Restaurant 2", "Super Fake Address 2", "Woodbridge", "VA", "22150", "34.0000", "-72.0000", "(571)123-1290", "fakePhoto1" );
        Restaurant res3 = new Restaurant("Restaurant 3", "Super Fake Address 3", "Woodbridge", "VA", "22150", "34.0003", "-73.0000", "(202) 231-4579", "fakePhoto1" );
        Restaurant res4 = new Restaurant("Restaurant 4", "Super Fake Address 4", "Woodbridge", "VA", "22150", "34.0004", "-74.0000", "(703)111-4909", "fakePhoto1" );
        Restaurant res5 = new Restaurant("Restaurant 5", "Super Fake Address 5", "Woodbridge", "VA", "22150", "34.0005", "-75.0000", "(583) 890-4444", "fakePhoto1" );
        Restaurant res6 = new Restaurant("Restaurant 6", "Super Fake Address 6", "Woodbridge", "VA", "22150", "34.0005", "-76.0000", "(757) 160-4444", "fakePhoto1" );
        Restaurant res7 = new Restaurant("Restaurant 7", "Super Fake Address 7", "Woodbridge", "VA", "22150", "34.0005", "-77.0000", "(571)234-4444", "fakePhoto1" );


        int comparison;

        comparison = res1.compareNames(res2);
        System.out.println(comparison);
        assertTrue(comparison < 0);

        comparison = res2.compareNames(res1);
        System.out.println(comparison);
        assertTrue(comparison > 0);

        comparison = res7.compareNames(res4);
        System.out.println(comparison);
        assertTrue(comparison > 0);
    }

    @Test
    public void comparePhoneNumbers() throws Exception {
        assertTrue(res1.comparePhoneNumbers(res4) == 0);
        assertTrue(res2.comparePhoneNumbers(res4) != 0);
        assertTrue(res3.comparePhoneNumbers(res7) != 0);
    }

    // http://www.movable-type.co.uk/scripts/latlong.html
    @Test
    public void calculateDistance(){

        // These coordinates should be about 3740km or 2323 miles

        System.out.println("Distance: " + res8.calculateDistance(res8, res9) + " miles");

        System.out.println("Distance: " + res9.calculateDistance(res9, res8) + " miles");

        System.out.println("Distance: " + res9.calculateDistance(res9, res9) + " miles");
    }

    @Test
    public void equals(){
        assertTrue(res1.equals(res1));
        assertFalse(res1.equals(res2));
        assertTrue(res1.equals(res10));
    }

}