/* *****************************************************************************
 *  Name:   Khoa Nam Pham
 *  Date:   01/07/2019
 *  File name: Board.java
 *  Description:    This class represent the board of size NxN
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private int[][] tiles;  // All the tiles in the board
    private final int manhattan; // The manhattan distance of this board to the goal board
    private final int hamming;   // The hamming distance of this board to the goal board

    /**
     * Initializes a Board with all the tiles given in an 2D array
     *
     * @param inTiles The 2D array two construct the Board
     */
    public Board(int[][] inTiles) {
        if (inTiles == null)
            throw new IllegalArgumentException();

        int length = inTiles.length;
        this.tiles = new int[length][];

        // Copy each tiles from the input to the tiles of the board
        for (int i = 0; i < length; i++) {
            tiles[i] = new int[length];
            for (int j = 0; j < length; j++) {
                tiles[i][j] = inTiles[i][j];
            }
        }

        manhattan = calcManhattan(inTiles);
        hamming = calcHamming(inTiles);
    }

    /**
     * Return the String representation of the Board. The first line of the string is the dimmension
     * of the board and the other values are the tiles
     *
     * @return The String representation of the Board
     */
    public String toString() {
        int length = tiles.length;

        String outStr;
        outStr = length + "\n";  // The board size is the first line

        // All the tiles of the board
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                outStr += tiles[i][j] + " ";
            }
            outStr += "\n";
        }

        return outStr;
    }

    /**
     * Returns the dimmesion of the board
     *
     * @return The dimmension of the board
     */
    public int dimension() {
        return tiles.length;
    }

    /**
     * Compares the current Board with another Board. Two Boards are considered equal if they have
     * the same dimmension and all the tiles are the same
     *
     * @param inObj The other Board to be compared with the current Board
     * @return True if two boards are the same or False otherwise
     */
    public boolean equals(Object inObj) {
        if (inObj == null) {
            return false;
        }

        // If the imported object is a Board
        if (inObj.getClass().getSimpleName().equals("Board")) {
            Board inBoard = (Board) inObj;
            if (inBoard.dimension() == this.dimension()) {
                int[][] thatTiles = inBoard.tiles;
                int thisLength = tiles.length;

                for (int i = 0; i < thisLength; i++) {
                    for (int j = 0; j < thisLength; j++) {
                        if (tiles[i][j] != thatTiles[i][j]) {
                            return false;
                        }
                    }
                }

                return true;
            }
        }

        return false;
    }

    /**
     * Returns the hamming distance of the current board
     *
     * @return The hamming distance
     */
    public int hamming() {
        return hamming;
    }

    /**
     * Returns the manhattan distance of the current board
     *
     * @return The manhattan distance
     */
    public int manhattan() {
        return manhattan;
    }

    /**
     * Checks if the current Board is the goal or not
     *
     * @return true if the current board is the goal or false otherwise
     */
    public boolean isGoal() {
        return manhattan == 0;
    }

    public Iterable<Board> neighbors() {
        // The stack containing all valid neighbors of the current node
        Stack<Board> neighbor = new Stack<>();

        // Find the blank block in the Board
        int[] blankSpot = findBlank();
        int row = blankSpot[0];
        int col = blankSpot[1];

        int[][] inTiles;    // Tiles of the current Board after moving a block

        // Check all the posible neighbors
        if (validateBlock(row - 1, col)) {
            Board upNeighbor = getNeighbor(row, col, row - 1, col);
            neighbor.push(upNeighbor);
        }

        if (validateBlock(row + 1, col)) {
            Board downNeighbor = getNeighbor(row, col, row + 1, col);
            neighbor.push(downNeighbor);
        }

        if (validateBlock(row, col - 1)) {
            Board leftNeighbor = getNeighbor(row, col, row, col - 1);
            neighbor.push(leftNeighbor);
        }

        if (validateBlock(row, col + 1)) {
            Board righNeighbor = getNeighbor(row, col, row, col + 1);
            neighbor.push(righNeighbor);
        }

        return neighbor;
    }

    /**
     * Returns a board that is obtained by swapping any pairs of blocks
     *
     * @return A new board by swapping a pair of blocks
     */
    public Board twin() {
        // Find the blank block
        int[] blank = findBlank();
        int blankRow = blank[0];

        // Get to the row that does not contain the blank block and choose
        // the first 2 adjacent blocks from that row
        int row = 0;
        while (row == blankRow) {
            row++;
        }

        swap(row, 0, row, 1);
        int[][] inTiles = this.getTiles();
        Board newBoard = new Board(inTiles);
        swap(row, 0, row, 1);

        return newBoard;
    }

    /**
     * Returns a neighbor of the current Board by swapping values in two blocks located at (thisRow,
     * thisCol) and (otherRow, otherCol)
     *
     * @param thisRow  Row of the 1st block
     * @param thisCol  Column of the 1st block
     * @param otherRow Row of the 2nd block
     * @param otherCol Column of the 2nd block
     * @return The neighbor when swapping 2 blocks
     */
    private Board getNeighbor(int thisRow, int thisCol, int otherRow, int otherCol) {
        swap(thisRow, thisCol, otherRow, otherCol);
        int[][] inTiles = this.getTiles();
        Board neighbor = new Board(inTiles);
        swap(thisRow, thisCol, otherRow, otherCol); // returns to the initial state
        return neighbor;
    }

    /**
     * Generates the true value for a position at (row, col)
     *
     * @param row The row index
     * @param col The col index
     * @return The true value which should be in that position
     */
    private int convert2Dto1D(int row, int col) {
        int length = tiles.length;
        return row * length + col + 1;
    }

    /**
     * Returns the manhattan distance for a single value
     *
     * @param value      the value from which we calculate the manhattan distance
     * @param currentCol the current column at which the value is located
     * @param currentRow the current row at which the value is located
     * @return The manhattan distance
     */
    private int singleManhattan(int value, int currentRow, int currentCol) {
        int length = tiles.length;
        // The true position where the value should be
        int trueRow = value / length;
        int trueCol = value - trueRow * length;

        // The mahattan distance is the sum of the differences between the true
        // row and column with the current ones
        return Math.abs(trueCol - currentCol) + Math.abs(trueRow - currentRow);
    }

    /**
     * Swaps 2 values at position (row1, col1) and (row2, col2)
     *
     * @param row1 The row of the first value
     * @param col1 The column of the first value
     * @param row2 The row of the second value
     * @param col2 The column of the second value
     */
    private void swap(int row1, int col1, int row2, int col2) {
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
    }

    /**
     * Find the blank block in the Board
     *
     * @return An integer array contain the position of the Board, the first integer is the row, the
     * second is the column
     */
    private int[] findBlank() {
        int row = 0;
        int col = 0;
        int length = tiles.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        int[] result = new int[2];
        result[0] = row;
        result[1] = col;

        return result;
    }

    /**
     * Checks if the given position is valid (not out of the Board)
     *
     * @param row Row of the block
     * @param col Column of the block
     * @return true if the block is valid or false otherwise
     */
    private boolean validateBlock(int row, int col) {
        int length = tiles.length;
        return (row >= 0 && row < length) && (col >= 0 && col < length);
    }

    /**
     * Returns a copy of the current Board
     *
     * @return A 2D array of integers represent the tiles of the board
     */
    private int[][] getTiles() {
        return tiles;
    }

    /**
     * Calculate the hamming distance of the board
     *
     * @param inTiles The values of the blocks in the board
     * @return The hamming distance
     */
    private int calcHamming(int[][] inTiles) {
        int length = inTiles.length;
        int count = 0;   // The hamming distance

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                // The blank tile is not considered a tile
                if (inTiles[i][j] != 0) {
                    int correctTile = convert2Dto1D(i, j);  // The true value

                    // If the current value in the tile is not the same as the
                    // true value, increase the hamming distance
                    if (correctTile != inTiles[i][j])
                        count++;
                }
            }
        }

        return count;
    }

    /**
     * Calculates the manhattan distance of the current board
     *
     * @param inTiles The values of the blocks in the board
     * @return The manhattan distance
     */
    private int calcManhattan(int[][] inTiles) {
        int length = inTiles.length;
        int count = 0;  // Total manhattan distance

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                // The zero block is not considered a tile
                if (inTiles[i][j] != 0) {
                    int manDis = singleManhattan(inTiles[i][j] - 1, i, j);
                    count += manDis;
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {
        String[] filenames = { "puzzle2x2-01.txt", "puzzle2x2-00.txt", "puzzle2x2-03.txt" };
        int[] expectedMan = { 1, 0, 3 };
        int[] expectedHam = { 1, 0, 3 };

        for (int i = 0; i < filenames.length; i++) {
            In input = new In(filenames[i]);

            int size = input.readInt();
            int[][] tiles = new int[size][size];

            for (int k = 0; k < size; k++) {
                for (int j = 0; j < size; j++) {
                    tiles[k][j] = input.readInt();
                }
            }

            Board board = new Board(tiles);
            int actualMan = board.manhattan();
            int actualHam = board.hamming();

            assert actualHam == expectedMan[i] : "Manhattan";
            assert actualHam == expectedHam[i] : "Hamming";

            StdOut.println(filenames[i]);
            StdOut.print(board.toString());
            StdOut.print(board.twin().toString());
        }

        In input = new In(filenames[0]);
        In input2 = new In("puzzle2x2-00.txt");

        int size = input.readInt();
        int[][] tiles = new int[size][size];
        int size2 = input2.readInt();
        int[][] tiles2 = new int[size2][size2];

        for (int k = 0; k < size; k++) {
            for (int j = 0; j < size; j++) {
                tiles[k][j] = input.readInt();
            }
        }

        for (int k = 0; k < size2; k++) {
            for (int j = 0; j < size2; j++) {
                tiles2[k][j] = input2.readInt();
            }
        }

        Board board1 = new Board(tiles);
        Board board2 = new Board(tiles);
        Board board3 = new Board(tiles2);

        StdOut.println(board1.equals(board2));
        StdOut.println(board1.equals(board3));
    }
}
