import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private final Digraph digraph;
    private int length;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("argument to SAP() is null");
        }
        this.digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        ancestor(v, w);
        return length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        length = Integer.MAX_VALUE;
        int ancestor = -1;
        BreadthFirstDirectedPaths graphV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths graphW = new BreadthFirstDirectedPaths(digraph, w);
        for (int i = 0; i < digraph.V(); i++) {
            if (graphV.hasPathTo(i) && graphW.hasPathTo(i)) {
                int shortest = graphV.distTo(i) + graphW.distTo(i);
                if (shortest < length) {
                    length = shortest;
                    ancestor = i;
                }
            }
        }
        if (ancestor == -1) {
            length = -1;
        }
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        ancestor(v, w);
        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        length = Integer.MAX_VALUE;
        int ancestor = -1;
        BreadthFirstDirectedPaths graphV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths graphW = new BreadthFirstDirectedPaths(digraph, w);
        for (int i = 0; i < digraph.V(); i++) {
            if (graphV.hasPathTo(i) && graphW.hasPathTo(i)) {
                int shortest = graphV.distTo(i) + graphW.distTo(i);
                if (shortest < length) {
                    length = shortest;
                    ancestor = i;
                }
            }
        }
        if (ancestor == -1) {
            length = -1;
        }
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
