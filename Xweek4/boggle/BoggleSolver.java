import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private final Trie dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = new Trie();
        for (String string : dictionary) {
            this.dictionary.insert(string);
        }
    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> words = new SET<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                Tile tile = new Tile(i, j);
                words = words.union(dfs(board, tile));
            }
        }
        return words;
    }

    private SET<String> dfs(BoggleBoard board, Tile tile) {
        boolean[][] marked = new boolean[board.rows()][board.cols()];
        String prefix = String.valueOf(board.getLetter(tile.row, tile.col));
        return dfs(board, tile, prefix, marked);
    }

    private SET<String> dfs(BoggleBoard board, Tile tile, String prefix, boolean[][] marked) {
        marked[tile.row][tile.col] = true;
        SET<String> words = new SET<>();
        if (prefix.charAt(prefix.length() - 1) == 'Q') {
            prefix += 'U';
        }

        if (dictionary.search(prefix) && prefix.length() > 2) {
            words.add(prefix);
        }
        for (Tile t : adj(board, tile)) {
            if (!marked[t.row][t.col]) {
                char next = board.getLetter(t.row, t.col);
                String nextWord = prefix + next;
                if (dictionary.isPrefix(nextWord)) {
                    words = words.union(dfs(board, t, nextWord, marked));
                }
            }
        }
        marked[tile.row][tile.col] = false;
        return words;
    }

    private static Iterable<Tile> adj(BoggleBoard board, Tile tile) {
        Stack<Tile> adj = new Stack<Tile>();
        int i = tile.row;
        int j = tile.col;
        if (i > 0) {
            adj.push(new Tile(i - 1, j));
            if (j > 0) {
                adj.push(new Tile(i - 1, j - 1));
            }
            if (j < board.cols() - 1) {
                adj.push(new Tile(i - 1, j + 1));
            }
        }
        if (i < board.rows() - 1) {
            adj.push(new Tile(i + 1, j));
            if (j > 0) {
                adj.push(new Tile(i + 1, j - 1));
            }
            if (j < board.cols() - 1) {
                adj.push(new Tile(i + 1, j + 1));
            }
        }
        if (j > 0) {
            adj.push(new Tile(i, j - 1));
        }
        if (j < board.cols() - 1) {
            adj.push(new Tile(i, j + 1));
        }
        return adj;
    }


    private static class Tile {
        public int row, col;

        public Tile(int i, int j) {
            this.row = i;
            this.col = j;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!dictionary.search(word)) {
            return 0;
        }
        int len = word.length();
        if (len < 3) {
            return 0;
        }
        else if (len < 5) {
            return 1;
        }
        else if (len == 5) {
            return 2;
        }
        else if (len == 6) {
            return 3;
        }
        else if (len == 7) {
            return 5;
        }
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
