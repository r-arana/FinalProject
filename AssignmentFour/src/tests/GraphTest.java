package tests;

import model.Restaurant;
import org.junit.Test;
import structure.Graph;
import structures.BoundedQueue;

import static org.junit.Assert.*;

/**
 * Created by REA on 7/18/2017.
 */
public class GraphTest {

    Restaurant res1 = new Restaurant("Restaurant 1", "Super Fake Address 1", "Woodbridge", "VA", "22150", "34.0000", "-71.0000", "(703) 111-4909", "fakePhoto1" );
    Restaurant res2 = new Restaurant("Restaurant 2", "Super Fake Address 2", "Woodbridge", "VA", "22150", "34.0000", "-72.0000", "(571)123-1290", "fakePhoto1" );
    Restaurant res3 = new Restaurant("Restaurant 3", "Super Fake Address 3", "Woodbridge", "VA", "22150", "34.0003", "-73.0000", "(202) 231-4579", "fakePhoto1" );
    Restaurant res4 = new Restaurant("Restaurant 4", "Super Fake Address 4", "Woodbridge", "VA", "22150", "34.0004", "-74.0000", "(703)111-4909", "fakePhoto1" );
    Restaurant res5 = new Restaurant("Restaurant 5", "Super Fake Address 5", "Woodbridge", "VA", "22150", "34.0005", "-75.0000", "(583) 890-4444", "fakePhoto1" );
    Restaurant res6 = new Restaurant("Restaurant 6", "Super Fake Address 6", "Woodbridge", "VA", "22150", "34.0005", "-76.0000", "(757) 160-4444", "fakePhoto1" );
    Restaurant res7 = new Restaurant("Restaurant 7", "Super Fake Address 7", "Woodbridge", "VA", "22150", "34.0005", "-77.0000", "(571)234-4444", "fakePhoto1" );

    Restaurant res8 = new Restaurant("Restaurant 1", "Super Fake Address 6", "Woodbridge", "VA", "22150", "38.837951", "-77.210698", "(757) 160-4444", "fakePhoto1" );
    Restaurant res9 = new Restaurant("Restaurant 2", "Super Fake Address 7", "Woodbridge", "VA", "22150", "47.283258", "-122.480733", "(571)234-4444", "fakePhoto1" );

    Graph<Restaurant> graph = new Graph<>();

    @Test
    public void addVertex() throws Exception {
        graph.addVertex(res1);
        graph.addVertex(res2);
        graph.addVertex(res7);
        graph.addVertex(res4);

        System.out.println(graph);

    }

    @Test
    public void hasVertex() throws Exception {
        graph.addVertex(res1);
        graph.addVertex(res2);
        graph.addVertex(res7);
        graph.addVertex(res4);

        assertTrue(graph.hasVertex(res1));
        assertFalse(graph.hasVertex(res8));
        assertTrue(graph.hasVertex(res4));
    }

    @Test
    public void addEdge() throws Exception {
        graph.addVertex(res1);
        graph.addVertex(res2);
        graph.addVertex(res7);
        graph.addVertex(res4);

        System.out.println(graph);

        graph.addEdge(res1, res2, Restaurant.calculateDistance(res1, res2));
        graph.addEdge(res2, res4, Restaurant.calculateDistance(res2, res4));
        graph.addEdge(res7, res4, Restaurant.calculateDistance(res7, res4));
        graph.addEdge(res4, res1, Restaurant.calculateDistance(res4, res1));

        System.out.println(graph.drawEdges());

    }

    @Test
    public void weightIs() throws Exception {
        graph.addVertex(res1);
        graph.addVertex(res2);
        graph.addVertex(res7);
        graph.addVertex(res4);

        System.out.println(graph);

        graph.addEdge(res1, res2, Restaurant.calculateDistance(res1, res2));
        graph.addEdge(res2, res4, Restaurant.calculateDistance(res2, res4));
        graph.addEdge(res7, res4, Restaurant.calculateDistance(res7, res4));
        graph.addEdge(res4, res1, Restaurant.calculateDistance(res4, res1));

        System.out.println(graph.drawEdges());

        assertTrue(graph.weightIs(res1, res7) == -1); // no edge exists
        assertTrue(graph.weightIs(res2, res4) != -1); // edge exists
        assertTrue(graph.weightIs(res4, res7) == -1); // no edge exists
        assertTrue(graph.weightIs(res7, res4) != -1); // edge exists

    }

    @Test
    public void getToVertices() throws Exception {
        graph.addVertex(res1);
        graph.addVertex(res2);
        graph.addVertex(res7);
        graph.addVertex(res4);

        System.out.println(graph);

        graph.addEdge(res1, res2, Restaurant.calculateDistance(res1, res2));
        graph.addEdge(res2, res4, Restaurant.calculateDistance(res2, res4));
        graph.addEdge(res7, res4, Restaurant.calculateDistance(res7, res4));
        graph.addEdge(res4, res4, Restaurant.calculateDistance(res4, res1));

        System.out.println(graph.drawEdges());

        BoundedQueue<Restaurant> q;

        q = graph.getToVertices(res1);

        System.out.println(q);
        Restaurant container = q.dequeue();

        // getToVertices will return any unmarked vertices that a vertex is connected to (including itself)
        assertTrue(container.compareTo(res2) == 0 || container.compareTo(res1) == 0);
        container = q.dequeue();
        assertTrue(container.compareTo(res2) == 0 || container.compareTo(res1) == 0);
        assertTrue(q.isEmpty());

        System.out.println(container);

    }

    @Test
    public void clearMarks() throws Exception {
        graph.addVertex(res1);
        graph.addVertex(res2);

        graph.markVertex(res1);
        graph.markVertex(res2);

        assertTrue(graph.isMarked(res1));
        assertTrue(graph.isMarked(res2));

        graph.clearMarks();

        assertFalse(graph.isMarked(res1));
        assertFalse(graph.isMarked(res2));
    }

    @Test
    public void markVertex() throws Exception {
        graph.addVertex(res1);
        graph.addVertex(res2);
        graph.addVertex(res7);
        graph.addVertex(res4);

        graph.markVertex(res1);
        graph.markVertex(res2);
        graph.markVertex(res7);
        graph.markVertex(res4);

        assertTrue(graph.isMarked(res1));
        assertTrue(graph.isMarked(res2));
        assertTrue(graph.isMarked(res7));
        assertTrue(graph.isMarked(res4));

        assertFalse(graph.isMarked(res8));
    }

    @Test
    public void isMarked() throws Exception {
        graph.addVertex(res1);
        graph.addVertex(res2);
        graph.addVertex(res7);

        graph.markVertex(res1);
        graph.markVertex(res2);
        graph.markVertex(res7);
        graph.markVertex(res4);

        assertTrue(graph.isMarked(res1));
        assertTrue(graph.isMarked(res2));
        assertTrue(graph.isMarked(res7));
        assertFalse(graph.isMarked(res4));

        assertFalse(graph.isMarked(res8));
    }

    @Test
    public void getUnmarked() throws Exception {
        graph.addVertex(res1);
        graph.addVertex(res2);
        graph.addVertex(res7);
        graph.addVertex(res3);

        graph.markVertex(res1);
        graph.markVertex(res2);
        graph.markVertex(res7);

        System.out.println(graph.getUnmarked());
    }

    @Test
    public void isEmpty() throws Exception {
        assertTrue(graph.isEmpty());

        graph.addVertex(res1);

        assertFalse(graph.isEmpty());
    }

    @Test
    public void isFull() throws Exception {
        Graph<Restaurant> graph2 = new Graph<>(3);

        assertFalse(graph2.isFull());

        graph2.addVertex(res1);
        assertFalse(graph2.isFull());

        graph2.addVertex(res2);
        assertFalse(graph2.isFull());

        graph2.addVertex(res3);
        assertTrue(graph2.isFull());
    }

}