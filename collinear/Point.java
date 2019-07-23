/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private static final double HORIZONTAL_LINE = 0.0;
    private static final double DEGENERATE_LINE = Double.NEGATIVE_INFINITY;
    private static final double VERTICAL_LINE = Double.POSITIVE_INFINITY;

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point. Formally, if the two points are
     * (x0, y0) and (x1, y1), then the slope is (y1 - y0) / (x1 - x0). For completeness, the slope
     * is defined to be +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical; and Double.NEGATIVE_INFINITY if
     * (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        double slope;
        // If we are comparing to the same point
        if (this.x == that.x && this.y == that.y) {
            slope = DEGENERATE_LINE;
        }
        // If the two points form a vertical line
        else if (this.x == that.x) {
            slope = VERTICAL_LINE;
        }
        // If the two points form a horizontal line
        else if (this.y == that.y) {
            slope = HORIZONTAL_LINE;
        }
        // Normal case
        else {
            slope = (double) (that.y - this.y) / (double) (that.x - this.x);
        }

        return slope;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate. Formally, the invoking
     * point (x0, y0) is less than the argument point (x1, y1) if and only if either y0 < y1 or if
     * y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument point (x0 = x1 and y0 =
     * y1); a negative integer if this point is less than the argument point; and a positive integer
     * if this point is greater than the argument point
     */
    public int compareTo(Point that) {
        int result = 0;
        if (this.y < that.y) {
            result = -1;
        }
        else if (this.y > that.y) {
            result = 1;
        }
        else {
            if (this.x < that.x) {
                result = -1;
            }
            else if (this.x > that.x) {
                result = 1;
            }
        }

        return result;
    }

    /**
     * Compares two points by the slope they make with this point. The slope is defined as in the
     * slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new ByOrder(this);
    }

    private class ByOrder implements Comparator<Point> {

        // Tolerance used to compare two real numbers
        private static final double TOL = 0.0001;

        // The point used to calculate the slope to the two points we want to
        // compare with each other
        private final Point current;

        public ByOrder(Point inPoint) {
            current = inPoint;
        }

        /**
         * Compares Point v and Point w base on the slope they have with the current point
         *
         * @param v The first point
         * @param w The second point
         * @return The value <tt>0</tt> if they create the same slope to the current point. The
         * value <tt>-1</tt> if the first point has smaller slope than the second point, and
         * <tt>1</tt> if the first point has the larger slope than the second point.
         */
        public int compare(Point v, Point w) {
            double slopeV = current.slopeTo(v);
            double slopeW = current.slopeTo(w);
            int result = Double.compare(slopeV, slopeW);
            return result;
        }
    }

    /**
     * Returns a string representation of this point. This method is provide for debugging; your
     * program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        int[] xV = { 10, 6, 5, 5 };
        int[] yV = { 5, 6, 10, 5 };
        int[] xW = { 6, 5, 5, 10 };
        int[] yW = { 6, 10, 5, 5 };
        int[] expected = { -1, -1, 1, 0, 0, -1, 1, 1, 1, 0, 1, 1, -1, -1, 0, -1 };
        String[] msgs = { "0", "1", "Inf", "-Inf" };
        Point current = new Point(5, 5);
        Comparator<Point> compare = current.slopeOrder();

        for (int i = 0; i < xV.length; i++) {
            Point v = new Point(xV[i], yV[i]);
            for (int j = 0; j < xW.length; j++) {
                Point w = new Point(xW[j], yW[j]);
                int actual = compare.compare(v, w);
                StdOut.print(actual + " ");
                assert actual == expected[i * 4 + j] : msgs[i];
            }
        }
    }
}
