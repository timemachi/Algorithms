public class Trie {
    private final TrieNode root;


    public Trie() {
        root = new TrieNode();
    }

    public void insert(String string) {
        TrieNode node = root;
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (!node.containsKey(ch)) {
                node.put(ch, new TrieNode());
            }
            node = node.get(ch);
        }
        node.setEnd();
    }

    public boolean search(String string) {
        TrieNode node = searchHelper(string);
        return node != null && node.isEnd();
    }

    public boolean isPrefix(String string) {
        TrieNode node = searchHelper(string);
        return node != null;
    }

    private TrieNode searchHelper(String string) {
        TrieNode node = root;
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (!node.containsKey(ch)) {
                return null;
            }
            node = node.get(ch);
        }
        return node;
    }

    class TrieNode {
        private static final int R = 26;
        private final TrieNode[] link;
        private boolean isEnd;

        private TrieNode() {
            link = new TrieNode[R];
        }

        private boolean containsKey(char ch) {
            return link[ch - 'A'] != null;
        }

        private TrieNode get(char ch) {
            return link[ch - 'A'];
        }

        private void put(char ch, TrieNode node) {
            link[ch - 'A'] = node;
        }

        private void setEnd() {
            isEnd = true;
        }

        private boolean isEnd() {
            return isEnd;
        }
    }
}
