import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* *****************************************************************************
 *  Name:   Khoa Nam Pham
 *  Date:   01/06/2019
 *  File name:  Deque.java
 *  Description:    Implementation of the Deque data structure which supports
 *                  operations such as add and remove items either from the back
 *                  or the front as well as operations used to traverse over the
 *                  items.
 **************************************************************************** */
public class Deque<Item> implements Iterable<Item> {
    private Node head;
    private Node tail;
    private int count;

    /**
     * Default constructor The Deque is empty therefore head, and tail will be null and count will
     * be 0
     */
    public Deque() {
        head = null;
        tail = null;
        count = 0;
    }

    /**
     * Checks if the Deque is empty or not
     *
     * @return true if the Deque is empty or false otherwise
     */
    public boolean isEmpty() {
        return (head == null) || (tail == null);
    }

    /**
     * Returns the number of items in Deque
     *
     * @return the nubmer of items in Deque
     */
    public int size() {
        return count;
    }

    public void addFirst(Item inItem) {
        if (inItem == null) {
            throw new IllegalArgumentException("Cannot add null item.");
        }

        Node newNode = new Node(inItem);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        }
        else {
            head.prev = newNode;
            newNode.next = head;
            head = newNode;
        }
        count++;
    }

    /**
     * Add new item to the end of the queue
     *
     * @param inItem The item to be added
     */
    public void addLast(Item inItem) {
        if (inItem == null) {
            throw new IllegalArgumentException("Cannot add null item.");
        }

        Node newNode = new Node(inItem);
        // Corner case, if the current queue is empty
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        }
        else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = tail.next;
        }
        count++;
    }

    /**
     * Returns and removes the item at the front of the queue
     *
     * @return item at the front
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("The Deque is empty");
        }

        Item outItem = head.item;
        head = head.next;

        // Corner case, if the queue is empty after removing the item
        if (isEmpty()) {
            tail = null;
        }
        else {
            head.prev = null;
        }
        count--;

        return outItem;
    }

    /**
     * Returns and removes the item at the end of the queue
     *
     * @return item at the end
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("The Deque is empty");
        }

        Item outItem = tail.item;
        tail = tail.prev;

        // Corner case, if the queue is empty after removing the item
        if (isEmpty()) {
            head = null;
        }
        else {
            tail.next = null;
        }
        count--;

        return outItem;
    }

    /**
     * Return an iterator over the items
     *
     * @return Iterator object
     */
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class Node {
        private final Item item;  // Data in the Node
        private Node next;  // Next Node
        private Node prev;  // Previous Node

        /**
         * Alternate Constructor
         *
         * @param inItem The data needs to be contained in the Node
         */
        public Node(Item inItem) {
            if (inItem == null) {
                throw new IllegalArgumentException("Null Item for Node");
            }

            item = inItem;
            next = null;
            prev = null;
        }
    }

    private class ListIterator implements Iterator<Item> {
        private Node current;  // Current Node

        /**
         * Default constructor. Set the current Node to the beginning of the Deque
         */
        public ListIterator() {
            current = head;
        }

        /**
         * Checks if there is a Node next to the current Node.
         *
         * @return true if there is a next Node otherwise false
         */
        public boolean hasNext() {
            return (current != null);
        }

        /**
         * If there is a next Node, returns the current item and move to the next item
         *
         * @return the current item
         */
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no next item.");
            }

            Item outItem = current.item;
            current = current.next;

            return outItem;
        }

        // The remove operation is not supported
        public void remove() {
            throw new UnsupportedOperationException("This opperation is not supported.");
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        for (int i = 0; i < 10; i++) {
            deque.addFirst(1);
        }
        int count = 0;
        Iterator<Integer> iterator = deque.iterator();
        for (int item : deque) {
            count++;
        }
        StdOut.print(count);
    }
}
