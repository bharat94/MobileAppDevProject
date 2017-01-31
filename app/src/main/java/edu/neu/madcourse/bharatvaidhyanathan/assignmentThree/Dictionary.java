package edu.neu.madcourse.bharatvaidhyanathan.assignmentThree;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vaidhyanathannarayanan on 31/01/17.
 */

//Dictionary implemented using a Trie Tree.
//Acknowledgement to prettymuchbryce for this part of the code from github

public class Dictionary {
    private HashMap<Character,Node> roots = new HashMap<Character,Node>();

    public boolean search(String string) {
        if (roots.containsKey(string.charAt(0))) {
            if (string.length()==1 && roots.get(string.charAt(0)).endOfWord) {
                return true;
            }
            return searchFor(string.substring(1),roots.get(string.charAt(0)));
        } else {
            return false;
        }
    }

    public void insert(String string) {
        if (!roots.containsKey(string.charAt(0))) {
            roots.put(string.charAt(0), new Node());
        }

        ArrayList<Character> ch_arr = new ArrayList<Character>();

        for(int i=1; i<string.length(); i++){
            ch_arr.add(string.charAt(i));
        }

        insertWord(ch_arr,roots.get(string.charAt(0)));
    }

    //Recursive method that inserts a new word into the trie tree.
    private void insertWord(ArrayList<Character> charr, Node node) {
        final Node nextChild;
        char ch = charr.remove(0);
        if (node.children.containsKey(ch)) {
            nextChild = node.children.get(ch);
        } else {
            nextChild = new Node();
            node.children.put(ch, nextChild);
        }

        if (charr.isEmpty()) {
            nextChild.endOfWord = true;
            return;
        } else {
            insertWord(charr,nextChild);
        }
    }


    //Recursive method that searches through the Trie Tree to find the value.
    private boolean searchFor(String string, Node node) {
        if (string.length()==0) {
            if (node.endOfWord) {
                return true;
            } else {
                return false;
            }
        }

        if (node.children.containsKey(string.charAt(0))) {
            return searchFor(string.substring(1),node.children.get(string.charAt(0)));
        } else {
            return false;
        }
    }
}

class Node {
    public Boolean endOfWord = false; //Does this Node mark the end of a particular word?
    public HashMap<Character,Node> children = new HashMap<Character,Node>();
}
