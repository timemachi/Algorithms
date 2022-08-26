import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private static int length;
    private static Integer[] index;
    private static char[] value;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        length = s.length();
        index = new Integer[length];
        value = new char[length];
        for (int i = 0; i < length; i++) {
            index[i] = i;
            value[i] = s.charAt(i);
        }

        Comparator<Integer> myComparator = (i1, i2) -> {
            for (int i = 0; i < length; i++) {
                char a = value[(i1 + i) % length];
                char b = value[(i2 + i) % length];
                if (a != b) {
                    return Character.compare(a, b);
                }
            }
            return 0;
        };
        Arrays.sort(index, myComparator);
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        assert (i >= 0 && i < length());
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray m = new CircularSuffixArray(s);
        System.out.println(m.index(1));
    }
}
