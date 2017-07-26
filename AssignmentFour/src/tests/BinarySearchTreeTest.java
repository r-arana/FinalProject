package tests;

import interfaces.BinaryTreeInterface;
import model.Restaurant;
import structure.BinarySearchTree;

import static org.junit.Assert.*;

/**
 * Created by REA on 7/3/2017.
 */
public class BinarySearchTreeTest {
    BinarySearchTree<Restaurant> tree = new BinarySearchTree<Restaurant>();

    Restaurant res1 = new Restaurant("Restaurant 1", "Super Fake Address 1", "Woodbridge", "VA", "22150", "34.0000", "-71.0000", "7031114444", "fakePhoto1" );
    Restaurant res2 = new Restaurant("Restaurant 2", "Super Fake Address 2", "Woodbridge", "VA", "22150", "34.0000", "-72.0000", "7031114444", "fakePhoto1" );
    Restaurant res3 = new Restaurant("Restaurant 3", "Super Fake Address 3", "Woodbridge", "VA", "22150", "34.0003", "-73.0000", "7031114444", "fakePhoto1" );
    Restaurant res4 = new Restaurant("Restaurant 4", "Super Fake Address 4", "Woodbridge", "VA", "22150", "34.0004", "-74.0000", "7031114444", "fakePhoto1" );
    Restaurant res5 = new Restaurant("Restaurant 5", "Super Fake Address 5", "Woodbridge", "VA", "22150", "34.0005", "-75.0000", "7031114444", "fakePhoto1" );
    Restaurant res6 = new Restaurant("Restaurant 6", "Super Fake Address 6", "Woodbridge", "VA", "22150", "34.0005", "-76.0000", "7031114444", "fakePhoto1" );
    Restaurant res7 = new Restaurant("Restaurant 7", "Super Fake Address 7", "Woodbridge", "VA", "22150", "34.0005", "-77.0000", "7031114444", "fakePhoto1" );


    @org.junit.Test
    public void add() throws Exception {

        tree.add(res3);
        tree.add(res2);
        tree.add(res1);
        tree.add(res4);
        tree.add(res5);

        System.out.print(tree);

        assertTrue(tree.contains(res1));
        assertTrue(tree.contains(res2));
        assertTrue(tree.contains(res3));
        assertTrue(tree.contains(res4));
        assertTrue(tree.contains(res5));
        assertFalse(tree.contains(res6));
    }

    @org.junit.Test
    public void remove() throws Exception {
        try{
            tree.remove(res1);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        tree.add(res3);
        tree.add(res2);
        tree.add(res1);

        System.out.println(tree);

        assertTrue(tree.remove(res2).compareTo(res2) == 0);
        assertTrue(tree.remove(res1).compareTo(res1) == 0);

        System.out.print(tree);


    }

    @org.junit.Test
    public void contains() throws Exception {
        assertFalse(tree.contains(res4));

        tree.add(res4);
        assertTrue(tree.contains(res4));

    }

    @org.junit.Test
    public void get() throws Exception {

        try{
            tree.get(res1);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        tree.add(res1);
        assertTrue(tree.get(res1).compareTo(res1) == 0);
    }

    @org.junit.Test
    public void getNext() throws Exception {
        tree.add(res5);
        tree.add(res2);
        tree.add(res1);
        tree.add(res4);

        System.out.println(tree);

        tree.reset(BinaryTreeInterface.TraversalOrder.INORDER);

        assertTrue(tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER).compareTo(res1) == 0);
        assertTrue(tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER).compareTo(res2) == 0);
        assertTrue(tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER).compareTo(res4) == 0);
        assertTrue(tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER).compareTo(res5) == 0);

        try{
            tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    @org.junit.Test
    public void reset() throws Exception {
        tree.add(res5);
        tree.add(res2);
        tree.add(res3);
        tree.add(res4);

        System.out.println(tree);
        tree.reset(BinaryTreeInterface.TraversalOrder.INORDER);

        assertTrue(tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER).compareTo(res2) == 0);

        tree.add(res1);
        tree.reset(BinaryTreeInterface.TraversalOrder.INORDER);

        assertTrue(tree.getNext(BinaryTreeInterface.TraversalOrder.INORDER).compareTo(res1) == 0);

        System.out.println(tree);

    }

    @org.junit.Test
    public void size() throws Exception {
        assertTrue(tree.size() == 0);
        tree.add(res1);
        assertTrue(tree.size() == 1);
        tree.add(res2);
        assertTrue(tree.size() == 2);
        tree.remove(res1);
        assertTrue(tree.size() == 1);
    }

    @org.junit.Test
    public void isEmpty() throws Exception {
        assertTrue(tree.isEmpty());

        tree.add(res1);
        assertFalse(tree.isEmpty());
        tree.remove(res1);
        assertTrue(tree.isEmpty());

    }

    @org.junit.Test
    public void balanceTree(){
        tree.add(res1);
        tree.add(res2);
        tree.add(res3);
        tree.add(res4);
        tree.add(res5);
        tree.add(res6);
        tree.add(res7);

        System.out.println(tree);
        System.out.println();

        tree.balanceTree();
        System.out.println(tree);
        System.out.println();
    }

}