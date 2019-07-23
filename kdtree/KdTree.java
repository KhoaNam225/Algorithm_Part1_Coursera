/* *****************************************************************************
 *  Name:   Khoa Nam Pham
 *  Date:   20/07/2019
 *  Description:    Range search using Kd-tree algorithm
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * This class is a 2d-tree supporting range search and find nearest neighbor problems. It stores the
 * information of all the points in a plane as a 2d-tree data structure by continuously divide the
 * plane into two halves (left and right, or up and down). Each point in the tree will represent a
 * sub-rectangle that they divide. KdTree supports the following operations: - isEmpty()          :
 * checks if the tree is empty - size()             : returns the size of the tree -
 * contains(Point2D)  : checks if the given point is already in the tree - draw()             : draw
 * all the points in the tree along with the rectangles they represents. - range(RectHV)      :
 * finds all the points that lie in the given rectangle - nearest(Point2D)   : finds the nearest
 * neighbor to the given point
 */
public class KdTree {
    // The base retangle representing the whole plane
    private static final RectHV BASE_RECT = new RectHV(0.0, 0.0, 1.0, 1.0);

    // The root of the tree
    private Node root;

    /**
     * Initializes the tree
     */
    public KdTree() {
        root = null;
    }

    /**
     * Checks if the tree is empty
     *
     * @return true if the tree is empty otherwise returns false.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the size (the number of points) of the tree
     *
     * @return An integer greater than or equal to zero indicating the size of the tree.
     */
    public int size() {
        return size(root);
    }

    /**
     * Recursive method to calculate the size of the tree.
     *
     * @param root The root of the current subtree
     * @return The size of the subtree. If the root is null (indicating the subtree is empty),
     * returns zero
     */
    private int size(Node root) {
        int result;
        if (root == null) {
            result = 0;
        }
        else {
            result = root.count;
        }

        return result;
    }

    /**
     * Insert a new point into the tree.
     *
     * @param inPoint The new point to be inserted
     * @throws IllegalArgumentException If the new point is null.
     */
    public void insert(Point2D inPoint) {
        if (inPoint == null)
            throw new IllegalArgumentException("Adding null point to tree.");

        root = insert(root, BASE_RECT, inPoint, 0);
    }

    /**
     * Insert a new node into the tree (recursively) when given a new RectHV, Point2D. Because the
     * way the rectangles are divided is based on the level of the node (even level: left and right,
     * odd level: up and down), we also need to know level to know how to devide the current
     * rectangle.
     *
     * @param root    The current node to be examined
     * @param inRect  The rectangle that root represent
     * @param inPoint The new point to be inserted
     * @param level   The current level
     * @return The root of the subtree containing the new point after being inserted
     */
    private Node insert(Node root, RectHV inRect, Point2D inPoint, int level) {
        Node result = root;
        // If we reach the end of the tree, insert the new node to the end
        if (root == null) {
            result = new Node(inPoint, inRect, 1);
        }
        else {
            // If the new Point is not already in the tree
            if (!root.point.equals(inPoint)) {
                RectHV rect;
                // If we are in the even level, which mean we have to divide the rectangle into
                // left and right halves.
                if (isComparingX(level)) {
                    int compX = Point2D.X_ORDER.compare(inPoint, root.point);
                    // Determine which subtree to insert the new node, left or right
                    if (compX < 0) {
                        rect = root.divideLeft();
                        root.left = insert(root.left, rect, inPoint, level + 1);
                    }
                    else {
                        rect = root.divideRight();
                        root.right = insert(root.right, rect, inPoint, level + 1);
                    }
                }
                // If we are in the odd level, we have to devide the rectangle by upper and lower halves
                else {
                    int compY = Point2D.Y_ORDER.compare(inPoint, root.point);
                    if (compY < 0) {
                        rect = root.divideDown();
                        root.left = insert(root.left, rect, inPoint, level + 1);
                    }
                    else {
                        rect = root.divideUp();
                        root.right = insert(root.right, rect, inPoint, level + 1);
                    }
                }

                // Update the size of the subtree after insert
                root.count = size(root.left) + size(root.right) + 1;
            }
        }

        return result;
    }

    /**
     * Determine the way to devide the rectangle and which subtree to insert the new point base on
     * the level
     *
     * @param level The current level
     * @return true if the current level is even (which mean we have to divive rectangle by left or
     * right halves) or false otherwise
     */
    private boolean isComparingX(int level) {
        return (level % 2 == 0);
    }

    /**
     * Checks if the searchPoint is already in the tree or not
     *
     * @param searchPoint The search point
     * @return true if the given point is already in the tree or false otherwise
     */
    public boolean contains(Point2D searchPoint) {
        if (searchPoint == null)
            throw new IllegalArgumentException("Searching null point.");

        return contains(root, searchPoint, 0);
    }

    /**
     * Recursive method to checks if the search point is already in the subtree rooted at root
     *
     * @param root        The root of the current subtree
     * @param searchPoint The search point
     * @param level       The level of the root from the root of the tree, which is used to
     *                    determine the branch to go down
     * @return true if the searchPoint is already in the tree or false otherwise
     */
    private boolean contains(Node root, Point2D searchPoint, int level) {
        boolean result;
        if (root == null) {
            result = false;
        }
        else if (root.point.equals(searchPoint)) {
            result = true;
        }
        else {
            if (isComparingX(level)) {
                int compX = Point2D.X_ORDER.compare(searchPoint, root.point);
                if (compX < 0) {
                    result = contains(root.left, searchPoint, level + 1);
                }
                else {
                    result = contains(root.right, searchPoint, level + 1);
                }
            }
            else {
                int compY = Point2D.Y_ORDER.compare(searchPoint, root.point);
                if (compY < 0) {
                    result = contains(root.left, searchPoint, level + 1);
                }
                else {
                    result = contains(root.right, searchPoint, level + 1);
                }
            }
        }

        return result;
    }

    /**
     * Draw all the points in the tree along with the rectangles each point represents.
     */
    public void draw() {
        draw(root, 0);
    }

    /**
     * Draw all the points in the current subtree rooted at root.
     *
     * @param root  The root of the current subtree
     * @param level The level of the current root.
     */
    private void draw(Node root, int level) {
        if (root != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            root.point.draw();
            if (isComparingX(level)) {
                // Draw a line which seperates the rectangle into two halves. Base on the level,
                // the line could be a horizontal line (divide upper and lower) or vertical line (divide left and right)
                double x = root.point.x();
                double ymin = root.rect.ymin();
                double ymax = root.rect.ymax();
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(x, ymin, x, ymax);
            }
            else {
                double y = root.point.y();
                double xmin = root.rect.xmin();
                double xmax = root.rect.xmax();
                StdDraw.setPenRadius();
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(xmin, y, xmax, y);
            }

            // draw the left subtree
            draw(root.left, level + 1);
            // draw the right subtree
            draw(root.right, level + 1);
        }
    }

    /**
     * Finds all the points that lie in the given rectangle
     *
     * @param rect The query rectangle
     * @return An Iterable<Point2D> object containing all the points satisfying the requirement. If
     * there are no points, return an empty Iterable object
     * @throws IllegalArgumentException The query rectangle is null
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Range searching with null rectangle");
        }

        Queue<Point2D> queue = new Queue<>();
        range(root, rect, queue);
        return queue;
    }

    /**
     * Recursively find all the points lie in the given rectangle and add them to the given Queue
     *
     * @param root  The root of the current subtree
     * @param rect  The query rectangle
     * @param queue The Queue containing all the points that lie in the rectangle
     */
    private void range(Node root, RectHV rect, Queue<Point2D> queue) {
        if (root != null) {
            // Checks if the root is inside the query rectangle
            if (rect.contains(root.point))
                queue.enqueue(root.point);

            // If the query rectangle intersects the rectangle of the left subtree, there is a chance
            // that the left subtree contains a point that satistfies the requirement.
            if (root.left != null && root.left.rect.intersects(rect))
                range(root.left, rect, queue);

            // If the query rectangle intersects the rectangle of the right subtree, there is a chance
            // that the right subtree contains a point that satistfies the requirement.
            if (root.right != null && root.right.rect.intersects(rect))
                range(root.right, rect, queue);
        }
    }

    /**
     * Find the nearest neighbor to the given query point
     *
     * @param queryPoint The query point to find the nearest neighbor to
     * @return If the tree is empty return null otherwise the nearest point to the given point
     * @throws IllegalArgumentException The query point is null.
     */
    public Point2D nearest(Point2D queryPoint) {
        if (queryPoint == null)
            throw new IllegalArgumentException("Finding nearest neighbor with null Point");

        Point2D result;
        if (isEmpty()) {
            result = null;
        }
        else {
            result = nearest(root, queryPoint, root.point, 0);
        }

        return result;
    }

    /**
     * Recursively find the nearest neighbor in the subtree rooted at the current root.
     *
     * @param root       The root of the current subtree
     * @param queryPoint The query point.
     * @param nearestSF  The nearest neighbor so far.
     * @param level      The current level of the current root.
     * @return The nearest neighbor in the current subtree
     */
    private Point2D nearest(Node root, Point2D queryPoint, Point2D nearestSF, int level) {
        Point2D result = nearestSF; // The nearest neighbor to return
        if (root != null) {
            // If the root is the new nearest neighbor
            if (root.point.distanceSquaredTo(queryPoint) <= queryPoint.distanceSquaredTo(nearestSF))
                result = root.point;

            // Going toward the query point, if the query point is in the left subtree we check the left
            // subtree first, if it is in the right, we check the right first. After finish checking on subtree,
            // if the distance of the nearest neighbor is smaller than the distance from the query point to the
            // rectangle of the other subtree, we can ignore that subtree.

            // If the query point is in the left subtree, we go to the left first
            if ((isComparingX(level) && queryPoint.x() < root.point.x()) || (!isComparingX(level)
                    && queryPoint.y() < root.point.y())) {
                result = nearest(root.left, queryPoint, result, level + 1);
                // After checking the left, checks the right subtree if there is a chance that it
                // contains the nearest neighbor
                if (root.right != null && queryPoint.distanceSquaredTo(result) >= root.right.rect
                        .distanceSquaredTo(queryPoint))
                    result = nearest(root.right, queryPoint, result, level + 1);
            }
            else {
                // Else check the right subtree first
                result = nearest(root.right, queryPoint, result, level + 1);
                if (root.left != null && queryPoint.distanceSquaredTo(result) >= root.left.rect
                        .distanceSquaredTo(queryPoint))
                    result = nearest(root.left, queryPoint, result, level + 1);
            }
        }
        return result;
    }

    /**
     * The Node in the 2d-tree
     */
    private class Node {
        private Point2D point; // The point in the node
        private RectHV rect;   // The rectangle that the point represents
        private Node left;
        private Node right;
        private int count;      // The total node that the subtree rooted at the current node has.

        public Node(Point2D inPoint, RectHV inRect, int inCount) {
            this.point = inPoint;
            this.rect = inRect;
            this.count = inCount;
            this.left = null;
            this.right = null;
        }

        // Divide the current rectangle to left and right halves, then return the left half
        public RectHV divideLeft() {
            double xmin = rect.xmin();
            double ymin = rect.ymin();
            double xmax = point.x();
            double ymax = rect.ymax();

            return new RectHV(xmin, ymin, xmax, ymax);
        }

        // Divide the current rectangle to left and right halves, then return the right half
        public RectHV divideRight() {
            double xmin = point.x();
            double ymin = rect.ymin();
            double xmax = rect.xmax();
            double ymax = rect.ymax();

            return new RectHV(xmin, ymin, xmax, ymax);
        }

        // Divide the current rectangle to upper and lower halves, then return the upper half
        public RectHV divideUp() {
            double xmin = rect.xmin();
            double ymin = point.y();
            double xmax = rect.xmax();
            double ymax = rect.ymax();

            return new RectHV(xmin, ymin, xmax, ymax);
        }

        // Divide the current rectangle to upper and lower halves, then return the lower half
        public RectHV divideDown() {
            double xmin = rect.xmin();
            double ymin = rect.ymin();
            double xmax = rect.xmax();
            double ymax = point.y();

            return new RectHV(xmin, ymin, xmax, ymax);
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        In input = new In(filename);
        KdTree tree = new KdTree();
        Point2D testPoint = null;
        int count = 0;
        while (!input.isEmpty()) {
            double x = input.readDouble();
            double y = input.readDouble();
            Point2D point = new Point2D(x, y);
            testPoint = new Point2D(x, y);

            tree.insert(point);
            count++;
        }

        StdOut.println("Expected size " + count + ": " + tree.size());
        StdOut.println("Expected empty = false: " + tree.isEmpty());
        StdOut.println("Expected false = " + tree.contains(new Point2D(0, 0)));
        StdOut.println("Expected true = " + tree.contains(testPoint));
    }
}
