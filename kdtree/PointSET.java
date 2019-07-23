/* *****************************************************************************
 *  Name:   Khoa Nam Pham
 *  Date:   19/07/2019
 *  Description:    Bruteforce algorithm for 2D range search
 *  File name: PointSET.java
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

/**
 * This class is used to store all the points in a plane using a red-black BST tree. It supports the
 * following operations: - isEmpty()          : checks if the PointSET is empty - size() : returns
 * the number of points in the PointSET - insert(Point2D )   : insert a new point into the SET -
 * range(RectHV )     : performs a range search for the given RectHV object finds all the points
 * that lie in the given rectangle - nearest(Point2D )  : find the nearest neighbor to the given
 * point among all the points in the plane
 */
public class PointSET {
    private SET<Point2D> pointSet;

    /**
     * Initialize the red-black tree used to store all the points.
     */
    public PointSET() {
        pointSet = new SET<Point2D>();
    }

    /**
     * Checks if the plane is empty.
     *
     * @return true if the PointSET is empty or false otherwise.
     */
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    /**
     * Returns the total number of points in the SET so far.
     *
     * @return The number of points in the SET (greater than or equal to zero)
     */
    public int size() {
        return pointSet.size();
    }

    /**
     * Inserts a new Point2D object into the PointSET
     *
     * @param newPoint The new Point2D object that is going to be added to the SET.
     * @throws IllegalArgumentException The imported Point2D object is null
     */
    public void insert(Point2D newPoint) {
        if (newPoint == null) {
            throw new IllegalArgumentException("Inserting null point to the SET");
        }

        pointSet.add(newPoint);
    }

    /**
     * Checks if the search point is already in the PointSet or not.
     *
     * @param searchPoint The search point
     * @return true if the search point is already in the PointSET or false otherwise
     * @throws IllegalArgumentException The imported search point is null
     */
    public boolean contains(Point2D searchPoint) {
        if (searchPoint == null) {
            throw new IllegalArgumentException("Searching null points.");
        }

        return pointSet.contains(searchPoint);
    }

    /**
     * Draws all the points on the plane
     *
     * @throws IllegalArgumentException Calling draw() if the SET is empty
     */
    public void draw() {
        if (pointSet.isEmpty()) {
            throw new IllegalArgumentException("The SET is empty.");
        }

        for (Point2D point : pointSet) {
            point.draw();
        }
    }

    /**
     * Returns all the points lie in the given rectangle (RectHV object)
     *
     * @param rect The rectangle used to search for points
     * @return An Iterable<Point2D> object that contains all the satisfying points. If there is no
     * point that could satisfy the requirement, returns an empty Iterable<Point2D> object.
     * @throws IllegalArgumentException The given RectHV object is null
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Range searching with null rectangle.");
        }

        Queue<Point2D> points = new Queue<>();
        for (Point2D point : pointSet) {
            if (rect.contains(point)) {
                points.enqueue(point);
            }
        }

        return points;
    }

    /**
     * Given a point on the plane, find the nearest neighbor of that point.
     *
     * @param queryPoint The point to find the nearest neighbor to.
     * @return The nearest neighbor of the query point. If the SET is empty, returns null.
     * @throws IllegalArgumentException The query point is null.
     */
    public Point2D nearest(Point2D queryPoint) {
        if (queryPoint == null) {
            throw new IllegalArgumentException("Find nearest with null query point");
        }

        if (pointSet.isEmpty()) {
            return null;
        }

        // Checks all the points in the tree and compare the distance to the query point.
        Point2D currPoint = pointSet.min();
        double shortestDist = queryPoint.distanceSquaredTo(currPoint);
        for (Point2D point : pointSet) {
            double currDist = queryPoint.distanceSquaredTo(point);
            if (currDist < shortestDist) {
                shortestDist = currDist;
                currPoint = point;
            }
        }

        return currPoint;
    }

    public static void main(String[] args) {
        PointSET set = new PointSET();
        In inputStream = new In(args[0]);

        while (!inputStream.isEmpty()) {
            double xCoord = inputStream.readDouble();
            double yCoord = inputStream.readDouble();
            Point2D newPoint = new Point2D(xCoord, yCoord);
            set.insert(newPoint);
        }

        StdOut.println("File name: " + args[0]);
        StdOut.println("SET size: " + set.size());
        StdOut.println("Expected not empty: " + set.isEmpty());
    }
}
