/* *****************************************************************************
 *  Name:   Khoa Nam Pham
 *  Date:   02/07/2019
 *  Filename: Solver.java
 *  Description:    This is the solver of the 8-puzzle problem using A* search
 *                  algorithm.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private static final Comparator<SearchNode> BY_MANHATTAN = new ByManhattan();

    private int steps;  // The number of steps taken to solve the puzzle
    private SearchNode result;
    // The lest step in the solution, we will trace back all the previous step from this step
    private boolean solvable = true;   // The puzzle is solvable or not?

    public Solver(Board init) {
        if (init == null) {
            throw new IllegalArgumentException("Cannot solve null puzzle.");
        }

        // If the puzzle has already been solved
        if (init.isGoal()) {
            result = new SearchNode(init, 0);
            steps = 0;
        }
        else {
            // Two MinPQ to solve the main puzzle and the twin puzzle simultaneously
            MinPQ<SearchNode> pq = new MinPQ<>(BY_MANHATTAN);
            MinPQ<SearchNode> pqTwin = new MinPQ<>(BY_MANHATTAN);

            SearchNode current = new SearchNode(init, 0);
            SearchNode currentTwin = new SearchNode(init.twin(), 0);

            while (!current.isSolved() && !currentTwin.isSolved()) {
                // Solve the main puzzle by one step
                pushChilds(pq, current);
                current = pq.delMin();

                // Solve the twin puzzle by one step
                pushChilds(pqTwin, currentTwin);
                currentTwin = pqTwin.delMin();
            }

            // If the twin game is solved --> the main game is unsolvable
            if (currentTwin.isSolved()) {
                solvable = false;
                steps = -1;
                result = null;
            }
            else {
                steps = current.getStep();  // The total number of steps
                result = current; // We haven't pushed in the last node
            }
        }
    }

    /**
     * Checks if the puzzle is solvable or not
     *
     * @return true if solvable and false otherwise
     */
    public boolean isSolvable() {
        return solvable;
    }

    /**
     * Returns the number of steps need to solve the puzzle
     *
     * @return the number of steps
     */
    public int moves() {
        return steps;
    }

    /**
     * Returns the solution to the puzzle, step by step
     *
     * @return The solution of the puzzle
     */
    public Iterable<Board> solution() {
        if (result == null)
            return null;

        Stack<Board> boards = new Stack<>();
        SearchNode current = result;
        while (current != null) {
            boards.push(current.getBoard());
            current = current.getParent();
        }
        return boards;
    }

    /**
     * Inserts all the neighbors of the current node to the MinPQ
     *
     * @param pq      The MinPQ
     * @param current The current node
     */
    private void pushChilds(MinPQ<SearchNode> pq, SearchNode current) {
        for (SearchNode node : current.neighbors()) {
            pq.insert(node);
        }
    }

    private class SearchNode {
        private final SearchNode parent;   // The parent node
        private final Board board;    // The current node
        private final int step;       // The number of steps taken to get to the current node
        private final int manhattan;

        /**
         * Constructs a SearchNode without a parent node
         *
         * @param inBoard The board that the node is containing
         * @param inStep  The number of steps taken to get to the current node
         */
        public SearchNode(Board inBoard, int inStep) {
            if (inBoard == null || inStep < 0) {
                throw new IllegalArgumentException("Invalid initial values for SearchNode");
            }

            parent = null;
            board = inBoard;
            step = inStep;
            manhattan = inBoard.manhattan();
        }

        /**
         * Constructs a SearchNode with a parent node
         *
         * @param inParent The parent node
         * @param inBoard  The current node
         * @param inStep   The number of steps taken to get to the current node
         */
        public SearchNode(SearchNode inParent, Board inBoard, int inStep) {
            if (inParent == null || inBoard == null || inStep < 0) {
                throw new IllegalArgumentException("Invalid initial values for SearchNode");
            }

            parent = inParent;
            board = inBoard;
            step = inStep;
            manhattan = inBoard.manhattan();
        }

        /**
         * Get the number of steps taken
         *
         * @return The total number of steps
         */
        public int getStep() {
            return step;
        }

        /**
         * Get the board contained in the current node
         *
         * @return The board in the node
         */
        public Board getBoard() {
            return board;
        }

        /**
         * Returns the parent of the current nodes
         *
         * @return The parent of the current node.
         */
        public SearchNode getParent() {
            return parent;
        }

        /**
         * Returns the manhattan distance of the current node
         *
         * @return The manhattan distance
         */
        public int getManhattan() {
            return manhattan;
        }

        /**
         * Returns the priority index of the current node. The priority index = step + manhattan
         * distance
         *
         * @return The priority index
         */
        public int getPriority() {
            return manhattan + step;
        }

        /**
         * Checks if the current node is the last node in the game tree (i.e the puzzle has been
         * solved) or not.
         *
         * @return true if the game has been solved or false otherwise
         */
        public boolean isSolved() {
            return board.isGoal();
        }

        /**
         * Gets all the neighbors of the current node
         *
         * @return A queue that contains all the neighbors of the current node.
         */
        public Iterable<SearchNode> neighbors() {
            Queue<SearchNode> neighborsList = new Queue<>();
            for (Board neighbor : board.neighbors()) {
                // Critical optimization
                // Because parent can be null (the root of the game), we have to call
                // equals() from the neighbor (the childs)
                if (parent == null) {
                    SearchNode newNode = new SearchNode(this, neighbor, step + 1);
                    neighborsList.enqueue(newNode);
                }
                else if (!neighbor.equals(parent.getBoard())) {
                    SearchNode newNode = new SearchNode(this, neighbor, step + 1);
                    neighborsList.enqueue(newNode);
                }
            }

            return neighborsList;
        }
    }

    /**
     * The comparator used to compare two SearchNode by Manhattan Distance
     */
    private static class ByManhattan implements Comparator<SearchNode> {
        public int compare(SearchNode node1, SearchNode node2) {
            int priority1 = node1.getPriority();
            int priority2 = node2.getPriority();
            int man1 = node1.getManhattan();
            int man2 = node2.getManhattan();

            if (priority1 < priority2)
                return -1;
            else if (priority1 > priority2)
                return 1;
            else
                // If the 2 nodes have equal priority indices,
                // Which node has smaller manhattan distance will be considered smaller
                return Integer.compare(man1, man2);
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
