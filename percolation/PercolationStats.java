import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double Z_VALUE = 1.96;

    private final double mu;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    public PercolationStats(int gridSize, int inTrials) {
        if (gridSize <= 0 || inTrials <= 0)
            throw new IllegalArgumentException("Invalid import values for Percolation stats");

        double[] sample = new double[inTrials];
        for (int i = 0; i < inTrials; i++) {
            Percolation grid = new Percolation(gridSize);
            int openSites;

            while (!grid.percolates()) {
                int row = StdRandom.uniform(1, gridSize + 1);
                int col = StdRandom.uniform(1, gridSize + 1);
                grid.open(row, col);
            }

            openSites = grid.numberOfOpenSites();

            sample[i] = (double) openSites / (double) (gridSize * gridSize);
        }

        mu = StdStats.mean(sample);
        if (inTrials == 1) {
            stddev = Double.NaN;
        }
        else {
            stddev = StdStats.stddev(sample);
        }
        confidenceLo = mu - Z_VALUE * stddev / Math.sqrt(inTrials);
        confidenceHi = mu + Z_VALUE * stddev / Math.sqrt(inTrials);
    }

    /*
        Submodule:  mean
        Imports:    none
        Export:     average
        Assertion:  returns the mean of the sample
    */
    public double mean() {
        return mu;
    }

    /*
        Submodule:  stddev
        Imports:    none
        Export:     sd
        Assertion:  returns the standard deviation of the sample
    */
    public double stddev() {
        return stddev;
    }

    /*
        Submodule:  confidenceLo
        Imports:    none
        Export:     confLo
        Assertion:  returns the lower bound of the 95% confidence interval of the mean
    */
    public double confidenceLo() {
        return confidenceLo;
    }

    /*
        Submodule:  confidenceHi
        Imports:    none
        Export:     confHi
        Assertion:  returns the upper bound of the 95% confidence interval of the mean
    */
    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int inTrials = Integer.parseInt(args[1]);

        PercolationStats perStat = new PercolationStats(n, inTrials);
        double mean = perStat.mean();
        double sd = perStat.stddev();
        double confLo = perStat.confidenceLo();
        double confHi = perStat.confidenceHi();

        System.out.println("mean = " + mean);
        System.out.println("standard deviation = " + sd);
        System.out.println("95% confidence interval = [" + confLo + ", " + confHi + "]");
    }
}
