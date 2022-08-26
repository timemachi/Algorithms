import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int lenght;
    private boolean[][] matrix;
    private final WeightedQuickUnionUF uf;
    private int numOfOpenSite;
    private final int bottom;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        lenght = n;
        numOfOpenSite = 0;
        matrix = new boolean[n][n];
        bottom = lenght * lenght + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = false;
            }
        }
        uf = new WeightedQuickUnionUF(n * n + 2);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row <= 0 || row > lenght || col <= 0 || col > lenght) {
            throw new IllegalArgumentException("");
        }
        if (!isOpen(row, col)) {
            numOfOpenSite += 1;
            matrix[col - 1][row - 1] = true;
            int thisSite = helperCalcul(row, col);

            if (row == 1) {
                uf.union(helperCalcul(row, col), 0);
            }
            if (row == lenght) {
                uf.union(helperCalcul(row, col), bottom);
            }
            // neighbors
            if (row > 1 && isOpen(row - 1, col)) {
                uf.union(thisSite, helperCalcul(row - 1, col));
            }
            if (row < lenght && isOpen(row + 1, col)) {
                uf.union(thisSite, helperCalcul(row + 1, col));
            }
            if (col > 1 && isOpen(row, col - 1)) {
                uf.union(thisSite, helperCalcul(row, col - 1));
            }
            if (col < lenght && isOpen(row, col + 1)) {
                uf.union(thisSite, helperCalcul(row, col + 1));
            }
        }
    }
    
    private int helperCalcul(int row, int col) {
        return col + (row - 1) * lenght;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > lenght || col <= 0 || col > lenght) {
            throw new IllegalArgumentException("");
        }
        return matrix[col - 1][row - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row <= 0 || row > lenght || col <= 0 || col > lenght) {
            throw new IllegalArgumentException("");
        }
        return uf.find(0) == uf.find(helperCalcul(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOfOpenSite;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(0) == uf.find(bottom);
    }
}
