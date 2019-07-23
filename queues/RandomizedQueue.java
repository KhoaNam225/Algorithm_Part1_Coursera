/* *****************************************************************************
 *  Name:       Khoa Nam Pham
 *  File name:  RandomizedQueue.java
 *  Date:       04/06/2019
 *  Description:    A Queue in which the items are returned in random order
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;  // The array to store the items
    private int count;     // The number of items stored so far

    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        count = 0;
    }

    /**
     * Checks if the queue is empty
     *
     * @return true if empty otherwise false
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns the number of items in the queue
     *
     * @return The number of items
     */
    public int size() {
        return count;
    }

    /**
     * Add a new item into the queue
     *
     * @param inItem The item to be added
     */
    public void enqueue(Item inItem) {
        if (inItem == null) {
            throw new IllegalArgumentException("Cannot add null value into the queue.");
        }

        // If the queue is full, double its size
        if (count == items.length) {
            resize(count * 2);
        }

        items[count] = inItem;
        count++;
    }

    /**
     * Returns and removes a random item from the queue
     *
     * @return A random item
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("The queue is empty.");
        }

        // Generate a random index an return the item at that index then
        // Replace the returned item by the last item of the queue
        int randIndex = StdRandom.uniform(count);
        Item outItem = items[randIndex];
        count--;
        items[randIndex] = items[count];
        items[count] = null;    // Avoid loitering

        // If there is too much empty spaces left in the queue
        // Resize it to half the size
        if (count == items.length / 4 && !isEmpty()) {
            resize(items.length / 2);
        }

        return outItem;
    }

    /**
     * Returns a random item from the queue but not remove it
     *
     * @return A random item
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("The queue is empty.");
        }

        // Generate a random index and return the item at that index
        int randIndex = StdRandom.uniform(count);
        Item outItem = items[randIndex];

        return outItem;
    }

    public Iterator<Item> iterator() {
        return new ReversedArrayIterator();
    }

    /**
     * Resizes the array holding items to achieve memory efficiency
     *
     * @param newSize The new size of the array
     */
    private void resize(int newSize) {
        Item[] temp = (Item[]) new Object[newSize];
        // Copy all items to the new array
        for (int i = 0; i < count; i++) {
            temp[i] = items[i];
        }
        items = temp;
    }

    /**
     * The iterator over all the items in the queue
     */
    private class ReversedArrayIterator implements Iterator<Item> {
        private int current;
        private final Item[] randItems;

        public ReversedArrayIterator() {
            current = count;
            randItems = (Item[]) new Object[count];
            for (int i = 0; i < count; i++) {
                randItems[i] = items[i];
            }
            StdRandom.shuffle(randItems);
        }

        /**
         * Checks if there is another item next to the current item
         *
         * @return true if there is an item next to current item or false otherwise
         */
        public boolean hasNext() {
            return current > 0;
        }

        /**
         * Returns the current item and move the current position to the next item
         *
         * @return The current item
         */
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no next item.");
            }

            current--;
            Item outItem = randItems[current];

            return outItem;
        }

        // The remove operation is not supported
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove items from queue.");
        }
    }
}
