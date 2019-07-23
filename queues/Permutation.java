/* *****************************************************************************
 *  Name:       Khoa Nam Pham
 *  File name:  Permutation.java
 *  Date:       04/06/2019
 *  Description:    Client code to test RandomizedQueue, reads in a sequence of
 *                  text and a number, prints a permatution of the text strings.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Permutation {
    public static void main(String[] args) {
        int num = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String inStr = StdIn.readString();
            queue.enqueue(inStr);
        }

        Iterator<String> iterator = queue.iterator();
        for (int i = 0; i < num; i++) {
            StdOut.println(iterator.next());
        }
    }
}
