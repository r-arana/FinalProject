package structure;

/**
 * Created by REA on 7/2/2017.
 * A node to store element information and two links.
 */
public class Node<E> {

    private E element;
    private Node<E> leftLink;
    private Node<E> rightLink;


    public Node(E element) {
        this.element = element;
        leftLink = null;
        rightLink = null;
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }

    public Node<E> getLeftLink() {
        return leftLink;
    }

    public void setLeftLink(Node<E> leftLink) {
        this.leftLink = leftLink;
    }

    public Node<E> getRightLink() {
        return rightLink;
    }

    public void setRightLink(Node<E> rightLink) {
        this.rightLink = rightLink;
    }
}
