package structure;

import exceptions.EmptyListException;
import interfaces.BinaryTreeInterface;
import structures.BoundedQueue;

import java.util.Comparator;

import static interfaces.BinaryTreeInterface.TraversalOrder.INORDER;
import static interfaces.BinaryTreeInterface.TraversalOrder.POSTORDER;
import static interfaces.BinaryTreeInterface.TraversalOrder.PREORDER;


/**
 * Created by REA on 7/2/2017.
 * Provides a binary search tree.  The balanceTree method is provided to help maintain log n search efficiency.
 */
public class BinarySearchTree<E extends Comparable<E>> implements BinaryTreeInterface<E>{

    private Node<E> tree = null;
    private Node<E> tempTree = null;
    private boolean found = false;
    private int numberOfElements = 0;

    private BoundedQueue<E> inOrderQueue;
    private BoundedQueue<E> preOrderQueue;
    private BoundedQueue<E> postOrderQueue;

    private E[] sortedArray;
    //private Iterator<E> treeIterator;

    /**
     * Adds passed element to the correct place given the order
     * of the list.
     *
     * @param element
     */
    @Override
    public void add(E element) {
        tree = recursiveAdd(element, tree);
        numberOfElements++;
    }

    // Our recursive add returns the root of our new tree.
    private Node<E> recursiveAdd(E element, Node<E> root){
        if (root == null){
            root = new Node<E>(element);
        }
        else if (element.compareTo(root.getElement()) <= 0){
            root.setLeftLink(recursiveAdd(element, root.getLeftLink()));
        }
        // if (element.compareTo(root.getElement()) > 0)
        else {
            root.setRightLink(recursiveAdd(element, root.getRightLink()));
        }
        return root;
    }

    /**
     * Removes the first occurrence of the specified element from the list.
     * Returns the element that was removed, or returns null if the element was not found.
     *
     * @param element
     */
    @Override
    public E remove(E element) throws EmptyListException {
        if (isEmpty()){
            throw new EmptyListException("Attempted to use remove() on an empty list.");
        }
        else {
            tree = recursiveRemove(element, tree);
        }

        if (found){
            numberOfElements--;
            return element;
        }
        return null;
    }

    /* Our recursive remove starts in a similar way to our add method.
       Once we reach the appropriate node, we rely on other methods to actually remove the node or replace the
       information within the node.

       There are three cases we need to handle in removing from a binary tree.
       Case 1: Remove a leaf
       - This can be taken care of by simply setting the node to null
       Case 2: Remove a root with one child.
       - We return the link to the child node in order to "hop over" the node we want to remove.
       Case 3: Remove a root with two children.
       - This is the trickiest situation.  We'll use the book's method of replacing the root's element with
       its predecessor's element and then removing the predecessor node instead.

     */
    private Node<E> recursiveRemove(E element, Node<E> node){
        if (node == null){
            // This method should not reach a null node unless the element doesn't exist in our tree.
            found = false;
        }
        else if (element.compareTo(node.getElement()) < 0){
            node.setLeftLink(recursiveRemove(element, node.getLeftLink()));
        }
        else if (element.compareTo(node.getElement()) > 0){
            node.setRightLink(recursiveRemove(element, node.getRightLink()));
        }
        else{
            node = removeNode(node);
            found = true;
        }
        return node;
    }

    // This will remove the passed element from the tree
    private Node<E> removeNode(Node<E> node){
        E element;
        // If our node only has one child, return the link for the child.
        // This also takes care of a leaf.
        if (node.getLeftLink() == null){
            return node.getRightLink();
        }
        else if (node.getRightLink() == null){
            return node.getLeftLink();
        }
        // If the above isn't true then we are dealing with a root with two children
        // This situation is handled by replacing the target node with its predecessor in order
        // to maintain our binary tree logic.
        else{
            // The node's predecessor is the highest value in its left subtree.
            element = getPredecessor(node.getLeftLink());
            node.setElement(element);
            // We can remove the predecessor after grabbing the information.
            node.setLeftLink(recursiveRemove(element, node.getLeftLink()));
            return node;
        }
    }
    // We are passing the left subtree of the node.  We need the closest/highest element in this subtree.
    // Because we're already at the left subtree we can simply iterate down
    // the linked list as far to the right as possible to find the highest element.
    private E getPredecessor(Node<E> subtree){
        Node<E> temp = subtree;

        while (temp.getRightLink() != null){
            temp = temp.getRightLink();
        }
        return temp.getElement();
    }

    /**
     * Searches the list to see whether or not an occurrence of the given element
     * already exists.
     *
     * @param element
     * @return True if the list contains the given element.
     */
    @Override
    public boolean contains(E element) {
        return recursiveContains(element, tree);
    }

    private boolean recursiveContains(E element, Node<E> root){
        // First base case returns false if we reach a null root before we find our passed element.
        if (root == null){
            return false;
        }
        // If our element is less than our current root, go to the left.
        else if (element.compareTo(root.getElement()) < 0){
            return recursiveContains(element, root.getLeftLink());
        }
        // If our element is greater than our current root, go to the right.
        else if (element.compareTo(root.getElement()) > 0){
            return recursiveContains(element, root.getRightLink());
        }
        // If we don't meet any of the above conditions then we've managed to find the element we wanted.
        else{
            return true;
        }
    }

    /**
     * Passed an argument, it returns an equivalent object if one exists on the list.
     * If not, returns null.
     *
     * @param element
     * @return matching element, if found
     */
    @Override
    public E get(E element) throws EmptyListException {

        if (isEmpty()){
            throw new EmptyListException("Attempted to use get() on an empty list.");
        }
        else{
            return recursiveGet(element, tree);
        }
    }

    private E recursiveGet(E element, Node<E> root){

        // First base case returns false if we reach a null root before we find our passed element.
        if (root == null){
            return null;
        }
        // If our element is less than our current root, go to the left.
        else if (element.compareTo(root.getElement()) < 0){
            return recursiveGet(element, root.getLeftLink());
        }
        // If our element is greater than our current root, go to the right.
        else if (element.compareTo(root.getElement()) > 0){
            return recursiveGet(element, root.getRightLink());
        }
        // If we don't meet any of the above conditions then we've managed to find the element we wanted.
        else{
            return root.getElement();
        }
    }

    /**
     * Accepts the TraversalOrder enumeration values INORDER, PREORDER, or POSTORDER.
     * Returns the next element in the corresponding list.
     *
     * Pre Condition: Must have used the reset method of the corresponding enumeration
     * prior to using this method.
     *
     * @return  The next element in the list
     */
    @Override
    public E getNext(TraversalOrder order) {
        /*
        // If our iterator is empty, return null.
        if (!treeIterator.hasNext()){
            return null;
        }
        // Else, return the next element
        else{
            return treeIterator.next();
        }
        */
        switch (order){
            case INORDER:
                if (inOrderQueue.isEmpty()){
                    throw new IndexOutOfBoundsException("You attempted to use getNext when there were no more elements.");
                }
                else {
                    return inOrderQueue.dequeue();
                }

            case PREORDER:
                if (preOrderQueue.isEmpty()){
                    throw new IndexOutOfBoundsException("You attempted to use getNext when there were no more elements.");
                }
                else {
                    return preOrderQueue.dequeue();
                }
            case POSTORDER:
                if (postOrderQueue.isEmpty()){
                    throw new IndexOutOfBoundsException("You attempted to use getNext when there were no more elements.");
                }
                else {
                    return postOrderQueue.dequeue();
                }
        }

        return null;
    }

    /**
     * Accepts the TraversalOrder enumeration values INORDER, PREORDER, or POSTORDER.
     * Initializes the structure used to store the traversal of the given enumeration.
     * Returns the number of elements in the binary search tree.
     */
    @Override
    public int reset(TraversalOrder order) {

        switch (order){
            case INORDER:
                inOrderQueue = new BoundedQueue<>(size());
                inOrder(tree);
                break;

            case PREORDER:
                preOrderQueue = new BoundedQueue<>(size());
                preOrder(tree);
                break;

            case POSTORDER:
                postOrderQueue = new BoundedQueue<>(size());
                postOrder(tree);
                break;
        }

        return size();
    }

    /**
     * Returns the size of our list.
     *
     * @return size of our list
     */
    @Override
    public int size() {
        return numberOfElements;
    }

    /**
     * Returns true if the list is empty.  Otherwise, returns false.
     *
     * @return true if the list is empty
     */
    @Override
    public boolean isEmpty() {
        return (tree == null);
    }

    @Override
    public String toString(){
        reset(INORDER);
        reset(PREORDER);
        reset(POSTORDER);

        String fullStringInOrder = "In Order:\n";
        String fullStringPreOrder = "Pre Order:\n";
        String fullStringPostOrder = "Post Order\n";

        if (inOrderQueue.isEmpty()){
            return "Tried to print an empty binary search tree.";
        }
        else{
            while (!inOrderQueue.isEmpty()){
                fullStringInOrder += inOrderQueue.dequeue();
                fullStringPreOrder += preOrderQueue.dequeue();
                fullStringPostOrder += postOrderQueue.dequeue();
            }
        }
        return fullStringInOrder + "\n" + fullStringPreOrder + "\n" + fullStringPostOrder;
    }

    private void inOrder(Node<E> node){
        if (node != null){
            inOrder(node.getLeftLink());
            inOrderQueue.enqueue(node.getElement());
            inOrder(node.getRightLink());
        }
    }

    private void preOrder(Node<E> node){
        if (node != null){
            preOrderQueue.enqueue(node.getElement());
            preOrder(node.getLeftLink());
            preOrder(node.getRightLink());
        }
    }

    private void postOrder(Node<E> node){
        if (node != null){
            postOrder(node.getLeftLink());
            postOrder(node.getRightLink());
            postOrderQueue.enqueue(node.getElement());
        }
    }

    // Balance our binary search tree to take advantage of fast search times.
    public void balanceTree(){
        reset(INORDER);
        // Look up the interaction between bounded generics and their initialization
        // https://stackoverflow.com/questions/34827626/cannot-be-cast-to-ljava-lang-comparable
        // https://stackoverflow.com/questions/18292282/java-lang-classcastexception-ljava-lang-comparable-cannot-be-cast-to
        sortedArray = (E[]) new Comparable[size()];

        for (int i = 0; i < sortedArray.length; i++){
            sortedArray[i] = inOrderQueue.dequeue();
        }

        recBalanceTree(0, sortedArray.length - 1);
        tree = tempTree;
    }


    private void recBalanceTree(int lowerBound, int upperBound){
        if(lowerBound + 1 == upperBound){
            tempTree = recursiveAdd(sortedArray[lowerBound], tempTree);
            tempTree = recursiveAdd(sortedArray[upperBound], tempTree);
        }
        else if(lowerBound == upperBound){
            tempTree = recursiveAdd(sortedArray[lowerBound], tempTree);
        }
        else{
            int midpoint = (lowerBound + upperBound) / 2;
            tempTree = recursiveAdd(sortedArray[midpoint], tempTree);
            recBalanceTree(lowerBound, midpoint - 1);
            recBalanceTree(midpoint + 1, upperBound);
        }
    }


}
