/* *****************************************************************************
 *  Name:   Khoa Nam Pham
 *  Date:   15/06/2019
 *  Description:    Find all the line segments that contain 4 points using
 *                  brute force algorithm. This algorithm assumes that there are
 *                  no lines that contain more than 4 points and no duplicated points.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class BruteCollinearPoints {

    // Minimum and maximum value of the coordinate of a point
    private static final int POINT_MIN = 0;
    private static final int POINT_MAX = 32767;

    private Stack<LineSegment> lineSegments;
    // The stack containing all the line segments created from the points

    /**
     * Find all the lines that have 4 points and store them in the stack
     *
     * @param points All the given points in the 2D-plane
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Points[] is null");
        }

        Merge.sort(points); // Sorts all the points in increasing order of y_order
        int length = points.length;
        lineSegments = new Stack<>();
        for (int i = 0; i < length; i++) {
            Comparator<Point> slopeOrder = points[i].slopeOrder();
            if (points[i] == null) {
                throw new IllegalArgumentException("One of the points is null.");
            }

            if ((i < length - 1) && (points[i].compareTo(points[i + 1]) == 0)) {
                throw new IllegalArgumentException("Duplicated points.");
            }

            for (int j = i + 1; j < length; j++) {
                for (int k = j + 1; k < length; k++) {
                    // If j and k have the same slope, which means they are on the same
                    // line as i
                    if (slopeOrder.compare(points[j], points[k]) == 0) {
                        for (int m = k + 1; m < length; m++) {
                            if (slopeOrder.compare(points[k], points[m]) == 0) {
                                LineSegment newLine = new LineSegment(points[i], points[m]);
                                lineSegments.push(newLine);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the number of lines that have 4 points
     *
     * @return The total number of lines found so far
     */
    public int numberOfSegments() {
        return lineSegments.size();
    }

    /**
     * Returns an array containing all the line segments that contain 4 points
     *
     * @return The array of all line segment
     */
    public LineSegment[] segments() {
        int size = lineSegments.size();
        LineSegment[] lines = new LineSegment[size];

        int i = 0;
        for (LineSegment newLine :
                lineSegments) {
            lines[i] = newLine;
            i++;
        }

        return lines;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        // FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
