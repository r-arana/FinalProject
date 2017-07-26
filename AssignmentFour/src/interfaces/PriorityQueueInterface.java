package interfaces;

/**
 * Created by REA on 7/16/2017.
 *
 * Interface for a priority queue.  Highest priority element should always be returned by dequeue.
 * Taken from pages 553-554 of our textbook.
 */
public interface PriorityQueueInterface<E> {

    /** Throws PriorityQueueOverflowException if this priority queue is full;
     *  otherwise, adds element to this priority queue
     */
    void enqueue(E element);

    /** Throws PriorityQueueUnderflowException if the priority queue is empty;
     *  otherwise, remove and return the highest priority element from this priority queue.
     */
    E dequeue();

    /** Returns true if this priority queue is empty;
     *  otherwise, returns false.
     */
    boolean isEmpty();

    /** Returns true if this priority queue is full;
     *  otherwise, returns false.
     */
    boolean isFull();

    /** Returns the number of elements in this priority queue.
     */
    int size();
}
