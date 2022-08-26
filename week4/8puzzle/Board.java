import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int[][] tiles;
    private final int dimension;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = hardCopy(tiles);
        this.dimension = this.tiles.length;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int k = 1;
        int numOfIncorrect = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] != k) {
                    numOfIncorrect++;
                }
                k++;
            }
        }
        return numOfIncorrect - 1;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int k = 1;
        int man = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] != k) {
                    man += helperMan(i, j);
                }
                k++;
            }
        }
        return man;
    }

    private int helperMan(int i, int j) {
        int value = tiles[i][j];
        int x, y;
        if (value != 0) {
            x = (value - 1) / dimension;
            y = (value - 1) % dimension;
        }
        else {
            return 0;
        }
        return Math.abs(x - i) + Math.abs(y - j);
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        return Arrays.deepEquals(tiles, that.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int x = 0;
        int y = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (tiles[i][j] == 0) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        List<Board> neighbors = new ArrayList<>();
        // exchange with upside tile
        if (y > 0) {
            int[][] upside = hardCopy(tiles);
            upside[x][y] = upside[x][y - 1];
            upside[x][y - 1] = 0;
            Board up = new Board(upside);
            neighbors.add(up);
        }
        if (y < dimension - 1) {
            int[][] downside = hardCopy(tiles);
            downside[x][y] = downside[x][y + 1];
            downside[x][y + 1] = 0;
            Board down = new Board(downside);
            neighbors.add(down);
        }
        if (x > 0) {
            int[][] leftside = hardCopy(tiles);
            leftside[x][y] = leftside[x - 1][y];
            leftside[x - 1][y] = 0;
            Board left = new Board(leftside);
            neighbors.add(left);
        }
        if (x < dimension - 1) {
            int[][] rightside = hardCopy(tiles);
            rightside[x][y] = rightside[x + 1][y];
            rightside[x + 1][y] = 0;
            Board right = new Board(rightside);
            neighbors.add(right);
        }
        return neighbors;
    }

    private int[][] hardCopy(int[][] array) {
        int[][] copy = new int[array.length][array[0].length];
        int lenght = array.length;
        for (int i = 0; i < lenght; i++) {
            for (int j = 0; j < lenght; j++) {
                copy[i][j] = array[i][j];
            }
        }
        return copy;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copy = hardCopy(tiles);
        int a = 0;
        int b = 0;
        while (copy[a][b] == 0 || copy[a + 1][b] == 0) {
            b++;
        }
        int temp = copy[a][b];
        copy[a][b] = copy[a + 1][b];
        copy[a + 1][b] = temp;
        return new Board(copy);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] intArray = new int[5][5];
        int value = 1;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                intArray[i][j] = value;
                value++;
            }
        }
        intArray[4][4] = 0;

        Board b = new Board(intArray);
        StdOut.println(b.toString());
        StdOut.println("Is this goal?" + b.isGoal());
        StdOut.println("hamming is " + b.hamming());
        StdOut.println("Manhaton number is " + b.manhattan());
        StdOut.println(b.twin().toString());
        for (Board bd : b.neighbors()) {
            StdOut.println(bd.toString());
        }
    }

}
