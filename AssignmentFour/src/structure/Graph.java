package structure;

import interfaces.GraphInterface;
import interfaces.Queue;
import structures.BoundedQueue;
import structures.BoundedStack;

/**
 * Created by REA on 7/17/2017.
 * An array based implementation of the weighted graph data structure.
 * The weighted edges of this graph are stored as double values.
 */
public class Graph<E extends Comparable<E>> implements GraphInterface<E>{

    private static final int NULL_EDGE = -1;
    private static final int DEFAULT_SIZE = 50;
    private int numberOfVertices;
    private int maxVertices;

    private boolean[] mark;
    private E[] vertices;
    private double[][] edge;

    public Graph(){
        numberOfVertices = 0;
        maxVertices = DEFAULT_SIZE;
        mark = new boolean[DEFAULT_SIZE];
        vertices = (E[]) new Comparable[DEFAULT_SIZE];
        edge = new double[DEFAULT_SIZE][DEFAULT_SIZE];
    }

    public Graph(int maxSize){
        numberOfVertices = 0;
        maxVertices = maxSize;
        mark = new boolean[maxSize];
        vertices = (E[]) new Comparable[maxSize];
        edge = new double[maxSize][maxSize];
    }

    /** Adds the passed vertex to the graph.
     *  Does not allow duplicate elements.
     *  The passed vertex cannot be null.
     *
     * @param vertex
     */
    @Override
    public void addVertex(E vertex) {
        // If the vertex is null, return
        if (vertex == null){
            return;
        }
        // If the vertex is already in our graph, return
        else if (hasVertex(vertex)){
            return;
        }
        else{
            vertices[numberOfVertices] = vertex;
            for (int index = 0; index < numberOfVertices; index++){
                edge[numberOfVertices][index] = NULL_EDGE;
                edge[index][numberOfVertices] = NULL_EDGE;
            }
            numberOfVertices++;
        }
    }

    /**
     * Returns true if the graph contains the passed vertex;
     * otherwise, returns false.
     *
     * @param vertex
     * @return true if the graph contains the passed vertex
     */
    @Override
    public boolean hasVertex(E vertex) {
        boolean found = false;

        for (int i = 0; i < numberOfVertices; i++){
            // If our vertex is equal to any of the vertices
            if (vertex.compareTo(vertices[i]) == 0){
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * Adds an edge with the specified weight from fromVertex to toVertex
     *
     * @param fromVertex
     * @param toVertex
     * @param weight
     */
    @Override
    public void addEdge(E fromVertex, E toVertex, double weight) throws IndexOutOfBoundsException {
        int row = indexIs(fromVertex);
        int column = indexIs(toVertex);

        if (row != -1 && column != -1) {
            edge[row][column] = weight;
        }
        else{
            throw new IndexOutOfBoundsException("Passed addEdge a vertex that is not on the graph.");
        }
    }

    /**
     * If edge from fromVertex to toVertex exists, returns the weight of edge;
     * otherwise, returns a special "null-edge" value.
     *
     * The null-edge value will be -1.
     *
     * @param fromVertex
     * @param toVertex
     * @return double value of weight
     */
    @Override
    public double weightIs(E fromVertex, E toVertex) throws IndexOutOfBoundsException{
        int row = indexIs(fromVertex);
        int column = indexIs(toVertex);

        if (row != -1 && column != -1) {
            return edge[indexIs(fromVertex)][indexIs(toVertex)];
        }
        else{
            throw new IndexOutOfBoundsException("Passed weightIs a vertex that is not on the graph.");
        }
    }

    /**
     * Returns a queue of the vertices that are adjacent from vertex.
     *
     * @param vertex
     */
    @Override
    public BoundedQueue<E> getToVertices(E vertex) {
        BoundedQueue<E> q = new BoundedQueue<E>(numberOfVertices);

        int vertexRow = indexIs(vertex);

        for (int column = 0; column < numberOfVertices; column++){
            if (edge[vertexRow][column] != NULL_EDGE){
                q.enqueue(vertices[column]);
            }
        }

        return q;
    }

    // Uses depth-first search with a stack to look for a path between the startVertex to the endVertex
    private boolean pathExistsDF(E startVertex, E endVertex){
        BoundedStack<E> stack = new BoundedStack<>(numberOfVertices);
        boolean found = false;
        E currentVertex;

        clearMarks();
        markVertex(startVertex);
        stack.push(startVertex);

        do{
            currentVertex = stack.top();
            stack.pop();

            if (currentVertex.compareTo(endVertex) == 0){
                found = true;
            }
            else{
                BoundedQueue<E> adjacentVertices = getToVertices(currentVertex);
                E adjacentVertex;

                while (!adjacentVertices.isEmpty()){
                    adjacentVertex = adjacentVertices.dequeue();

                    // If our adjacent vertex is not marked
                    if (!isMarked(adjacentVertex)) {
                        // Mark our adjacent vertex
                        markVertex(adjacentVertex);
                        // Push the adjacent vertex onto our stack
                        stack.push(adjacentVertex);
                    }
                }
            }
        } while (!stack.isEmpty() && !found);

        return found;
    }

    // Breadth-first search using a stack
    private boolean pathExistsBF(E startVertex, E endVertex){
        BoundedQueue<E> queue = new BoundedQueue<E>(numberOfVertices);
        boolean found = false;
        E currentVertex;

        clearMarks();
        markVertex(startVertex);
        queue.enqueue(startVertex);

        do{
            currentVertex = queue.dequeue();

            if (currentVertex.compareTo(endVertex) == 0){
                found = true;
            }
            else{
                BoundedQueue<E> adjacentVertices = getToVertices(currentVertex);
                E adjacentVertex;

                while (!adjacentVertices.isEmpty()){
                    adjacentVertex = adjacentVertices.dequeue();

                    // If not marked
                    if (!isMarked(adjacentVertex)){
                        // Mark the vertex
                        markVertex(adjacentVertex);
                        // Enqueue the vertex
                        queue.enqueue(adjacentVertex);
                    }
                }
            }
        } while (!queue.isEmpty() && !found);

        return found;
    }

    /** Returns the index of the vertex, if found.
     *  If the vertex is not found, returns -1.
     * @param vertex
     * @return
     */
    private int indexIs(E vertex){
        boolean found = false;
        int index = 0;

        for (int i = 0; i < numberOfVertices; i++) {
            // If our vertex is equal to any of the vertices
            if (vertex.compareTo(vertices[i]) == 0) {
                found = true;
                index = i;
                break;
            }
        }
        if (!found){
            index = -1;
        }

        return index;
    }

    /**
     * Unmarks all vertices.
     */
    @Override
    public void clearMarks() {
        for (int i = 0; i < numberOfVertices; i++){
            mark[i] = false;
        }
    }

    /**
     * Marks the vertex.
     * This is used to indicate that the vertex has been visited.
     *
     * @param vertex
     */
    @Override
    public void markVertex(E vertex) {
        int vertexIndex = indexIs(vertex);

        if (vertexIndex != -1){
            mark[indexIs(vertex)] = true;
        }
    }

    /**
     * Returns true if the vertex is marked;
     * otherwise, returns false.
     *
     * @param vertex
     * @return true if the vertex is marked
     */
    @Override
    public boolean isMarked(E vertex) {
        int vertexIndex = indexIs(vertex);

        if (vertexIndex != -1){
            return mark[vertexIndex];
        }
        return false;
    }

    /**
     * Returns a single unmarked vertex, if any exist;
     * otherwise, returns null.
     *
     * @return unmarked vertex
     */
    @Override
    public E getUnmarked() {
        for (int i = 0; i < numberOfVertices; i++){
            if (mark[i] == false) {
                return vertices[i];
            }
        }
        return null;
    }

    /**
     * Returns true if this graph is empty;
     * otherwise, returns false.
     *
     * @return true if this graph is empty
     */
    @Override
    public boolean isEmpty() {
        return numberOfVertices == 0;
    }

    /**
     * Returns true if this graph is full;
     * otherwise, returns false.
     *
     * @return true if this graph is full
     */
    @Override
    public boolean isFull() {
        return numberOfVertices == maxVertices;
    }

    /** Returns the number of vertices in the graph.
     *
     * @return number of vertices
     */
    public int getNumberOfVertices(){
        return numberOfVertices;
    }

    public String toString(){
        String fullString = "";

        if (isEmpty()){
            fullString = "Tried to print an empty graph.";
        }
        else{
            for (int i = 0; i < numberOfVertices; i++){
                fullString += vertices[i];
            }
        }

        return fullString;
    }

    public String drawEdges(){
        String fullString = "";

        if (isEmpty()){
            fullString = "Tried to print the edges of an empty graph.";
        }
        else{
            for (int row = 0; row < numberOfVertices; row++){
                for (int column = 0; column < numberOfVertices; column++){
                    fullString += edge[row][column] + "  ";
                }
                fullString += "\n";
            }
        }

        return fullString;
    }
}
