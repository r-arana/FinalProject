package interfaces;

import exceptions.EmptyListException;

/**
 * Created by REA on 7/2/2017.
 * An interface for a binary search tree.
 * This tree allows traversal inorder, preorder, and postorder.
 * The provided enumerations must be used with the reset and getNext methods to indicate the desired traversal.
 */
public interface BinaryTreeInterface<E> {

    enum TraversalOrder{INORDER, PREORDER, POSTORDER}

    /**
     * Adds passed element to the correct place given the order
     * of the list.
     *
     * @param element
     */
    void add(E element);

    /**Removes the first occurrence of the specified element from the list.
     * Returns the element that was removed, or returns null if the element was not found.
     * @param element
     */
    E remove(E element);

    /**Searches the list to see whether or not an occurrence of the given element
     * already exists.
     *
     * @param element
     * @return  True if the list contains the given element. False otherwise.
     */
    boolean contains(E element);

    /**Passed an argument, it returns an equivalent object if one exists on the list.
     * If not, returns null.
     *
     * @return  matching element, if found
     */
    E get(E element) throws EmptyListException;


    /**
     * Accepts the TraversalOrder enumeration values INORDER, PREORDER, or POSTORDER.
     * Returns the next element in the corresponding list.
     *
     * Pre Condition: Must have used the reset method of the corresponding enumeration
     * prior to using this method.
     *
     * @return  The next element in the list
     */
    E getNext(TraversalOrder order);

    /**Accepts the TraversalOrder enumeration values INORDER, PREORDER, or POSTORDER.
     * Initializes the structure used to store the traversal of the given enumeration.
     * Returns the number of elements in the binary search tree.
     *
     */
    int reset(TraversalOrder order);

    /**Returns the size of our list.
     *
     * @return  size of our list
     */
    int size();

    /**Returns true if the list is empty.  Otherwise, returns false.
     *
     * @return true if the list is empty
     */
    boolean isEmpty();

    /** Returns a string representation of our list.
     *
     * @return
     */
    String toString();




}
