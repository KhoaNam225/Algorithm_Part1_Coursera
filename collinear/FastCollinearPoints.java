/* *****************************************************************************
 *  Name:   Khoa Nam Pham
 *  File Name:  FastCollinearPoints.java
 *  Date:       17/06/2019
 *  Description:    Finds all the line segments that contain 4 or more points
 *                  using merge sort algorithm. This algorithm can deal with the
 *                  case when the line has 4 or more points and duplicated points (lines intersect).
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class FastCollinearPoints {

    // A Stack used to store all the line segments
    private Stack<LineSegment> lineSegments;

    /**
     * Finds all the line segments that contain 4 or more points from an array of Points
     *
     * @param points The array of points from which we find the lines
     */
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points[] cannot be null.");
        }

        lineSegments = new Stack<>();
        int length = points.length;
        Merge.sort(points);
        Point[] copied = new Point[length];
        // Create another copied array
        for (int i = 0; i < length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("One of the points is null.");
            }

            if (i < length - 1 && points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicated points.");
            }

            copied[i] = points[i];
        }

        // For each point in the array, we sort all the other points by slope order to
        // that point. All the points on the same line, will have the same slope order
        // and therefore stay in group with each other. This is very usefule to find
        // just the start and end point of the line segment. We just need to scan through a group
        // to find the end of the group and take that as ending point, the starting point is the one
        // we used to sort the array by slope order.
        for (int i = 0; i < length; i++) {
            Point pivot = copied[i];
            sortBySlopeOrder(pivot, points); // Sort the array
            findSegments(0, points,
                         lineSegments); // Find all the line segments satisfying the requirement
        }
    }

    /**
     * Returns the number of line segments created from the set of given points
     *
     * @return Number of Line Segments
     */
    public int numberOfSegments() {
        return lineSegments.size();
    }

    /**
     * Returns all the line segments found
     *
     * @return An array containing all the line segments.
     */
    public LineSegment[] segments() {
        LineSegment[] lines = new LineSegment[lineSegments.size()];
        int i = 0;
        for (LineSegment line : lineSegments) {
            lines[i] = line;
            i++;
        }

        return lines;
    }

    /**
     * Sorts all the points in the given array by slope order. The slope is calculated with respect
     * to points[indice]
     *
     * @param pivot  The pivot point from which we calculate the slope to every other points in the
     *               array
     * @param points The array of Points to be sorted
     */
    private void sortBySlopeOrder(Point pivot, Point[] points) {
        Point[] aux = new Point[points.length];
        sortBySlopeOrder(points, aux, 0, points.length - 1, pivot);
    }

    /**
     * Sorts all the points in the given array (from low to high) in slope order
     *
     * @param points     The given array to be sorted
     * @param aux        The auxiliary array used in merge sort algorithm
     * @param low        The beginning position in the array from which the sorting is performed
     * @param high       The ending position in the array to which the sorting ends
     * @param pivotPoint The point used as a pivot point from which we calculate the slope
     */
    private void sortBySlopeOrder(Point[] points, Point[] aux, int low, int high,
                                  Point pivotPoint) {
        if (!(high <= low)) {
            Comparator<Point> comparator = pivotPoint.slopeOrder();
            int mid = low + (high - low) / 2;

            sortBySlopeOrder(points, aux, low, mid, pivotPoint);
            sortBySlopeOrder(points, aux, mid + 1, high, pivotPoint);
            merge(points, aux, low, mid, high, comparator);
        }
    }

    /**
     * Merging two sub-arrays into one sorted array (mergesort)
     */
    private void merge(Point[] points, Point[] aux, int low, int mid, int high,
                       Comparator<Point> comparator) {
        if (!(comparator.compare(points[mid], points[mid + 1]) < 0)) {
            int i = low;
            int j = mid + 1;

            for (int k = low; k <= high; k++) {
                aux[k] = points[k];
            }

            for (int k = low; k <= high; k++) {
                if (i > mid) {
                    points[k] = aux[j];
                    j++;
                }
                else if (j > high) {
                    points[k] = aux[i];
                    i++;
                }
                else if (comparator.compare(aux[i], aux[j]) <= 0) {
                    points[k] = aux[i];
                    i++;
                }
                else {
                    points[k] = aux[j];
                    j++;
                }
            }
        }
    }

    /**
     * Find all the line segments containing the point[indice] and has 4 or more points. Push all
     * the found lines in the stack
     *
     * @param indice the starting point
     * @param points all the points after being sorted in the slope order with the starting point
     * @param lines  the stack containing all the lines found
     */
    private void findSegments(int indice, Point[] points, Stack<LineSegment> lines) {
        int count = 1;   // Count the number of points that have the same slope
        int i = points.length - 1;   // Scan backward
        Comparator<Point> comparator = points[indice].slopeOrder();
        boolean isDuplicate = false;     // Checks if the line is a subsegment

        while (i > indice) {
            int j = i - 1;
            while (comparator.compare(points[i], points[j]) == 0 && j > indice) {
                // Because the origin of a segment will always have the smallest natural order
                // so if there are any points that is smaller than the origin
                // We know that the line is a subsegment
                if (points[indice].compareTo(points[i]) > 0
                        || points[indice].compareTo(points[j]) > 0) {
                    isDuplicate = true;
                }

                // If the next point is "larger" than the current point
                // Move the current point to the next point so we don't miss
                // any point and can draw the full segment
                if (points[j].compareTo(points[i]) > 0) {
                    i = j;
                }

                j--;
                count++;
            }

            // If the line contains 4 or more points and is not a subsegment
            if (count >= 3 && !isDuplicate) {
                LineSegment newLine = new LineSegment(points[indice], points[i]);
                lineSegments.push(newLine);
            }

            i = j;
            count = 1;
            isDuplicate = false;
        }
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
