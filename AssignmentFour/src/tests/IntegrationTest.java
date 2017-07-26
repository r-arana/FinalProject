package tests;

import model.Restaurant;
import model.User;
import org.junit.Assert;
import org.junit.Test;
import structure.BinarySearchTree;
import structure.Graph;
import structure.PriorityQueue;
import structures.BoundedQueue;
import structures.BoundedStack;
import structures.IndexedList;
import structures.SortedList;
import org.junit.Assert.*;

/**
 * Created by REA on 7/26/2017.
 */
public class IntegrationTest {
    @Test
    public void testingDataStructures(){
        Restaurant res1 = new Restaurant("Restaurant 1", "Super Fake Address 1", "Woodbridge", "VA", "22150", "34.0000", "-71.0000", "(703) 111-4909", "fakePhoto1" );
        Restaurant res2 = new Restaurant("Restaurant 2", "Super Fake Address 2", "Woodbridge", "VA", "22150", "34.0000", "-72.0000", "(571)123-1290", "fakePhoto1" );
        Restaurant res3 = new Restaurant("Restaurant 3", "Super Fake Address 3", "Woodbridge", "VA", "22150", "34.0003", "-73.0000", "(202) 231-4579", "fakePhoto1" );
        Restaurant res4 = new Restaurant("Restaurant 4", "Super Fake Address 4", "Woodbridge", "VA", "22150", "34.0004", "-74.0000", "(703)111-4909", "fakePhoto1" );
        Restaurant res5 = new Restaurant("Restaurant 5", "Super Fake Address 5", "Woodbridge", "VA", "22150", "34.0005", "-75.0000", "(583) 890-4444", "fakePhoto1" );
        Restaurant res6 = new Restaurant("Restaurant 6", "Super Fake Address 6", "Woodbridge", "VA", "22150", "34.0005", "-76.0000", "(757) 160-4444", "fakePhoto1" );
        Restaurant res7 = new Restaurant("Restaurant 7", "Super Fake Address 7", "Woodbridge", "VA", "22150", "34.0005", "-77.0000", "(571)234-4444", "fakePhoto1" );

        Restaurant res8 = new Restaurant("Restaurant 1", "Super Fake Address 6", "Woodbridge", "VA", "22150", "38.837951", "-77.210698", "(757) 160-4444", "fakePhoto1" );
        Restaurant res9 = new Restaurant("Restaurant 2", "Super Fake Address 7", "Woodbridge", "VA", "22150", "47.283258", "-122.480733", "(571)234-4444", "fakePhoto1" );


        User user1 = new User("Bob", "Becker", "male", "123456789", "01/14/1990", "bob1", "bob1@gmail.com", "1@Bob", "7031236789", "fakepath1");
        User user2 = new User("Sue", "Burgen", "female", "123456789", "02/14/1995", "sue2", "sue2@gmail.com", "2@Sue", "7031236789", "fakepath2");
        User user3 = new User("George", "Donly", "male", "123456789", "03/14/1990", "geo3", "geo1@gmail.com", "3@Geo", "7031238900", "fakepath3");


        BoundedQueue<Restaurant> restaurantQueue = new BoundedQueue<>();
        BoundedStack<Restaurant> restaurantStack = new BoundedStack<>();
        SortedList<User> userSortedList = new SortedList<>();
        PriorityQueue<Restaurant> restaurantPriorityQueue = new PriorityQueue<Restaurant>(100);

        // This should sort the users according to username
        userSortedList.add(user1);
        userSortedList.add(user2);
        userSortedList.add(user3);

        // This should push all of our restaurants in so that res1 is at the bottom, and res7 is on top
        restaurantStack.push(res1);
        restaurantStack.push(res2);
        restaurantStack.push(res3);
        restaurantStack.push(res4);
        restaurantStack.push(res5);
        restaurantStack.push(res6);
        restaurantStack.push(res7);

        // Enqueues all of the elements so that res7 is first, and res1 is last
        while (!restaurantStack.isEmpty()){
            restaurantQueue.enqueue(restaurantStack.top());
            restaurantStack.pop();
        }

        // Now our priority queue should have all of our restaurant elements
        while (!restaurantQueue.isEmpty()){
            restaurantPriorityQueue.enqueue(restaurantQueue.dequeue());
        }

        // Everything should now be in order according to the compareTo method
        Assert.assertTrue(restaurantPriorityQueue.dequeue().compareTo(res1) == 0);
        Assert.assertTrue(restaurantPriorityQueue.dequeue().compareTo(res2) == 0);
        Assert.assertTrue(restaurantPriorityQueue.dequeue().compareTo(res3) == 0);
        Assert.assertTrue(restaurantPriorityQueue.dequeue().compareTo(res4) == 0);
        Assert.assertTrue(restaurantPriorityQueue.dequeue().compareTo(res5) == 0);
        Assert.assertTrue(restaurantPriorityQueue.dequeue().compareTo(res6) == 0);
        Assert.assertTrue(restaurantPriorityQueue.dequeue().compareTo(res7) == 0);


        BinarySearchTree<Restaurant> restaurantTree = new BinarySearchTree<>();
        Graph<Restaurant> restaurantGraph = new Graph<>();

        restaurantTree.add(res1);
        restaurantTree.add(res2);
        restaurantTree.add(res3);
        restaurantTree.add(res4);
        restaurantTree.add(res5);

        restaurantTree.balanceTree();

        restaurantGraph.addVertex(restaurantTree.get(res5));
        restaurantGraph.addVertex(restaurantTree.get(res2));
        restaurantGraph.addVertex(restaurantTree.get(res1));
        restaurantGraph.addVertex(restaurantTree.get(res3));
        restaurantGraph.addVertex(restaurantTree.get(res4));

        restaurantGraph.addEdge(res1, res2, Restaurant.calculateDistance(res1, res2));
        restaurantGraph.addEdge(res3, res4, Restaurant.calculateDistance(res1, res2));
        restaurantGraph.addEdge(res2, res3, Restaurant.calculateDistance(res1, res2));
        restaurantGraph.addEdge(res1, res4, Restaurant.calculateDistance(res1, res2));



        restaurantQueue = restaurantGraph.getToVertices(res1);

        restaurantPriorityQueue = new PriorityQueue<Restaurant>(restaurantQueue.size(), Restaurant.getDistanceComparator());
        Restaurant container;


        while (!restaurantQueue.isEmpty()){
            container = restaurantQueue.dequeue();
            container.setDistance(Restaurant.calculateDistance(container, res1));
            restaurantPriorityQueue.enqueue(container);
        }

        // Should print our adjacent restaurants in order by distance
        System.out.println(restaurantPriorityQueue);
    }

}
