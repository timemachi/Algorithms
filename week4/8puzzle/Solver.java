import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Solver {
    private final SearchNode lastNode;
    private final boolean solvable;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        if (initial.isGoal()) {
            lastNode = new SearchNode(initial);
            solvable = true;
            return;
        }
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();
        SearchNode init = new SearchNode(initial);
        SearchNode initTwin = new SearchNode(initial.twin());
        pq.insert(init);
        pqTwin.insert(initTwin);

        lableA:
        while (true) {
            SearchNode minNode = pq.delMin();
            for (Board b : minNode.board.neighbors()) {
                SearchNode parent = minNode.parent;
                if (parent != null) {
                    if (parent.board.equals(b)) {
                        continue;
                    }
                }
                SearchNode sn = new SearchNode(b, minNode);
                pq.insert(sn);
                if (sn.isGoal()) {
                    this.solvable = true;
                    this.lastNode = sn;
                    break lableA;
                }
            }
            SearchNode minNodeTwin = pqTwin.delMin();
            SearchNode parentTwin = minNodeTwin.parent;
            for (Board b : minNodeTwin.board.neighbors()) {
                if (parentTwin != null) {
                    if (parentTwin.board.equals(b)) {
                        continue;
                    }
                }
                SearchNode sn = new SearchNode(b, minNodeTwin);
                pqTwin.insert(sn);
                if (sn.isGoal()) {
                    this.solvable = false;
                    this.lastNode = null;
                    break lableA;
                }
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode parent;
        private final int move;
        private final int priority;

        public SearchNode(Board b) {
            this.board = b;
            this.parent = null;
            this.move = 0;
            this.priority = b.manhattan() + move;
        }

        public SearchNode(Board b, SearchNode parent) {
            this.board = b;
            this.parent = parent;
            this.move = parent.move + 1;
            this.priority = b.manhattan() + move;
        }

        public int compareTo(SearchNode sn) {
            return priority - sn.priority;
        }

        public boolean isGoal() {
            return board.isGoal();
        }

        public Board getboard() {
            return board;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable) {
            return -1;
        }
        return lastNode.move;
    }

    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) {
            return null;
        }
        Board[] solution = new Board[lastNode.move + 1];
        SearchNode pointer = this.lastNode;
        while (pointer != null) {
            int index = pointer.move;
            solution[index] = pointer.board;
            pointer = pointer.parent;
        }
        return Arrays.asList(solution);
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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
