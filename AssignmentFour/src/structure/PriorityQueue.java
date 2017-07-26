package structure;

import exceptions.PriorityQueueOverflowException;
import exceptions.PriorityQueueUnderflowException;
import interfaces.PriorityQueueInterface;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *  Created by REA on 7/16/2017.
 *
 *  This is creating a minimum heap. A lot of the logic is in chapter 9 of the book, but modifications were made
 *  to create a minimum heap instead of a maximum heap.
 */
public class PriorityQueue<E extends Comparable<E>> implements PriorityQueueInterface<E> {

    private ArrayList<E> elementArray;
    private Comparator<E> comparator;
    private int lastIndex;
    private int maxIndex;

    public PriorityQueue(int maxSize){
        elementArray = new ArrayList<>(maxSize);
        lastIndex = -1;
        maxIndex = maxSize - 1;

        // Inner class uses natural order for our default comparator
        comparator = new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return o1.compareTo(o2);
            }
        };
    }

    // We can accept a comparator for the given object with whatever order the user wants
    public PriorityQueue(int maxSize, Comparator<E> comparator){
        elementArray = new ArrayList<>(maxSize);
        lastIndex = -1;
        maxIndex = maxSize - 1;

        this.comparator = comparator;
    }

    /**
     * Throws PriorityQueueOverflowException if this priority queue is full;
     * otherwise, adds element to this priority queue.
     *
     * @param element
     */
    @Override
    public void enqueue(E element) {
        if (isFull()){
            throw new PriorityQueueOverflowException("Tried to enqueue to a full priority queue.");
        }
        else{
            lastIndex++;
            elementArray.add(lastIndex, element);
            reheapUp(element);
        }
    }

    private void reheapUp(E element){
        int hole = lastIndex;
        // We keep iterating while our index is greater than 0 (our root) and our element is less than its
        // parent element.
        // Parent = (index - 1) / 2
        while (hole > 0 && (comparator.compare(element, elementArray.get((hole - 1) / 2)) < 0)){
            // Our element is less than our parent element, so we move our parent element into our hole
            elementArray.set(hole, elementArray.get((hole - 1) / 2));
            // Our hole now moves to the parent we just removed
            hole = (hole - 1) / 2;
        }

        elementArray.set(hole, element);
    }

    /**
     * Throws PriorityQueueUnderflowException if the priority queue is empty;
     * otherwise, remove and return the highest priority element from this priority queue.
     */
    @Override
    public E dequeue() {
        E container = null;
        E toMove;

        if (isEmpty()){
            throw new PriorityQueueUnderflowException("Tried to dequeue an empty priority queue.");
        }
        else{
            // Instead of removing the root right away, we remove the last index to avoid shifting the list
            container = elementArray.get(0);
            toMove = elementArray.remove(lastIndex);
            lastIndex--;
            if (lastIndex != -1){
                reheapDown(toMove);
            }
        }
        return container;
    }

    private void reheapDown(E element){
        int hole = 0; // start at root
        int nextHole = newHole(hole, element);

        while(nextHole != hole){
            // Move our child element into our hole
            elementArray.set(hole, elementArray.get(nextHole));
            // Move our hole to the next hole
            hole = nextHole;
            // Search for the next hole
            nextHole = newHole(hole, element);
        }
        elementArray.set(hole, element);
    }

    private int newHole(int hole, E element){
        int left = (hole * 2) + 1;
        int right = (hole *2) + 2;

        // No children
        if (left > lastIndex){
            return hole;
        }
        // No right child
        else if (left == lastIndex){
            // If our element is greater than the child then we return the child
            if (comparator.compare(element, elementArray.get(left)) > 0){
                return left;
            }
            else{
                return hole;
            }
        }
        // Two children
        else{
            // If left is less than right
            if (comparator.compare(elementArray.get(left), elementArray.get(right)) < 0){
                // And if our element is greater than our left element
                if (comparator.compare(element, elementArray.get(left)) > 0){
                    return left;
                }
                else{
                    return hole;
                }
            }
            // Else if right is less than or equal to left
            // if (comparator.compare(elementArray.get(left), elementArray.get(right)) >= 0)
            else {
                // And if our element is greater than our right element
                if (comparator.compare(element, elementArray.get(right)) > 0){
                    return right;
                }
                else{
                    return hole;
                }
            }
        }
    }

    /**
     * Returns true if this priority queue is empty;
     * otherwise, returns false.
     */
    @Override
    public boolean isEmpty() {
        return lastIndex == -1;
    }

    /**
     * Returns true if this priority queue is full;
     * otherwise, returns false.
     */
    @Override
    public boolean isFull() {
        return lastIndex == maxIndex;
    }

    /**
     * Returns the number of elements in this priority queue.
     */
    @Override
    public int size() {
        return lastIndex + 1;
    }

    @Override
    public String toString(){

        return elementArray.toString();
    }
}
