package interfaces;

import structures.BoundedQueue;

/**
 * Created by REA on 7/16/2017.
 * Interface for a weighted graph.
 *
 */
public interface GraphInterface<E> {

    // Pages 590 - 591

    /** Adds the passed vertex to the graph.
     *  Does not allow duplicate elements.
     *  The passed vertex cannot be null.
     *
     * @param vertex
     */
    void addVertex(E vertex);

    /** Returns true if the graph contains the passed vertex;
     *  otherwise, returns false.
     *
     *  @param vertex
     *  @return true if the graph contains the passed vertex
     */
    boolean hasVertex(E vertex);

    /** Adds an edge with the specified weight from fromVertex to toVertex
     *
     *  @param fromVertex
     *  @param toVertex
     *  @param weight
     */
    void addEdge(E fromVertex, E toVertex, double weight);

    /** If edge from fromVertex to toVertex exists, returns the weight of edge;
     *  otherwise, returns a special "null-edge" value.
     *
     *  @param fromVertex
     *  @param toVertex
     *  @return double value of weight
     */
     double weightIs(E fromVertex, E toVertex);

    /** Returns a queue of the vertices that are adjacent from vertex.
     *
     */
    BoundedQueue<E> getToVertices(E vertex);

    /** Unmarks all vertices.
     */
    void clearMarks();

    /** Marks the vertex.
     * This is used to indicate that the vertex has been visited.
     *
     *  @param vertex
     */
    void markVertex(E vertex);

    /** Returns true if the vertex is marked;
     *  otherwise, returns false.
     *
     *  @param vertex
     *  @return true if the vertex is marked
     */
    boolean isMarked(E vertex);

    /** Returns a single unmarked vertex, if any exist;
     *  otherwise, returns null.
     *
     *  @return unmarked vertex
     */
    E getUnmarked();

    /** Returns true if this graph is empty;
     *  otherwise, returns false.
     *
     *  @return true if this graph is empty
     */
    boolean isEmpty();

    /** Returns true if this graph is full;
     *  otherwise, returns false.
     *
     *  @return true if this graph is full
     */
    boolean isFull();

}
