import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final boolean OPEN = true;
    private static final boolean BLOCKED = false;

    private final WeightedQuickUnionUF ufPercolate;
    private final WeightedQuickUnionUF ufFull;
    private boolean[][] states;
    private int openSites;
    private final int gridSize;
    private final int virtualTop;
    private final int virtualBottom;

    public Percolation(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Grid size cannot be less than 1!");
        }

        gridSize = size;
        openSites = 0;
        virtualBottom = size * size + 1;
        virtualTop = size * size;

        ufFull = new WeightedQuickUnionUF(size * size + 1);
        ufPercolate = new WeightedQuickUnionUF(size * size + 2);
        for (int i = 0; i < size; i++) {
            ufPercolate.union(convert2Dto1D(1, i + 1), virtualTop);
            //ufPercolate.union(convert2Dto1D(size, i + 1), virtualBottom);
            ufFull.union(convert2Dto1D(1, i + 1), virtualTop);
        }

        states = new boolean[size][];
        for (int i = 0; i < size; i++) {
            states[i] = new boolean[size];
            for (int j = 0; j < size; j++) {
                states[i][j] = BLOCKED;
            }
        }
    }

    public void open(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException("Invalid value for row or col.");
        }

        if (!isOpen(row, col)) {
            openSites++;
            states[row - 1][col - 1] = OPEN;

            // Connect it to all the neighbors
            connectDown(row, col);
            connectLeft(row, col);
            connectUp(row, col);
            connectRight(row, col);

            // If the current site is in the bottom row
            // connect it to the virtual bottom site
            if (row == gridSize) {
                ufPercolate.union(convert2Dto1D(row, col), virtualBottom);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException("Invalid value for row or col.");
        }

        return states[row - 1][col - 1] == OPEN;
    }

    public boolean isFull(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException("Invalid value for row or col.");
        }

        return ufFull.connected(virtualTop, convert2Dto1D(row, col)) && isOpen(row, col);
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        return ufPercolate.connected(virtualTop, virtualBottom);
    }

    private void connectLeft(int row, int col) {
        if (validate(row, col - 1) && isOpen(row, col - 1)) {
            int leftIndex = convert2Dto1D(row, col - 1);
            int currentIndex = convert2Dto1D(row, col);
            ufFull.union(currentIndex, leftIndex);
            ufPercolate.union(currentIndex, leftIndex);
        }
    }

    private void connectRight(int row, int col) {
        if (validate(row, col + 1) && isOpen(row, col + 1)) {
            int rightIndex = convert2Dto1D(row, col + 1);
            int currentIndex = convert2Dto1D(row, col);
            ufFull.union(currentIndex, rightIndex);
            ufPercolate.union(currentIndex, rightIndex);
        }
    }

    private void connectUp(int row, int col) {
        if (validate(row - 1, col) && isOpen(row - 1, col)) {
            int upIndex = convert2Dto1D(row - 1, col);
            int currentIndex = convert2Dto1D(row, col);
            ufFull.union(currentIndex, upIndex);
            ufPercolate.union(currentIndex, upIndex);
        }
    }

    private void connectDown(int row, int col) {
        if (validate(row + 1, col) && isOpen(row + 1, col)) {
            int downIndex = convert2Dto1D(row + 1, col);
            int currentIndex = convert2Dto1D(row, col);
            ufFull.union(currentIndex, downIndex);
            ufPercolate.union(currentIndex, downIndex);
        }
    }

    private int convert2Dto1D(int row, int col) {
        if (!validate(row, col)) {
            throw new IllegalArgumentException("Invalid value for row or col.");
        }

        return (row - 1) * gridSize + col - 1;
    }

    private boolean validate(int row, int col) {
        return (row <= gridSize && row >= 1) && (col <= gridSize && col >= 1);
    }
}
