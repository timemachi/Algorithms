import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] aux = new char[R];
        for (int i = 0; i < R; i++) {
            aux[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            for (int i = 0; i < R; i++) {
                if (ch == aux[i]) {
                    if (i != 0) {
                        char temp = aux[i];
                        for (int j = i; j > 0; j--) {
                            aux[j] = aux[j - 1];
                        }
                        aux[0] = temp;
                    }
                    BinaryStdOut.write((byte) i);
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] aux = new char[R];
        for (int i = 0; i < R; i++) {
            aux[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int n = BinaryStdIn.readInt(8);
            char ch = aux[n];
            if (n != 0) {
                for (int j = n; j > 0; j--) {
                    aux[j] = aux[j - 1];
                }
                aux[0] = ch;
            }
            BinaryStdOut.write(ch);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        }
        if (args[0].equals("+")) {
            decode();
        }
    }
}
