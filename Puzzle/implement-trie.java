/*

实现一个 Trie，包含 insert, search, 和 startsWith 这三个方法。

 注意事项

你可以假设所有的输入都是小写字母a-z。

insert("lintcode")
search("code") // return false
startsWith("lint") // return true
startsWith("linterror") // return false
insert("linterror")
search("lintcode) // return true
startsWith("linterror") // return true

*/

class TrieNode {
    // Initialize your data structure here.
    Map<Character, TrieNode> m;
    boolean hasEnd;
    
    public TrieNode() {
        m = new HashMap<Character, TrieNode>();
        hasEnd = false;
    }
}

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        insertHelper(word, 0, root);
    }
    
    private void insertHelper(String word, int i, TrieNode t) {
        char c = word.charAt(i);
        TrieNode subNode = t.m.get(c);
        if (subNode == null) {
            subNode = new TrieNode();
            t.m.put(c, subNode);
        }
        
        if (i == word.length() - 1) {
            subNode.hasEnd = true;
        } else {
            insertHelper(word, i+1, subNode);
        }
    }

    // Returns if the word is in the trie.
    public boolean search(String word) {
        TrieNode t = root;
        
        for (int i = 0; i < word.length(); i++) {
            t = t.m.get(word.charAt(i));
            if (t == null) {
                return false;
            }
        }
        
        return t.hasEnd;
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        TrieNode t = root;
        
        for (int i = 0; i < prefix.length(); i++) {
            t = t.m.get(prefix.charAt(i));
            if (t == null) {
                return false;
            }
        }
        
        return true;
    }
    
    
}
