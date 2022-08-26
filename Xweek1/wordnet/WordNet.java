import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WordNet {
    // constructor takes the name of the two input files
    private final Digraph wordnet;
    private final HashMap<Integer, String> synSets;
    private final HashMap<String, List<Integer>> synMaps;
    private SAP sap;

    private int count = 0;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("argument is null");
        }
        synSets = new HashMap<>();
        synMaps = new HashMap<>();
        readSynsets(synsets);
        wordnet = new Digraph(count);
        readHypernyms(hypernyms);

        DirectedCycle dc = new DirectedCycle(wordnet);
        if (dc.hasCycle()) {
            throw new IllegalArgumentException("Input has cycle.");
        }

        // check if root is 38003
        int rootNum = 0;
        for (int i = 0; i < count; i++) {
            if (wordnet.outdegree(i) == 0) {
                rootNum++;
            }
        }
        if (rootNum != 1) {
            throw new IllegalArgumentException("Input has " + rootNum + " roots.");
        }

        this.sap = new SAP(wordnet);
    }

    private void readSynsets(String synsets) {
        In in = new In(synsets);
        while (in.hasNextLine()) {
            count++;
            String line = in.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            synSets.put(id, parts[1]);
            String[] nouns = parts[1].split(" ");
            for (String s : nouns) {
                if (synMaps.get(s) != null) {
                    List<Integer> list = synMaps.get(s);
                    list.add(id);
                }
                else {
                    List<Integer> list = new ArrayList<>();
                    list.add(id);
                    synMaps.put(s, list);
                }
            }
        }
    }

    private void readHypernyms(String hypernyms) {
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int a = Integer.parseInt(parts[0]);
            if (parts.length == 1) {
                continue;
            }

            for (int i = 1; i < parts.length; i++) {
                int b = Integer.parseInt(parts[i]);
                wordnet.addEdge(a, b);
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synMaps.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("word is null");
        }
        return synMaps.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        Iterable<Integer> a = synMaps.get(nounA);
        Iterable<Integer> b = synMaps.get(nounB);
        return sap.length(a, b);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateNoun(nounA);
        validateNoun(nounB);
        Iterable<Integer> a = synMaps.get(nounA);
        Iterable<Integer> b = synMaps.get(nounB);
        int ancestor = sap.ancestor(a, b);
        return synSets.get(ancestor);
    }

    private void validateNoun(String word) {
        if (!isNoun(word)) {
            throw new IllegalArgumentException(word + " is not a WordNet noun.");
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyme = args[1];
        WordNet wordNet = new WordNet(synsets, hypernyme);
        String ancestor = wordNet.sap("white_marlin", "mileage");
        int distance = wordNet.distance("white_marlin", "mileage");
        System.out.println(ancestor);
        System.out.println("Distance is " + distance);
    }
}
